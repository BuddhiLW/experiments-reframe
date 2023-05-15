(ns playground.inbox.subs
  (:require
   [re-frame.core :as rf]))

(defn reverse-cmp [a b]
  (compare b a))

(rf/reg-sub
 :inboxes
 (fn [db _]
   (get db :inboxes)))

(rf/reg-sub
 :user/inboxes
 :<- [:recipes/user]
 (fn [user _]
   (sort-by :update-at reverse-cmp (get user :inboxes))))

(rf/reg-sub
 :inbox/user-image
 :<- [:users]
 (fn [users [_ uid]]
   (get-in users [uid :profile :img])))

(rf/reg-sub
 :inbox/inbox-messages
 :<- [:inboxes]
 :<- [:nav/active-inbox]
 (fn [[inboxes active-inbox] _]
   (let [messages (get-in inboxes [active-inbox :messages])]
     (sort-by :created-at reverse-cmp messages))))

(rf/reg-sub
 :inbox/chat-with
 :<- [:uid]
 :<- [:inboxes]
 :<- [:nav/active-inbox]
 (fn [[uid inboxes active-inbox] _]
   (let [participants (get-in inboxes [active-inbox :participants])]
     (first (disj participants uid)))))
