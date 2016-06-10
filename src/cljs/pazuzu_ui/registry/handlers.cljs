(ns pazuzu-ui.registry.handlers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :refer [register-handler dispatch]]
            [pazuzu-ui.registry.service :as service]
            [taoensso.timbre :as log]))

;;Loading helper function
(defn start-loading [db type] (assoc-in db [:ui-state :registry-page type] true))
(defn stop-loading [db type] (assoc-in db [:ui-state :registry-page type] false))

;; whener a feature is clicked in the registry page
(register-handler :feature-selected
                  (fn [db [_ feature]]
                    (do
                      (service/get-feature (:name feature)
                                            #(do (log/debug "Fetched : " %)
                                                (dispatch [:feature-selected-loaded %]))
                                            #((dispatch [:add-message {:type "error" :header "Error Retrieving the feature" :message %} ])))
                      (start-loading db :feature-detail-loading?))))

;; whenever the feature selected is loaded, update the db
(register-handler :feature-selected-loaded
                  (fn [db [_ feature]]
                    (-> db
                        (stop-loading :feature-detail-loading?)
                        (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] false)
                        (assoc-in [:ui-state :registry-page :feature-pane :feature] feature)
                        (assoc-in [:ui-state :registry-page :selected-feature-name] (:name feature)))))

;; when the search input value is updated, propagates the change to the db
(register-handler :search-input-changed
                  (fn [db [_ value]]
                    (assoc-in db [:ui-state :registry-page :search-input-value] value)))

(register-handler :feature-edited
                  (fn [db [_ feature]]
                    (assoc-in db [:ui-state :registry-page :feature-pane :feature] feature)))

;; when the save button is clicked, update the db by pushing the new feature to the list of features
(register-handler :save-feature-clicked
                  (fn [db [_ _]]
                    (let [feature (-> db :ui-state :registry-page :feature-pane :feature)
                          new-feature? (-> db :ui-state :registry-page :feature-pane :new-feature?)]
                      ; if creating feature and there is already one with that name, alert and do nothing
                      (if new-feature?
                        (service/add-feature feature
                          #(dispatch [:saved-feature %])
                          #(dispatch [:add-message {:type "error" :header "Error Saving the features" :message %}]))
                        (service/update-feature feature
                          #(dispatch [:updated-feature %])
                          #(dispatch [:add-message {:type "error" :header "Error Updating the features" :message %}])))
                      (start-loading db :feature-detail-loading?))))

;; when dependency is removed from the list, only db state is updated
(register-handler :delete-dependency-clicked
                  (fn [db [_ deleted_dep]]
                    (let [dependencies (-> db :ui-state :registry-page :feature-pane :feature :dependencies)
                          dependencies_after_removal (vec (remove #{deleted_dep} dependencies))]
                      (-> db
                          (assoc-in [:ui-state :registry-page :feature-pane :feature :dependencies] dependencies_after_removal)))))

;; when dependency is removed from the list, only db state is updated
(register-handler :add-dependency-clicked
                  (fn [db [_]]
                    (let [new_dependency (-> db :ui-state :registry-page :feature-pane :feature :new-dependency)
                          dependencies (-> db :ui-state :registry-page :feature-pane :feature :dependencies)
                          extended_dependencies (vec (conj (set dependencies) {:name new_dependency}))]
                      (-> db
                          (assoc-in [:ui-state :registry-page :feature-pane :feature :dependencies] extended_dependencies)
                          (assoc-in [:ui-state :registry-page :feature-pane :feature :new-dependency] nil)))))


;; update db state after api retures success for adding a feature
(register-handler :saved-feature
                  (fn [db [_ feature]]
                    (let [current_features (-> db :registry :features)
                          per-page (-> db :ui-state :registry-page :per-page)
                          total-features (-> db :ui-state :registry-page :total-features)]
                      (dispatch [:add-message {:type "success" :header "Your feature has been saved" :time 3}])
                      (-> db
                          (assoc-in [:registry :features] (conj (if (< (count current_features) per-page) current_features (butlast current_features)) feature))
                          (assoc-in [:ui-state :registry-page :total-features] (inc (int total-features)))
                          (assoc-in [:ui-state :registry-page :selected-feature-name] (:name feature))
                          (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] false)
                          (stop-loading :feature-detail-loading?)))))


;; update db state after api retures success for updating a feature
(register-handler :updated-feature
                  (fn [db [_ feature]]
                    (dispatch [:add-message {:type "success" :header "Your feature has been updated" :time 3}])
                    (stop-loading db :feature-detail-loading?)))


;; when new feature button is clicked, push an empty feature to the db flagged new-feature = true
(register-handler :new-feature-clicked
                  (fn [db [_ _]]
                    (-> db
                        (assoc-in [:ui-state :registry-page :feature-pane :feature] {})
                        (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] true))))

;; when the delete feature button is clicked, make an API call
(register-handler :delete-feature-clicked
                  (fn [db [_ _]]
                    (let [feature (-> db :ui-state :registry-page :feature-pane :feature)]
                      (start-loading db :feature-detail-loading?)
                      (service/delete-feature feature
                        #(dispatch [:deleted-feature])
                        #(dispatch [:add-message {:type "error" :header "Error Deleting the feature" :message %}]))
                      db)))

;; when the delete operation was successful, update the db state
(register-handler :deleted-feature
                  (fn [db [_ _]]
                    (let [feature (-> db :ui-state :registry-page :feature-pane :feature)
                          features (-> db :registry :features)
                          features_after_removal (vec (filter #(not= (:name %) (:name feature)) features))]
                      (-> db
                          (stop-loading :feature-detail-loading?)
                          (assoc-in [:registry :features] features_after_removal)
                          (assoc-in [:ui-state :registry-page :feature-pane :feature] {})
                          (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] true)))))

;;when the registry page loads call the backend to list available features
(register-handler :load-features
                  (fn [db [_ _]]
                    (service/get-features #(do (log/debug "Features received from the backend : " %)
                                               (dispatch [:loaded-features %]))
                                          #(do (log/debug "Fail to retrive features : " %)
                                               (dispatch [:add-message {:type "error" :header "Error Retrieving the features" :message %} ])))
                    (start-loading db :features-loading?)))

;;load just a page of the registre features
(register-handler :load-features-page
                  (fn [db [_ _]]
                    (let [per-page (-> db :ui-state :registry-page :per-page)
                          page (-> db :ui-state :registry-page :page)
                          offset (* (- page 1) per-page)]
                          (service/get-features-page offset per-page
                              (fn [features total]
                                (log/debug "Features received from the backend : " total)
                                (dispatch [:loaded-features-page (list features total)]))
                              #(do (log/debug "Fail to retrive features : " %)
                                   (dispatch [:add-message {:type "error" :header "Error Retrieving the features" :message %} ])))
                    (assoc-in db [:ui-state :registry-page :features-loading?] true))))


;; update the db state by setting the features
(register-handler :loaded-features
                  (fn [db [_ features]]
                    (-> db
                        (stop-loading :features-loading?)
                        (assoc-in [:registry :features] features))))

;;when a page is loaded we got total-features parameter
(register-handler :loaded-features-page
                  (fn [db [_ params]]
                    (-> db
                        (assoc-in [:ui-state :registry-page :features-loading?] false)
                        (assoc-in [:ui-state :registry-page :total-features] (nth params 1))
                        (assoc-in [:registry :features] (nth params 0)))))

;;set a new page and call load-features
(register-handler :change-feature-page
                  (fn [db [_ page]]
                    (dispatch [:load-features-page])
                    (assoc-in db [:ui-state :registry-page :page] page)))
