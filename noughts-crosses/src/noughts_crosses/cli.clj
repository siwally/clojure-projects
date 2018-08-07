(ns noughts-crosses.cli
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [noughts-crosses.game :as game]))

(defn print-grid
  ([grid] (print-grid grid 0))
  ([grid idx]
   (when (seq grid)
     (when (= 0 (mod idx game/grid-width)) (newline)) ; print newline if new row
     (print (format "(%d)%s " idx (first grid))) ; print X, O or - at this position
     (recur (rest grid) (inc idx)))))

;; i'd probably try to make the fn above pure, so that you can easily test it
;; i'd also parameterize grid-width?

;; Implementation-wise, it looks good! I guess I'd go for the pipeliney
;; approach. I'm super used to those, so it's a little easier for me to read,
;; but i think it's arguable whether it is or not:

(defn zip-with-index
  "Zips "
  [xs]
  (map vector (range) xs))

(zip-with-index [:a :b :c])
;; => ([0 :a] [1 :b] [2 :c])

(defn grid-rows-str
  [grid-width grid]
  (->> grid
       (zip-with-index)
       (map (fn [[idx el]] (format "(%d)%s" idx el)))
       (partition grid-width)
       (map (partial str/join " "))))

(deftest grid-rows-str-test
  (is
   (= ["(0):- (1):- (2):-"
       "(3):- (4):- (5):-"
       "(6):- (7):- (8):-"]
      (grid-rows-str game/grid-width (game/initial-grid)))))

(defn grid-str
  [grid grid-width]
  (->> grid
       (grid-rows-str grid-width)
       (str/join "\n")))

;; Then print-grid is just:
(print (grid-str (game/initial-grid) game/grid-width))


; TODO Handle invalid input and retry
(defn read-and-apply-move
  [grid plyr]
  (println (format "\n\nYour move, %s" plyr))
  (game/move grid (Integer/parseInt (read-line)) plyr))

(defn play
  [grid plyr]
  (print-grid grid)
  (cond
    (game/winner? grid plyr) (println (format "\n\n%s wins!" plyr))
    (game/grid-full? grid)   (println "\n\nGame is drawn: no heroes, no zeroes.")
    :else (let [next-plyr (if (= :X plyr) :O :X)
                new-grid (read-and-apply-move grid next-plyr)]
            (recur new-grid next-plyr))))

(defn -main
  []
  (play (game/initial-grid) :O))
