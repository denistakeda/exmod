(ns exmod.quiz.data
  (:require
   [exmod.question :as question]
   [exmod.quiz.view :as quiz-view]
   [re-frame.core :refer [dispatch]]
   [clojure.spec.alpha :as s]))

(declare has-nth-q? has-prev-q? has-next-q? fully-answered? current-q)

;; -- Data definition and constructor --

(s/def ::questons (s/coll-of any? :min-count 1))
(s/def ::current int?)
(s/def ::score int?)

;; -- Quiz definition --

(s/def ::quiz
  (s/and (s/keys :req [::questons ::current]
                 :opt [::score])
         #(< (::current %) (-> ::questions count %))))
(defn quiz
  "Data constructor"
  [questions]
  {::questions questions
   ::current 0})

;; -- Data manipulations --

(defn goto-nth-q
  "Go to question number n if it exists"
  [quiz n]
  (if (has-nth-q? quiz n)
    (assoc quiz ::current n)
    quiz))

(defn next-q
  "Go to next question"
  [quiz]
  (goto-nth-q quiz (inc (::current quiz))))

(defn prev-q
  "Go to previous question"
  [quiz]
  (goto-nth-q quiz (dec (::current quiz))))

(defn answer-current-q
  "Answer current question"
  [quiz n]
  (update-in quiz [::questions (::current quiz)] #(question/answer % n)))

(defn unanswer-current-q
  "Unanswer current question"
  [quiz n]
  (update-in quiz [::questions (::current quiz)] #(question/unanswer % n)))

(defn view [quiz]
  (quiz-view/view
   #::quiz-view
   {:fully-answered         (fully-answered? quiz)
    :has-previous-question? (has-prev-q? quiz)
    :has-next-question?     (has-next-q? quiz)
    :current-number         (::current quiz)
    :questions-count        (count (::questions quiz))
    :current-question       (current-q quiz)
    :scored?                (not (nil? (::score quiz)))

    :on-finish-quiz         ::finish-quiz
    :on-next-question       ::next
    :on-previous-question   ::prev
    :on-select-question     ::goto-question
    :on-answer-current      ::answer-current
    :on-unanswer-current    ::unanswer-current}))

;; -- Private helpers --

(defn- has-nth-q?
  "Does the quiz has question number n"
  [quiz n]
  (< n (count (::questions quiz))))

(defn- has-next-q? [quiz]
  (has-nth-q? quiz (inc (::current quiz))))

(defn- has-prev-q? [quiz]
  (has-nth-q? quiz (dec (::current quiz))))

(defn- current-q [quiz]
  (get (::questions quiz) (::current quiz)))

(defn- fully-answered? [quiz]
  (every? question/answered? (::questons quiz)))
