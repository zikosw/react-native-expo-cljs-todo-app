(ns mymobileapp.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
  :todo/filter
  (fn [db _]
    (:todo/todos db)))

(reg-sub
  :todo/todos
  (fn [db _]
    (:todo/todos db)))

(reg-sub
  :todo/visible-todos
  (fn [query _]
    [(subscribe [:todo/todos])
     (subscribe [:todo/filter])])
  (fn [[todos filter] _]
    todos))