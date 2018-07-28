(ns noughts-crosses.core
  (:gen-class))

(defn initial-grid
  []
  [:- :- :-   ; 0, 1, 2
   :- :- :-   ; 3, 4, 5
   :- :- :-]) ; 6, 7, 8

   ; verticals - just need same mod for three els
   ; diagonals from l-to-r, for each row: (row* 3) + row
   ; diagonals from r-to-l: (3 * row+1) - 1, then - row

(defn move
  [grid pos plyr]
  {:pre [(<= 0 pos (dec (count grid))), (= :- (nth grid pos))]}
  (assoc grid pos plyr))

(defn idxs-in-row
  [row]
  (range (* row 3) (* (inc row) 3)))

;  (map vector (range 3)  [:X :X :X])
; make map with the index, then filter based on the keys - better than nth?

; can turn into winning-line and take indexes or fn, but build scenarios first
(defn winning-row?
  [grid row plyr]()
  (let [moves (map #(nth grid %1) (idxs-in-row row))]
  (= (count moves) (count (filter #(= %1 plyr) moves)))
))

(defn winner?
  [grid plyr]
  (or (winning-row? grid 0 plyr) (winning-row? grid 1 plyr) (winning-row? grid 2 plyr)))

(defn play-fn
  []
  (let [grid (atom (initial-grid))]
    (fn [pos plyr] (do (swap! grid move pos plyr)))))
