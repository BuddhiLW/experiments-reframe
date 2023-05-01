(ns playground.theme
  (:require
   ["@mui/material/styles" :refer [createTheme]]
   ["react-bootstrap" :as bt]))

(def cheffy-theme
  [:<>
   [:> bt/style
    {:black "#243B53"
     :primary "#27AB83"
     :secondary "#F2F2F2"
     :light "#F2F2F2"
     :dark "#243B53"
     :borderRadius "14px"
     :borderRadiusSm "8px"
     :borderRadiusLg "20px"
     :modalHeaderBorderColor "white"}]])

(def theme (createTheme
            (clj->js
             {:palette
              {:primary
               {:main "#27AB83"
                :black "#AAAAAA"
                :primary "#27AB83"
                :secondary "#F7D070"
                :light "#D9E2EC"
                :dark "#243B53"
                :borderRadius "20px"
                :borderRadiusSm "10px"
                :borderRadiusLg "18px"
                :modalHeaderBorderColor "white"}}})))

(def nav (createTheme
          (clj->js
           {:palette
            {:primary
             {:main "#001"
              :secondary "#FF00AA"
              :tertiary "#4433FF"}}})))
