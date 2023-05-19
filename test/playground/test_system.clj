(ns playground.test-system
  (:require
   [integrant.repl.state :as state]
   [muuntaja.core :as m]
   [ring.mock.request :as mock]))

(defn test-endpoint
  ([method uri]
   (test-endpoint method uri nil))
  ([method uri opts]
   (let [app (-> state/system :playground/app)
         request (app (-> (mock/request method uri)
                          (cond-> (:body opts) (mock/json-body (:body opts)))))]
     (update request :body (partial m/decode "application/json")))))

;; (m/decode-response-body))))
(comment
  ;; (= (fn [data] (m/decode "application/json" data)) (partial m/decode "application/json"))
  (test-endpoint :get "/v1/recipes")
  (test-endpoint :post "/v1/recipes" {:img "string-img"
                                      :name "string-name"
                                      :prep-time 30}))
