(ns playground.nav.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :active-nav
 (fn [db _]
   (get-in db [:nav :active-nav])))

(reg-sub
 :active-page
 (fn [db _]
   (get-in db [:nav :active-page])))

(reg-sub
 :recipes/recipe
 (fn [db _]
   (let [recipe-id (get-in db [:nav :active-recipe])
         recipe (get-in db [:recipes recipe-id])]
     recipe)))
