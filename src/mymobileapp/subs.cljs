(ns mymobileapp.subs
  (:require [re-frame.core :refer [reg-sub subscribe]]))

(reg-sub
  :todo/showing
  (fn [db _]
    (:todo/showing db)))

(reg-sub
  :todo/todos
  (fn [db _]
    (vals (:todo/todos db)))) ;; use vals with sorted map to extract val

(reg-sub
  :todo/visible-todos
  (fn [query _]
    [(subscribe [:todo/todos])
     (subscribe [:todo/showing])])

  ;; Filter todo by showing
  (fn [[todos showing] _]
    (let [filter-fn (case showing
                      :active (complement :todo/done)
                      :done   :todo/done
                      :all    identity)]
      (filter filter-fn todos))))
        
