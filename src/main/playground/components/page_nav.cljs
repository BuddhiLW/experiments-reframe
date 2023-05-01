(ns playground.components.page-nav
  (:require
   ;; ["@mui/base/Tab" :refer [Tab]]
   ;; ["@mui/base/Tabs" :refer [Tabs]]
   ;; ["@mui/base/TabsList" :refer [TabsList]]
   ;; ["@mui/base/Menu" :refer [Menu]]
   ;; ["@mui/base/MenuItem" :refer [MenuItem]]
   ["@mui/material" :refer [ChevronLeftIcon Box Button Grid Typography Container]]))

(defn page-nav
  [{:keys [left center right]}]
  [:> Container
   (when left
     [:> Button {:as "a"
                 :my "20"
                 :variant "light"
                 :aria-label "Back"
                 :href left}
      [:> ChevronLeftIcon {:size 16}]])
   [:> Typography {:variant "h2"
                   :py 20
                   :font-weight 700}
    center]
   (when right
     right)])
