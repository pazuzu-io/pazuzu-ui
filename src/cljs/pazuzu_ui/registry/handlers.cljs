(ns pazuzu-ui.registry.handlers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :refer [register-handler dispatch]]
            [pazuzu-ui.registry.service :as service]
            [taoensso.timbre :as log]))

;; whener a feature is clicked in the registry page
(register-handler :feature-selected
                  (fn [db [_ feature]]
                    (do
                      (service/get-feature (:name feature)
                                           #(do (log/debug "Fetched : " %)
                                                (dispatch [:feature-selected-loaded %])))
                      (assoc-in db [:ui-state :registry-page :feature-detail-loading?] true))))

;; whenever the feature selected is loaded, update the db
(register-handler :feature-selected-loaded
                  (fn [db [_ feature]]
                    (-> db
                        (assoc-in [:ui-state :registry-page :feature-detail-loading?] false)
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
                      db)))

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
                            (assoc-in [:ui-state :registry-page :feature-pane :feature :new-dependency] nil))
                        )))

;; when tag was added to a feature
(register-handler :add-feature-tag-clicked
                  (fn [db [_]]
                    (let [new_feature_tag  (-> db :ui-state :registry-page :feature-pane :feature :new-feature-tag)
                          tags (-> db :ui-state :registry-page :feature-pane :feature :tags)
                          updated_feature_tags (vec (conj (set tags) {:name new_feature_tag}))]

                      (-> db
                          (assoc-in [:ui-state :registry-page :feature-pane :feature :tags] updated_feature_tags)
                          (assoc-in [:ui-state :registry-page :feature-pane :feature :new-feature-tag] nil)
                          )
                      )))

;; when tag delete

(register-handler :delete-feature-tag-clicked
                  (fn [db [_ deleted_tag]]
                    (let [tags (-> db :ui-state :registry-page :feature-pane :feature :tags)
                          updated_feature_tags (vec (remove #{deleted_tag} tags))]
                      (-> db
                          (assoc-in [:ui-state :registry-page :feature-pane :feature :tags] updated_feature_tags)))))

;; update db state after api retures success for adding a feature
(register-handler :saved-feature
                  (fn [db [_ feature]]
                    (let [current_features (-> db :registry :features)]
                      (dispatch [:add-message {:type "success" :header "Your feature has been saved" :time 3}])
                      (-> db
                          (assoc-in [:registry :features] (conj current_features feature))
                          (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] false)))))


;; update db state after api retures success for updating a feature
(register-handler :updated-feature
                  (fn [db [_ feature]]
                    (#(log/debug "updated feature " %) feature)
                    (dispatch [:add-message {:type "success" :header "Your feature has been updated" :time 3}])
                    db))


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
                    (assoc-in db [:ui-state :registry-page :features-loading?] true)))

;; update the db state by setting the features
(register-handler :loaded-features
                  (fn [db [_ features]]
                    (-> db
                        (assoc-in [:ui-state :registry-page :features-loading?] false)
                        (assoc-in [:registry :features] features))))
