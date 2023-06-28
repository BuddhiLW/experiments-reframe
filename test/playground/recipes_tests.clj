(ns playground.recipes-tests
  (:require
   [clojure.test :as t]
   [muuntaja.core :as m]
   [playground.test-system :as test-system]))

(def recipe-id (atom nil))
(def recipe {:img "https://wallpaperaccess.com/full/2882747.jpg"
             :prep-time 36
             :name "recipe name"
             :public true})

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

(t/deftest recipes-tests
  (t/testing "Create recipe"
    (let [{:keys [status body]}
          (test-system/test-endpoint :post "/v1/recipes" {:auth true :body recipe})]
      (reset! recipe-id (:recipe-id body))
      (t/is (= 201 status))))

  (t/testing "Update recipe"
    (let [{:keys [status body]} (test-system/test-endpoint :put (str "/v1/recipes/" @recipe-id)
                                                           {:auth true
                                                            :body (assoc recipe :public true)})]
      (t/is (= 204 status))
      (t/is (= true (:recipe/public body)))))

  (t/testing "Delete recipe"
    (let [{:keys [status body]} (test-system/test-endpoint :delete (str "/v1/recipes/" @recipe-id)
                                                           {:auth true})]
      (t/is (= 204 status))
      (t/is (= true (:recipe/public body))))))

;; (recipes-tests)

(comment
  (assoc {:a 1} :b 2)
  ;; (let [recipe]
  (test-system/test-endpoint :put "/v1/recipes/103b7e7f-7928-4c40-8515-219baba85fcf" {:auth true
                                                                                      :body recipe})
  (test-system/test-endpoint :get "/v1/recipes" {:auth true}))
