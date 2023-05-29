(ns playground.upload-file.events
  (:require
   [ajax.core :as ajax]
   [clojure.string :as s]
   [playground.helpers :as h]
   [re-frame.core :as rf]))

(rf/reg-event-fx
 :http/upload-file
 (fn [_ [_ file]]
   (let [form-data (doto (js/FormData.)
                     (.append "file" file))]
     {:http-xhrio
      {:method :post
       :uri             (h/endpoint "v1" "upload" "file")
       :params          form-data
       :body            form-data
       :format          (ajax/json-request-format {:keywords? true})
       :response-format (ajax/json-response-format {:keywords? true})
       :on-success      [:http/upload-file-success]
       :on-failure      (fn [response] (js/console.log "response error: " response))}})))

(rf/reg-event-db
 :http/upload-file-success
 (fn [db [_ response]]
   (let [paths (get-in db [:files :path])
         log-response (js/console.log "response success:" (clj->js response))]
     (assoc-in db [:files :path]
               (->
                (get-in response [:body :file])
                (s/split #"resources")
                (second))))))
