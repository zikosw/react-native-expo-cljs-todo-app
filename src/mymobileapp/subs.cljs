(ns mymobileapp.subs
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
 :get-greeting
 (fn [db _]
   (:greeting db)))

(reg-sub
  :todos
  (fn [db _]
    (:todos db)))