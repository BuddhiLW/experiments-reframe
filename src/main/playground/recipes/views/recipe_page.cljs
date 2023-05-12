(ns playground.recipes.views.recipe-page
  (:require
   ["@mui/material" :refer [Grid Typography]]
   [playground.components.page-nav :refer [page-nav]]
   [playground.nav.subs]
   [playground.recipes.views.recipe-editor :refer [recipe-editor]]
   [playground.recipes.views.recipe-info :refer [recipe-info]]
   [playground.recipes.views.recipe-steps :refer [recipe-steps]]
   [re-frame.core :as rf]))

(defn recipe-page
  []
  (fn []
    (let [recipe @(rf/subscribe [:recipes/recipe])
          author? @(rf/subscribe [:recipe/author?])
          active-nav @(rf/subscribe [:active-nav])
          {:keys [name]} recipe]
      [:<>
       [page-nav {:left (if (active-nav :saved)
                          :saved
                          :recipes)
                  :center (if author?
                            recipe-editor
                            (fn []
                              [:> Typography {:variant "div"
                                              :color "inherit"
                                              :py 5
                                              :justify-content "center"
                                              :font-size "2.5rem"
                                              :font-weight 700}
                               name]))}]
       [:> Grid {:container true
                 :columns {:xs 6
                           :sm 7
                           :md 12}
                 :spacing {:md 1}
                 :direction {:sx "column"
                             :xs "column"
                             :sm "column"
                             :md "row"}
                 :justify-content {:sx "center"
                                   :xs "center"
                                   :sm "center"
                                   :md "space-evenly"}
                 :px 2}
        [recipe-info]
        [recipe-steps]]])))
