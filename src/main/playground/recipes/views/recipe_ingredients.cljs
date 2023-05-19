(ns playground.recipes.views.recipe-ingredients
  (:require
   ["@mui/icons-material/AddCircleOutline" :default AddCircleOutlineIcon]
   ["@mui/icons-material/Create" :default CreateIcon]
   ["@mui/icons-material/DeleteOutline" :default DeleteOutlineIcon]
   ["@mui/material" :refer [Box Button colors FormControl Grid IconButton
                            Paper Typography]]
   [clojure.string :as str]
   [playground.components.form-group :refer [form-group]]
   [playground.components.modal :refer [modal]]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [playground.helpers :as h]))

(defn ingredients-modal
  [{:keys [save values]}]
  [modal {:modal-key :ingredient-editor
          :title "Ingredient"
          :body
          [:form {:on-submit #(save % @values)}
           [:> Grid {:p 5
                     :align-items "center"}
            [:> FormControl  {:component "fieldset"
                              :variant "standard"
                              :fullWidth true
                              :margin "normal"
                              :size "small"}
             [form-group {:id :name
                          :label "Name"
                          :type "text"
                          :values values}]
             [form-group {:id :amount
                          :label "Amount"
                          :type "number"
                          :values values}]
             [form-group {:id :measure
                          :label "Measure"
                          :type "text"
                          :values values
                          :on-key-down #(when (= (.-which %) 13)
                                          (save % @values))}]]]]
          :footer
          [:> Grid {:display "flex"
                    :flex-direction "row"
                    :justify-content "space-between"
                    :px 5
                    :py 3
                    :sx {:border-radius "18px"
                         :box-shadow 10}
                    :bgcolor (get-in (js->clj colors :keywordize-keys true) [:grey :100])}
           (when-let [ingredient-id (:id @values)]
             [:> Button {:variant "contained"
                         :color "warning"
                         :on-click #(when (js/confirm "Are you sure?")
                                      (rf/dispatch [:recipe/delete-ingredient ingredient-id]))}
              "Delete"])
           [:> Button {:variant "contained"
                       :color "warning"
                       :on-click #(rf/dispatch [:recipes/close-modal])}
            "Cancel"]
           [:> Button {:variant "contained"
                       :color "primary"
                       :on-click #(save % @values)}
            "Save"]]}])

(defn ingredient-comp
  [{:recipe/keys [id name amount measure] :as ingredient} open-modal]
  (fn []
    (let [author? @(rf/subscribe [:recipe/author?])]
      [:<>
       [:> Grid {:container true
                 :p 2
                 :m 1
                 :mx 3
                 :sx {:border-radius "18px"
                      :box-shadow 10
                      :bgcolor (get-in (js->clj colors :keywordize-keys true) [:grey :100])
                      :justify-content "space-between"}}
        [:> Grid {:item true
                  :px 2
                  :sx {:justify-content "space-between"}}
         [:> Grid {:item true}
          [:> Typography {:variant "h5"
                          :font-weight 700
                          :class-name "text-slate-700"}
           name]]
         [:> Grid {:item true}
          [:> Typography {:variant "body2" :color "text.secondary"}
           amount " " measure]]]
        (when author?
          [:> Grid {:item true
                    :display "flex"
                    :align-items "right"
                    :flex-direction "column"}
           [:> IconButton {:on-click #(open-modal {:modal-key :ingredient-editor
                                                   :ingredient ingredient})}
            [:> CreateIcon {:color "primary"
                            :sx {:font-size "2rem"}}]]
           [:> IconButton {:on-click #(when (js/confirm "Are you sure you want to delete?")
                                        (rf/dispatch [:recipe/delete-ingredient id]))}
            [:> DeleteOutlineIcon {:color "warning"
                                   :sx {:font-size "2rem"}}]]])]])))

(defn header-ingredients
  [{:keys [open-modal values]}]
  [:> Grid {:container true :spacing 2 :p 2}
   [:> Typography {:p 2
                   :variant "h4"
                   :component "h4"
                   :align "center"
                   :font-weight 500
                   :class-name "text-slate-700"}
    "Ingredients"]
   (when-let [author? @(rf/subscribe [:recipe/author?])]
     [:> Box {:p 2}
      [:> IconButton {:on-click #(open-modal {:modal-key :ingredient-editor
                                              :ingredient values})
                      :class-name "text-slate-700"}
       [:> AddCircleOutlineIcon {:color "primary"
                                 :sx {:font-size "2rem"}}]]])])

(defn ingredients []
  (let [initial-values {:id nil
                        :name ""
                        :amount ""
                        :measure ""}
        values (r/atom initial-values)
        open-modal (fn [{:keys [modal-key ingredient]}]
                     (rf/dispatch [:recipes/open-modal modal-key])
                     (reset! values ingredient))
        save (fn [e {:keys [id name amount measure]}]
               (.preventDefault e)
               (when (and (not (str/blank? name))
                          (h/valid-number amount)
                          (not (str/blank? measure)))
                 (rf/dispatch [:recipe/save-ingredient {:id (or (keyword id) (keyword (str "ingredient-" (random-uuid))))
                                                        :name (str/trim name)
                                                        :amount (js/parseInt amount)
                                                        :measure (str/trim measure)}])
                 (reset! values initial-values)))]
    (fn []
      (let [ingredients @(rf/subscribe [:recipe/ingredients])
            author? @(rf/subscribe [:recipe/author?])]
        [:<>
         (when author?
           #_[ingredients-modal {:values values
                                 :save save}])
         [:> Box {:pt 2
                  :display "flex"
                  :class-name "flex-wrap"}
          [:> Paper {:p 2 :variant "outlined"
                     :pl 2
                     :sx {:border-radius "10px"}
                     :display "flex"
                     :class-name "flex-wrap"}

             ;; Header ingredients
           #_[header-ingredients {:open-modal open-modal
                                  :values initial-values}]
             ;; Ingredients
           [:> Grid {:container true :spacing 2
                     :pl 2
                     :border-radius "10px"
                     :class-name "flex-wrap"
                     :sx {:min-width 0
                          :max-width "600px"}}
            [:hr {:class-name "w-full border-0 h-px my-2 bg-gray-200 dark:bg-gray-700"}]
            #_[:<>
               (for [ingredient ingredients]
                 ^{:key (:id ingredient)}
                 [ingredient-comp ingredient open-modal])]]]]]))))

              ;; Modal add a Ingredient
