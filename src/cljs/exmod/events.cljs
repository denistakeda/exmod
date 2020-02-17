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

;; -- Quiz --

(re-frame/reg-event-db
 ::quiz/next
 (fn-traced [db _]
   (update db :quiz quiz/next-q)))

(re-frame/reg-event-db
 ::quiz/prev
 (fn-traced [db _]
   (update db :quiz quiz/prev-q)))

(re-frame/reg-event-db
 ::quiz/goto-question
 (fn-traced [db [_ n]]
   (update db :quiz #(quiz/goto-nth-q % n))))

(re-frame/reg-event-db
 ::quiz/answer-current
 (fn-traced [db [_ n]]
   (update db :quiz #(quiz/answer-current-q % n))))

(re-frame/reg-event-db
 ::quiz/unanswer-current
 (fn-traced [db [_ n]]
   (update db :quiz #(quiz/unanswer-current-q % n))))

(re-frame/reg-event-db
 ::quiz/finish-quiz
 (fn-traced [db _]
   db))
