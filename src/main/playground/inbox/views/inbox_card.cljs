(ns playground.inbox.views.inbox-card
  (:require
   ["@mui/icons-material/AccessTime" :default AccessTimeIcon] ;; import MarkChatUnreadIcon from '@mui/icons-material/MarkChatUnread'));))
   ["@mui/icons-material/Notifications" :default NotificationsIcon] ;; import NotificationsNoneIcon from '@mui/icons-material/NotificationsNone'
   ["@mui/icons-material/NotificationsNone" :default NotificationsNoneIcon] ;; import AccessTimeIcon from '@mui/icons-material/AccessTime';
   ["@mui/material" :refer [Card Grid IconButton Typography]] ;; import MarkChatUnreadIcon from '@mui/icons-material/MarkChatUnread'));))
   [playground.helpers :as h]
   [playground.router :as router]
   [re-frame-flow.macros :refer-macros [dispatch dispatch-sync]]
   [re-frame.core :as rf]))

(defn card-text
  [{:keys [user-email inbox-id notifications? notifications updated-at]}]
  [:> Grid {:item true
            :pl 1
            :pt 1}
            ;; :display "flex"
            ;; :max-width "40vw"
            ;; :flex-wrap "wrap"}
   [:> Grid {:container true
             :pl 2}
    [:> Typography {:class-name "flex items-center justify-between text-slate-600"}
     user-email]]
   [:> Grid {:container true
             :display "flex"
             :direction "row"
             :justify-content "space-between"}
    (if notifications?
      [:<>
       [:> IconButton
        [:> NotificationsIcon {:class-name "text-gray-400"
                               :font-size "small"}]
        [:> Typography {:class-name "text-gray-400"
                        :font-size "small"}
         notifications]]]
      [:> IconButton
       [:> NotificationsNoneIcon {:class-name "text-gray-400"
                                  :font-size "small"}]])
    [:> Grid {:item true
              :gap 1
              :display "flex"
              :sx {:flex-direction "row"}
              :align-items "center"
              :justify-content "space-between"
              :flex-wrap "wrap"}
     [:> AccessTimeIcon {:class-name "text-gray-400"
                         :font-size "small"}]
     [:> Typography {:class-name "text-gray-400"
                     :font-size "small"}
      (h/time-ago updated-at)]]]])

(defn card-rounded-img
  [{:keys [user-email user-image]}]
  [:> Grid {:item true}
   [:img {:class-name "h-[58px] w-[58px] rounded-full object-cover object-center"
          :alt (str "user image: " user-email)
          :src user-image}]])

(defn inbox-card
  [{:keys [user-email inbox-id notifications?] :as inbox}]
  (let [user-image @(rf/subscribe [:inbox/user-image user-email])]
    ^{:key user-email}
    [:> Grid {:item true
              :as "a"
              :href     (router/path-for :inbox :inbox-id inbox-id)
              :on-click #(when notifications?
                           (dispatch [:inbox/mark-as-read user-email]))}
     [:> Card {:sx {:box-shadow 10
                    :border-radius "10px"}
               :class-name "transition hover:shadow-2xl hover:drop-shadow-2xl hover:-translate-y-1 ease-in-out delay-150 duration-500"}
               ;; :flex-wrap "wrap"
               ;; :max-width "40vw"}
      [:> Grid {:container true
                :display "flex"
                :direction "row"
                ;; :flex-direction "row"
                :p 3
                :py 2
                :align-items "center"}
       [card-rounded-img {:user-email user-email :user-image user-image}]
       [card-text inbox]]]]))
