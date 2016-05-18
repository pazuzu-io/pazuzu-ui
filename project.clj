(defproject pazuzu-ui "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170"]
                 [com.taoensso/timbre "4.1.4"]              ; Clojure/Script logging

                 [reagent "0.5.1"]                          ; React rendering wrapper
                 [re-frame "0.7.0"]                         ; Data-flow library
                 [bidi "2.0.6"]                             ; Frontend routing
                 [kibu/pushy "0.3.2"]                       ; HTML5 history
                 [cljs-http "0.1.39"]                       ; HTTP client
                 [camel-snake-kebab "0.3.0"]                ; Transform snake case to kebab case
                 ]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-figwheel "0.5.0-6"]
            [lein-less "1.7.5"]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :less {:source-paths ["src/less"]
         :target-path  "resources/public/css/compiled"}

  :profiles {:dev {:dependencies [[compojure "1.5.0"]]
                   :figwheel {:ring-handler pazuzu-ui.core/app
                              :css-dirs ["resources/public/css"]}}}

  :cljsbuild {:builds {:dev {:source-paths ["src/cljs"]
                             :figwheel {:on-jsload "pazuzu-ui.core/mount-root"}
                             :compiler {:main pazuzu-ui.core
                                        :output-to "resources/public/js/compiled/app.js"
                                        :output-dir "resources/public/js/compiled/out"
                                        :asset-path "js/compiled/out"
                                        :source-map-timestamp true}}

                       :min {:source-paths ["src/cljs"]
                             :compiler {:main pazuzu-ui.core
                                        :output-to "resources/public/js/compiled/app.js"
                                        :optimizations :advanced
                                        :closure-defines {goog.DEBUG false}
                                        :pretty-print false}}}})
