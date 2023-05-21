(ns playground.stripe.views.stripe-example
  (:require
   ["@mui/material" :refer [Box Button Card CardContent CardMedia Grid
                            Typography]]
   [playground.components.page-nav :refer [page-nav]]
   [re-frame.core :as rf]))

(defn product-display
  []
  [:> Grid {:container true
            :p 2
            :mb 5
            :alignItems "center"
            :justify-content "center"}
   [:> Card {:sx {:box-shadow 10}
             :border-radius "10px"
             :class-name "hover:shadow-2xl"}
    [:> CardMedia {:className "product"}
     [:img {:src "https://i.imgur.com/EHyR2nP.png"
            :alt "The cover of Stubborn Attachments"}]]
    [:> CardContent
     [:> Box {:className "description"
              :pb 2}
      [:h3 "Stubborn Attachments"]
      [:> Typography "$20.00"]
      [:> Button {:variant "contained"
                  :on-click #(rf/dispatch [:stripe/checkout])}
       "Checkout"]]]]])

(defn stripe-page
  []
  [:<>
   [page-nav {:center "Stripe example"}]
   [product-display]])
