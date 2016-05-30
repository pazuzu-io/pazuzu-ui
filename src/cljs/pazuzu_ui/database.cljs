(ns pazuzu-ui.database)

; This is default state of the app
(def default-db
  {:name     "pazuzu-ui"
   :registry {:features []}
   :ui-state {:active-page   :home-page
              :registry-page {:search-input-value      ""
                              :features-loading?       false
                              :feature-detail-loading? false
                              :selected-feature-name   nil
                              :feature-pane            {:new-feature? false
                                                        :feature      nil}}}})