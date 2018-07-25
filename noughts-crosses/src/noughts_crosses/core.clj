(ns noughts-crosses.core
  (:gen-class))

(type :X)
(type :O)
(type :-)

(defn initial-grid
  []
  [:- :- :-])

(defn free?
  [grid pos]
  (= :- (nth grid pos)))

(defn in-range?
  [pos]
  (<= 0 pos 2))

(defn move
  [grid pos plyr]
  (if (and (in-range? pos) (free? grid pos))
    (map-indexed (fn [idx itm] (if (= idx pos) plyr itm)) grid)
    (throw (IllegalArgumentException. "Move must be in the range 0..2 and refer to a free position on the grid"))))

(defn winner
  [grid plyr]
  (= (count grid) (count (filter (#(= % plyr) grid)))))

(defn play-fn
  []
  (let [grid (atom (initial-grid))]
    (fn [pos plyr] (do (swap! grid move pos plyr)))))
