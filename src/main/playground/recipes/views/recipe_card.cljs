(ns playground.recipes.views.recipe-card
  (:require
;; import AccessAlarmIcon from '@mui/icons-material/AccessAlarm';
   ["@mui/icons-material/AccessAlarm" :default AccessAlarm]
   ;; ["@mui/icons-material/FavoriteBorderOutlined" :default FavoriteBorderOutlined]
   ["@mui/icons-material/FavoriteOutlined" :default FavoriteOutlined]
   #_["@mui/icons-material" :refer [DeleteOutlined EditOutlined FavoriteOutlined FavoriteBorderOutlined
                                    ShareOutlined StarOutlined StarBorderOutlined StarBorderRounded StarRounded]]
   ["@mui/material" :refer [Grid Paper Typography Card CardMedia CssBaseline]]
   ["@mui/material/styles" :refer [ThemeProvider]]
   [playground.router :as router]
   [playground.theme :refer [cards]]))

(defn recipe-card [recipe]
  (let [{:keys [name saved-count prep-time img id]} recipe]
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
                       :href (router/path-for :recipe :recipe-id id)
                       :sx {:height 300
                            :width 400}
                       :image (str (or img
                                       "img/placeholder.jpg"))}]
        [:> Grid {:container true :xs 12}
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
            (str saved-count)]]]

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
            (str prep-time " min")]]]]]]]]))
