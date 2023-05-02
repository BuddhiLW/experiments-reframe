(ns playground.auth.views.log-in
  (:require
    ;; Input from '@mui/joy/Input']
   ;; ["@mui/joy" :refer [Input]]
   ["@mui/material" :refer [Box TextField FormControl
                            Grid Typography Button Input]]
   [playground.components.form-group :refer [form-group]]
   [playground.components.page-nav :refer [page-nav]]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn footer
  [values]
  [:> Grid {:container true
            :justify-content "space-between"
            :class-name "grid grid-cols-2 gap-4"}
   [:> Grid {:py 1}
    [:> Typography
     {:font-size 17
      :font-weight 700
      :color "primary.black"
      :component "a"
      :href "#sign-up"
      :on-click #(rf/dispatch [:set-active-nav :sign-up])}
     "New to Cheffy? Create an account!"]]
   [:> Grid
    [:> Button
     {:variant "contained"
      :on-click #(rf/dispatch [:log-in @values])}
     "Log-in"]]])

(defn log-in
  []
  (let [initial-values {:email "" :password ""}
        values (r/atom initial-values)]
    (fn []
      [:<>
       [:> Box {:justify-content "center"}
        [page-nav {:center "Log in"}]
        [:> Grid {:justify-content "center"
                  :component "form"
                  :container true
                  :no-validate true
                  :auto-complete "off"
                  :py 8
                  :class-name "pl-10 ml-10"}
         [:div.grid.grid-cols-1.gap-4
          [form-group {:id :email
                       :label "E-mail"
                       :type "text"
                       :values values}]
          [form-group {:id :password
                       :label "Password"
                       :type "password"
                       :values values}]
          [footer values]]]]])))
