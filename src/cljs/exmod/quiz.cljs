(ns exmod.quiz
  (:require
   [exmod.question :as question]
   [re-frame.core :refer [dispatch]]))

(declare has-nth-q quiz-view score-view navigation-view finish-button)

(defn quiz
  "Data constructor"
  [questions]
  {::questions questions
   ::current 0})

;; -- Data manipulations --

(defn goto-nth-q
  "Go to question number n if it exists"
  [quiz n]
  (if (has-nth-q quiz n)
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

;; -- Private helpers --

(defn- has-nth-q
  "Does the quiz has question number n"
  [quiz n]
  (< n (count (::questions quiz))))

(defn- has-next-q [quiz]
  (has-nth-q quiz (inc (::current quiz))))

(defn- has-prev-q [quiz]
  (has-nth-q quiz (dec (::current quiz))))

(defn- current-q [quiz]
  (get (::questions quiz) (::current quiz)))

(defn- fully-answered? [quiz]
  (every? question/answered? (::questons quiz)))

;; -- Views --


(defn view [quiz]
  (if (nil? (::score quiz))
    (quiz-view quiz)
    (score-view quiz)))

(defn quiz-view [quiz]
  [:div.quiz
   (navigation-view quiz)
   (finish-button quiz)
   [question/view (current-q quiz)]])

(defn navigation-view
  "Navigation panel"
  [quiz]
  [:div.quiz-navigation
   [:button {:on-click #(dispatch [::prev])
             :disabled (not (has-prev-q quiz))}
    "<<"]

   (for [i (-> quiz ::questions count range)]
     ^{:key i} [:div.quiz-navigation-item {:on-click #(dispatch [::goto-question i])
                                           :class (if (= i (::current quiz)) "selected")}
                (str (inc i))])

   [:button {:on-click #(dispatch [::next])
             :disabled (not (has-next-q quiz))}
    ">>"]])

(defn finish-button [quiz]
  (when (fully-answered? quiz)
    [:button.finish-button {:on-click #(dispatch [::finish-quiz])} "Finish"]))

(defn score-view [quiz]
  [:div.score "Score view"])
