(ns playground.recipes.views.recipe-page
  (:require
   ["@mui/material" :refer [Button Grid Typography]]
   [playground.components.page-nav :refer [page-nav]]
   [playground.nav.subs]
   [playground.recipes.views.publish-recipe :refer [publish-recipe]]
   [playground.recipes.views.recipe-editor :refer [recipe-editor]]
   [playground.recipes.views.recipe-info :refer [recipe-info]]
   [playground.recipes.views.recipe-steps :refer [recipe-steps]]
   [playground.recipes.views.request-to-cook :refer [request-to-cook]]
   [playground.router :as router]
   [playground.utilites :as util]
   [re-frame.core :as rf]))

(defn recipe-page
  []
  (fn []
    (let [recipe @(rf/subscribe [:recipes/recipe])
          author? @(rf/subscribe [:recipe/author?])
          active-nav @(rf/subscribe [:active-nav])
          logged-in? @(rf/subscribe [:logged-in?])
          chef? @(rf/subscribe [:auth/chef?])
          {:keys [name]} recipe]
      [:<>
       [page-nav {:left (if (active-nav :saved)
                          :saved
                          :recipes)
                  :center (if author?
                            [recipe-editor]
                            [:> Typography {:variant "div"
                                            :color "inherit"
                                            :py 5
                                            :justify-content "center"
                                            :font-size "2.5rem"
                                            :font-weight 700}
                             name])
                  :right (cond
                           (not logged-in?)  [:> Button {:sx {:bgcolor (util/color [:green :500])}
                                                         :href (router/path-for :sign-up)
                                                         :on-click #(rf/dispatch [:set-active-nav :sign-up])}
                                              "Sign up"]
                           (not author?)    [request-to-cook]
                           (and (not chef?) author?)  [:> Button {:variant "contained"
                                                                  :sx {:bgcolor (util/color [:pink :400])}
                                                                  :href (router/path-for :become-a-chef)
                                                                  :on-click #(rf/dispatch [:set-active-nav :become-a-chef])}
                                                       "Become a chef"]
                           author?          [publish-recipe])}]
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
