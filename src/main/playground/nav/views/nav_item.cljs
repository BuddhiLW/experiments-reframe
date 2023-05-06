(ns playground.nav.views.nav-item
  (:require
   ["@mui/material" :refer [Button Typography Tooltip
                            ThemeProvider]]
   [playground.theme :refer [nav]]))

(defn box
  [{:keys [id name href dispatch active-page]}]
  ^{:key id}
  [:> ThemeProvider {:theme nav}
   [:> Tooltip {:title (str "Hello, from " name) :follow-cursor true}
    [:> Button {:display "flex"
                :alignitems "center"
                :on-click dispatch
                :ml 2
                :pb 10
                :sx {"&:hover" {:background-color "primary.main"}
                     :as "a"}
                :href href}
     (if (= active-page id)
       [:> Typography {:variant "h2"
                       :font-size "1.3em"
                       :color "primary.secondary"
                       :sx {"&:hover" {:color "primary.tertiary"}}
                       :border-bottom "2px solid #102A43"}
        name]
       [:> Typography {:variant "h6"
                       :font-size "1.1em"
                       :sx {"&:hover" {:color "primary.secondary"}}
                       :color "primary.tertiary"}
        name])]]])
