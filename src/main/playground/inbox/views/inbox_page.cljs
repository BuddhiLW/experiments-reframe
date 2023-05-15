(ns playground.inbox.views.inbox-page
  (:require
   ["@mui/material" :refer [Box Button Card Grid Input Paper Typography
                            FormControl]]
   [playground.components.page-nav :refer [page-nav]]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [clojure.string :as str]
   [playground.utilites :as util]
   [playground.helpers :as h]))

(defn inbox-page
  []
  (let [initial-values {:message ""}
        values (r/atom initial-values)
        save (fn [{:keys [message]}]
               (when (not (str/blank? message))
                 (rf/dispatch [:inbox/insert-message {:message (str/trim message)}])
                 (reset! values initial-values)))]
    (fn []
      (let [chat-with @(rf/subscribe [:inbox/chat-with])
            messages  @(rf/subscribe [:inbox/inbox-messages])]
        [:> Grid
         [page-nav {:left   :inboxes
                    :center chat-with}]
         [:> Grid {:container true
                   :direction "column"
                   :justify-content "center"
                   :align-items "center"}
          [:> Grid {:item true :xs 12 :md 6}
           [:> Box {:display "flex"
                    :justify-content "center"
                    :gap 5}
            [:> FormControl {:on-submit (fn [e]
                                          (.preventDefault e)
                                          (save @values))
                             :component "form"}
             [:> Input {:value (:message @values)
                        :on-change #(swap! values assoc :message (.. % -target -value))}]]
            [:> Box
             [:> Button {:size "small"
                         :variant "contained"
                         :on-click #(save @values)}
              "Send"]]]]
          [:> Grid {:item true
                    :my 4
                    :sx {:min-width "50vw"
                         :max-width "70vw"
                         :min-height "60vh"
                         :max-height "60vh"}
                    :sm {:min-height "65vw"}
                    :md {:min-height "70vw"}
                    :lg {:min-height "60vw"}
                    :display "flex"
                    :flex-wrap "wrap"
                    :class-name "overflow-hidden overflow-y-auto"}
           [:> Paper {:sx {:p 2
                           :background-color (util/color [:grey :300])
                           :min-width "65vw"
                           :min-height "60vh"
                           :max-height "70vh"}
                      :sm {:min-height "65vw"}
                      :md {:min-height "70vw"}
                      :lg {:min-height "60vw"}
                      :class-name "absolute blur-sm"}]
           [:> Grid {:class-name "relative"
                     :min-height "60vh"
                     :min-width "64vw"
                     :max-width "70vw"}
            (for [{:keys [message author created-at]} messages]
              ^{:key (:created-at message)}
              [:> Card {:sx {:border-radius 10
                             ;; :px 2
                             :mx 2
                             :mt 2}
                        :max-width "70vw"
                        :variant "outlined"
                        :overflow "hidden"
                        :class-name "overflow-hidden overflow-y-auto"}
               [:> Grid {:container true
                         :align-items "center"
                         :xs 12
                         :py 2
                         :pl 2
                         :class-name "whitespace-pre-wrap"}
                [:> Grid {:item true
                          :xs 1}
                 [:img {:src @(rf/subscribe [:inbox/user-image author])
                        :alt "user image"
                        :class-name "h-[58px] w-[58px] rounded-full object-cover object-center"}]]
                [:> Grid {:item true
                          :class-name "white-space-pre-wrap"
                          :max-width "50vw"
                          :overflow "hidden"}
                 [:> Typography {:class-name "text-slate-700 whitespace-wrap break-words"
                                 :font-size "1.2rem"}
                  message]]
                [:> Grid {:item true
                          :px 2
                          :ml "auto"}
                 [:> Typography {:variant "body2"}
                  (h/time-ago created-at)]]]])]]]]))))

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
