(ns playground.inbox.events
  (:require
   [re-frame.core :as rf]
   [playground.helpers :as h]
   [playground.spec :refer [check-spec-interceptor]]))

(def inbox-interceptors [check-spec-interceptor])

;; "The =user-email= relates to the person the chat is with, related to the logged user (=uid=)")
(rf/reg-event-db
 :inbox/mark-as-read
 inbox-interceptors
 (fn [db [_ user-email]]
   (let [uid (get-in db [:auth :uid])]
     (assoc-in db [:users uid :inboxes user-email :notifications] 0))))

(rf/reg-event-db
 :inbox/send-notification
 inbox-interceptors
 (fn [db [_ {:keys [notify inbox-id]}]]
   (let [uid (get-in db [:auth :uid])
         notification-count (get-in db [:users notify :inboxes uid :notifications])]
     (-> db
         (assoc-in [:users notify :inboxes uid] {:id inbox-id
                                                 :notifications (inc notification-count)
                                                 :updated-at (js/Date.now)})
         (assoc-in [:users uid :inboxes notify] {:id inbox-id
                                                 :notifications 0
                                                 :updated-at (js/Date.now)})))))

(rf/reg-event-fx
 :inbox/insert-message
 inbox-interceptors
 (fn [{:keys [db]} [_ {:keys [message]}]]
   (let [uid (get-in db [:auth :uid])
         inbox-id (get-in db [:nav :active-inbox])
         participants (get-in db [:inboxes inbox-id :participants])
         chat-with (first (disj participants uid))]
     {:db (update-in db [:inboxes inbox-id :messages] conj {:message message
                                                            :author uid
                                                            :created-at (js/Date.now)})
      :dispatch [:inbox/send-notification {:notify chat-with
                                           :inbox-id inbox-id}]})))

(rf/reg-event-fx
 :recipe/request-message
 inbox-interceptors
 (fn [{:keys [db]} [_ {:keys [message]}]]
   (let [uid (get-in db [:auth :uid])
         recipe-id              (get-in db [:nav :active-recipe])
         cook                   (get-in db [:recipes recipe-id :cook])
         existing-inbox-id      (get-in db [:users uid :inboxes cook :id])
         new-inbox-id           (str uid "-" (random-uuid))
         {:keys [name price]}   (get-in db [:recipes recipe-id])
         message                {:message (str "(" name " - " (h/format-price price) ") \n" message)
                                 :author uid
                                 :created-at (js/Date.now)}]
     {:db (if existing-inbox-id
            (update-in db [:inboxes existing-inbox-id :messages] conj message)
            (assoc-in db [:inboxes new-inbox-id] {:id new-inbox-id
                                                  :participants #{uid cook}
                                                  :messages [message]
                                                  :notifications 0
                                                  :updated-at (js/Date.now)}))
      :dispatch-n [[:inbox/send-notification {:notify cook
                                              :inbox-id (or existing-inbox-id
                                                            new-inbox-id)}]
                   [:recipes/close-modal]]})))
