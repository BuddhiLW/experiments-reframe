(ns playground.recipes.views.recipe-steps
  (:require
   ["@mui/material" :refer [Box Grid Paper Typography]]
   [re-frame.core :as rf]))

(defn recipe-steps []
  (let [steps @(rf/subscribe [:recipe/steps])]
    [:> Grid {:container true
              :max-width "100%"
              :xs 7
              :p 3}
     [:> Paper {:variant "outlined"
                :sx {:min-width "100%"}}
      [:> Grid {:container true
                :spacing 2
                :xs 12}
       [:> Grid {:p 4
                 :item true
                 :xs 12
                 :pb 0
                 :align-items "center"}
        [:> Typography {:variant "h4"
                        :align "center"
                        :font-weight 500
                        :class-name "text-slate-700"}
         "Preparation"]]
       [:hr {:class-name "w-full border-0 h-px my-2 bg-gray-200 dark:bg-gray-700"}]]
      (for [[index {:keys [id desc]}] (map-indexed (fn [i m] [i m]) steps)]
        [:> Grid {:item true
                  :key id
                  :p 2}
         [:> Typography {:variant "h4"
                         :class-name "text-slate-600"} "Step " (inc index)]
         [:hr {:class-name "w-1/4 border-0 h-px my-2 bg-gray-200 dark:bg-gray-700"}]
         [:> Typography
          {:variant "body1"
           :font-size "1.2rem"
           :component "p"
           :class-name "text-slate-800"}
          desc]])]])
  #_[:> Grid {:xs 6
              :pt 4}
     [:> Paper {:variant "outlined"}
      [:> Box {:p 2}
       [:> Typography {:variant "h6"} "Instructions"]
       [:> Typography {:variant "body1"} "Mix ingredients"]
       [:> Typography {:variant "body1"} "Bake at 350 degrees"]]]])
