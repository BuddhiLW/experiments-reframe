(ns playground.components.page-nav
  (:require
   ["@mui/icons-material/Adjust" :default AdjustIcon]
   ;; ["@mui/icons-material/ChevronLeft" :default ChevronLeftIcon]
   ["@mui/icons-material/ArrowCircleLeftRounded" :default ArrowCircleLeftRoundedIcon]
   ["@mui/material" :refer [Box Button Grid Typography  colors]]
   [playground.router :as router]))

(defn page-nav
  [{:keys [left center right]}]
  [:<>
   [:> Box {:py 5
            :px 5
            :component "nav"
            :variant "dense"
            :fluid "false"
            :display "flex"
            :justify-content "space-between"
            :container "true"
            :align-items "center"}
    [:> Box {:display "flex"
             :justify-content "flex-start"
             :py 1}
     (if left
       [:> Button {:my "20"
                   :variant "light"
                   :aria-label "Back"
                   :href (router/path-for left)}
        [:> ArrowCircleLeftRoundedIcon {:sx {:font-size "2.5rem"
                                             :color (get-in (js->clj colors :keywordize-keys true) [:pink :500])}}]]
       [:> Button {:my "20"
                   :variant "light"
                   :aria-label "Back"}
        [:> AdjustIcon {:sx {:font-size "2.5em"}}]])]
    [:> Box {:justify-content "center"}
     (if (= (type center) (type "String"))
       [:> Typography {:variant "h2"
                       :color "inherit"
                       :py 5
                       :font-size "2.5rem"
                       :font-weight 700}
        center]
       center)]
    [:> Box
     (cond
       (= (type right) (type "String"))  [:> Box {:display "flex"
                                                  :justify-content "flex-end"
                                                  :py 1}
                                          right]
       (nil? right)                      [:div " "]
       :else                             right)]]])
