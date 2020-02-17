(ns exmod.quiz.data
  (:require
   [exmod.question :as question]
   [exmod.quiz.view :as quiz-view]
   [clojure.spec.gen.alpha :as gen]
   [re-frame.core :refer [dispatch]]
   [clojure.spec.alpha :as s]))

(declare has-nth-q? has-prev-q? has-next-q? fully-answered? current-q)

;; -- Data definition and constructor --

(s/def ::questions (s/coll-of record? :min-count 1))
(s/def ::current int?)
(s/def ::score (s/nilable int?))

;; -- Quiz definition --

(s/def ::quiz
  (s/and
     ;; The shape
   (s/keys :req [::questions ::current]
           :opt [::score])

     ;; Current question should be between 0 and count of questions minus one
   (fn [{:keys [::current ::questions]}]
     (and (>= current 0) (< current (count questions))))

     ;; Score field should be either nil or between 0 and count of questions
   (fn [{:keys [::score ::questions]}]
     (s/or :not-scored #(nil? score)
           :scored     #(<= 0 score (count questions))))))

;; -- Constructors --

(s/fdef quiz
  :args (s/and (s/cat :questions (s/coll-of any? :min-count 1)))
  :ret ::quiz)
(defn quiz
  "Data constructor"
  [questions]
  {::questions questions
   ::current   0})

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

;; -- Data extractors --

(defn has-nth-q?
  "Does the quiz has question number n"
  [quiz n]
  (< n (count (::questions quiz))))

(defn has-next-q? [quiz]
  (has-nth-q? quiz (inc (::current quiz))))

(defn has-prev-q? [quiz]
  (has-nth-q? quiz (dec (::current quiz))))

(defn current-q [quiz]
  (get (::questions quiz) (::current quiz)))

(defn fully-answered? [quiz]
  (every? question/answered? (::questons quiz)))

;; -- View related staff --

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
