(ns playground.become-a-chef.views.agreement
  (:require
   ["@mui/material" :refer [Button Grid Typography]]
   [playground.components.modal :refer [modal]]
   [playground.utilites :as util]
   [re-frame.core :as rf]))

(defn agreement-modal
  []
  (modal {:modal-key :agreement
          :title "Agreement"
          :body
            [:> Grid {:p 5}
             [:> Typography {:variant "h5"}
              "I agree to the terms and conditions of this website, in order to become a Chef."]]
          :footer
            [:> Grid {:container true :spacing 2
                      :p 2}
             [:> Grid {:item true :xs 6}
              [:> Button {:variant "contained"
                          :sx {:bgcolor (util/color [:green :400])
                               "&:hover" {:bgcolor (util/color [:green :500])}}
                          :fullWidth true
                          :on-click #(rf/dispatch [:become-a-chef/agree])}
               "Agree"]]
             [:> Grid {:item true :xs 6}
              [:> Button {:variant "contained"
                          :sx {:bgcolor (util/color [:red :500])
                               "&:hover" {:bgcolor (util/color [:red :600])}}
                          :fullWidth true
                          :on-click #(rf/dispatch [:recipes/close-modal])}
               "Cancel"]]]}))

(defn agreement
  []
  (let [open-modal #(rf/dispatch [:recipes/open-modal :agreement])]
    [:<>
     [agreement-modal]
     [:> Button {:variant "contained"
                 :color "primary"
                 :on-click #(open-modal)}
      "Agreement"]]))
