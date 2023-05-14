(ns playground.components.form-group
  (:require
   ["@mui/material" :refer [FormControl Grid Input TextareaAutosize]]))

(defn form-group
  [{:keys [id label type values & not-deref? text-area?]}]
  [:> FormControl {:container "true"
                   :class-name "grid grid-cols-1 gap-4"}
   [:> Grid
    [:> Grid {:class-name "pl-1 text-slate-700"
              :html-for id
              :font-weight 500
              :component "h4"}
     [:p label]]
    (if text-area?
      [:> Input {:variant "outlined"
                 :value (if not-deref?
                          (id values)
                          (id @values))
                 :type type
                 :on-change #(do
                               (js/console.log @values)
                               (swap! values assoc id (.. % -target -value)))
                 :full-width true
                 :multiline true
                 :rows 4}]
      [:> Input {:variant "outlined"
                 :value (if not-deref?
                          (id values)
                          (id @values))
                 :type type
                 :on-change #(do
                               (js/console.log @values)
                               (swap! values assoc id (.. % -target -value)))
                 :full-width true}])]])
