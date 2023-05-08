(ns playground.recipes.views.recipe-list
  (:require
   ["@mui/material" :refer [Grid]]
   [playground.recipes.views.recipe-card :refer [recipe-card]]))

(defn recipe-list
  [recipes]
  [:> Grid {:container true
            :sx {:flex-grow 1}
            :flex-direction "row"
            :justify-content "start"}
   (for [recipe recipes]
     ^{:key (:id recipe)}
     (recipe-card recipe))])

;;
#_[:> Grid
   [:> Paper
    [:> Box {:p 2}
     [:> Grid {:container true :spacing 2}
      [:> Grid {:item true :xs 12}
       [:> Typography {:variant "h2"} "Recipe 2"]]
      [:> Grid {:item true :xs 12}
       [:> Typography {:variant "body1"} "Description 2"]]]]]]

    ;; (defn recipe-list []
    ;;   (let [recipes (recipes)]
    ;;     (html5
    ;;       [:head
    ;;         [:title "Recipes"]]
    ;;       [:body
    ;;         [:h1 "Recipes"]
    ;;         [:ul
    ;;           (for [recipe recipes]
    ;;             [:li
    ;;               [:a {:href (str "/recipes/" (:id recipe))} (:name recipe)]])]])))

    ;; (defn recipe [id]
    ;;   (let [recipe (recipe id)]
    ;;     (html5
    ;;       [:head
    ;;         [:title (:name recipe)]]
    ;;       [:body
    ;;         [:h1 (:name recipe)]
    ;;         [:p (:description recipe)]])))

    ;; (defroutes app
    ;;   (GET "/" [] (recipe-list))
    ;;   (GET "/recipes/:id" [id] (recipe id)))

    ;; (defn -main [& args]
    ;;   (run-jetty #'app {:port 8080 :join? false})))
