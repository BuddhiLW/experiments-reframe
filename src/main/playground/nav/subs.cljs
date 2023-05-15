(ns playground.nav.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :nav
 (fn [db _]
   (get db :nav)))

(reg-sub
 :active-nav
 :<- [:nav]
 (fn [nav _]
   (get nav :active-nav)))

(reg-sub
 :active-page
 :<- [:nav]
 (fn [nav _]
   (get nav :active-page)))

(reg-sub
 :nav/active-recipe
 (fn [db _]
   (get-in db [:nav :active-recipe])))

(reg-sub
 :recipes/recipe
 :<- [:recipes]
 :<- [:nav/active-recipe]
 (fn [[recipes active-recipe] _]
   (get recipes active-recipe)))

;; (reg-sub
;;  :nav/active-page
;;  :<- [:nav]
;;  (fn [nav _]
;;    (get nav :active-page)))

(reg-sub
 :nav/active-inbox
 :<- [:nav]
 (fn [nav _]
   (:active-inbox nav)))
