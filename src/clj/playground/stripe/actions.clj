(ns playground.stripe.actions
  (:import
   com.stripe.Stripe
   com.stripe.model.checkout.Session
   com.stripe.param.checkout.SessionCreateParams
   com.stripe.param.checkout.SessionCreateParams$LineItem
   com.stripe.param.checkout.SessionCreateParams$Mode)
  (:require
   ;; [clojure.pprint :as pprint]
   ;; [playground.responses :as responses]
   [ring.util.response :as rr]))

(def my-domain "http://localhost:8101")

(comment
  (set! (. Stripe apiKey) "sk_test_51MRNOfEu1RjbDj23gf1Q54CQXMPj4kLa1eXTMIIDDwPB79TOTj1DjfaLWt8GIOExqbc6tIYtrlYsEklOZS8QlDpF00iD7DZVFG")
  (let [params  (-> (. SessionCreateParams builder)
                    (. setMode SessionCreateParams$Mode/PAYMENT)
                    (. setSuccessUrl (str my-domain "?success=true"))
                    (. setCancelUrl (str my-domain "?canceled=true"))
                    (. addLineItem (-> (. SessionCreateParams$LineItem builder)
                                       (. setQuantity 1)
                                       (. setPrice "price_1MRNW5Eu1RjbDj23rH4J5DPv")
                                       (. build)))
                    (. build))
        session (Session/create params)]))

(defn stripe-checkout!
  [api-key]
  (set! (. Stripe apiKey) api-key)
  (let [params  (-> (. SessionCreateParams builder)
                    (. setMode SessionCreateParams$Mode/PAYMENT)
                    (. setSuccessUrl (str my-domain "?success=true"))
                    (. setCancelUrl (str my-domain "?canceled=true"))
                    (. addLineItem (-> (. SessionCreateParams$LineItem builder)
                                       (. setQuantity 1)
                                       (. setPrice "price_1MRNW5Eu1RjbDj23rH4J5DPv")
                                       (. build)))
                    (. build))
        session (Session/create params)]
    (if session
      {:url (. session getUrl)
       :status 302}
      nil)))
