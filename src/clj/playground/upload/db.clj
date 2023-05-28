(ns playground.upload.db
  (:require
   [clojure.java.io :as io]))

(defn copy-to-local! [file]
  (let [local-file (io/file "resources/files/" (:filename file))]
    (io/copy (:tempfile file) local-file)
    local-file))
