(ns pazuzu-ui.registry.handlers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :refer [register-handler dispatch]]
            [pazuzu-ui.registry.service :as service]
            [taoensso.timbre :as log]))

;; whener a feature is clicked in the registry page
(register-handler :feature-selected
                  (fn [db [_ feature]]
                    (service/get-feature (:name feature)
                                         #(do (log/debug "Fetched : " %)
                                              (dispatch [:feature-selected-loaded %])))
                    db))

;; whenever the feature selected is loaded, update the db
(register-handler :feature-selected-loaded
                  (fn [db [_ feature]]
                    (-> db
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
                        (service/add-feature feature #(do
                                                         (log/debug "added " %)
                                                         (dispatch [:saved-feature %])))

                        ;(do
                        ;  (-> db
                        ;      (assoc-in [:registry :features] feature)
                        ;      ;(assoc-in [:registry :features (:name feature)] feature)
                        ;      ;(assoc-in [:ui-state :registry-page :feature-pane :new-feature?] false)
                        ;      )
                        (do
                          (service/update-feature feature #(log/debug "updated")))))
                    db
                    ))


;; update db state after api retures success for adding a feature
(register-handler :saved-feature
                  (fn [db [_ feature]]
                    (log/debug "in handler ")
                    (-> db
                        (assoc-in [:registry :features] (conj (-> db :registry :features) feature)))))


;; when new feature button is clicked, push an empty feature to the db flagged new-feature = true
(register-handler :new-feature-clicked
                  (fn [db [_ _]]
                    (-> db
                        (assoc-in [:ui-state :registry-page :feature-pane :feature] {})
                        (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] true))))

;; when the delete feature button is clicked, remove from the db
(register-handler :delete-feature-clicked
                  (fn [db [_ _]]
                    (let [feature (-> db :ui-state :registry-page :feature-pane :feature)]
                      (-> db
                          (update-in [:registry :features] dissoc (:name feature))
                          (assoc-in [:ui-state :registry-page :feature-pane :feature] {})
                          (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] true)))))

;;when the registry page loads call the backend to list available features
(register-handler :load-features
                  (fn [db [_ _]]
                    (service/get-features #(do (log/debug "Features received from the backend : " %)
                                               (dispatch [:loaded-features %])))
                    db))

;; update the db state by setting the features
(register-handler :loaded-features
                  (fn [db [_ features]]
                    (-> db
                        (assoc-in [:registry :features] features))))