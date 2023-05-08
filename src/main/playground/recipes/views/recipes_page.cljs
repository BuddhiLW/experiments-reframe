(ns playground.recipes.views.recipes-page
  (:require
   ["@mui/material" :refer [Grid Typography Box]]
   [re-frame.core :as rf]
   [playground.components.page-nav :refer [page-nav]]
   [playground.auth.subs]
   [playground.recipes.subs]
   [playground.recipes.views.recipe-list :refer [recipe-list]]))

(defn recipes-page
  []
  (let [public @(rf/subscribe [:filter/public])
        drafts @(rf/subscribe [:filter/draft])
        logged-in? @(rf/subscribe [:logged-in?])]
    [:<>
     [page-nav {:center "Recipes"}]
     [:> Grid {:display "flex"
               :flex-direction "column"
               :container true}
      ;; => equivalent to (not (empty? drafts)), read =empty?= description at: https://clojuredocs.org/clojure.core/empty_q
      (when (seq drafts)
        [:> Box
         [:<>
          [:> Typography {:text-align "left"
                          :component "h2"
                          :variant "h2"
                          :pt 10
                          :pb 5
                          :pl 5
                          :font-weight 700}
           "Drafts"]
          [recipe-list drafts]]])
      (when logged-in?
        [:<>
         [:> Grid {:container true
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
          [recipe-list public]]])]]))
