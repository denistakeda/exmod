(ns exmod.quiz.subs
  (:require
   [re-frame.core :as re-frame]
   [exmod.quiz.data :as quiz]))

(re-frame/reg-sub
 ::quiz
 (fn [db]
   (:quiz db)))

(re-frame/reg-sub
 ::quiz-view
 :<- [::quiz]
 (fn [q _]
   #:exmod.quiz.view{:fully-answered?        (quiz/fully-answered? q)
                     :has-previous-question? (quiz/has-prev-q? q)
                     :has-next-question?     (quiz/has-next-q? q)
                     :current-number         (::quiz/current q)
                     :questions-count        (quiz/count-q q)
                     :current-question       (quiz/current-q q)
                     :scored?                (quiz/scored? q)}))
