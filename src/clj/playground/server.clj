(ns playground.server
  (:require
   [aleph.http :as http]
   [environ.core :refer [env]]
   [integrant.core :as ig]
   [next.jdbc :as jdbc]
   [playground.router :as router]))

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

(defmethod ig/init-key :playground/app
  [_ config]
  (println "\nStarted app")
  (app config))

(defmethod ig/prep-key :db/postgres
  [_ config]
  (merge config {:jdbc-url (env :jdbc-url)}))

(defmethod ig/init-key :db/postgres
  [_ {:keys [jdbc-url]}]
  (println "\nConfigured db")
  (jdbc/with-options jdbc-url jdbc/snake-kebab-opts))

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
