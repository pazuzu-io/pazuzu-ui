(ns pazuzu-ui.registry.view
  "Describes components related to registry part of the UI"
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [clojure.string :as s]
            [pazuzu-ui.views.loading-component :refer [loading-component]]
            [re-frame.core :refer [subscribe dispatch]]
            [pazuzu-ui.views.pagination :refer [pagination]]))

(defn feature-details []
  (let [ui-state (subscribe [:ui-state :registry-page :feature-pane])
        feature (reaction (:feature @ui-state))
        dependencies (:dependencies @feature)
        update-state-fn (fn [event path]
                          (let [value (-> event .-target .-value)
                                updated (assoc-in @feature path value)]
                            (dispatch [:feature-edited updated])))]
    (identity
      [:div.ui.grid.container
       [:div.row
        [:div.column
         [:div.ui.form
          [:div.field
           (if (:new-feature? @ui-state)
             [:div.ui.huge.input.fluid
              [:input {:type      "text" :placeholder "Feature Name"
                       :value     (:name @feature)
                       :on-change #(update-state-fn % [:name])}]]
             [:h1 (:name @feature)])]
          [:div.field
           (if (empty? (:dependencies @feature))
             [:div.field
              [:label "No dependencies"]]
             [:div.field
              [:label "Dependencies"]
              (map #(identity
                     [:div.ui.label
                      {:key (:name %)} (:name %)
                      [:i.delete.icon
                       {:on-click (fn [] (dispatch [:delete-dependency-clicked %]))}]]) dependencies)])
           [:div
            [:div.ui.mini.action.input
             [:input.ui {:type      "text" :placeholder "Dependency name"
                         :value     (:new-dependency @feature)
                         :on-change #(update-state-fn % [:new-dependency])}]
             [:div.ui.mini.icon.button.positive
              {:on-click #(dispatch [:add-dependency-clicked])
               :class    (if (empty? (:new-dependency @feature)) :disabled)}
              [:i.add.icon] "Add"]]]]
          [:div.field.code
           [:label "Docker file Snippet"]
           [:textarea {:field     :textarea
                       :rows      5
                       :value     (:docker_data @feature)
                       :on-change #(update-state-fn % [:docker_data])}]]
          [:div.field.code
           [:label "Test Case command"]
           [:textarea {:field     :textarea
                       :rows      3
                       :value     (:test_instruction @feature)
                       :on-change #(update-state-fn % [:test_instruction])}]]]]]])))

(defn feature-details-menu []
  [:div.ui.secondary.menu
   [:div.right.menu
    [:div.item [:div.ui.labeled.icon.button.positive
                {:on-click #(dispatch [:save-feature-clicked])}
                [:i.save.icon] "Save"]]
    [:div.item [:div.ui.labeled.icon.button.negative
                {:on-click #(dispatch [:delete-feature-clicked])}
                [:i.delete.icon] "Delete"]]]])

(defn feature-list-item [{:keys [is-selected feature]}]
  [:div.ui.card.feature
   {:on-click #(dispatch [:feature-selected feature])
    :class    (if is-selected "selected" "not-selected")}
   [:div.content
    [:div.header (:name feature)]]])

(defn features-list [features selected-name]
  (let [total-features (subscribe [:ui-state :registry-page :total-features])
        per-page (subscribe [:ui-state :registry-page :per-page])
        page (subscribe [:ui-state :registry-page :page])]
    [:div.features-list.ui.cards
     (doall (for [feature features]
              [feature-list-item {:key         (:name feature)
                                  :is-selected (= selected-name (:name feature))
                                  :feature     feature}]))
      [pagination @total-features @per-page @page :change-feature-page]]))

(defn feature-list-menu [suffix]
  [:div.ui.secondary.menu
   [:div.ui.icon.input
    [:input {:type      "text" :placeholder "search"
             :value     suffix
             :on-change #(dispatch [:search-input-changed (-> % .-target .-value)])}]
    [:i.search.link.icon]]
   [:div.right.menu
    [:div.item [:div.ui.primary.button
                {:on-click #(dispatch [:new-feature-clicked])}
                "New"]]]])

(defn page
  "Top-level page layout and state"
  []
  (let [registry (subscribe [:registry])
        all-features (reaction (-> @registry :features))
        page-state (subscribe [:ui-state :registry-page])
        features-loading? (reaction (-> @page-state :features-loading?))
        feature-detail-loading? (reaction (-> @page-state :feature-detail-loading?))
        selected-name (reaction (:selected-feature-name @page-state))
        search-suffix (reaction (-> @page-state :search-input-value s/lower-case))]
    (dispatch [:load-features-page])
    (fn []
      (let [suffix-predicate #(s/includes? (-> % :name s/lower-case) @search-suffix)
            features (->> @all-features
                          (filter suffix-predicate)
                          (sort-by :name))]
        [:div#registry.ui.padded.grid
         [:div#features-pane.five.wide.column
          [feature-list-menu @search-suffix]
          (loading-component @features-loading? [features-list features @selected-name])]

         [:div#feature-details.eleven.wide.column
          (loading-component @feature-detail-loading? [:div [feature-details] [feature-details-menu]])]]))))
