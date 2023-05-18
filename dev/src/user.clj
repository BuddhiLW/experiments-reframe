(ns user
  (:require
   [integrant.core :as ig]
   [integrant.repl :as ig-repl]
   [integrant.repl.state :as state]
   [next.jdbc :as jdbc]
   [next.jdbc.sql :as sql]
   [playground.server]
   [playground.recipe.routes :as recipe]
   [playground.router :as router]))

(ig-repl/set-prep!
 (fn [] (-> "resources/config.edn"
            slurp
            ig/read-string)))

(def go ig-repl/go)
(def halt ig-repl/halt)
(def reset ig-repl/reset)
(def reset-all ig-repl/reset-all)

(def app (-> state/system :playground/app))
(def db (-> state/system :db/postgres))

(comment
  (app {:request-method :get
        :uri "/v1/recipes"})
  (app {:request-method :get
        :uri "/v1/recipes/1234-recipe"})
  (app {:request-method :get
        :uri "/swagger.json"})
  (jdbc/execute! db ["SELECT * FROM recipe"])
  (sql/find-by-keys db :recipe {:public false :uid "auth0|5ef440986e8fbb001355fd9c"})
  (sql/find-by-keys db :recipe {:recipe_id "a3dde84c-4a33-45aa-b0f3-4bf9ac997680"})
  (with-open [conn (jdbc/get-connection db)]
    (sql/get-by-id conn :recipe "a3dde84c-4a33-45aa-b0f3-4bf9ac997680" :recipe_id {}))
  (with-open [conn (jdbc/get-connection db)]
    (let [recipe-id "a3dde84c-4a33-45aa-b0f3-4bf9ac997680"]
      ;; returns only one map
      (sql/get-by-id conn :recipe recipe-id :recipe_id {})
      ;; returns a vector with a map
      (sql/find-by-keys conn :recipe {:recipe_id recipe-id})))

  (with-open [conn (jdbc/get-connection db)]
    (let [recipe-id "a3dde84c-4a33-45aa-b0f3-4bf9ac997680"
          recipe-id-query {:recipe_id recipe-id}
          [recipe]    (sql/find-by-keys conn :recipe recipe-id-query)
          steps       (sql/find-by-keys conn :step  recipe-id-query)
          ingredients (sql/find-by-keys conn :ingredient recipe-id-query)]
      (when (seq recipe)
        (assoc recipe
               :recipe/steps steps
               :recipe/ingredients ingredients))))

  (require '[clojure.pprint :as pprint])
  (require '[reitit.core])
  (require '[reitit.coercion])
  (require '[reitit.coercion.spec])
  (app {:request-method :get
        :uri "/v1/recipes/1234"})
  (->
   (app {:request-method :get
         :uri "/v1/recipes/a3dde84c-4a33-45aa-b0f3-4bf9ac997680"})
   :body
   (slurp))
  (def router
    (reitit.core/router
     ["/v1/recipes/:recipe-id"
      {:coercion reitit.coercion.spec/coercion
       :parameters {:path {:recipe-id int?}}}]
     {:compile reitit.coercion/compile-request-coercers}))
  (reitit.coercion/coerce!
   (reitit.core/match-by-path router "/v1/recipes/1234"))
  ;; => {:path {:recipe-id 1234}}

  (go)
  (halt)
  (reset))
