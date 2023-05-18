(ns playground.server
  (:require
   [aleph.http :as http]
   [environ.core :refer [env]]
   [ring.adapter.jetty :as jetty]
   [reitit.ring :as ring]
   [integrant.core :as ig]
   [playground.router :as router]
   [next.jdbc :as jdbc]))
    ;; (:gen-class))

(defn app
  [env]
  (router/routes env))

(defmethod ig/prep-key :server/aleph
  [_ config]
  (merge config {:port (Integer/parseInt (env :port))}))

(defmethod ig/init-key :server/aleph
  [_ {:keys [handler port]}]
  (println (str "\nServer running on port " port))
  (http/start-server handler {:port port}))

  ;; (jetty/run-jetty handler {:port port :join false}))

(defmethod ig/init-key :playground/app
  [_ config]
  (println "\nStarted app")
  (app config))

(defmethod ig/prep-key :db/postgres
  [_ config]
  (merge config {:jdbc-url (env :jdbc-url)}))

(defmethod ig/init-key :db/postgres
  [_ config]
  (println "\nConfigured db")
  (:jdbc-url config))

(defmethod ig/halt-key! :server/aleph
  [_ aleph]
  (println "\nStopping server")
  (.close aleph))

(defn -main
  [config-file]
  (let [config (-> config-file slurp ig/read-string)]
    (-> config
        ig/prep
        ig/init)))

(comment
  (start)
  (app {:request-method :get
        :uri "/"})
  (-main))
