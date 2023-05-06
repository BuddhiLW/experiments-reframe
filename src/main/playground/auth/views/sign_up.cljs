(ns playground.auth.views.sign-up
  (:require
   ["@mui/material" :refer [Box Button Grid Typography]]
   [playground.components.form-group :refer [form-group]]
   [playground.components.page-nav :refer [page-nav]]
   [playground.router :as router]
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
      :href (router/path-for :log-in)
      :on-click #(rf/dispatch [:set-active-page :log-in])}
     "Got an accound? Log in!"]]
   [:> Grid
    [:> Button
     {:variant "contained"
      :on-click #(rf/dispatch [:sign-up @values])}
     "Sign up"]]])

(defn sign-up
  []
  (let [initial-values {:email "" :password ""
                        :first-name "" :last-name ""}
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
          [form-group {:id :first-name
                       :label "First name"
                       :type "text"
                       :values values}]
          [form-group {:id :last-name
                       :label "Last name"
                       :type "text"
                       :values values}]
          [form-group {:id :email
                       :label "E-mail"
                       :type "text"
                       :values values}]
          [form-group {:id :password
                       :label "Password"
                       :type "password"
                       :values values}]
          [footer values]]]]])))
