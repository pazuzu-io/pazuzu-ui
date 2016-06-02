(ns pazuzu-ui.handlers
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [re-frame.core :refer [register-handler dispatch]]
            [pazuzu-ui.database :as db])
  (:import [goog.ui IdGenerator]))

;;general purpose event handlers
(register-handler :initialize-db
                  (fn [_ _] db/default-db))

(register-handler :set-active-page
                  (fn [previous [_ active-page]]
                    (assoc-in previous [:ui-state :active-page] active-page)))

;;UI messages event handlers
(defn allocate-next-id
  "Returns the next message id"
  []
  (.getNextUniqueId (.getInstance IdGenerator)))


(register-handler :add-message
                  (fn [db [_ msg]]
                    (let [id (allocate-next-id)]
                      (if (> (:time msg) 0)
                        (js/setTimeout #(dispatch [:remove-message id]) (* (:time msg) 1000)))
                      (update-in db [:ui-state :messages] conj (assoc msg :id id)))))

(register-handler :remove-message
                  (fn [db [_ id]]
                    (update-in db [:ui-state :messages] #(filter (fn [msg] (not= id (:id msg))) %) id)))