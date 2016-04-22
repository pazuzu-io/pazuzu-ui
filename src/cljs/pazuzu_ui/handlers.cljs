(ns pazuzu-ui.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [pazuzu-ui.database :as db]))

(register-handler :initialize-db
  (fn [_ _] db/default-db))

(register-handler :set-active-page
  (fn [db [_ active-page]] (assoc-in db [:ui-state :active-page] active-page)))

(register-handler :select-feature
  (fn [db [_ feature]] (assoc-in db [:registry :selected] feature)))

(register-handler :search-input-changed
  (fn [db [_ feature]] (assoc-in db [:registry :page] feature)))

