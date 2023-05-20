(ns playground.recipes-tests
  (:require
   [clojure.test :as t]
   [muuntaja.core :as m]
   [playground.test-system :as test-system]))

(t/deftest recipes-tests
  (t/testing "List recipes"
    (t/testing "with auth -- public and drafts"
      (let [{:keys [status body]} (test-system/test-endpoint :get "/v1/recipes" {:auth true})]
        (t/is (= status 200))
        (t/is (vector? (:public body)))
        (t/is (vector? (:drafts body)))))

    (t/testing "Without auth -- public"
      (let [{:keys [status body]} (test-system/test-endpoint :get "/v1/recipes" {:auth false})]
        (t/is (= 200 status))
        (t/is (vector? (:public body)))
        (t/is (nil? (:drafts body)))))))

    ;; (t/testing "Without auth -- drafts")))

;; (m/decode "application/json" "{\"name\":\"string-name\",\"prep-time\":30,\"img\":\"string-img\"}")
