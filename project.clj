(defproject playground "0.1.0-SNAPSHOT"
  :description "Playground backend"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [aleph/aleph "0.6.1"]
                 [ring/ring "1.10.0"]
                 [integrant "0.8.0"]
                 [environ "1.2.0"]
                 [metosin/muuntaja "0.6.8"]
                 [metosin/reitit "0.7.0-alpha3"]
                 [com.github.seancorfield/next.jdbc "1.3.874"]
                 ;; [org.clojure/java.jdbc "0.7.12"]
                 ;; [mysql/mysql-connector-java "8.0.33"]
                 [org.postgresql/postgresql "42.6.0"]
                 [clj-http "3.12.3"]
                 [net.clojars.kelveden/ring-jwt "2.4.0"]
                 [com.github.seancorfield/honeysql "2.4.1026"]
                 [migratus "1.4.9"]
                 [ring-cors "0.1.13"]
                 [camel-snake-kebab "0.4.3"]
                 [dev.gethop/payments.stripe "0.3.8"]
                 [com.stripe/stripe-java "22.20.0"]
                 [philoskim/debux-stubs "0.8.3"]
                 ;; https://mvnrepository.com/artifact/org.slf4j/slf4j-log4j12
                 [org.slf4j/slf4j-log4j12 "2.0.7" :extension "pom"]]

  :source-paths ["src/clj"]
  :resource-paths ["resources"]
  :profiles {:uberjar {:aot :all}
             :dev {:source-paths ["dev/src"]
                   :resource-paths ["dev/resources"]
                   :dependencies [[philoskim/debux "0.8.3"]
                                  [ring/ring-mock "0.4.0"]
                                  [integrant/repl "0.3.2"]]}}
  :uberjar-name "playground.jar")

;; :license {:name "Eclipse Public License"
;;           :url "http://www.eclipse.org/legal/epl-v10.html"}
;; :main ^:skip-aot playground.server
;; :target-path "target/%s"
