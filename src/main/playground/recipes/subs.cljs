(ns playground.recipes.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :recipes
 (fn [db _]
   (:recipes db)))

(rf/reg-sub
 :filter/draft
 :<- [:recipes]
 :<- [:uid]
 (fn [[recipes uid] _]
   (let [filters [#(= (:public? %) false) #(= (:cook %) uid)]]
     (filter (apply every-pred filters) (vals recipes)))))

(rf/reg-sub
 :filter/public
 :<- [:recipes]
 (fn [recipes _]
   (filter #(= (:recipe/public %) true) (vals recipes))))

(rf/reg-sub
 :recipe/author?
 :<- [:recipes/recipe]
 :<- [:auth/current-user]
 (fn [[{:keys [cook]} uid] _]
   (= cook uid)))

(rf/reg-sub
 :recipes/active-modal
 (fn [db _]
   (get-in db [:nav :active-modal])))

(rf/reg-sub
 :recipe/ingredients
 :<- [:recipes/recipe]
 (fn [recipe _]
   (->> (:ingredients recipe)
        (vals)
        (sort-by :order))))

(rf/reg-sub
 :recipe/steps
 :<- [:recipes/recipe]
 (fn [recipe _]
   (->> (:steps recipe)
        (vals)
        (sort-by :order))))

#_(rf/reg-fx
   :recipe/update-recipe
   (fn [{:keys [db]} [_ recipe-id values]]
     (let [recipe (get-in db [:recipes recipe-id])]
       {:db (update-in db [:recipes recipe-id] merge values)
        :dispatch [:recipes/close-modal]})))
(rf/reg-sub
 :recipe/ingredient
 (fn [db [_ id-ingredient]]
   (let [active-recipe (get-in db [:nav :active-recipe])]
     (get-in db [:recipes active-recipe :ingredients id-ingredient]))))

(rf/reg-sub
 :recipe/public?
 (fn [db [_]]
   (let [active-recipe (get-in db [:nav :active-recipe])]
     (get-in db [:recipes active-recipe :public?]))))

(rf/reg-sub
 :recipes/saved
 :<- [:recipes]
 :<- [:recipes/user]
 (fn [[recipes user] _]
   (let [saved (:saved user)]
     (filter #(contains? saved (:id %)) (vals recipes)))))

(rf/reg-sub
 :recipes/loaded?
 (fn [db [_]]
   (let [log (js/console.log "recipes loaded?" (not (get-in db [:loading :recipes])))]
     (not (get-in db [:loading :recipes])))))
