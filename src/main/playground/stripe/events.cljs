(ns playground.stripe.events
  (:require
   [ajax.core :as ajax]
   [playground.helpers :as h]
   [re-frame.core :as rf]))

(rf/reg-event-fx
 :stripe/checkout
 (fn [_ _]
   {:http-xhrio {:method          :post
                 :uri             (h/endpoint "v1" "stripe" "create-checkout-session")
                 :format          (ajax/json-request-format {:keywords? true})
                 :response-format (ajax/json-response-format {:keywords? true})
                 :on-success      (fn [response] (js/console.log "response success, stripe:" response))
                 :on-failure      (fn [response] (js/console.log "response error, stripe:" response))}}))
;; {:method      :post
;;                  :uri         (h/endpoint "v1" "recipes")
;;                  :format      (ajax/json-request-format)
;;                  :params      recipe
;;                  :on-success  [:success/post-recipe-success]
;;                  :on-failure  [:error/endpoint-request]}
;; (h/endpoint "v1" "create-checkout-session")
;; =>
;; "http://localhost:8666/v1/stripe/create-checkout-session"
(comment
  (= (h/endpoint "v1" "stripe" "create-checkout-session"
                 "http://localhost:8666/v1/stripe/create-checkout-session")))
