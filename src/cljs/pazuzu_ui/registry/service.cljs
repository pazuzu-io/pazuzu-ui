(ns pazuzu-ui.registry.service
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [pazuzu-ui.config :as conf]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]))

(defn call-response-callback
  "Check for a response if it successful call the normal callback if not call the error callback"
  [response callback error-callback]
  (if (:success response) (callback (:body response)) (error-callback (-> response :body :message))))

(defn flatten_dependencies
  "Takes featrue and changes the dependency objects vector to plane names vector"
  [feature]
  (let [dependencies_names (vec (map #(:name %) (:dependencies feature)))]
    (assoc-in feature [:dependencies] dependencies_names)))

(defn get-features
  "Get the features and execute the callback with the features as argument"
  [callback error-callback]
  (go (let [response (<! (http/get (str conf/registry-api "/api/features") {}))]
      (call-response-callback response  callback error-callback))))

(defn get-features-page
  "Get a features page and execute the callback with the features as argument"
  [offset limit callback error-callback]
  (go (let [response (<! (http/get (str conf/registry-api "/api/features?offset=" offset "&limit=" limit)  {}))
            headers (-> response :headers)]
        (if (:success response) (callback (:body response) (get headers "x-total-count")) (error-callback (-> response :body :message))))))

(defn get-feature
  "Get the detailed feature with the given id"
  [feature-name callback error-callback]
  (go (let [response (<! (http/get (str conf/registry-api (str "/api/features/" feature-name)) {}))]
        (call-response-callback response callback error-callback))))

(defn add-feature
  "Add a new feature to the registry"
  [feature callback error-callback]
  (go (let [response (<! (http/post
                           (str conf/registry-api "/api/features") {:json-params (flatten_dependencies feature)}))]
        (call-response-callback response  callback error-callback))))

(defn update-feature
  "Update a feature in the registry"
  [feature callback error-callback]
  (go (let [response (<! (http/put
                           (str conf/registry-api "/api/features/" (:name feature))
                           {:json-params (flatten_dependencies feature)}))]
        (call-response-callback response  callback error-callback))))


(defn delete-feature
  "Delete a feature in the registry"
  [feature callback error-callback]
  (go (let [response (<! (http/delete (str conf/registry-api "/api/features/" (:name feature))))]
        (call-response-callback response callback error-callback))))

(defn search-tags [query callback error-callback]
  "Fetch tags by query"
  (let [query-url (str conf/registry-api "/api/tags" "/query/" query)]
    (when (not (empty? query)) (go (let [response (<! (http/get query-url {}))]
                        (call-response-callback response  callback error-callback))))))
