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

(defn add-feature
  "Add a new feature to the registry"
  [feature callback]
  (go (let [response (<! (http/post (str conf/registry-api "/api/features") {:json-params feature}))]
        (callback (:body response)))))

(defn update-feature
  "Update a feature in the registry"
  [feature callback]
  (go (let [response (<! (http/put  (str conf/registry-api "/api/features/" (:name feature)) {:json-params feature}))]
        (callback (:body response)))))