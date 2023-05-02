(ns playground.components.page-nav
  (:require
   ;; ["@mui/icons-material" :refer [ChevronLeftIcon AdjustIcon]]
   ;; import AdjustIcon from '@mui/icons-material/Adjust';
   ["@mui/icons-material/Adjust" :default AdjustIcon]
   ;; import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
   ["@mui/icons-material/ChevronLeft" :default ChevronLeftIcon]
   ["@mui/material" :refer [Box Button Grid Typography Container Toolbar]]))

;; import AdjustIcon from '@mui/icons-material/Adjust'
(defn page-nav
  [{:keys [left center right]}]
  [:<>
   [:> Box {:py 1
            :px 5
            :component "nav"
            :variant "dense"
            :fluid "false"
            :display "flex"
            :justify-content "space-between"
            :container "true"}
    [:> Box {:display "flex"
             :justify-content "flex-start"
             :py 1}
     (if left
       [:> Button {:as "a"
                   :my "20"
                   :variant "light"
                   :aria-label "Back"
                   :href left}
        [:> ChevronLeftIcon {:font-size "2.5rem"}]]
       [:> Button {:my "20"
                   :variant "light"
                   :aria-label "Back"}
        [:> AdjustIcon {:sx {:font-size "2.5em"}}]])]
    [:> Box {:justify-content "center"}
     [:> Typography {:variant "div"
                     :color "inherit"
                     :py 20
                     :justify-content "center"
                     :font-size "2.5rem"
                     :font-weight 700}
      center]]
    (if right
      right
      [:div " "])]])
