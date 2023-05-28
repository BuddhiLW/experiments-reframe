(ns playground.stripe.routes
  (:require
   [playground.middleware :as mw]
   [playground.stripe.handlers :as stripe]))

(defn routes
  [env]
  (let [stripe-secret-key (:api-key env)]
    ["/stripe" {:swagger {:tags ["Stripe"]}
                :middleware [[mw/wrap-auth0]]}
     ["/create-checkout-session"
      {:post {:handler   (stripe/create-checkout-session! stripe-secret-key)
              :responses {200 {:body map?}}
              :summary   "Stripe checkout session routing"}}]]))
