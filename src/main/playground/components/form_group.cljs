(ns playground.components.form-group
  (:require
   ["@mui/material" :refer [FormControl Grid Input]]))

(defn form-group
  [{:keys [id label type values]}]
  [:> FormControl {:container true
                   :class-name "grid grid-cols-1 gap-4"}
   [:> Grid
    [:> Grid {:class-name "pl-1"
              :html-for id
              :font-weight 500
              :component "h4"}
     [:p label]]
    [:> Input {:variant "outlined"
               :value (id @values)
               :type type
               :on-change #(swap! values assoc id (.. % -target -value))
               :full-width true}]]])
