(ns pazuzu-ui.views.registry
  "Describes components related to registry part of the UI"
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [clojure.string :as s]
            [re-frame.core :refer [subscribe dispatch]]))

(defn feature-details []
  (let [ui-state (subscribe [:ui-state :registry-page :feature-pane])
        feature (reaction (:feature @ui-state))
        update-state-fn (fn [event path]
                          (let [value (-> event .-target .-value)
                                updated (assoc-in @feature path value)]
                            (dispatch [:feature-edited updated])))]
    (fn []
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
           [:label "Dependencies"]
           (for [dep (:dependencies @feature)] [:div.ui.label {:key dep} dep])]
          [:div.field.code
           [:label "Docker file Snippet"]
           [:textarea {:field     :textarea
                       :rows      5
                       :value     (:docker-data @feature)
                       :on-change #(update-state-fn % [:docker-data])}]]
          [:div.field.code
           [:label "Test Case command"]
           [:textarea {:field     :textarea
                       :rows      3
                       :value     (:test @feature)
                       :on-change #(update-state-fn % [:test])}]]
          [:div.field
           [:label "Attached files"]
           [:div.ui.label "id-rsa.pub"]
           [:button.mini.ui.basic.button [:i.icon.upload] "Upload file"]]]]]])))

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
    [:div.header (:name feature)]
    [:div.meta "Dependencies:"
     (for [dep (:dependencies feature)] [:a {:href "#" :key dep} dep])]
    [:div.description "Feature description"]]])

(defn features-list [features selected-name]
  [:div.features-list.ui.cards
   (doall (for [feature features]
            [feature-list-item {:key         (:name feature)
                                :is-selected (= selected-name (:name feature))
                                :feature     feature}]))])

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
        all-features (reaction (-> @registry :features vals))
        page-state (subscribe [:ui-state :registry-page])
        selected-name (reaction (:selected-feature-name @page-state))
        search-suffix (reaction (-> @page-state :search-input-value s/lower-case))]
    (dispatch [:load-features])
    (fn []
      (let [features [{:dependencies []
                       :docker_data "hello"
                       :files [:name "id-rsa.pub"
                               :url "http://example.com/id-rsa.pub"]}]]
        [:div#registry.ui.padded.grid
         [:div#features-pane.eight.wide.column
          [feature-list-menu @search-suffix]
          [features-list features @selected-name]]

         [:div#feature-details.eight.wide.column
          [feature-details-menu]
          [feature-details]]]))))