(ns noughts-crosses.core-test
  (:require [clojure.test :refer :all]
            [noughts-crosses.core :refer :all]))

(deftest play-game
  (testing "Play game and check outcomes for happy path"
    (is (= [0 1 2] (play (play (play [] 0) 1) 2)))))

(deftest illegal-moves-rejected
  (testing "Check illegal moves are rejected"
    (let [moves (play (play (play [] 0) 1) 2)]
    (is (thrown? IllegalArgumentException (play moves 2))))))
