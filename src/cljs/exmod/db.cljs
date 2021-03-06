(ns exmod.db
  (:require
   [exmod.question.single-answer-question :refer [single-answer-question]]
   [exmod.question.multiple-answer-question :refer [multiple-answer-question]]
   [exmod.quiz.data :refer [quiz]]))

(def questions
  [(single-answer-question "Do you like Clojure" ["Yes" "No" "I don't know"] 0)
   (single-answer-question "Do you like FP" ["Yes" "No" "I don't know"] 0)
   (single-answer-question "Do you like OOP" ["Yes" "No" "I don't know"] 1)
   (single-answer-question "Is Clojure better than TypeScript" ["Definately!" "Who knows" "Type what?"] 2)
   (single-answer-question "Is Clojure better than Elm" ["Yes" "Who knows" "No"] 1)
   (multiple-answer-question "What is your favorite language?" ["Clojure" "Haskell" "JavaScript"] 0 1)])

(def default-db
  {:name "re-frame"
   :quiz (quiz questions)})
