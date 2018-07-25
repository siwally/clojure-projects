(ns noughts-crosses.core-test
  (:require [clojure.test :refer :all]
            [noughts-crosses.core :refer :all]))

(deftest make-valid-moves
  (testing "Play game and check outcomes for happy path"
    (is (= [:X :X :X] (make-move (make-move (make-move (initial-grid) 0) 1) 2))))

(deftest make-illegal-moves
  (testing "Check illegal moves are rejected"
    (let [moves (make-move (make-move (make-move (initial-grid) 0) 1) 2)]
      (is (thrown? IllegalArgumentException (make-move moves 2)))
      (is (thrown? IllegalArgumentException (make-move moves -1)))
      (is (thrown? IllegalArgumentException (make-move moves 3)))))))

; TODO Test when there is a winner, e.g. game-won? or winner

; TODO Introduce 'X and 'O symbols
