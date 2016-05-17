(ns pazuzu-ui.database
  (:require [clojure.string :as s]))


(defn feature-stub
  "Simple helper to create features"
  [name deps]
  {:name name
   :dependencies deps
   :docker-data (s/join "\n" (for [dep deps] (str "RUN apt-get install " dep)))
   :test "lein -v"
   :files [:name "id-rsa.pub"
           :url "http://example.com/id-rsa.pub"]})

;TODO: this stub should be replaced with data from server
(def features (into [(feature-stub "Foo" ["python" "scala" "postgresql"])
                     (feature-stub "Bar" ["java" "ruby" "mongo"])
                     (feature-stub "Baz" ["golang" "cassandra" "redis"])]
                    (for [i (range 10)] (feature-stub (str "Feature - " i)
                                                       (for [j (range (min (+ i 1) 10))]
                                                         (str "dependency-" j))))))

; This is default state of the app
(def default-db
  {:name "pazuzu-ui"
   :registry {:features []}
   :ui-state {:active-page :home-page
              :registry-page {:search-input-value ""
                              :selected-feature-name (-> features first :name)
                              :feature-pane {:new-feature? false
                                             :feature (first features)}}}})