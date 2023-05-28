(ns playground.stripe.views.stripe-example
  (:require
   ["@mui/material" :refer [Box Button Card CardContent CardMedia Grid
                            Typography]]
   [playground.components.page-nav :refer [page-nav]]
   [re-frame.core :as rf]))

(defn product-display
  []
  [:> Grid {:container true
            :mb 5
            :alignItems "center"
            :justify-content "center"}
   [:> Card {:sx {:box-shadow 10
                  :border-radius "10px"
                  :p 2
                  :pb 0}
             :class-name "hover:shadow-2xl"}
    [:> CardMedia {:className "product"}
     [:img {:src "https://pngimg.com/uploads/mug_coffee/mug_coffee_PNG16835.png" ;; https://i.imgur.com/EHyR2nP.png
            :height "400px"
            :width "300px"
            :alt "Coffe mug"}]]
    [:> CardContent
     [:> Grid {:className "description"
               :container true
               :pb 2
               :sx {:diplay "flex"
                    :flex-direction "column"
                    :justify-content "space-between"
                    :spacing 2}}
      [:> Typography {:variant "h4"} "Cup of Coffee"]
      [:> Typography {:mt 2 :mb 2} "R$10.00"]
      [:> Button {:variant "contained"
                  :on-click #(rf/dispatch [:stripe/checkout])}
       "Checkout"]]]]])
;;
;; (.open js/window "https://www.clojure.org")
(defn stripe-page
  []
  [:<>
   [page-nav {:center "Stripe example"}]
   [product-display]])
