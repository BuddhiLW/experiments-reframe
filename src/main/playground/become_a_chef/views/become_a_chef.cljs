(ns playground.become-a-chef.views.become-a-chef
  (:require
   [playground.components.page-nav :refer [page-nav]]
   [playground.become-a-chef.views.agreement :refer [agreement]]
   ["@mui/material" :refer [Box Grid Typography ButtonIcon IconButton
                            Card CardMedia]]
   ["@mui/icons-material/CheckCircle" :default CheckCircleIcon]))

(defn become-a-chef
  []
  (let [steps [{:header "Create your recipes for free"
                :sub-header "Start by creating a draft. Add name and cooking time. Make your recipe stand out by adding picture and describe all needed ingredients and steps."}
               {:header "Publish and get noticed"
                :sub-header "Easily publish your recipes and allow people to get in touch with you via one click. Await a message for your first cooking event."}
               {:header "Cook for the first time"
                :sub-header "Talk to the interested person and agree on the date, time, and location. Show your best at the even and become a chef."}]]
    [:> Box
     [page-nav {:center "Become a Chef"
                :right [agreement]}]
     [:> Grid {:container true
               :direction "row"
               :spacing 2}
      [:> Grid {:item true
                :xs 6
                :display "flex"
                :justify-content "center"
                :align-items "center"}
       #_[:iframe {:src "https://br.investing.com/commodities/us-coffee-c"
                   :style {:border "0px #ffffff none"
                           :name "myiFrame"
                           :scrolling "no"
                           :frameborder "1"
                           :marginheight "0px"
                           :marginwidth "0px"
                           :height "400px"
                           :width "600px"
                           :allowfullscreen true}}]

       ;; <iframe src="https://br.investing.com/commodities/us-coffee-c" style="border:0px #ffffff none;" name="myiFrame" scrolling="no" frameborder="1" marginheight="0px" marginwidth="0px" height="400px" width="600px" allowfullscreen></iframe>
       [:> Card {:sx {:max-width 700
                      :max-height 700
                      :border-radius "100%"}
                 :class-name "rounded-full"
                 :variant "outlined"}
        [:> CardMedia {:component "img"
                       :class-name "rounded-full"
                       :image "https://s0.wp.com/wp-content/themes/pub/cortado/assets/images/foodiesfeed.com_holding-espresso-cup_web-1024x1024.jpg"
                       :alt "Hand holding a cup of coffee"}]]]

      [:> Grid {:item true
                :xs 6
                :display "flex"
                :sx {:flex-direction "column"}
                :gap 3
                :justify-content "flex-start"}
       (for [{:keys [header sub-header]} steps]
         ^{:key header}
         [:> Card {:sx {:maxwidth "70%"}}
          [:> Box {:display "flex"
                   :justify-content "flex-start"
                   :maxwidth "95%"}
           [:> Box {:display "flex"
                    :justify-content "center"
                    :pl 2
                    :align-items "center"}
            [:> IconButton {:sx {:color "#27AB83"}}
             [:> CheckCircleIcon {:sx {:font-size "2em"}}]]]
           [:> Box {:p 7
                    :pl 3}
            [:> Grid
             [:> Typography {:variant "h4"}
              header]]
            [:> Grid
             [:> Typography {:variant "body1"}
              sub-header]]]]])]]]))
