(ns playground.inbox.views.inbox-page
  (:require
   ["@mui/material" :refer [Box Button Card Grid Input Paper Typography]]
   [playground.components.page-nav :refer [page-nav]]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn inbox-page
  []
  (let [initial-values {:message ""}
        values (r/atom initial-values)
        save (fn [message]
               (rf/dispatch [:inbox/insert-message message])
               (reset! values initial-values))]
    (fn []
      (let [inbox-messages @(rf/subscribe [:inbox/inbox-messages])
            chat-with @(rf/subscribe [:inbox/chat-with])
            log-chat-with (js/console.log chat-with)]

        [:> Grid {:conteiner true}
         [page-nav {:left   :inboxes
                    :center chat-with}]
         [:> Grid {:item true :xs 12 :md 6}
          [:> Box {:display "flex"
                   :justify-content "center"
                   :gap 5}
           [:> Input {:value (:message @values)
                      :on-change #(swap! values assoc :message (.. % -target -value))}]
           [:> Box
            [:> Button {:size "small"
                        :variant "contained"
                        :on-click #(save (:message @values))}
             "Send"]]]]
         [:> Paper {:sx {:border-radius 10}
                    :mt 2}
          (for [message inbox-messages]
            ^{:key (:created-at message)}
            [:> Card {:sx {:border-radius 10}
                      :variant "outlined"
                      :mt 2
                      :p 2}
             [:> Grid {:container true}
              [:> Typography {:variant "h5"} (:message message)]
              [:> Typography {:variant "body2"} (:created-at message)]]])]]))))

(comment
  #_[:> Box {:sx {:display "flex"
                  :flexDirection "column"
                  :height "100%"}}
     [:> Paper {:sx {:flexGrow 1
                     :overflow "auto"}}]
     [:> Container {:sx {:display "flex"
                         :flexDirection "column"
                         :height "100%"}}
      [:> Typography {:variant "h5" :sx {:flexGrow 1}} "Chat"]
      [:> Box {:sx {:display "flex"
                    :flexDirection "column"
                    :flexGrow 1
                    :p 2}}
       [:> Paper {:sx {:flexGrow 1
                       :overflow "auto"}}]
       [:> Container {:sx {:display "flex"
                           :flexDirection "column"
                           :flexGrow 1}}
        [:> Typography {:variant "h5" :sx {:flexGrow 1}} "Chat"]
        [:> Box {:sx {:display "flex"
                      :flexDirection "column"
                      :flexGrow 1
                      :p 2}}
         [:> Paper {:sx {:flexGrow 1
                         :overflow "auto"}}]
         [:> Container {:sx {:display "flex"
                             :flexDirection "column"
                             :flexGrow 1}}
          [:> Typography {:variant "h5" :sx {:flexGrow 1}} "Chat"]
          [:> Box {:sx {:display "flex"
                        :flexDirection "column"
                        :flexGrow 1
                        :p 2}}
           [:> Paper {:sx {:flexGrow 1
                           :overflow "auto"}}]
           [:> Container {:sx {:display "flex"
                               :flexDirection "column"
                               :flexGrow 1}}
            [:> Typography {:variant "h5" :sx {:flexGrow 1}} "Chat"]
            [:> Box {:sx {:display "flex"
                          :flexDirection "column"
                          :flexGrow 1
                          :p 2}}
             [:> Paper {:sx {:flexGrow 1
                             :overflow "auto"}}]
             [:> Container {:sx {:display "flex"
                                 :flexDirection "column"
                                 :flexGrow 1}}
              [:> Typography {:variant "h5" :sx {:flexGrow 1}} "Chat"]
              [:> Box {:sx {:display "flex"
                            :flexDirection "column"
                            :flexGrow 1}}]]]]]]]]]]]

  #_[:div
     [page-nav]
     [:div
      [:h1 "Inbox"]
      [:div
       (for [message inbox-messages]
         [:div
          [:p (:message message)]
          [:p (:created-at message)]])]]
     [:div
      [:form
       {:on-submit
        (fn [e]
          (.preventDefault e)
          (save (:message @values)))}
       [:div
        [:label "Message"]
        [:input
         {:type "text"
          :value (:message @values)
          :on-change
          (fn [e]
            (reset! values (assoc @values :message (-> e .-target .-value))))}]]
       [:button "Save"]]]])
