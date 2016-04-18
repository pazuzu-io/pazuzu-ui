(ns pazuzu-ui.core
    (:require [taoensso.timbre :as log :include-macros true]
              [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [pazuzu-ui.handlers]
              [pazuzu-ui.subscriptions]
              [pazuzu-ui.routes :as routes]
              [pazuzu-ui.views :as views]
              [pazuzu-ui.config :as config]))

(when config/debug?
  (log/debug "Running app in dev mode"))

(defn mount-root []
  (reagent/render [views/root-component]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (routes/init-routes)
  (mount-root))
