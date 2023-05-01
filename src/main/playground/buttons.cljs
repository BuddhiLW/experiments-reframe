(ns playground.buttons
  (:require
   [playground.theme :refer [cheffy-theme theme]]
   ["@mui/material" :as mui]
   ["@mui/icons-material" :as mui-icons]
   ["react-bootstrap" :as bt]))
;; [reagent-mui.material.icon-button :as mb]))
;; ["@mui/icons-material/ShoppingCart" :as ShoppingCart]
;; ["@mui/styles" :as mui-styles]))
;; [cljsjs.material-ui :as m]

(defn buttons-component
  []
  [:div {:style {:display "flex"
                 :flex-direction "row"
                 :flex-wrap "wrap"
                 :align-items "center"
                 :justify-content "space-around"
                 :height "100vh"
                 :width "100vw"}}
   [:div
    [:> mui/CssBaseline]
    [:> mui/ThemeProvider {:theme theme}
     [:> mui/Checkbox]
     [:> mui/Button {:variant "contained"}
      [:> mui-icons/ShoppingCart "add"]
      "Hello World"]]]
   [:> bt/ThemeProvider {:theme cheffy-theme}
    [:> bt/Button {:variant "primary"} "Hello World"]]
   [:div
    [:> mui/IconButton {:color "primary"} "icon"]]
   [:div
    [:> mui/Button {:variant "contained"} "Hello World"]]])
