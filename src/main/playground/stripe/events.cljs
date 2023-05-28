(ns playground.stripe.events
  (:require
   [ajax.core :as ajax]
   [playground.helpers :as h]
   [re-frame.core :as rf]))

(rf/reg-event-fx
 :http/redirect
 (fn [_ [_ {:keys [url]}]]
   (.open js/window url)))

(rf/reg-event-fx
 :stripe/checkout
 (fn [_ _]
   {:http-xhrio {:method          :post
                 :uri             (h/endpoint "v1" "stripe" "create-checkout-session")
                 :format          (ajax/json-request-format {:keywords? true})
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      [:http/redirect]
                 :on-failure      (fn [response] (js/console.log "response error, stripe:" response))}}))

(comment
  (= (h/endpoint "v1" "stripe" "create-checkout-session"
                 "http://localhost:8666/v1/stripe/create-checkout-session")))
