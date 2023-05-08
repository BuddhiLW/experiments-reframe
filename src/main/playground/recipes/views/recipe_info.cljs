(ns playground.recipes.views.recipe-info
  (:require
   ["@mui/icons-material/AccessAlarm" :default AccessAlarm]
   ["@mui/icons-material/FavoriteBorderOutlined" :default FavoriteBorderOutlined]
   ["@mui/icons-material/FavoriteOutlined" :default FavoriteOutlined]
   ["@mui/material" :refer [Grid Paper Typography Card CardMedia CssBaseline Box Grid Paper Typography Card CardMedia CardContent CardActions IconButton]]
   ["@mui/material/styles" :refer [ThemeProvider]]
   [playground.recipes.events]
   [playground.recipes.views.recipe-image :refer [recipe-img]]
   [playground.theme :refer [cards]]
   [re-frame.core :as rf]))

(defn footer
  [can-save? saved? prep-time saved-count id]
  [:> Grid {:container true}
   [:> Grid {:item true
             :p 2
             :xs 7
             :sx {:display "flex"
                  :flex-direction "row"
                  :align-items "center"}}
    [:> Grid {:container true
              :sx
              {:flex-direction "row"
               :align-items "center"}}
     (if can-save?
       [:> CardActions
        [:> IconButton
         [:> FavoriteBorderOutlined {:class-name "text-pink-500"
                                     :sx {:font-size 40}
                                     :on-click #(rf/dispatch [:recipes/save-recipe id])
                                     :href "#"}]]]
       [:> CardActions
        [:> IconButton
         [:> FavoriteOutlined {:class-name "text-red-500"
                               :on-click  (when saved? #(rf/dispatch [:recipes/unsave-recipe id]))
                               :sx {:font-size 40}}]]])
     [:> Typography {:variant "body1"
                     :class-name "text-slate-700"}
      (str saved-count)]]]
   [:> Grid {:align-items "center"
             :container true
             :xs 5
             :p 2
             :sx
             {:flex-direction "row"
              :justify-content "flex-end"}}
    [:> AccessAlarm {:color "primary"
                     :sx {:font-size 45}
                     :class-name "pr-3"}]
    [:> Typography {:variant "body1"
                    :class-name "text-slate-700"}
     (str prep-time " min")]]])

(defn recipe-info
  []
  (let [{:keys [id cook saved-count prep-time]} @(rf/subscribe [:recipes/recipe])
        {:keys [uid saved]} @(rf/subscribe [:recipes/user])
        logged-in? @(rf/subscribe [:logged-in?])
        saved? (contains? saved id)
        author? @(rf/subscribe [:recipes/author?])
        can-save? (and logged-in? (not author?) (not saved?))]
    [:> Grid {:px 2
              :pt 4}
     [:> Paper {:pb 4
                :sx {:box-shadow 3}
                :class-name (when author? "transition hover:shadow-2xl hover:drop-shadow-2xl hover:-translate-y-1 ease-in-out delay-150 duration-500")}
      [:> CssBaseline]
      [:> ThemeProvider {:theme cards}
       [:> Grid {:item true :xs 12}
        [:> Grid {:container true
                  :xs 12
                  :p 2
                  :align-items "center"}
         [:> Typography {:variant "p"
                         :px 2
                         :class-name "text-3xl decoration-2 text-slate-700"}
          cook]]]
       [recipe-img]
       [footer can-save? saved? prep-time saved-count id]]]]))
