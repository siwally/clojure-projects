(ns noughts-crosses.core-test
  (:require [clojure.test :refer :all]
            [noughts-crosses.core :refer :all]))

(deftest make-valid-moves
  (testing "Play game and check outcomes for happy path"
    (is (= [0 1 2] (make-move (make-move (make-move [] 0) 1) 2)))))

(deftest make-illegal-moves
  (testing "Check illegal moves are rejected"
    (let [moves (make-move (make-move (make-move [] 0) 1) 2)]
      (is (thrown? IllegalArgumentException (make-move moves 2)))
      (is (thrown? IllegalArgumentException (make-move moves -1)))
      (is (thrown? IllegalArgumentException (make-move moves 3))))))

; TODO Test when there is a winner, e.g. game-won?

; TODO Introduce 'X and 'O symbols and set up array with nils initially
