(ns noughts-crosses.core-test
  (:require [clojure.test :refer :all]
            [noughts-crosses.core :refer :all]))

(deftest move-test
  (testing "Play game and check outcomes for happy path"
    (let [f (play-fn)]
    (is (= [:X :X :O] (do (f 0 :X) (f 1 :X) (f 2 :O)))))))

(deftest move-illegal-positions-test
  (testing "Check illegal moves are rejected"
    (let [grid (move (move (move (initial-grid) 0 :X) 1 :X) 2 :O)]
      (is (thrown? IllegalArgumentException (move grid 2 :X)))
      (is (thrown? IllegalArgumentException (move grid -1 :X)))
      (is (thrown? IllegalArgumentException (move grid 3 :X))))))

(deftest winner-test
  (testing "Check function to determine when game is won"

  ))
