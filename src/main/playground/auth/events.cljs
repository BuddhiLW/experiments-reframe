(ns playground.auth.events
  (:require
   [cljs.reader :as reader]
   [playground.spec :refer [check-spec-interceptor]]
   [re-frame.core :refer [reg-event-fx
                          reg-event-db
                          reg-cofx
                          after]]))

(def playground-user-key "playgroud-user")

(defn set-user-ls!
  "Interceptor function to set user in local storage"
  [{:keys [auth]}]
  (when auth
    (.setItem js/localStorage playground-user-key (str auth))))

(defn remove-user-ls!
  "Interceptor function to remove user from local storage"
  []
  (.removeItem js/localStorage playground-user-key))

(def set-user-interceptors
  [(after set-user-ls!)
   check-spec-interceptor])

(def remove-user-interceptors
  [(after remove-user-ls!)
   check-spec-interceptor])

(reg-cofx
 :local-store-user
 (fn [cofx _]
   (assoc cofx :local-store-user
          (reader/read-string (.getItem js/localStorage playground-user-key)))))

(reg-event-fx
 :sign-up
 set-user-interceptors
 (fn [{:keys [db]} [_ {:keys [email password
                              first-name last-name]}]]
   {:db (-> db
            (assoc-in [:auth :uid] email)
            (assoc-in [:users email]
                      {:id email
                       :profile {:first-name first-name
                                 :last-name last-name
                                 :email email
                                 :password password
                                 :img "img/avatar.jpg"}
                       :saved #{}
                       :inboxes {}}))
    :dispatch [:set-active-page :saved]
    :navigate-to {:path "/saved"}}))

(reg-event-fx
 :log-in
 set-user-interceptors
 (fn [{:keys [db]} [_ {:keys [email password]}]]
   (let [user (get-in db [:users email])
         correct-password? (= (get-in user [:profile :password])
                              password)]
     (cond
       (not user)
       {:db (assoc-in db [:errors :email] "User not found")}
       (not correct-password?)
       {:db (assoc-in db [:errors :email] "Wrong password")}
       correct-password?
       {:db (-> db
                (assoc-in [:auth :uid] email)
                (update :errors dissoc :email))
        :dispatch [:set-active-page :saved]
        :navigate-to {:path "/saved"}}))))

(reg-event-fx
 :auth/log-out
 remove-user-interceptors
 (fn [{:keys [db]} _]
   {:db (assoc-in db [:auth :uid] nil)
    :dispatch [:set-active-page :recipes]
    :navigate-to {:path "/"}}))

(reg-event-db
 :update/profile
 (fn [db [_ profile]]
   (let [uid (get-in db [:auth :uid])]
     (update-in db [:users uid :profile] merge (select-keys profile [:first-name :last-name])))))

(reg-event-fx
 :auth/delete-account
 remove-user-interceptors
 (fn [{:keys [db]} _]
   (let [uid (get-in db [:auth :uid])]
     {:db (-> db
              (assoc-in [:auth :uid] nil)
              (update :users dissoc uid))
      :dispatch [:set-active-page :recipes]
      :navigate-to {:path "/recipes"}})))

(comment
  ;; Side-effects local-storage process
  (reg-event-db
   :set-user-ls!
   (fn [db [_]]
     (let [auth (get db :auth)]
       (.setItem js/localStorage playground-user-key (str auth)))))

  (reg-event-db
   :load-user-ls!
   (fn [db _]
     (let [auth (js->clj (.getItem js/localStorage playground-user-key))]
       (assoc db [:auth] auth)))))

 ;; (fn [db [_ user]]))
   ;; (set! (.-localStorage js/window) (clj->js {playground-user-key user}))
   ;; (assoc db :user user)))
