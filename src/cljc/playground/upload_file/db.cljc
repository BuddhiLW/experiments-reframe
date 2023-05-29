(ns playground.upload-file.db
  (:require
   [clojure.core :refer [file-seq]]
   [clojure.java.io :as io]
   #?(:cljs [re-frame.core :as rf])))

#?(:clj (def directory (io/file "resources/files/")))
#?(:clj (def files (file-seq directory)))
#?(:clj (rest (for [file (take 10 files)]
                (str "files/" (.getName file)))))

#?(:cljs
   (rf/reg-cofx
    :local-store-images
    (fn [cofx _]
      (assoc cofx :local-store-images (atom {})))))
