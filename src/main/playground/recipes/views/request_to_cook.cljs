(ns playground.recipes.views.request-to-cook
  (:require
   ["@mui/material" :refer [Button FormControl Grid]]
   [clojure.string :as str]
   [playground.components.form-group :refer [form-group]]
   [playground.components.modal :refer [modal]]
   [playground.helpers :as h]
   [playground.utilites :as util]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn request-to-cook-modal
  [{:keys [values]}]
  (fn []
    (let [request (fn [{:keys [message]}]
                    (rf/dispatch [:recipe/request-message {:message (str/trim message)}])
                    (reset! values {:message ""}))]
      (modal {:modal-key :request-to-cook
              :title "Order"
              :body
              [:form
               [:> Grid {:container true
                         :sx {:justify-content "center"}
                         :pb 5
                         :pt 2}
                [:> FormControl  {:component "fieldset"
                                  :variant "filled"
                                  :display "flex"
                                  :full-width true
                                  :margin "normal"
                                  :size "small"
                                  :sx {:justify-content "center"}}
                 [:> Grid {:display "flex"
                           :direction "column"
                           :sx {:justify-content "center"}
                           :pb 5
                           :px 5
                           :pt 2}
                  [form-group {:id :message
                               :label "Describe the order (event, number of people, etc.)"
                               :type "text"
                               :values values}]]]]]
              :footer
              ;; (fn []
              [:<>
               [:> Grid {:display "flex"
                         :flex-direction "row"
                         :justify-content "space-between"
                         :px 5
                         :py 3
                         :sx {:border-radius "18px"
                              :box-shadow 10}
                         :bgcolor (util/color [:grey :100])}
                [:> Button {:variant "contained"
                            :color "warning"
                            :on-click #(rf/dispatch [:recipes/close-modal])}
                 "Cancel"]
                [:> Button {:variant "contained"
                            :color "primary"
                            :on-click #(request @values)}
                 "Request"]]]}))))

(defn request-to-cook
  []
  (let [initial-values {:message ""}
        values (r/atom initial-values)
        open-modal (fn [{:keys [modal-name recipe]}]
                     (rf/dispatch [:recipes/open-modal modal-name])
                     (reset! values (if recipe recipe initial-values)))]
    (fn []
      (let [{:keys [price]} @(rf/subscribe [:recipes/recipe])]
        [:> Grid
         [:> Button {:on-click #(open-modal {:modal-name :request-to-cook})
                     :sx {:bgcolor (util/color [:red :500])
                          :color "white"}}
          "Order for " (h/format-price price)]
         [request-to-cook-modal {:values values}]]))))
