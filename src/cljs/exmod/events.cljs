(ns exmod.events
  (:require
   [re-frame.core :as re-frame]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ;; Local
   [exmod.db :as db]
   [exmod.quiz.data :as quiz]
   ))

;; -- Global --

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))
