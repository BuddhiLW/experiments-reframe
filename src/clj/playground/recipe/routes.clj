(ns playground.recipe.routes
  (:require
   [playground.recipe.handlers :as recipe]
   [playground.responses :as responses]))
   ;; [next.jdbc :as jdbc]))

(defn routes
  [env]
  (let [db (:jdbc-url env)]
    ["/recipes"
     [""
      {:swagger {:tags ["recipes"]}
       :get {:handler (recipe/list-all-recipes db)}
       :access-control  {:allow-origin "*"}
                         ;; :allow-methods #{:get :post :put}
                         ;; :allow-credentials true}
                         ;; :access-control-allow-origin "*"}
              ;; :allow-headers #{"X-PINGOTHER" "Content-Type"}
              ;; :expose-headers #{"X-My-Custom-Header" "X-Another-Custom-Header"}
              ;; :max-age 86400}}
            ;; :access-control-allow-origin "http://localhost:8101"
            ;; [#(wrap-cors % :access-control-allow-origin #"http://yoursite.com")
            ;; :headers {"Access-Control-Allow-Origin" "*"}
       :responses {200 {:body responses/recipes}}
       :summary "List all recipes"}]
     ["/:recipe-id"
      {:get (recipe/retrive-recipe db)
       :parameters {:path {:recipe-id string?}}
       :access-control  {:allow-origin "*"}
                         ;; :allow-methods #{:get :post :put}
                         ;; :allow-credentials true}
                         ;; :access-control-allow-origin "*"}
       ;;
       :responses {200 {:body responses/recipe}}
       :summary "Retrive recipe by a recipe-id"}]]))
