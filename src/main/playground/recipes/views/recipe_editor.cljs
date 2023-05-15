(ns playground.recipes.views.recipe-editor
  (:require
   ["@mui/icons-material/AddCircleOutline" :default AddCircleOutlineIcon]
   ["@mui/material" :refer [Button colors Grid IconButton Typography]]
   [clojure.string :as str]
   [playground.components.form-group :refer [form-group]]
   [playground.components.modal :refer [modal]]
   [playground.utilites :as util]
   [re-frame.core :as rf]
   [reagent.core :as r]
   [playground.helpers :as h]))

(defn modal-recipe-editor
  [{:keys [values save]}]
  (modal {:modal-key :recipe-editor
          :title "Edit Recipe"
          :body
          [:> Grid {:container true
                    :p 5
                    :pt 0
                    :align-items "center"
                    :justify-content "center"
                    :class-name "w-full"}
           [:form {:on-submit #(save % @values)
                   :class-name "w-full"}
                     ;; :min-width "50vw"}
            [:> Grid {:display "flex"
                      ;; :direction "row"
                      :flex-direction "row"
                      :justify-content "center"
                      :align-items "center"
                      :fullWidth true
                      :class-name "w-full"
                      :gap 3}
             [:> Grid {:item true
                       :xs 8}
              [form-group {:id :name
                           :label "Name"
                           :type "text"
                           :values values}]]
             [:> Grid {:item true
                       :xs 4}
              [form-group {:id :prep-time
                           :label "Cooking time (min)"
                           :type "number"
                           :values values
                           :on-key-down #(when (= (.-which %) 13)
                                           (save % @values))}]]]]]
          :footer
          [:> Grid {:display "flex"
                    :flex-direction "row"
                    :justify-content "space-between"
                    :px 5
                    :py 3
                    :sx {:border-radius "18px"
                         :box-shadow 10}
                    :bgcolor (get-in (js->clj colors :keywordize-keys true) [:grey :100])}
           (when-let [author? @(rf/subscribe [:recipes/recipe :author?])]
             [:> Button {:variant "contained"
                         :sx {:bgcolor (get-in (js->clj colors :keywordize-keys true) [:red :600])}
                         :on-click #(when (js/confirm "Are you sure?")
                                      (rf/dispatch [:recipe/delete-recipe]))}
              "Delete"])
           [:> Button {:variant "contained"
                       :color "warning"
                       :on-click #(rf/dispatch [:recipes/close-modal])}
            "Cancel"]
           [:> Button {:variant "contained"
                       :color "primary"
                       :on-click #(save % @values)}
            "Save"]]}))

(defn recipe-editor
  []
  (let [initial-values {:name "" :prep-time ""}
        values (r/atom initial-values)
        open-modal (fn [{:keys [modal-name recipe]}]
                     (rf/dispatch [:recipes/open-modal modal-name])
                     (reset! values recipe))
        save (fn [e {:keys [name prep-time]}]
               (.preventDefault e)
               (when (and (not (str/blank? name))
                          (h/valid-number prep-time))
                 (rf/dispatch [:recipe/save-recipe {:name name :prep-time prep-time}])
                 (reset! values initial-values)))]
    (fn []
      (let [{:keys [name prep-time] :as recipe} @(rf/subscribe [:recipes/recipe])
            active-page @(rf/subscribe [:active-page])]
        [:> Grid {:item true}
         [modal-recipe-editor {:values values :save save}]
         (if (= active-page :recipe)
           [:> Grid {:sx
                     {:color (util/color [:indigo :700])
                      "&:hover" {:color (util/color [:pink :500])}}}
            [:> Typography {:variant "h2"
                            :as "a"
                            :color "inherit"
                            :justify-content "center"
                            :font-size "2.5rem"
                            :font-weight 700
                            :on-click #(open-modal {:modal-name :recipe-editor
                                                    :recipe recipe})}
             (str name)]]
           [:> IconButton {:on-click #(rf/dispatch [:recipes/open-modal :recipe-editor])}
            [:> AddCircleOutlineIcon
             {:color "primary"
              :sx {:font-size "2em"}}]])]))))
