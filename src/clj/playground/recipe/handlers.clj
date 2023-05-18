(ns playground.recipe.handlers
  (:require
   [clojure.pprint :as pprint]
   [playground.recipe.db :as recipe-db]
   [ring.util.response :as rr]))
   ;; [playground.recipe.routes :as recipe]))

(defn list-all-recipes
  [db]
;; (sql/find-by-keys db :recipe {:public false :uid "auth0|5ef440986e8fbb001355fd9c"}
  (fn [request]
    (let [recipes (recipe-db/find-all-recipes db "auth0|5ef440986e8fbb001355fd9c")]
      (rr/response recipes))))

(defn retrive-recipe
  [db]
  (fn [request]
    ;; (clojure.pprint/pprint request)
    (let [recipe-id "a3dde84c-4a33-45aa-b0f3-4bf9ac997680"
          recipe (recipe-db/find-recipe-by-id db recipe-id)]
      (if recipe
        (rr/response recipe)
        (rr/not-found {:type "recipe-not-found"
                       :message "Recipe not found"
                       :data (str "recipe-id " recipe-id)})))))
