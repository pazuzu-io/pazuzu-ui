(defproject pazuzu-ui "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [com.taoensso/timbre "4.1.4"]              ; Clojure/Script logging

                 [javax.servlet/servlet-api "2.5"] [ring "1.3.2"] [compojure "1.5.0"] ;;to be able to run compojure as a standalone jar

                 [reagent "0.6.0-rc"]                       ; React rendering wrapper
                 [re-frame "0.7.0"]                         ; Data-flow library
                 [bidi "2.0.6"]                             ; Frontend routing
                 [kibu/pushy "0.3.2"]                       ; HTML5 history
                 [cljs-http "0.1.39"]                       ; HTTP client
                 [adzerk/env "0.3.0"]                       ;;managing env variables
                 [cljsjs/codemirror "5.11.0-2"]             ; Editor for Docker File higlighting
                 ]

  :min-lein-version "2.5.3"

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.0-6"]
            [lein-less "1.7.5"]]

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :less {:source-paths ["src/less"]
         :target-path  "resources/public/css/compiled"}

  :profiles {:dev     {:figwheel  {:ring-handler pazuzu-ui.core/app-routes
                                   :css-dirs     ["resources/public/css"]}
                       :cljsbuild {:builds [{:id           "dev"
                                             :source-paths ["src/cljs"]
                                             :figwheel     {:on-jsload "pazuzu-ui.core/mount-root"}
                                             :compiler     {:main                 pazuzu-ui.core
                                                            :output-to            "resources/public/js/compiled/app.js"
                                                            :output-dir           "resources/public/js/compiled/out"
                                                            :asset-path           "/js/compiled/out"
                                                            :source-map-timestamp true}}
                                            {:id           "min"
                                             :source-paths ["src/cljs"]
                                             :compiler     {:main            pazuzu-ui.core
                                                            :output-to       "resources/public/js/compiled/app.js"
                                                            :optimizations   :none
                                                            :closure-defines {"goog.DEBUG" false}
                                                            :pretty-print    false}}]}}

             :uberjar {:aot          :all
                       :uberjar-name "pazuzu-ui.jar"
                       :cljsbuild    {:builds [{:source-paths ["src/cljs"]
                                                :compiler     {:main                 pazuzu-ui.core
                                                               :output-to            "resources/public/js/compiled/app.js"
                                                               :output-dir           "resources/public/js/compiled/out"
                                                               :asset-path           "js/compiled/out"
                                                               :optimizations        :advanced
                                                               :closure-defines      {debug? false}
                                                               :pretty-print         false
                                                               :source-map-timestamp true}}]}}}

  :uberjar-name "pazuzu-ui.jar"                             ;;the name of the fat jar
  :hooks [leiningen.cljsbuild]
  :main pazuzu-ui.core)
