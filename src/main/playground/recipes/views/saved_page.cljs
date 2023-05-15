(ns playground.recipes.views.saved-page
  (:require [re-frame.core :as re-frame]
            [reagent.core :as reagent]
            [playground.recipes.views.recipe-list :refer [recipe-list]]
            [playground.components.page-nav :refer [page-nav]]
            ;; ["@mui/icons-material" :refer [FavoriteIcon]]
            ["@mui/material" :refer [Box Typography Grid]]
            ["@mui/icons-material/Favorite" :default FavoriteIcon]
            [re-frame.core :as rf]
            [playground.utilites :as util]))

(defn saved-page
  []
  (let [saved @(rf/subscribe [:recipes/saved])]
    [:<>
     [page-nav {:center "Saved Recipes"}]
     (if (seq saved)
       [recipe-list saved]
       [:div
        [:> Box {:sx {:display "flex" :justify-content "center" :align-items "center" :height "100%" :flex-direction "column"}}
         [:> FavoriteIcon {:sx {:width "100px" :height "100px"
                                :color (util/color [:red :500])}}]
         [:> Typography {:variant "h4" :sx {:mt "20px"}} "No saved recipes, yet!"]]])]))
