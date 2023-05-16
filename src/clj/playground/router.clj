(ns playground.router
  (:require
   [reitit.ring :as ring]
   [playground.recipe.routes :as recipe]))

(defn routes
  [env]
  (ring/ring-handler
   (ring/router
    [["/v1"
      (recipe/routes env)
      #_{:get {:handler (fn [req] {:status 200
                                   :body "hello"})}}]])))
