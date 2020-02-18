(ns exmod.quiz.data
  (:require
   [exmod.question :as question]
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

(defn count-q
  "Count of questions"
  [quiz]
  (count (::questions quiz)))

;; -- Data extractors --

(defn has-nth-q?
  "Does the quiz has question number n"
  [quiz n]
  (< n (count (::questions quiz))))

(defn has-next-q? [quiz]
  (has-nth-q? quiz (inc (::current quiz))))

(defn has-prev-q? [quiz]
  (has-nth-q? quiz (dec (::current quiz))))

(defn scored? [quiz]
  (not (nil? (::score quiz))))

(defn current-q [quiz]
  (get (::questions quiz) (::current quiz)))

(defn fully-answered? [quiz]
  (every? question/answered? (::questons quiz)))
