(ns noughts-crosses.core-test
  (:require [clojure.test :refer :all]
            [noughts-crosses.core :refer :all]))

(deftest horz-winner-test
  (testing "Play game and check wins and loses for a horizontal line."
    (let [grid
          (-> (initial-grid)
              (move 0 :X)
              (move 1 :X)
              (move 2 :X))]
      (is (true? (winner? grid :X)))
      (is (false? (winner? grid :O))))))

(deftest vert-winner-test
  (testing "Play game and check wins and losses for a vertical line."
    (let [grid
          (-> (initial-grid)
              (move 1 :O)
              (move 4 :O)
              (move 7 :O))]
      (is (true? (winner? grid :O)))
      (is (false? (winner? grid :X))))))

(deftest diag-ltor-winner-test
  (testing "Play game and check wins and losses for a diagonal line, starting top left."
    (let [grid
          (-> (initial-grid)
              (move 0 :O)
              (move 4 :O)
              (move 8 :O))]
      (is (true? (winner? grid :O)))
      (is (false? (winner? grid :X))))))

  (deftest diag-rtol-winner-test
    (testing "Play game and check wins and losses for a diagonal line, starting top right."
      (let [grid
            (-> (initial-grid)
                (move 2 :X)
                (move 4 :X)
                (move 6 :X))]
        (is (true? (winner? grid :X)))
        (is (false? (winner? grid :O))))))

(deftest move-illegal-positions-test
  (testing "Check illegal moves are rejected."
    (let [grid
          (-> (initial-grid)
              (move 0 :X)
              (move 1 :X)
              (move 2 :X))]
      (is (thrown? AssertionError (move grid 1 :X)))
      (is (thrown? AssertionError (move grid 2 :O)))
      (is (thrown? AssertionError (move grid -1 :X)))
      (is (thrown? AssertionError (move grid 9 :X))))))
