(ns playground.recipe.views.recipes
  (:require [playground.components.page-nav :refer [page-nav]]))

(defn recipes
  []
  [page-nav {:center "Recipes"}])
