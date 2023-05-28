(ns playground.recipes.views.recipes-page
  (:require
   ["@mui/material" :refer [Grid Box Typography Button]]
   [playground.auth.subs]
   [playground.components.page-nav :refer [page-nav]]
   [playground.recipes.subs]
   [playground.recipes.views.recipe-editor :refer [recipe-editor]]
   [playground.recipes.views.recipe-list :refer [recipe-list]]
   [re-frame.core :as rf]))

(defn recipes-page
  []
  (let [logged-in? @(rf/subscribe [:logged-in?])]
    (fn []
      (let [drafts @(rf/subscribe [:filter/draft])]
        [:<>
         [page-nav {:center "Recipes"
                    :right (when logged-in?
                             [:<>
                              [recipe-editor]])}]
         [:> Grid {:display "flex"
                   :flex-direction "column"
                   :container true}
          [:> Grid {:display "flex"
                    :gap 2
                    :mx "auto"}
           [:> Button {:variant "contained"
                       :on-click #(rf/dispatch [:http/get-recipes-log])}
            "Load log recipes"]

           [:> Button {:variant "contained"}
            :on-click #(rf/dispatch [:http/post-recipe-mock])
            "log post recipes"]]

            ;; [stripe-card]
            ;; => equivalent to (not (empty? drafts)), read =empty?= description at: https://clojuredocs.org/clojure.core/empty_q
          (when (seq drafts)
            [:> Box
             [:<>
              [:> Typography {:text-align "left"
                              :component "h2"
                              :variant "h2"
                              :pb 5
                              :pl 5
                              :font-weight 700}
               "Drafts"]
              [recipe-list drafts]]])
           ;; (when logged-in?
          [:<>
           [:> Grid {:container true
                     :mb 5
                     :row-spacing {:xs 1
                                   :sm 2
                                   :md 3}
                     :flex-direction "column"
                     :sx {:display "flex"
                          :flex-wrap "wrap"
                          :align-items "strech"}}
            [:> Box {:sx {:flex-shrink 1}}
             [:> Typography {:text-align "left"
                             :component "h2"
                             :variant "h2"
                             :pt 10
                             :pb 7
                             :pl 5
                             :font-weight 700}
              "Public"]]
            (when @(rf/subscribe [:recipes/loaded?])
              [recipe-list @(rf/subscribe [:filter/public])])]]]]))))
