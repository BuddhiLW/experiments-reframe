(ns playground.router
  (:require
   [muuntaja.core :as m]
   [playground.recipe.routes :as recipe]
   [reitit.ring :as ring]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.swagger :as swagger]
   [reitit.swagger-ui :as swagger-ui]))

(def router-config
  {:data {:muuntaja     m/instance
          :middleware   [swagger/swagger-feature
                         muuntaja/format-middleware]}})

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
