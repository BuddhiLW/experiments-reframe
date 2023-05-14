(ns playground.recipes.views.recipe-list
  (:require
   ["@mui/material" :refer [Grid]]
   [playground.recipes.views.recipe-card :refer [recipe-card]]))

(defn recipe-list
  [recipes]
  [:> Grid {:container true
            :sx {:flex-grow 1}
            :flex-direction "row"
            :justify-content "start"}
   (for [recipe recipes]
     ^{:key (:id recipe)}
     [recipe-card recipe])])
