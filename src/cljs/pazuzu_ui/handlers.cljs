(ns pazuzu-ui.handlers
    (:require [re-frame.core :refer [register-handler dispatch]]
              [pazuzu-ui.database :as db]))

(register-handler :initialize-db
                  (fn [_ _] db/default-db))

(register-handler :set-active-page
                  (fn [db [_ active-page]] (assoc db :active-page active-page)))
