(ns pazuzu-ui.views.code-editor
  (:require
    [reagent.core :as reagent]
    [cljsjs.codemirror]
    [taoensso.timbre :as log]))

(defn code-editor
  [mode text change-callback]
     (reagent/create-class
       {:component-did-mount
         (fn [comp] (let [cm (.fromTextArea
             js/CodeMirror
             (reagent/dom-node comp)
             #js {
               :mode mode
               :lineNumbers   true
             })]
             ;(reagent/set-state comp (assoc (reagent/state comp) :editor cm))
             (.on cm "change" (fn [cm change] (change-callback (.getValue cm))))))
        :display-name  (str "code-editor-" mode)
        ;:component-did-update
          ;(fn [comp _] (println (.getValue (reagent/dom-node comp))))
          ;(fn [comp _] (.setValue (:editor (reagent/state comp)) (get-in (reagent/props comp) [:text] "")))
        :reagent-render
          (fn [mode text change-callback]
            [:textarea {:field     :textarea
                        :rows      3
                        :value     text
                        :on-change #(identity %)}])}))
