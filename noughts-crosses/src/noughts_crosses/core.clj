(ns noughts-crosses.core
  (:gen-class))

(def grid-width  3)
(def grid-el-count (* grid-width grid-width))

(defn initial-grid
  []
  [:- :- :-   ; 0, 1, 2
   :- :- :-   ; 3, 4, 5
   :- :- :-]) ; 6, 7, 8

(defn move
  [grid pos plyr]
  {:pre [(< -1 pos grid-el-count), (= :- (nth grid pos))]}
  (assoc grid pos plyr))

; Can simplify with reduce or by creating map with idx and keys up front?
(defn winning-line?
  [fn-idxs-for-line grid plyr]
  (let [moves (map #(nth grid %1) (fn-idxs-for-line))]
    (= grid-width (count (filter #(= %1 plyr) moves)))))

; Make winning-row? and winning-col check all rows and columns, using grid-width
(defn winning-row?
  [grid row plyr]
  (winning-line? #(range (* row grid-width) (* (inc row) grid-width))
                 grid
                 plyr))

(defn winning-col?
  [grid col plyr]
  (winning-line? #(filter (fn [i] (= col (mod i grid-width))) (range grid-el-count))
                 grid
                 plyr))

(defn winning-diag?
  [grid plyr]
  (or
   ; pass indexes for diagonal line, starting at top-left corner
   (winning-line? (fn [] (map #(+ % (* % grid-width)) (range grid-width)))
                  grid
                  plyr)
   ; pass indexes for diagonal line, starting at top-right corner
   (winning-line? (fn [] (map #(+ (- (dec grid-width) %) (* % grid-width)) (range grid-width)))
                  grid
                  plyr)))

(defn winner?
  [grid plyr]
  (or (winning-row? grid 0 plyr) (winning-row? grid 1 plyr) (winning-row? grid 2 plyr)
      (winning-col? grid 0 plyr) (winning-col? grid 1 plyr) (winning-col? grid 2 plyr)
      (winning-diag? grid plyr)))

; TODO Handle invalid input and allow retry
(defn play
  [grid plyr]
  (prn grid)
  (prn (format "Your move, %s" plyr))
  (let [new-grid (move grid (Integer/parseInt (read-line)) plyr)]
    (if (true? (winner? new-grid plyr))
        (prn (format "%s wins!" plyr))
        (recur new-grid (if (= :X plyr) :O :X)))))
