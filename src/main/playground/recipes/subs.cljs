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
 :recipe/author?
 :<- [:recipes/recipe]
 :<- [:recipes/user]
 (fn [[{:keys [cook]} {:keys [uid]}] _]
   (= cook uid)))

(rf/reg-sub
 :recipes/active-modal
 (fn [db _]
   (get-in db [:nav :active-modal])))

(rf/reg-sub
 :recipe/ingredients
 (fn [db _]
   (let [active-recipe (get-in db [:nav :active-recipe])
         ingredients (get-in db [:recipes active-recipe :ingredients])]
     (->> ingredients
          (vals)
          (sort-by :order)))))

(rf/reg-sub
 :recipe/steps
 (fn [db _]
   (let [active-recipe (get-in db [:nav :active-recipe])
         ingredients (get-in db [:recipes active-recipe :steps])]
     (->> ingredients
          (vals)
          (sort-by :order)))))

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
