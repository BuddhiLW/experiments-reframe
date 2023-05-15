(ns playground.components.form-group
  (:require
   ["@mui/material" :refer [FormControl Grid Input Typography]]
   [clojure.string :as str]
   [playground.utilites :as util]
   [re-frame.core :as rf]))

(defn form-group
  [{:keys [id label type values  & not-deref? text-area? on-key-down]}]
  [:> FormControl {:full-width true
                   :component "form"}
   [:> Grid {:container true
             :display "flex"
             :flex-direction "column"}
    [:> Grid {:item true
              :sx {:justify-content "center"}
              :class-name "pl-1 text-slate-700"
              :html-for id
              :font-weight 500
              :component "h4"}
     [:p label]]
    (let [errors @(rf/subscribe [:form/errors])
          input-error (get errors id)
          error (if input-error true false)
          is-empty? (str/blank? (id @values))
          validate (fn []
                     (if is-empty?
                       (rf/dispatch [:form/has-value? id])
                       (rf/dispatch [:form/clear-error id])))]

      [:> Grid {:item true}
       (if text-area?
         [:> Input {:variant "outlined"
                    :value (if not-deref?
                             (id values)
                             (id @values))
                    :on-blur validate
                    :type type
                    :on-change #(do
                                  (js/console.log @values)
                                  (swap! values assoc id (.. % -target -value)))
                    :full-width true
                    :multiline true
                    :rows 4
                    :error error
                    :helperText input-error}]
         [:> Input {:variant "outlined"
                    :value (if not-deref?
                             (id values)
                             (id @values))
                    :type type
                    :on-blur validate
                    :on-change #(do
                                  (js/console.log @values)
                                  (swap! values assoc id (.. % -target -value)))
                    :full-width true
                    :on-key-down on-key-down
                    :error error
                    :helperText input-error}])
       (when error
         [:> Typography {:variant "caption"
                         :color (util/color [:red :600])
                         :sx {:mt 1}}
          [:p input-error]])])]])
