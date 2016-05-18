(ns pazuzu-ui.registry.service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [pazuzu-ui.config :as conf]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(defn get-features
  "Get the features and execute the callback with the features as argument"
  [callback]
  (go (let [response (<! (http/get (str conf/registry-api "/api/features") {}))]
        (callback (:body response)))))

(defn get-feature
  "Get the detailed feature with the given id"
  [feature-name callback]
  (go (let [response (<! (http/get (str conf/registry-api (str "/api/features/" feature-name)) {}))]
        (callback (:body response))))
  )