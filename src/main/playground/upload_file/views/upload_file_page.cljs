(ns playground.upload-file.views.upload-file-page
  (:require
   ["@mui/material" :refer [Grid Typography Input Button]]
   ["mui-file-input" :refer [MuiFileInput]]
   [playground.components.page-nav :refer [page-nav]]
   [reagent.core :as r]
   [goog.dom :as gdom]
   [re-frame.core :as rf]))

(defn upload-file-page
  []
  (let [handle-change (fn [e]
                        (let [file (first (.-files (.-target e)))]
                          (rf/dispatch [:http/upload-file file])))
        initial-file-data {:file nil}
        file-data (r/atom initial-file-data)]
    [:<>
     [page-nav {:center "Upload File Example"}
      [:> Grid
       [:> Typography {:variant "h4"}
        "Upload File"]]]
     [:<>
      [:h3 "Upload File"]
      [:<>
       [:> Button
        {:variant "contained"
         :component "label"
         :color "primary"
         :on-change #(handle-change %)}
        [:> Input
         {:type "file"
          :id "input"
          :placeholder "Insert File"
          :style {:display "none"}}]
        "Upload File"]]]]))

(comment
  (let [el (gdom/getElement "input")
        file (-> el
                 (.-files)
                 (aget 0))
        form-data (doto (js/FormData.)
                    (.append "file" file))]
    (js/console.log form-data)))
