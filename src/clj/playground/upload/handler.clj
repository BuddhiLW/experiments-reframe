(ns playground.upload.handler
  (:require
   [playground.upload.db :as filesystem]
   [clojure.java.io :as io]
   [ring.util.response :as rr]
   [clojure.pprint :as pprint]
   [clojure.string :as s]))

;; ------- request format: ----------
;;
;; {:multipart
;;  {:file
;;   {:size 99333,
;;    :filename "349283107_1167415907272124_617616191493669989_n.jpg",
;;    :content-type "image/jpeg",
;;    :tempfile
;;    #object[java.io.File 0x43bb9bc0 "/tmp/ring-multipart-11449623210680755703.tmp"]}}}

;; (io/file "/home/buddhilw/Pictures/1680438472597.jpeg")

(defn upload-file!
  [db]
  (fn [request]
    (let [pprint (do (pprint/pprint (-> request :params))
                     (pprint/pprint (-> request :body)))
          file   (or
                  (-> request
                      :params
                      (get "file"))
                  (-> request :params :multipart :file))]
      (if (instance? java.io.File (:tempfile file))
        (rr/response {:status 200
                      :body   {:message "File uploaded successfully"
                               :file    (filesystem/copy-to-local! file)}})
        (rr/response {:status 400 :body {:message "No file provided"}})))))

(comment
  (let [map {"file"
             {:filename "pic-selected-230422-1317-54.png",
              :content-type "image/png",
              :size 69849}}]
    (get map "file"))
  (let [path "/home/buddhilw/PP/Clojure/cljs-reagent-template/resources/files/pic-selected-230422-1317-54.png"]
    (-> path
        (s/split #"resources/")
        (second))))
