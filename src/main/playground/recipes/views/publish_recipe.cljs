(ns playground.recipes.views.publish-recipe
  (:require
   ["@mui/material" :refer [Button Grid]]
   [playground.components.form-group :refer [form-group]]
   [playground.components.modal :refer [modal]]
   [playground.helpers :as h]
   [playground.utilites :as util]
   [re-frame.core :as rf]
   [reagent.core :as r]))

(defn public-recipe-modal
  [{:keys [values]}]
  (fn []
    (let [publish (fn [e {:keys [price]}]
                    (.preventDefault e)
                    (when (h/valid-number price)
                      (rf/dispatch [:recipe/publish {:price (js/parseInt price)}])
                      (reset! values {:price ""})))]
      (modal {:modal-key :publish-recipe
              :title "Publish Recipe"
              :body
              [:> Grid {:container true
                        :sx {:justify-content "center"}
                        :pb 5
                        :pt 2}
               [:form {:on-submit (fn [e]
                                    (.preventDefault e)
                                    (publish e @values))
                       :class-name "w-full"}
                [:> Grid {:display "flex"
                          :direction "column"
                          :sx {:justify-content "center"}
                          :pb 5
                          :px 5
                          :pt 2}
                 [form-group {:id :price
                              :label "Price"
                              :type "number"
                              :values values
                              :on-key-down #(when (= (.-which %) 13)
                                              (publish % @values))}]]]]
              :footer
              (fn []
                (let [public? @(rf/subscribe [:recipe/public?])]
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
                    (if public?
                      [:> Button {:variant "contained"
                                  :sx {:bgcolor (util/color [:red :500])}
                                  :on-click #(rf/dispatch [:recipe/unpublish])}
                       "Unpublish"]
                      [:> Button {:variant "contained"
                                  :color "primary"
                                  :on-click #(publish % @values)}
                       "Publish"])]]))}))))

(defn publish-recipe
  []
  (let [initial-values {:price ""}
        values (r/atom initial-values)
        open-modal (fn [{:keys [modal-name recipe]}]
                     (rf/dispatch [:recipes/open-modal modal-name])
                     (reset! values (if recipe recipe initial-values)))]
    (fn []
      (let [{:keys [price]} @(rf/subscribe [:recipes/recipe])
            public? @(rf/subscribe [:recipe/public?])]
        [:> Grid
         (cond
           public? [:> Button {:on-click #(open-modal {:modal-name :publish-recipe})
                               :sx {:bgcolor (util/color [:red :500])
                                    :color "white"}}
                    (h/format-price price)]

           (not public?) [:> Button {:on-click #(open-modal {:modal-name :publish-recipe
                                                             :recipe {:price price}})
                                     :sx {:bgcolor (util/color [:red :500])
                                          :color "white"}}
                          "Publish"])
         [public-recipe-modal {:values values}]]))))
