(ns exmod.quiz.events
  (:require
   [re-frame.core :as re-frame]
   [exmod.quiz.data :as quiz]))

(defn reg-quiz-handler
  "Register quiz-related events. Take two arguments, the key for register and
  handler function (the same shape as a regular re-frame handlers)"
  [k updater]
  (re-frame/reg-event-db
   k
   (fn [db signal]
              (update db :quiz #(updater (:quiz db) signal)))))

(reg-quiz-handler
 ::next
 (fn [q _]
   (quiz/next-q q)))

(reg-quiz-handler
 ::prev
 (fn [q _]
   (quiz/prev-q q)))

(reg-quiz-handler
 ::goto-question
 (fn [q [_ n]]
   (quiz/goto-nth-q q n)))

(reg-quiz-handler
 ::answer-current
 (fn [q [_ n]]
   (quiz/answer-current-q q n)))

(reg-quiz-handler
 ::unanswer-current
 (fn [q [_ n]]
   (quiz/unanswer-current-q q n)))
