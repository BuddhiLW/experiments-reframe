(ns playground.inbox.events
  (:require
   [re-frame.core :as rf]))

;; "The =user-email= relates to the person the chat is with, related to the logged user (=uid=)")
(rf/reg-event-db
 :inbox/mark-as-read
 (fn [db [_ user-email]]
   (let [uid (get-in db [:auth :uid])]
     (assoc-in db [:users uid :inboxes user-email :notifications] 0))))
