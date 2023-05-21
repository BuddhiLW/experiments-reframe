(ns playground.stripe.handlers
  (:require
   [clojure.pprint :as pprint]
   [playground.stripe.actions :as stripe-actions]
   [ring.util.response :as rr]
   [playground.responses :as responses]))

(defn create-checkout-session!
  [api-key]
  (fn [request]
    (let [checkout (stripe-actions/stripe-checkout! api-key)]
      (if (seq checkout)
        (rr/redirect (:url checkout)
                     (:status checkout))
        (rr/not-found {:type "Couldn't create Stripe session"
                       :message "Failed to create Stripe session."})))))
