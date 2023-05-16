(ns playground.server
  (:require
   [aleph.http :as http]
   [environ.core :refer [env]]
   [ring.adapter.jetty :as jetty]
   [reitit.ring :as ring]
   [integrant.core :as ig]
   [playground.router :as router]))
    ;; (:gen-class))

(defn app
  [env]
  (ring/ring-handler
   (ring/router ["/"
                 {:get {:handler (fn [req] {:staus 200}
                                   :body "Hello Reiti")}}])))

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

(defmethod ig/init-key :db/postgres
  [_ config]
  (println "\nConfigured db")
  (:jdbc-url config))

(defmethod ig/halt-key! :server/aleph
  [_ aleph]
  (println "\nStopping server")
  aleph
  (println aleph)
  (println (str "\nServer stopped on port " (.port aleph)))
  (.close aleph))

(defn -main
  [config-file]
  (let [config (-> config-file slurp ig/read-string)]
    (-> config ig/prep ig/init)))

(comment
  (start)
  (app {:request-method :get
        :uri "/"})
  (-main))
