(ns pazuzu-ui.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [pazuzu-ui.database :as db]))

(register-handler :initialize-db
  (fn [_ _] db/default-db))

(register-handler :set-active-page
  (fn [previous [_ active-page]]
    (assoc-in previous [:ui-state :active-page] active-page)))

(register-handler :feature-selected
  (fn [db [_ feature]]
    (-> db
        (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] false)
        (assoc-in [:ui-state :registry-page :feature-pane :feature] feature)
        (assoc-in [:ui-state :registry-page :selected-feature-name] (:name feature)))))

(register-handler :search-input-changed
  (fn [db [_ value]]
    (assoc-in db [:ui-state :registry-page :search-input-value] value)))

(register-handler :feature-edited
  (fn [db [_ feature]]
    (assoc-in db [:ui-state :registry-page :feature-pane :feature] feature)))

(register-handler :save-feature-clicked
  (fn [db [_ _]]
    (let [feature (-> db :ui-state :registry-page :feature-pane :feature)
          new-feature? (-> db :ui-state :registry-page :feature-pane :new-feature?)]

      ; if creating feature and there is already one with that name, alert and do nothing
      (if (and new-feature? (get-in db [:registry :features(:name feature)]))

        (do (js/alert "Feature with that name already exists")
            db)

        (-> db
            (assoc-in [:registry :features (:name feature)] feature)
            (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] false))))))

(register-handler :new-feature-clicked
  (fn [db [_ _]]
    (-> db
        (assoc-in [:ui-state :registry-page :feature-pane :feature] {})
        (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] true))))

(register-handler :delete-feature-clicked
  (fn [db [_ _]]
    (let [feature (-> db :ui-state :registry-page :feature-pane :feature)]
      (-> db
          (update-in [:registry :features] dissoc (:name feature))
          (assoc-in [:ui-state :registry-page :feature-pane :feature] {})
          (assoc-in [:ui-state :registry-page :feature-pane :new-feature?] true)))))
