(ns user
  (:require
   [integrant.core :as ig]
   [integrant.repl :as ig-repl]
   [integrant.repl.state :as state]
   [playground.server]))

(ig-repl/set-prep!
 (fn [] (-> "resources/config.edn" slurp ig/read-string)))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> state/system :playground/app))
(def db (-> state/system :db/postgres))

(comment
  (app {:request-method :get
        :uri "/v1/recipes"})
  (app {:request-method :get
        :uri "/swagger.json"})
  (go)
  (halt)
  (reset))
