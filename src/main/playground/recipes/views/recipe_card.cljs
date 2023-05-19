(ns playground.recipes.views.recipe-card
  (:require
   ["@mui/icons-material/AccessAlarm" :default AccessAlarm]
   ["@mui/icons-material/FavoriteOutlined" :default FavoriteOutlined]
   ["@mui/material" :refer [Grid Paper Typography Card CardMedia CssBaseline]]
   ["@mui/material/styles" :refer [ThemeProvider]]
   [playground.router :as router]
   [playground.theme :refer [cards]]))
(defn recipe-card [recipe]
  (let [{:recipe/keys [name favorite_count prep_time img recipe_id]} recipe
        log (js/console.log name favorite_count prep_time img recipe_id)]
    ^{:key recipe_id}
    [:> Grid {:px 2}
     [:> Paper {:pb 4
                :sx {:box-shadow 3}
                :class-name "transition hover:shadow-2xl hover:drop-shadow-2xl hover:-translate-y-1 ease-in-out delay-150 duration-500"}
      [:> CssBaseline]
      [:> ThemeProvider {:theme cards}
       [:> Card {:p 2
                 :sx {:box-shadow 10}
                 :class-name "hover:shadow-2xl"}
        [:> CardMedia {:class "img-card"
                       :as "a"
                       :href (router/path-for :recipe :recipe-id recipe_id)
                       :sx {:height 300
                            :width 400}
                       :image (str (or img
                                       "img/placeholder.jpg"))}]
        [:> Grid {:container true}
         [:> Grid {:item true :xs 12 :pt 2}
          [:> Typography {:variant "p"
                          :px 2
                          :class-name "text-3xl decoration-2 text-slate-700"}
           name]]]
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
           [:> FavoriteOutlined {:color "primary"
                                 :sx {:font-size 45}
                                 :class-name "pr-2"}]
           [:> Typography {:variant "body1"
                           :class-name "text-slate-700"}
            (str favorite_count)]]]

         [:> Grid {:item true
                   :xs 5
                   :p 2}
          [:> Grid {:container true
                    :sx
                    {:flex-direction "row"
                     :justify-content "flex-end"
                     :align-items "center"}}
           [:> AccessAlarm {:color "primary"
                            :sx {:font-size 45}
                            :class-name "pr-3"}]
           [:> Typography {:variant "body1"
                           :class-name "text-slate-700"}
            (str prep_time " min")]]]]]]]]))
