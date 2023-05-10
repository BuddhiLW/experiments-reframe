(ns playground.recipes.events
  (:require
   [re-frame.core :as rf]))

(rf/reg-event-db
 :recipes/save-recipe
 (fn [db [_ recipe-id]]
   (let [uid (get-in db [:auth :uid])]
     (-> db
         (update-in [:users uid :saved] conj recipe-id)
         (update-in [:recipes recipe-id :saved-count] inc)))))

(rf/reg-event-fx
 :recipe/update-ingredient
 (fn [{:keys [db]} [_ {:keys [id] :as new-values}]]
   (js/console.log "update ingredient" db id new-values)
   (let [active-recipe (get-in db [:nav :active-recipe])]
     {:db (assoc-in db [:recipes active-recipe :ingredients id] new-values)
      :dispatch [:recipes/close-modal]})))

(rf/reg-event-db
 :recipes/unsave-recipe
 (fn [db [_ recipe-id]]
   (let [uid (get-in db [:auth :uid])]
     (-> db
         (update-in [:users uid :saved] disj recipe-id)
         (update-in [:recipes recipe-id :saved-count] dec)))))

(rf/reg-event-db
 :recipes/open-modal
 (fn [db [_ modal-name]]
   (assoc-in db [:nav :active-modal] modal-name)))

(rf/reg-event-fx
 :recipe/save-ingredient
 (fn [{:keys [db]} [_ {:keys [id name amount measure]}]]
   (let [active-recipe (get-in db [:nav :active-recipe])
         ingredients   (get-in db [:recipes active-recipe :ingredients])
         order (or (get-in ingredients [id :order])
                   (inc (count ingredients)))]
     {:db (assoc-in db [:recipes active-recipe :ingredients id]  {:id id
                                                                  :name name
                                                                  :amount amount
                                                                  :measure measure
                                                                  :order order})
      :dispatch [:recipes/close-modal]})))

(rf/reg-event-fx
 :recipe/delete-ingredient
 (fn [{:keys [db]} [_ ingredient-id]]
   (let [active-recipe (get-in db [:nav :active-recipe])]
     {:db (update-in db [:recipes active-recipe :ingredients] dissoc ingredient-id)
      :dispatch [:recipes/close-modal]})))
