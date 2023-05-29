(ns playground.upload-file.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :upload/latest-upload
 (fn [db _]
   (js/console.log "db: " db)
   (get-in db [:files :path])))
