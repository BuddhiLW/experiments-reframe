(ns playground.router
  (:require
   [muuntaja.core :as m]
   [playground.recipe.routes :as recipe]
   [playground.stripe.routes :as stripe]
   [playground.upload.routes :as upload]
   [reitit.coercion.spec :as coercion-spec]
   [reitit.ring :as ring]
   [reitit.ring.coercion :as coercion]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]
   [ring.middleware.cors :as cors]))

(def swagger-docs
  ["/swagger.json"
   {:get {:no-doc true
          :swagger {:basePath "/"
                    :info {:title "Playground API"
                           :description "REST APIs, for Cheffy app."
                           :version "1.0.0"}}
          :handler (swagger/create-swagger-handler)}}])

;;exception -> ring
;;coercion -> ring
(def router-config
  {:data {:coercion     coercion-spec/coercion
          :muuntaja     m/instance
          :middleware   [swagger/swagger-feature
                         muuntaja/format-middleware
                         ;; exception/exception-middleware
                         coercion/coerce-request-middleware
                         coercion/coerce-response-middleware
                         ;; https://clojurians.slack.com/archives/C03S1KBA2/p1678681740112949
                         [cors/wrap-cors
                          :access-control-allow-origin [#".*" #"https://checkout.stripe.com/*"
                                                        #"http://localhost*"
                                                        #"http://localhost:8101/*"]
                          :access-control-allow-methods [:get :post :put :patch :delete]
                          :Access-Control-Allow-Credentials "true"]]}})

(defn routes
  [env]
  (ring/ring-handler
   (ring/router
    [swagger-docs
     ["/v1"
      (upload/routes env)
      (recipe/routes env)
      (stripe/routes env)]]
    router-config)
   (ring/routes
    (swagger-ui/create-swagger-ui-handler {:path "/"}))))
