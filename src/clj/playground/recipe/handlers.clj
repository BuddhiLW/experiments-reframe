(ns playground.recipe.handlers
  (:require
   [clojure.pprint :as pprint]
   [playground.recipe.db :as recipe-db]
   [playground.responses :as responses]
   [ring.util.response :as rr]
   [clojure.pprint :as pprint])
  (:import
   (java.util UUID)))

(defn list-all-recipes
  [db]
  (fn [request]
    (let [uid (do
                (pprint/pprint (-> request :claims :sub))
                (-> request :claims :sub))
          recipes (recipe-db/find-all-recipes db uid)]
      (rr/response recipes))))

(defn create-recipe!
  [db]
  (fn [request]
    (let [recipe-id (str (UUID/randomUUID))
          uid (or (-> request :claims :sub) "mike@mailinator.com")
          recipe (-> request :parameters :body)]
      (recipe-db/insert-recipe! db (assoc recipe :recipe-id recipe-id :uid uid))
      (rr/created (str responses/base-url "/recipes/" recipe-id) {:recipe-id recipe-id}))))

(defn retrieve-recipe
  [db]
  (fn [request]
    (let [recipe-id (-> request :claims :sub)
          recipe (recipe-db/find-recipe-by-id db recipe-id)]
      (if recipe
        (rr/response recipe)
        (rr/not-found {:type "recipe-not-found"
                       :message "Recipe not found"
                       :data (str "recipe-id " recipe-id)})))))

(defn update-recipe!
  [db]
  (fn [request]
    (let [recipe-id (-> request :parameters :path :recipe-id)
          recipe (-> request :parameters :body)
          update-successful? (recipe-db/update-recipe! db (assoc recipe :recipe-id recipe-id))]
      (if update-successful?
        (rr/status 204)
        (rr/not-found {:type "recipe-not-found"
                       :message "Recipe not found"
                       :data (str "recipe-id " recipe-id)})))))

(defn delete-recipe!
  [db]
  (fn [request]
    (let [recipe-id (-> request :parameters :path :recipe-id)
          deleted? (recipe-db/delete-recipe! db {:recipe-id recipe-id})]
      (if deleted?
        (rr/status 204)
        (rr/not-found {:type "recipe-not-found"
                       :message "Recipe not found"
                       :data (str "recipe-id " recipe-id)})))))

