(ns pazuzu-ui.registry.service
  (:require-macros [cljs.core.async.macros :refer [go]]
                   [taoensso.timbre :as log])
  (:require [pazuzu-ui.config :as conf]
            [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [cemerick.url :refer [url query->map map->URL url-encode]]))

(defn call-response-callback
  "Check for a response if it successful call the normal callback if not call the error callback"
  [response callback error-callback]
  (if (:success response)
    (callback (:body response))
    (error-callback (get-in response [:body :title]))))

(defn flatten_dependencies
  "Takes featrue and changes the dependency objects vector to plane names vector"
  [feature]
  (let [dependencies_names (vec (map #(:name %) (:dependencies feature)))]
    (assoc-in feature [:dependencies] dependencies_names)))

(defn- registry-url
  [{:keys [protocol host path]
    :or {host conf/registry-api
         protocol conf/registry-protocol}}]
  (map->URL {:protocol protocol
             :host host
             :path path}))

(defn- authentication-header
  [token]
  {:headers {"Authorization" (str "Bearer " token)}})

(defn features
  [token
   {:keys [offset limit name method query-params json-params]
    :or {name "" method http/get query-params {} json-params {}}}]
  (method (registry-url {:path (str "/api/features/" name)})
          (-> (authentication-header token)
              (assoc :query-params query-params)
              (assoc :json-params json-params))))

(defn get-features-page
  "Get a features page and execute the callback with the features as argument"
  [token offset limit callback error-callback]
  (go
    (let [response (<! (features token {:query-params {:offset offset :limit limit}}))]
      (if (:success response)
        (callback (:body response) (get-in response [:headers "x-total-count"]))
        (error-callback (get-in response [:body :title]))))))

(defn get-feature
  "Get the detailed feature with the given id"
  [token feature-name callback error-callback]
  (go
    (let [response (<! (features token {:name feature-name}))]
      (call-response-callback response callback error-callback))))

(defn add-feature
  "Add a new feature to the registry"
  [token feature callback error-callback]
  (go
    (let [response (<! (features token
                                 {:method http/post
                                  :json-params (flatten_dependencies feature)}))]
      (call-response-callback response  callback error-callback))))

(defn update-feature
  "Update a feature in the registry"
  [token feature callback error-callback]
  (go
    (let [response (<! (features token
                                 {:method http/put
                                  :name (:name feature)
                                  :json-params (flatten_dependencies feature)}))]
      (call-response-callback response  callback error-callback))))

(defn delete-feature
  "Delete a feature in the registry"
  [token feature callback error-callback]
  (go (let [response (<! (features token {:method http/delete
                                          :name (:name feature)}))]
        (call-response-callback response callback error-callback))))

(defn search-tags [token query callback error-callback]
  "Fetch tags by query"
  (when (not (empty? query))
    (go
      (let [query-url (registry-url {:path (str "/api/tags/query/" query)})
            response (<! (http/get query-url (authentication-header token)))]
        (call-response-callback response callback error-callback)))))
