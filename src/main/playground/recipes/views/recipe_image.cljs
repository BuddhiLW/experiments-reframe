(ns playground.recipes.views.recipe-image
  (:require
   ["@mui/material" :refer [Button Card CardMedia colors Grid]]
   [clojure.string :as str]
   [playground.components.form-group :refer [form-group]]
   [playground.components.modal :refer [modal]]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn recipe-img []
  (let [initial-values {:img ""}
        values (r/atom initial-values)
        author? @(rf/subscribe [:recipe/author?])
        save (fn [e {:keys [img]}]
               (.preventDefault e)
               (when (not (str/blank? img))
                 (rf/dispatch [:recipe/upsert-image {:img img}])
                 (reset! values initial-values)))
        open-modal (fn [{:keys [modal-name recipe]}]
                     (rf/dispatch [:recipes/open-modal modal-name])
                     (reset! values recipe))]
    (fn []
      (let [{:recipe/keys [img name]} @(rf/subscribe [:recipes/recipe])]
        [:<>
         [:> Card {:p 2
                   :sx {:box-shadow 10
                        :border-radius "10px"}
                   :class-name "hover:shadow-2xl"}
          [:> CardMedia {:class (when author? "editable")
                         :sx {:height 500
                              :width 600}
                         :image (str (or img
                                         "../img/placeholder.jpg"))
                         :alt name
                         :on-click (when author?
                                     #(open-modal {:modal-name :image-editor
                                                   :recipe {:img img}}))}]]
         (when author?
           [modal {:modal-key :image-editor
                   :title "Image"
                   :body
                   [:form {:on-submit #(save % values)
                           :class-name "w-full"}
                    [:> Grid {:p 5
                              :align-items "center"}
                     [form-group {:id :img
                                  :label "URL"
                                  :type "text"
                                  :values values
                                  :on-key-down #(when (= (.-which %) 13)
                                                  (save % values))}]]]
                   :footer
                   [:> Grid {:display "flex"
                             :flex-direction "row"
                             :justify-content "space-between"
                             :px 5
                             :py 3
                             :sx {:border-radius "18px"
                                  :box-shadow 10}
                             :bgcolor (get-in (js->clj colors :keywordize-keys true) [:grey :100])}
                    [:> Button {:variant "contained"
                                :color "warning"
                                :on-click #(rf/dispatch [:recipes/close-modal])}
                     "Cancel"]
                    [:> Button {:variant "contained"
                                :color "primary"
                                :on-click #(save % values)}
                     "Save"]]}])]))))
