(ns pazuzu-ui.views.message
  (:require [re-frame.core :refer [subscribe dispatch]]))

(defn simple-message
  "Semantic UI Message with header and text description that can be autoclosed after some seconds"
  [key type header message time]
  (fn []
    (if (> time 0) (js/setTimeout #(dispatch [:remove-message key]) (* time 1000)))
    [:div.ui.message {:class-name type}
     [:i.close.icon {:on-click #(dispatch [:remove-message key])}]
     [:div.header header]
     [:p message]
     ]
  )

)

(defn simple-message-list
  "It will process a list of messages that be expected formed by {type header message time} and render a simple-message for each one"
  [messages]
  [:div.message-list
    (map-indexed (fn [idx msg] ^{:key (str idx "-" (:header msg))} [simple-message idx (:type msg) (:header msg) (:message msg) (:time msg)]) messages)
  ]
)
