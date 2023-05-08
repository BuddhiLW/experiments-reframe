(ns playground.recipes.views.recipe-steps
  (:require
   ["@mui/material" :refer [Box Grid Paper Typography]]))

(defn recipe-steps []
  [:> Grid {:xs 6
            :pt 4}
   [:> Paper {:variant "outlined"}
    [:> Box {:p 2}
     [:> Typography {:variant "h6"} "Instructions"]
     [:> Typography {:variant "body1"} "Mix ingredients"]
     [:> Typography {:variant "body1"} "Bake at 350 degrees"]]]])
