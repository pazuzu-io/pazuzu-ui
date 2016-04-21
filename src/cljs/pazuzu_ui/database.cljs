(ns pazuzu-ui.database)


(defn feature-stub [name deps]
  {:name name
   :dependencies deps
   :snippet "RUN apt-get install leiningen"
   :test "lein -v"
   :files [:name "id-rsa.pub"
           :url "http://example.com/id-rsa.pub"]})

(def features [(feature-stub "Foo" ["python" "scala" "postgresql"])
               (feature-stub "Bar" ["java" "ruby" "mongo"])
               (feature-stub "Baz" ["golang" "cassandra" "redis"])])

(def default-db
  {:name "pazuzu-ui"
   :active-page :home-page
   :registry {:features features
              :selected (first features)}})
