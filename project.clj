(defproject pazuzu-ui "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [reagent "0.5.1"]
                 [binaryage/devtools "0.6.1"]
                 [re-frame "0.7.0"]
                 [compojure "1.5.0"]
                 [ring "1.4.0"]

                 [cheshire "5.6.1"]                         ; json parsing/generating
                 [com.taoensso/timbre "4.1.4"]              ; Clojure/Script logging

                 [bidi "2.0.6"]
                 [kibu/pushy "0.3.2"]
                 [cljs-http "0.1.39"]                       ; HTTP client
                 [adzerk/env "0.3.0"]                       ;;managing env variables
                 [cljsjs/codemirror "5.11.0-2"]             ; Editor for Docker File higlighting
                 [com.cemerick/url "0.1.1"]
                 ]

  :plugins [[lein-cljsbuild "1.1.3"]
            [lein-less "1.7.5"]]

  :min-lein-version "2.5.3"

  :source-paths ["src/clj"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"
                                    "test/js"]

  :figwheel {:css-dirs ["resources/public/css"]
             :ring-handler pazuzu-ui.core/app-dev
             :server-ip "0.0.0.0"}

  :less {:source-paths ["src/less"]
         :target-path  "resources/public/css"}

  :repl-options {:nrepl-middleware [cemerick.piggieback/wrap-cljs-repl]}

  :profiles
  {:dev
   {:dependencies [
                   [figwheel-sidecar "0.5.4-3"]
                   [com.cemerick/piggieback "0.2.1"]]

    :plugins      [[lein-figwheel "0.5.4-3"]
                   [lein-doo "0.1.6"]
                   [cider/cider-nrepl "0.13.0-SNAPSHOT"]]
    }}

  :cljsbuild
  {:builds
   [{:id           "dev"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "pazuzu-ui.core/mount-root"
                    :websocket-url "wss://localhost:8080/figwheel-ws"}
     :compiler     {:main                 pazuzu-ui.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true}}
    {:id           "dev-noauth"
     :source-paths ["src/cljs"]
     :figwheel     {:on-jsload "pazuzu-ui.core/mount-root"}
     :compiler     {:main                 pazuzu-ui.core
                    :output-to            "resources/public/js/compiled/app.js"
                    :output-dir           "resources/public/js/compiled/out"
                    :asset-path           "js/compiled/out"
                    :source-map-timestamp true}}
    {:id           "min"
     :source-paths ["src/cljs"]
     :jar true
     :compiler     {:main            pazuzu-ui.core
                    :output-to       "resources/public/js/compiled/app.js"
                    :optimizations   :advanced
                    :closure-defines {goog.DEBUG false}
                    :pretty-print    false}}
    {:id           "test"
     :source-paths ["src/cljs" "test/cljs"]
     :compiler     {:output-to     "resources/public/js/compiled/test.js"
                    :main          pazuzu-ui.runner
                    :optimizations :none}}
    ]}

  :main pazuzu-ui.core

  :aot [pazuzu-ui.core]

  :prep-tasks [["cljsbuild" "once" "min"] "compile"]
  )
