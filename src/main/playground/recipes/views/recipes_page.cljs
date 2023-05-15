(ns playground.recipes.views.recipes-page
  (:require
   ["@mui/material" :refer [Grid Box Typography]] ;; Button
   [playground.auth.subs]
   [playground.components.page-nav :refer [page-nav]]
   [playground.recipes.subs]
   [playground.recipes.views.recipe-editor :refer [recipe-editor]]
   [playground.recipes.views.recipe-list :refer [recipe-list]]
   [re-frame.core :as rf]))

(defn recipes-page
  []
  (let [public @(rf/subscribe [:filter/public])
        drafts @(rf/subscribe [:filter/draft])
        logged-in? @(rf/subscribe [:logged-in?])]
    [:<>
     [page-nav {:center "Recipes"
                :right (when logged-in?
                         [:<>
                          [recipe-editor]])}]
     ;; [:> Button {:variant "contained"
     ;;             :color "primary"
     ;;             :sx {:ml 5
     ;;                  :mt 5}
     ;;             :on-click #(rf/dispatch [:http/get-recipes])}
     ;;  "Get recipes"]
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
                          :pb 5
                          :pl 5
                          :font-weight 700}
           "Drafts"]
          [recipe-list drafts]]])
      (when logged-in?
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
          [recipe-list public]]])]]))
