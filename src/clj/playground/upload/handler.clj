(ns playground.upload.handler
  (:require
   [playground.upload.db :as filesystem]
   [clojure.java.io :as io]
   [ring.util.response :as rr]
   [clojure.pprint :as pprint]))

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
    (let [pprint (pprint/pprint request)
          file   (-> request :body :multipart :file)]
      (if (instance? java.io.File (:tempfile file))
        (rr/response {:status 200
                      :body   {:message "File uploaded successfully"
                               :file    (filesystem/copy-to-local! file)}})
        (rr/response {:status 400 :body {:message "No file provided"}})))))

