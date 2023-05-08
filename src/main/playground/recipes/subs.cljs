(ns playground.recipes.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :filter/draft
 (fn [db _]
   (let [recipes (vals (:recipes db))
         uid (get-in db [:auth :uid])
         filters [#(= (:public? %) false) #(= (:cook %) uid)]]
     (filter (apply every-pred filters) recipes))))

(rf/reg-sub
 :filter/public
 (fn [db _]
   (let [recipes (vals (:recipes db))
         uid (get-in db [:auth :uid])]
     (filter #(= (:public? %) true) recipes))))

(rf/reg-sub
 :recipes/author?
 :<- [:recipes/recipe]
 :<- [:recipes/user]
 (fn [[{:keys [cook]} {:keys [uid]}] _]
   (= cook uid)))

(rf/reg-sub
 :recipes/active-modal
 (fn [db _]
   (get-in db [:nav :active-modal])))
