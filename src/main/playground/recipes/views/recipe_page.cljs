(ns playground.recipes.views.recipe-page
  (:require
   [playground.components.page-nav :refer [page-nav]]
   ["@mui/material" :refer [Grid]]
   [re-frame.core :as rf]
   [playground.nav.subs]
   [playground.recipes.views.recipe-steps :refer [recipe-steps]]
   [playground.recipes.views.recipe-info :refer [recipe-info]]))

(defn recipe-page
  []
  (let [recipe @(rf/subscribe [:recipes/recipe])
        {:keys [name steps ingredients price prep-time img saved-count cook]} recipe]
    [:<>
     [page-nav {:center (str name)}]
     [:> Grid {:container true
               :direction "row"
               :justify-content "space-around"
               :px 2}
      [recipe-info]
      [recipe-steps]]]))
