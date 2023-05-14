(ns playground.inbox.views.inboxes-page
  (:require
   ["@mui/material" :refer [Grid Box]]
   [playground.components.page-nav :refer [page-nav]]
   [playground.inbox.views.inbox-card :refer [inbox-card]]
   [re-frame.core :as rf]))

(defn inboxes-page
  []
  (let [user-inboxes @(rf/subscribe [:user/inboxes])]
    [:<>
     [page-nav {:center "Inbox"}]
     [:> Grid {:container true
               :display "flex"
               :direction "row"
               :spacing 2
               :pl 20}
      (for [[k {:keys [id notifications updated-at]}] user-inboxes
            :let [notifications? (> notifications 0)]]
        ^{:key id}
        [inbox-card {:user-email      k
                     :inbox-id        id
                     :notifications?  notifications?
                     :notifications   notifications
                     :updated-at      updated-at}]
        #_[:ul
           (for [inbox user-inboxes]
             ^{:key (:id inbox)}
             [:li
              [:a {:href (str "/inbox/" (:id inbox))}
               (:name inbox)]])])]]))
