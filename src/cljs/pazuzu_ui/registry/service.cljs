(ns pazuzu-ui.registry.service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [pazuzu-ui.config :as conf]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [taoensso.timbre :as log]))

(defn flatten_dependencies
  "Takes featrue and changes the dependency objects vector to plane names vector"
  [feature]
  (let [dependencies_names (vec (map #(:name %) (:dependencies feature)))]
    (assoc-in feature [:dependencies] dependencies_names)))

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

(defn add-feature
  "Add a new feature to the registry"
  [feature callback]
  (go (let [response (<! (http/post
                           (str conf/registry-api "/api/features") {:json-params (flatten_dependencies feature)}))]
        (callback (:body response)))))

(defn update-feature
  "Update a feature in the registry"
  [feature callback]
  (go (let [response (<! (http/put
                           (str conf/registry-api "/api/features/" (:name feature))
                           {:json-params (flatten_dependencies feature)}))]
        (callback (:body response)))))

