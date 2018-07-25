(ns noughts-crosses.core
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello World!"))

(type :X)
(type :O)
(type :-)

(defn initial-grid
  []
  [:- :- :-])

(defn free?
  [moves pos]
  (= :- (nth moves pos)))

(defn in-range?
  [pos]
  (<= 0 pos 2))

;; play function takes a move, validates it and returns an updates list of moves
(defn make-move
  [moves move]
  (if (and (free? moves move) (in-range? move))
    (map-indexed (fn [idx itm] (if (= move idx) :X itm)) moves)
    (throw (IllegalArgumentException. "Move must be in the range 0..2 and refer to a free slot on the grid"))))

;; use map to swap the element if = pos - probably something built in, but go with this!

(defn play-fn
  []
  (let [moves (atom (initial-grid))]
    (fn [move] (do (swap! moves make-move move)))))
