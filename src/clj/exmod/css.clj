(ns exmod.css
  (:require [garden.def :refer [defstyles]]))

(def lightest-grey-color "#f0f0f0")
(def green-color "#28a745")

(defstyles screen
  [:.quiz
   [:.quiz-navigation {:display "flex"
                       :flex-direction "row"}
    [:.quiz-navigation-item :button {:flex       1
                                     :text-align "center"
                                     :cursor "   pointer"}]
    [:.quiz-navigation-item {:padding "1rem"}
     [:&:hover {:text-decoration "underline"
                :background-color lightest-grey-color}]
     [:&.selected {:font-weight      "bold"
                   :background-color lightest-grey-color}]]]
   [:.question
    [:.question-text {:text-align    "center"
                      :margin-top    "2rem"
                      :margin-bottom "1rem"
                      :font-size     "18px"
                      :font-weight   "bold"}]
    [:.answer {:text-align "center"
               :font-size "15px"
               :padding ".5rem 0"
               :cursor "pointer"}
     [:&:hover {:background-color lightest-grey-color
                :text-decoration "underline"}]
     [:&.selected {:background-color lightest-grey-color}]]]
   [:.finish-button {:width "100%"
                     :cursor "pointer"
                     :padding "1rem"
                     :background-color green-color
                     :color "white"
                     :font-size "18px"
                     :font-weight "bold"
                     :border "none"
                     :outline "none"
                     :margin-top ".5rem"}]])
