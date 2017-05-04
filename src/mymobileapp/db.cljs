(ns mymobileapp.db
  (:require [clojure.spec :as s]))

;; spec of app-db
(s/def ::id int?)
(s/def :todo/filter keyword?)
(s/def :todo/todo (s/keys :req [:todo/id :todo/title :todo/done]))
(s/def :todo/todos (s/map-of ::id :todo/todo))
(s/def ::app-db
  (s/keys :req [:todo/todos :todo/filter]))
;; initial state of app-db
(def app-db {:todo/todos {}
             :todo/filter :all})
