(ns noughts-crosses.core-test
  (:require [clojure.test :refer :all]
            [noughts-crosses.core :refer :all]))

(deftest move_test
  (testing "test simple valid move."
    (start-game)
    (is (true? (move 0 'x')))
    (is (true? (move 1 'x')))
    (is (true? (move 2 'x'))))
  )

(deftest move_off_grid_test
  (testing "test simple invalid move pos off grid."
    (start-game)
    ;;(is (false? (move -1 'x')))
    ;;(is (false? (move 3 'x')))
    (is (thrown? IllegalArgumentException (move -1 'x')))
    )
  )

(deftest move_pos_in_use_test
  (testing "position in use")
  (start-game)
  (move 0 'x')
  (is (false? (move 0 'x')))
  (move 1 'x')
  (is (false? (move 1 'x')))
  (move 2 'x')
  (is (false? (move 2 'x')))
  )

;; change move to return nothing and throw exception if invalid