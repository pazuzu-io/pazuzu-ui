(ns pazuzu-ui.views.registry
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [taoensso.timbre :as log]
            [re-frame.core :refer [subscribe dispatch]]))

(defn feature-details [props]
  [:div.ui.grid.container
   [:div.row
    [:div.column
     [:div.ui.form
      [:div.field
       [:div.ui.huge.input.fluid
        [:input {:type "text" :placeholder "Feature Name"
                 :value (:name props)
                 :on-change (fn [e] (log/debug e))}]]]
      [:div.field
       [:label "Dependencies"]
       (for [dep (:dependencies props)] [:div.ui.label {:key dep} dep])]
      [:div.field.code
       [:label "Snippet"]
       [:textarea {:field :textarea
                   :rows 5
                   :value (:snippet props)
                   :on-change (fn [e] (log/debug e))}]]
      [:div.field.code
       [:label "Test Case command"]
       [:textarea {:field :textarea
                   :rows 3
                   :value (:test props)
                   :on-change (fn [e] (log/debug e))}]]
      [:div.field
       [:label "Attached files"]
       [:div.ui.label "id-rsa.pub"]
       [:button.mini.ui.basic.button [:i.icon.upload] "Upload file"]]]]]])

(defn feature-list-item [{:keys [is-selected feature]}]
  [:div.ui.card.feature
   {:on-click #(dispatch [:select-feature feature])
    :class (if is-selected "selected" "not-selected")}
   [:div.content
    [:div.header (:name feature)]
    [:div.meta "Dependencies:"
     (for [dep (:dependencies feature)] [:a {:href "#" :key dep} dep])]
    [:div.description "Feature description"]]])

(defn page []
  (let [state (subscribe [:registry])
        features (reaction (:features @state))
        selected (reaction (:selected @state))]
    (fn []
      [:div#registry.ui.padded.grid

       [:div#features-pane.eight.wide.column
        [:div.ui.secondary.menu
         [:div.ui.icon.input
          [:input {:type "text" :placeholder "search"}]
          [:i.search.link.icon]]
         [:div.right.menu
          [:div.item [:div.ui.primary.button "New"]]]]

        [:div.features-list.ui.cards
         (doall (for [f @features]
                  [feature-list-item {:key (:name f)
                                      :is-selected (= f @selected)
                                      :feature f}]))]]

       [:div#feature-details.eight.wide.column
        [:div.ui.secondary.menu
         [:div.right.menu
          [:div.item [:div.ui.labeled.icon.button.positive [:i.save.icon] "Save"]]
          [:div.item [:div.ui.labeled.icon.button.negative [:i.delete.icon] "Delete"]]]]

        [feature-details @selected]]])))