(ns playground.server
  (:require
   [aleph.http :as http]
   [environ.core :refer [env]]
   [integrant.core :as ig]
   [playground.router :as router]))
  ;; (:gen-class))

(defn app
  [env]
  (router/routes env))
  ;; (ring/ring-handler
  ;;  (ring/router  ["/" {:get {:handler (fn [req] {:status 200
  ;;                                                :body "hello"})}}]))

(defmethod ig/prep-key :server/aleph
  [_ config]
  (merge config {:port (Integer/parseInt (env :port))}))

;; This lives under "resources/config.edn"
#_(def config
    {:server/aleph   {:handler (ig/ref :playground/app)
                      :port 8666}
     :playground/app {:jdbc-url (ig/ref :db/postgres)}
     :db/postgres    {:jdbc-url "jdbc:postgresql://localhost:5432/playground"
                      :db-user "postgres"
                      :db-password "postgres"}})

(defmethod ig/init-key :server/aleph
  [_ {:keys [handler port]}]
  (http/start-server handler {:port port})
  (println (str "\nServer started on port: " port)))

(defmethod ig/init-key :playground/app
  [_ config]
  (println "\n Initializing playground app")
  (app config))

(defmethod ig/init-key :db/postgres
  [_ config] #_{:keys [jdbc-url db-user db-password]}
  (println "\n Initializing postgres db")
  (:jdbc-url config)
  #_(next.jdbc/get-datasource {:dbtype "postgresql"
                               :dbname jdbc-url
                               :user db-user
                               :password db-password}))

(defmethod ig/halt-key! :server/aleph
  [a aleph-server]
  (println a)
  (println (type aleph-server))
  (println aleph-server)
  (println "\nStopping server")
  (.close aleph-server))

(defn -main
  [config-file]
  (let [config (-> config-file
                   slurp
                   ig/read-string)]
    (-> config
        ig/prep    ;; Needed for real builds, using secrets/prod-environment.
        ig/init)))

#_(defn start
    []
    (http/start-server app {:port 8666})
    (println "\n Server started on port 8666"))

(comment
  (-main)
  (start)
  (app {:request-method :get
        :uri "/"})
  ;; => {:status 200, :body "hello"}

  ;; Alternative way to start the server
  ;; using `jetty`.
  (defn start
    []
    (jetty/run-jetty app {:port 8666}))

  ;; Using `aleph`.
  (def s (http/start-server app {:port 8082}))
  (.close s)
  (.port s)
  s
  (http/trace s)
  (http/delete s))
;; java.io.Closeable.close()
