(ns playground.test-system
  (:require
   [integrant.repl.state :as state]
   [muuntaja.core :as m]
   [playground.auth0 :as auth0]
   [ring.mock.request :as mock]
   [clojure.pprint :as pprint]))

(defn test-endpoint
  ([method uri]
   (test-endpoint method uri nil))
  ([method uri opts]
   (let [app (-> state/system :playground/app)
         request (app (-> (mock/request method uri)
                          (cond-> (:auth opts) (mock/header :authorization (str "Bearer " (auth0/get-test-token)))
                                  (:body opts) (mock/json-body (:body opts)))))]
     (update request :body (fn [x]
                             (if (string? x)
                               x
                               (m/decode "application/json" x)))))))
     ;; ;; (ppr/pprint request)
     ;; (-> request
     ;;     :body
     ;;     (cond-> string? (update request :body)
     ;;             map? (update request :body (partial m/decode "application/json")))))))
     ;; ;; (cond-> (:body request)
     ;; ;;        (= 400)) (update request :body (partial identity))
     ;; ;;   (not= 400))  (update request :body (fn [data] (m/decode "application/json" data)))))

(comment
  ;; (= (fn [data] (m/decode "application/json" data)) (partial m/decode "application/json"))
  (test-endpoint :get "/v1/recipes" {:auth true})
  (test-endpoint :post "/v1/recipes" {:img "string-img"
                                      :name "string-name"
                                      :prep-time 30}))
