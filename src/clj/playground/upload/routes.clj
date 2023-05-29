(ns playground.upload.routes
  (:require
   [playground.upload.handler :as upload]
   [reitit.ring.middleware.multipart :as multipart]
   [reitit.ring.middleware.parameters :as parameters]))

(defn routes
  [env]
  (let [db (:jdbc-url env)]
    ["/upload" {:swagger {:tags ["uploads"]}
                :middleware [parameters/parameters-middleware
                             multipart/multipart-middleware]}
     ["/file"
      {:post {:handler (upload/upload-file! db)
              :body {:multipart {:file multipart/temp-file-part}}
              :parameters {:multipart {:file multipart/temp-file-part}}
              :responses {200 {:body map?}}
              :summary "Upload a file"}}]]))
