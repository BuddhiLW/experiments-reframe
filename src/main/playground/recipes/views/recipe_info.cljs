(ns playground.recipes.views.recipe-info
  (:require
   ["@mui/icons-material/AccessAlarm" :default AccessAlarm]
   ["@mui/icons-material/FavoriteBorderOutlined" :default FavoriteBorderOutlined]
   ["@mui/icons-material/FavoriteOutlined" :default FavoriteOutlined]
   ["@mui/material" :refer [CardActions Grid Grid IconButton Paper Paper
                            Typography Typography]]
   [playground.recipes.events]
   [playground.recipes.views.recipe-image :refer [recipe-img]]
   [playground.recipes.views.recipe-ingredients :refer [ingredients]]
   [re-frame.core :as rf]))

(defn footer
  []
  (fn []
    (let [{:recipe/keys [uid prep_time favorite_count]} @(rf/subscribe [:recipes/recipe])
          {:keys [saved]} @(rf/subscribe [:recipes/user])
          logged-in? @(rf/subscribe [:logged-in?])
          saved? (contains? saved uid)
          author? @(rf/subscribe [:recipe/author?])
          can-save? (and logged-in? (not author?) (not saved?))]
      [:> Grid {:container true
                :justify-content "space-between"
                :px 2
                :py 1}
       [:> Grid {:item true
                 :xs 7
                 :display "flex"
                 :align-items "center"}
        (if can-save?
          [:> CardActions
           [:> IconButton {:on-click #(rf/dispatch [:recipes/save-recipe uid])}
            [:> FavoriteBorderOutlined {:class-name "text-pink-500"
                                        :sx {:font-size 40}
                                        :href "#"}]]]
          [:> CardActions
           [:> IconButton
            [:> FavoriteOutlined {:class-name "text-red-500"
                                  :on-click  (when saved? #(rf/dispatch [:recipes/unsave-recipe uid]))
                                  :sx {:font-size 40}}]]])
        [:> Typography {:variant "body1"
                        :class-name "text-slate-700"}
         (str favorite_count)]]
       [:> Grid {:item true
                 :align-items "center"
                 :p 2
                 :display "flex"
                 :sx {:justify-content "flex-end"}}
        [:> AccessAlarm {:color "primary"
                         :sx {:font-size 45}
                         :class-name "pr-3"}]
        [:> Typography {:variant "body1"
                        :class-name "text-slate-700"}
         (str prep_time " min")]]])))

(defn recipe-info
  []
  (fn []
    (let [{:recipe/keys [recipe_id uid]} @(rf/subscribe [:recipes/recipe])
          {:keys [saved]} @(rf/subscribe [:recipes/user])
          author? @(rf/subscribe [:recipe/author?])]
      ^{:key recipe_id}
      [:> Grid {:item true}
       [:> Paper {:pb 4
                  :sx {:box-shadow 3
                       :max-width 600}

                  :direction "column"
                  :class-name (when author?
                                (str "transition hover:shadow-2xl hover:drop-shadow-2xl hover:-translate-y-1 ease-in-out delay-150 duration-500" "flex-wrap")
                                "flex-wrap")}
        [:> Grid {:item true :xs 12}
         [:> Grid {:container true
                   :p 2
                   :align-items "center"}
          [:> Typography {:variant "p"
                          :px 2
                          :class-name "text-3xl decoration-2 text-slate-700"}
           uid]]]
        [recipe-img]
        [footer]]
       #_[ingredients]])))
