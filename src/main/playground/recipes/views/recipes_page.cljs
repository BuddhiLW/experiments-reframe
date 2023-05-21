(ns playground.recipes.views.recipes-page
  (:require
   ["@mui/material" :refer [Grid Box Typography Button]]
   [playground.auth.subs]
   [playground.components.page-nav :refer [page-nav]]
   [playground.recipes.subs]
   [playground.recipes.views.recipe-editor :refer [recipe-editor]]
   [playground.recipes.views.recipe-list :refer [recipe-list]]
   [re-frame.core :as rf]))
;;    ["@stripe/react-stripe-js" :refer [CardElement Elements useStripe useElements]]
;;    ["@stripe/stripe-js" :refer [loadStripe]]
;;    [reagent.core :as r]
;;    [cljs.core.async :refer [go <!]]
;;    [cljs.core.async.interop :refer-macros [<p!]]))

;; (def stripe-test-key "pk_test_51MRNOfEu1RjbDj23MvPdmtUlmdPVE0gDUeXTHM2WXFmCCH25q4vNgQWxrZVFpoP0YhguMKGaAPiAqrh7zKd6mh6k00z2ijuiZu")

#_(defn check-out-form
    []
    (r/create-class
     {:component-did-mount
      (fn []
        (let [stripe (useStripe)
              elements (useElements)
              submit (fn [e]
                       (.preventDefault e)
                       (go
                         (let [{:keys [error paymentMethod]} ((.createPaymentMethod stripe)
                                                              {:type "card"
                                                               :card ((.-getElement elements) CardElement)})])))]))}))

;; (defn stripe-card
;;   []
;;   [:<>
;;    [:div "hello"]
;;    [:> Elements {:stripe}
;;     [:> Box {:sx {:width "100%"}
;;              :component "form"}]]]
     ;; [:> CardElement {:options {:style {:base {:fontSize "16px"}}}}]
     ;; [:> Button {:type "submit"}
     ;;  "Submit"]]]]
#_(go
    (let [promise (<! (loadStripe stripe-test-key))]
      (js/console.log "promise" promise)
      (let [stripe (useStripe promise)
            elements (useElements promise)]
        (js/console.log "stripe" stripe)
        (js/console.log "elements" elements)
        (let [submit (fn [e]
                       (.preventDefault e)
                       (go
                         (let [{:keys [error paymentMethod]} ((.createPaymentMethod stripe)
                                                              {:type "card"
                                                               :card ((.-getElement elements) CardElement)})])))]
          (fn []
            [:> Elements {:stripe stripe}
             [:> Box {:sx {:width "100%"}
                      :component "form"
                      :on-submit submit}
              [:> CardElement {:options {:style {:base {:fontSize "16px"}}}}]
              [:> Button {:type "submit"}
               "Submit"]]])))))

(defn recipes-page
  []
  (let [logged-in? @(rf/subscribe [:logged-in?])]
    (fn []
      [:<>
       (let [drafts @(rf/subscribe [:filter/draft])]
         [:<>
          [page-nav {:center "Recipes"
                     :right (when logged-in?
                              [:<>
                               [recipe-editor]])}]
          [:> Grid {:display "flex"
                    :flex-direction "column"
                    :container true}
           [:> Button {:variant "contained"
                       :on-click #(rf/dispatch [:http/get-recipes-log])}
            "Load log recipes"]

           [:> Button {:variant "contained"}
            :on-click #(rf/dispatch [:http/post-recipe-mock])
            "log post recipes"]

           ;; [stripe-card]
           ;; => equivalent to (not (empty? drafts)), read =empty?= description at: https://clojuredocs.org/clojure.core/empty_q
           (when (seq drafts)
             [:> Box
              [:<>
               [:> Typography {:text-align "left"
                               :component "h2"
                               :variant "h2"
                               :pb 5
                               :pl 5
                               :font-weight 700}
                "Drafts"]
               [recipe-list drafts]]])
           (when logged-in?
             [:<>
              [:> Grid {:container true
                        :mb 5
                        :row-spacing {:xs 1
                                      :sm 2
                                      :md 3}
                        :flex-direction "column"
                        :sx {:display "flex"
                             :flex-wrap "wrap"
                             :align-items "strech"}}
               [:> Box {:sx {:flex-shrink 1}}
                [:> Typography {:text-align "left"
                                :component "h2"
                                :variant "h2"
                                :pt 10
                                :pb 7
                                :pl 5
                                :font-weight 700}
                 "Public"]]
               (when @(rf/subscribe [:recipes/loaded?])
                 ;; (js/console.log "recipes loaded" @(rf/subscribe [:filter/public]))
                 [recipe-list @(rf/subscribe [:filter/public])])]])]])])))

;; (comment
;;   ["stripe" :as initStripe]
;;   (def stripe (initStripe. "pk_test_51MRNOfEu1RjbDj23MvPdmtUlmdPVE0gDUeXTHM2WXFmCCH25q4vNgQWxrZVFpoP0YhguMKGaAPiAqrh7zKd6mh6k00z2ijuiZu"))
;;   ((.. stripe customer create) {:email "hello@mail.com"})
;;   (stripe.customer .-create))
