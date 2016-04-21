(ns pazuzu-ui.handlers
  (:require [re-frame.core :refer [register-handler dispatch]]
            [pazuzu-ui.database :as db]
            [taoensso.timbre :as log]))

(register-handler :initialize-db
  (fn [_ _] db/default-db))

(register-handler :set-active-page
  (fn [db [_ active-page]] (assoc db :active-page active-page)))

(register-handler :select-feature
  (fn [db [_ feature]] (assoc-in db [:registry :selected] feature)))
