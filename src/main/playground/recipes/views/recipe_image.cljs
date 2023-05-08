(ns playground.recipes.views.recipe-image
  (:require
   ["@mui/material" :refer [Card CardMedia Modal Typography Grid Button Box
                            FormControl]]
   [playground.components.form-group :refer [form-group]]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn recipe-img []
  (let [initial-values {:img ""}
        values (r/atom initial-values)
        {:keys [img name]} @(rf/subscribe [:recipes/recipe])
        author? @(rf/subscribe [:recipes/author?])
        save (fn [img]
               (rf/dispatch [:upsert-image img])
               (reset! values initial-values))
        open-modal (fn [{:keys [modal-name recipe]}]
                     (rf/dispatch [:recipes/open-modal modal-name])
                     (reset! values recipe))]
    (fn []
      (let [active-modal @(rf/subscribe [:recipes/active-modal])]
        [:<>
         [:> Card {:p 2
                   :sx {:box-shadow 10}
                   :class-name "hover:shadow-2xl"}
          [:> CardMedia {:class (when author? "editable")
                         :sx {:height 300
                              :width 400}
                         :image (str (or img
                                         "img/placeholder.jpg"))
                         :alt name
                         :on-click (when author?
                                     #(open-modal {:modal-name :image-editor
                                                   :recipe {:img img}}))}]]
         (when author?
           (do (js/console.log author? active-modal ":image-editor")
               [:> Modal {:open (= active-modal :image-editor)
                          :on-close #(rf/dispatch [:recipes/close-modal])}
                [:> Box {:sx
                         {:position "absolute"
                          :top "50%"
                          :left "50%"
                          :transform "translate(-50%, -50%)"
                          :width 800
                          :bgcolor "background.paper"
                          :border "1px solid #AFAFAF"
                          :p 4}}
                 [:> Typography {:pb 2
                                 :variant "h4"
                                 :align "center"}
                  "Image"]
                 [:> Grid {:pb 5
                           :align-items "center"}
                  [:> FormControl
                   {:component "fieldset"
                    :variant "standard"
                    :fullWidth true
                    :margin "normal"
                    :size "small"}
                   [form-group {:id :img
                                :label "URL"
                                :type "text"
                                :values values}]]]
                 [:> Grid {:display "flex"
                           :flex-direction "row"
                           :justify-content "space-between"}
                  [:> Button {:variant "contained"
                              :color "warning"
                              :on-click #(rf/dispatch [:recipes/close-modal])}
                   "Cancel"]
                  [:> Button {:variant "contained"
                              :color "primary"
                              :on-click #(save values)}
                   "Save"]]]]))]))))
