(ns playground.auth.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub
 :uid
 (fn [db _]
   (get-in db [:auth :uid])))

(rf/reg-sub
 :users
 (fn [db _]
   (:users db)))

(rf/reg-sub
 :logged-in?
 :<- [:uid]
 (fn [uid _]
   (boolean uid)))

(rf/reg-sub
 :auth/user-profile
 :<- [:uid]
 :<- [:users]
 (fn [[uid users] _]
   (get-in users [uid :profile])))

(rf/reg-sub
 :recipes/user
 :<- [:uid]
 :<- [:users]
 (fn [[uid users] _]
   (get users uid)))

(rf/reg-sub
 :auth/current-user
 (fn [db _]
   (get-in db [:auth :uid])))

(rf/reg-sub
 :auth/chef?
 :<- [:uid]
 :<- [:users]
 (fn [[uid users] _]
  (= (get-in users [uid :role])
     :chef)))
