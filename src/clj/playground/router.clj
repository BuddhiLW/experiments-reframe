(ns playground.router
  (:require
   [muuntaja.core :as m]
   [playground.recipe.routes :as recipe]
   [reitit.coercion.spec :as coercion-spec]
   [reitit.ring :as ring]
   [reitit.ring.coercion :as coercion]
   [reitit.ring.middleware.exception :as exception]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   ;; [ring.middleware.cors :as cors]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]))

;;exception -> ring
;;coercion -> ring
(def router-config
  {:data {:coercion     coercion-spec/coercion
          :muuntaja     m/instance
          :middleware   [swagger/swagger-feature
                         muuntaja/format-middleware
                         exception/exception-middleware
                         coercion/coerce-request-middleware
                         coercion/coerce-response-middleware]}})
                         ;; cors/wrap-cors]}})

(def swagger-docs
  ["/swagger.json"
   {:get {:no-doc true
          :swagger {:basePath "/"
                    :info {:title "Playground API"
                           :description "REST APIs, for Cheffy app."
                           :version "1.0.0"}}
          :handler (swagger/create-swagger-handler)}}])

(defn routes
  [env]
  (ring/ring-handler
   (ring/router
    [swagger-docs
     ["/v1"
      (recipe/routes env)]]
    router-config)
   (ring/routes
    (swagger-ui/create-swagger-ui-handler {:path "/"}))))
