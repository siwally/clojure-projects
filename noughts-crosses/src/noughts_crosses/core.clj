(ns noughts-crosses.core
  (:gen-class))

(defn initial-grid
  []
  [:- :- :-   ; 0, 1, 2
   :- :- :-   ; 3, 4, 5
   :- :- :-]) ; 6, 7, 8

   ; diagonals from l-to-r, for each row: (row* 3) + row
   ; diagonals from r-to-l: (3 * row+1) - 1, then - row

(defn move
  [grid pos plyr]
  {:pre [(< -1 pos (count grid)), (= :- (nth grid pos))]}
  (assoc grid pos plyr))

(defn winning-line?
  [get-idxs-fn grid plyr]
  (let [moves (map #(nth grid %1) (get-idxs-fn))] ; TODO Simplify, maybe reducing by row or col key?
    (= (count moves) (count (filter #(= %1 plyr) moves)))))

(defn winning-row?
  [grid row plyr]
  (winning-line? (fn [] (range (* row 3) (* (inc row) 3))) grid plyr))

(defn winning-col?
  [grid col plyr]
  (winning-line? (fn [] (filter (fn [i] (= col (mod i 3))) (range 9))) grid plyr))

(defn winner?
  [grid plyr]
  (or (winning-row? grid 0 plyr) (winning-row? grid 1 plyr) (winning-row? grid 2 plyr)
      (winning-col? grid 0 plyr) (winning-col? grid 1 plyr) (winning-col? grid 2 plyr)))

(defn play-fn
  []
  (let [grid (atom (initial-grid))]
    (fn [pos plyr] (do (swap! grid move pos plyr)))))
