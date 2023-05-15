(ns playground.recipes.views.recipe-steps
  (:require
   ["@mui/icons-material/Create" :default CreateIcon]
   ["@mui/icons-material/DeleteOutline" :default DeleteOutlineIcon]
   ["@mui/material" :refer [Button colors FormControl Grid IconButton Paper
                            Typography]]
   [clojure.string :as str]
   [playground.components.form-group :refer [form-group]]
   [playground.components.modal :refer [modal]]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn modal-step-editor
  [{:keys [values save]}]
  [modal {:modal-key :step-editor
          :title "Describe the step"
          :body
          [:form
           [:> Grid {:p 5
                     :align-items "center"}
            [:> FormControl  {:component "fieldset"
                              :variant "standard"
                              :fullWidth true
                              :margin "normal"
                              :size "small"}
             [form-group {:id :desc
                          :label "Description"
                          :type "text"
                          :text-area? true
                          :values values}]]]]
          :footer
          [:> Grid {:display "flex"
                    :flex-direction "row"
                    :justify-content "space-between"
                    :px 5
                    :py 3
                    :sx {:border-radius "18px"
                         :box-shadow 10}
                    :bgcolor (get-in (js->clj colors :keywordize-keys true) [:grey :100])}
           (when-let [step-id (:id @values)]
             [:> Button {:variant "contained"
                         :color "warning"
                         :on-click #(when (js/confirm "Are you sure?")
                                      (rf/dispatch [:recipe/delete-step step-id]))}
              "Delete"])
           [:> Button {:variant "contained"
                       :color "warning"
                       :on-click #(rf/dispatch [:recipes/close-modal])}
            "Cancel"]
           [:> Button {:variant "contained"
                       :color "primary"
                       :on-click #(save @values)}
            "Save"]]}])

(defn recipe-steps []
  (let [initial-values {:id nil
                        :order ""
                        :desc ""}
        values (r/atom initial-values)
        open-modal (fn [{:keys [modal-key step]}]
                     (rf/dispatch [:recipes/open-modal modal-key])
                     (reset! values step))
        save (fn [{:keys [id order desc]}]
               (when (not (str/blank? desc))
                 (rf/dispatch [:recipe/save-step {:id (or (keyword id) (keyword (str "step-" (random-uuid))))
                                                  :order order
                                                  :desc desc}])
                 (reset! values initial-values)))]
    (fn []
      (let [steps @(rf/subscribe [:recipe/steps])
            author? @(rf/subscribe [:recipe/author?])]
        [:> Grid {:item true
                  :max-width "100%"
                  :xs 6
                  :sm 6
                  :md 6
                  :lg 7
                  :xl 8}
                  ;; :p 3
                  ;; :m 2}
         (when author?
           [modal-step-editor {:values values
                               :save save}])
         [:> Paper {:variant "outlined"
                    :sx {:min-width "100%"}}
          [:> Grid {:container true
                     ;; :xs 12
                    ;; :direction "row"
                    :align-items "center"}
           [:> Grid {:item true
                     :xs (if author? 11 12)
                     :pb 0
                     :align-items "center"}
            [:> Typography {:variant "h4"
                            :align "center"
                            :font-weight 500
                            :class-name "text-slate-700"}
             "Preparation"]]
           (when author?
             [:> Grid {:item true
                       :align-items "center"
                       :xs 1}
              [:> IconButton {:on-click #(open-modal {:modal-key :step-editor
                                                      :step initial-values})}
               [:> CreateIcon {:color "primary"
                               :sx {:font-size "2rem"}}]]])
           [:hr {:class-name "w-full border-0 h-px my-2 bg-gray-200 dark:bg-gray-700"}]]
          (for [[index {:keys [id desc] :as values}] (map-indexed (fn [i m] [i m]) steps)]
            ^{:key id}
            [:<>
             [:> Grid {:container true
                       :direction "row"
                        ;; :xs 12
                       :justify-content "space-between"}
              [:> Grid {:item true
                        :xs 6
                        :px 3}
               [:> Typography {:variant "h4"
                               :class-name "text-slate-600"} "Step " (inc index)]]
              (when author?
                [:> Grid {:item true
                          :display "flex"
                          :sx {:justify-content "flex-end"}
                          :pr 4
                          :xs 1}
                 [:> IconButton {:on-click #(open-modal {:modal-key :step-editor
                                                         :step values})}
                  [:> CreateIcon {:color "primary"
                                  :sx {:font-size "2rem"}}]]
                 [:> IconButton {:on-click #(when (js/confirm "Are you sure you want to delete?")
                                              (rf/dispatch [:recipe/delete-step id]))}
                  [:> DeleteOutlineIcon {:color "warning"
                                         :sx {:font-size "2rem"}}]]])]
             [:hr {:class-name "w-1/4 border-0 h-px my-2 bg-gray-200 dark:bg-gray-700"}]
             [:> Grid {:p 3}
              [:> Typography
               {:variant "body1"
                :font-size "1.2rem"
                :component "p"
                :class-name "text-slate-800"}
               desc]]])]]))))
