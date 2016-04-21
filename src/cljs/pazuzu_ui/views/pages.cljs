(ns pazuzu-ui.views.pages
  (:require [re-frame.core :refer [subscribe dispatch]]
            [taoensso.timbre :as log]))

(defn welcome []
  [:div.ui.vertical.masthead.center.aligned.segment
   {:style {:min-height "500px"}}
   [:div.ui.test.container
    [:h1.header {:style {:margin-top "2em"
                         :font-size "3em"}}
     "Welcome to Pazuzu UI"]
    [:h3 "Web application to operate docker features"]
    [:a.ui.huge.primary.button {:href "/api/auth"}
     "Authorize" [:i.icon.right.arrow]]]])

(defn about []
  [:div.ui.text.container
   [:h2 "About Pazuzu"]
   [:p "An evil ancient moster"]])

(defn feature-list-item [props]
  [:div.ui.card.feature
   [:div.content
    #_[:img.right.floated.ui.image {:src "https://placekitten.com/g/50/50"}]
    [:div.header (:name props)]
    [:div.meta "Dependencies:" [:a {:href "#"} "python"]]
    [:div.description "Feature description"]]])

(defn feature-details [name]
  [:div.ui.grid.container
   [:div.row
    [:div.column
     [:div.ui.form
      [:div.field
       [:div.ui.huge.input.fluid
        [:input {:type "text" :placeholder "Feature Name"
                 :value name
                 :on-change (fn [e] (log/debug e))}]]]
      [:div.field
       [:label "Dependencies"]
       [:div.ui.label "python"]
       [:div.ui.label "scala"]]
      [:div.field.code
       [:label "Snippet"]
       [:textarea {:field :textarea
                   :rows 5
                   :value "RUN apt-get install leiningen"
                   :on-change (fn [e] (log/debug e))}]]
      [:div.field.code
       [:label "Test Case command"]
       [:textarea {:field :textarea
                   :rows 3
                   :value "lein -v"
                   :on-change (fn [e] (log/debug e))}]]
      [:div.field
       [:label "Attached files"]
       [:div.ui.label "id-rsa.pub"]
       [:button.mini.ui.basic.button [:i.icon.upload] "Upload file"]]]]]])

(defn registry []
  [:div#registry.ui.padded.grid

   [:div#features-pane.eight.wide.column
    [:div.ui.secondary.menu
     [:div.ui.icon.input
      [:input {:type "text" :placeholder "search"}]
      [:i.search.link.icon]]
     [:div.right.menu
      [:div.item [:div.ui.primary.button "New"]]]]

    [:div.features-list.ui.cards
     (for [i (range 100)] [feature-list-item {:key i :name (str "feature " i)}])]]

   [:div#feature-details.eight.wide.column
    [:div.ui.secondary.menu
     [:div.right.menu
      [:div.item [:div.ui.labeled.icon.button.positive [:i.save.icon] "Save"]]
      [:div.item [:div.ui.labeled.icon.button.negative [:i.delete.icon] "Delete"]]]]

    [feature-details "Supa Dupa Feature"]]])

(defn active-page []
  (let [page (subscribe [:active-page])]
    (fn []
      [:div#page.ui.container
       (case @page
         :home-page     [welcome]
         :about-page    [about]
         :registry-page [registry]
         :not-found     [:div [:h1 "404?!"]]

         ;; default is wtf, unknown routes should be covered abouve
         [:div [:h1 "Internal problem with routes"]])])))