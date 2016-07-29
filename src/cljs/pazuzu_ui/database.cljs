(ns pazuzu-ui.database)

; This is default state of the app
(def default-db
  {:name     "pazuzu-ui"
   :registry {:features []}
   :authentication {:token "not-found"}
   :ui-state {
              :messages []
              :active-page   :home-page
              :registry-page {:search-input-value      ""
                              :features-loading?       false
                              :feature-detail-loading? false
                              :selected-feature-name   nil
                              :feature-pane            {:new-feature? false
                                                        :tag-list-index -1
                                                        :feature      nil}
                              :total-features         0
                              :per-page               10
                              :page                   1}}})
