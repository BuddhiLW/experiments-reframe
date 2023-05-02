(ns playground.auth.views.profile
  (:require
   ["@mui/material" :refer [Box TextField FormControl
                            Grid Typography Button Input
                            Card]]
   [playground.components.form-group :refer [form-group]]
   [playground.components.page-nav :refer [page-nav]]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn profile
  []
  (let [initial-values {:first-name "" :last-name ""}
        values (r/atom initial-values)]
    (fn []
      [:<>
       [:> Box {:justify-content "center"}
        [page-nav {:center "Log in"}]
        ;; rowSpacing={1} columnSpacing={{ xs: 1, sm: 2, md: 3 }}
        [:> Grid {:justify-content "center"
                  :direction "column"
                  :align-items "center"
                  :component "form"
                  :container true
                  :no-validate true
                  :auto-complete "off"
                  :py 8
                  :class-name "pl-10 ml-10"
                  :row-spacing 1}
         [:> Card {:variant "outlined"
                   :sx {:background-color "primary.white"
                        :min-height "300px"
                        :min-width "45rem"
                        :border-color "#AAA"
                        :pt 1
                        :px 5
                        :pb 5}}
          [:div.grid.grid-cols-1.gap-4
           [:> Typography {:variant "h4"
                           :font-weight "500"}
            "Personal Info"]
           [form-group {:id :first-name
                        :label "First name"
                        :type "text"
                        :values values}]
           [form-group {:id :last-name
                        :label "Last name"
                        :type "text"
                        :values values}]
           [:> Grid {:justify-content "flex-end"
                     :container true
                     :pt 1}
            [:> Button {:variant "contained"}
             "Save"]]]]
         [:> Card {:variant "outlined"
                   :sx {:background-color "primary.white"
                        :min-height "150px"
                        :min-width "45rem"
                        :border-color "#AAA"
                        :mt 5 :pt 1
                        :px 5 :pb 2}}
          [:div.grid.grid-cols-1.gap-4
           [:> Typography {:variant "h4"
                           :font-weight "500"}
            "Danger Zone!"]
           [:> Grid
            [:> Button {:variant "contained"
                        :color "error"}
             "Delete account"]]]]]]])))

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
     "Got an accound? Log in!"]]
   [:> Grid
    [:> Button
     {:variant "contained"
      :on-click #(rf/dispatch [:log-up @values])}
     "Sign up"]]])

#_(defn sign-up
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
