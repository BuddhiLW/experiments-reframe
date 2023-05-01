(ns playground.nav.views.authenticated
  (:require
   ["@mui/material" :refer [Box Button Grid Typography]]
   [playground.nav.views.nav-item :refer [box]]
   [re-frame.core :as rf]))

(defn authenticated
  []
  (let [active-nav @(rf/subscribe [:active-nav])
        nav-items [{:id :saved
                    :name "Saved"
                    :href "#saved"
                    :dispatch #(rf/dispatch [:set-active-nav :saved])}
                   {:id :recipes
                    :name "Recipes"
                    :href "#recipes"
                    :dispatch #(rf/dispatch [:set-active-nav :recipes])}
                   {:id :inbox
                    :name "Inbox"
                    :href "#inbox"
                    :dispatch #(rf/dispatch [:set-active-nav :inbox])}
                   {:id :become-a-chef
                    :name "Chef"
                    :href "#become-a-chef"
                    :dispatch #(rf/dispatch [:set-active-nav :become-a-chef])}
                   {:id :profile
                    :name "Profile"
                    :href "#profile"
                    :dispatch #(rf/dispatch [:set-active-nav :profile])}]]
    [:<>
     [:> Box {:display "flex"
              :justify-content "flex-end"
              :py 1}
      (for [{:keys [id name href dispatch]} nav-items]
        [box {:key id
              :id id
              :name name
              :href href
              :dispatch dispatch
              :active-nav active-nav}])]]))
