(ns playground.helpers
  (:require [cljs-time.core :as t]
            [clojure.string :as str]
            [playground.utilites :refer [keywordize-at-keys
                                         associate-by]]))

;; https://stackoverflow.com/questions/32511405/how-would-time-ago-function-implementation-look-like-in-clojure
(defn time-ago
  [timestamp]
  (let [units [{:name "second" :limit 60 :in-second 1}
               {:name "minute" :limit 3600 :in-second 60}
               {:name "hour" :limit 86400 :in-second 3600}
               {:name "day" :limit 604800 :in-second 86400}
               {:name "week" :limit 2629743 :in-second 604800}
               {:name "month" :limit 31556926 :in-second 2629743}
               {:name "year" :limit js/Number.MAX_VALUE :in-second 31556926}]
        time (js/Date. timestamp)
        diff (t/in-seconds (t/interval time (t/now)))]
    (if (< diff 5)
      "just now"
      (let [unit (first (drop-while #(or (>= diff (:limit %))
                                         (not (:limit %)))
                                    units))]
        (-> (/ diff (:in-second unit))
            Math/floor
            int
            (#(str % " " (:name unit) (when (> % 1) "s") " ago")))))))

(defn format-price
  [price]
  (str "R$ " (/ price 100)))

(defn valid-number
  [number]
  (and (not (js/isNaN (js/parseInt number)))
       (number? (js/parseInt number))))

(defn associate-by-kkeyword-value
  "Associate by key the key-work values and transform key to keyword, as well as it's value.
  Generally, used for grouping data by the unique values in a coll, coming from requests."
  [key coll]
  (->> coll
       (keywordize-at-keys #{key})
       (associate-by key)))

(def api-url "http://localhost:8666")
(defn endpoint
  "Concat any params to api-url separated by /"
  [& params]
  (str/join "/" (cons api-url params)))
;; (endpoint "v1")

(comment
  ;; Examples:

  (let [a [{:id "a" :b 1}
           {:id "b" :b 2}
           {:id "c" :b 3}]]
    (->> a
         (keywordize-at-keys #{:id})
         (associate-by :id)))
  ;; => {:a {:id :a, :b 1}, :b {:id :b, :b 2}, :c {:id :c, :b 3}}

  (let [ex [{:recipe/recipe_id "a1995316-80ea-4a98-939d-7c6295e4bb46"
             :recipe/public true
             :recipe/prep_time 5
             :recipe/name "Avocado Salad"
             :recipe/img "https://res.cloudina … cipe/vegie-salad.jpg"
             :recipe/favorite_count 5
             :recipe/uid "jade@mailinator.com"}]]
    (associate-by-kkeyword-value :recipe/recipe_id ex))
  ;; => {:a1995316-80ea-4a98-939d-7c6295e4bb46
  ;;     #:recipe{:recipe_id :a1995316-80ea-4a98-939d-7c6295e4bb46,
  ;;              :public true,
  ;;              :prep_time 5,
  ;;              :name "Avocado Salad",
  ;;              :img "https://res.cloudina … cipe/vegie-salad.jpg",
  ;;              :favorite_count 5,
  ;;              :uid "jade@mailinator.com"}}

         ;; (map (fn [[k v]] (println k v)))
         ;; (into {} (map (fn [[k v]] {(keyword k) (if (= k :id) v (keyword v))})))))

  (def a [{:a "a" :b 1}
          {:a "b" :b 2}
          {:a "c" :b 3}])
  ;; => #'cljs.user/a
  a
  ;; => [{:a "a", :b 1} {:a "b", :b 2} {:a "c", :b 3}]
  (map (juxt :a identity) a))
  ;; => (["a" {:a "a", :b 1}] ["b" {:a "b", :b 2}] ["c" {:a "c", :b 3}])
