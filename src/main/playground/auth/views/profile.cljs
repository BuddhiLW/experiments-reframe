(ns playground.auth.views.profile
  (:require
   ["@mui/material" :refer [Box Grid Typography
                            Button Card ThemeProvider]]
   [playground.auth.subs]
   [playground.components.form-group :refer [form-group]]
   [playground.components.page-nav :refer [page-nav]]
   [playground.theme :refer [buttons]]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [playground.router :as router]))

(defn footer
  [values]
  [:div.grid.grid-cols-1.gap-4
   [:> Typography {:variant "h4"
                   :font-weight "500"}
    "Danger Zone!"]
   [:> Grid
    [:> Button {:variant "contained"
                :color "error"
                :on-click #(when (js/confirm "This will delete your account")
                             (rf/dispatch [:auth/delete-account]))}
     "Delete account"]]])

(defn profile
  []
  (let [{:keys [first-name last-name]} @(rf/subscribe [:auth/user-profile])
        initial-values {:first-name first-name :last-name last-name}
        values (r/atom initial-values)]
    (fn []
      [:<>
       [:> Box {:justify-content "center"}
        [page-nav {:center
                   "Log in"
                   :right
                   [:<>
                    [:> ThemeProvider {:theme buttons}]
                    [:> Grid {:sx {:justify-content "center"
                                   :align-items "center"
                                   :align-self "center"}
                              :item true
                              :pt 1}
                     [:> Button {:variant "contained"
                                 :color "primary"
                                  ;; :href (router/routes :recipes)
                                 :on-click #(rf/dispatch [:auth/log-out])}
                      "Log out"]]]}]
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
            [:> Button {:variant "contained"
                        :on-click #(rf/dispatch [:update/profile @values])}
             "Save"]]]]
         [:> Card {:variant "outlined"
                   :sx {:background-color "primary.white"
                        :min-height "150px"
                        :min-width "45rem"
                        :border-color "#AAA"
                        :mt 5 :pt 1
                        :px 5 :pb 2}}
          [footer]]]]])))
