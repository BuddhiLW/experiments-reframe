(ns playground.recipe.views.recipes-page
  (:require
   ["@mui/material" :refer [Grid Typography Box]]
   [playground.components.page-nav :refer [page-nav]]))

(defn recipes-page
  []
  [:<>
   [page-nav {:center "Recipes"}]
   [:> Grid {:display "flex"
             :flex-direction "column"
             :container true}
    [:> Box
     [:> Typography {:text-align "center"
                     :component "h4"
                     :variant "h2"
                     :py 20
                     :font-weight 700}
      "Drafts"]]
    [:> Box
     [:> Typography {:text-align "center"
                     :component "h4"
                     :variant "h2"
                     :py 20
                     :font-weight 700}
      "Public"]]]])
