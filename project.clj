(defproject cheffy "0.1.0-SNAPSHOT"
  :description "Playground backend"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [aleph/aleph "0.6.1"]
                 [ring/ring-core "1.10.0"]
                 [integrant "0.8.0"]
                 [environ "1.2.0"]
                 [metosin/reitit "0.7.0-alpha3"]
                 [com.github.seancorfield/next.jdbc "1.3.874"]
                 [org.postgresql/postgresql "42.6.0"]
                 [clj-http "3.12.3"]
                 [net.clojars.kelveden/ring-jwt "2.4.0"]]
  :source-paths ["src/clj"]
  :resource-paths ["resources"]
  :profiles {:uberjar {:aot :all}
             :dev {:source-paths ["dev/src"]
                   :resource-paths ["dev/resources"]
                   :dependencies [[ring/ring-mock "0.4.0"]
                                  [integrant/repl "0.3.2"]]}}
  :uberjar-name "playground.jar")

;; :license {:name "Eclipse Public License"
;;           :url "http://www.eclipse.org/legal/epl-v10.html"}
;; :main ^:skip-aot playground.server
;; :target-path "target/%s"
