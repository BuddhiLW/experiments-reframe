(ns playground.recipe.db
  (:require
   [next.jdbc :as jdbc]
   [next.jdbc.sql :as sql]))

(defn find-all-recipes
  [db uid]
  (with-open [conn (jdbc/get-connection db)]
    (let [public (sql/find-by-keys conn :recipe {:public true})]
      (if uid
        (let [drafts (sql/find-by-keys conn :recipe {:public false :uid uid})]
          {:public public
           :drafts drafts})
        {:public public}))))

(defn find-recipe-by-id
  [db recipe-id]
  (with-open [conn (jdbc/get-connection db)]
    (let [recipe-id-query {:recipe_id recipe-id}
          [recipe]    (sql/find-by-keys conn :recipe recipe-id-query)
          steps       (sql/find-by-keys conn :step  recipe-id-query)
          ingredients (sql/find-by-keys conn :ingredient recipe-id-query)]
      (when (seq recipe)
        (assoc recipe
               :recipe/steps steps
               :recipe/ingredients ingredients)))))
;; "auth0|5ef440986e8fbb001355fd9c"
