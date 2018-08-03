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

; Simplify by creating map with idx and keys first, or collecting into groups?
(defn winning-line?
  [fn-idxs grid plyr]
  (let [moves (map #(nth grid %1) (fn-idxs))]
    (= grid-width (count (filter #(= %1 plyr) moves)))))

; Could form a closure over row and call multiple times from here?
(defn winning-row?
  [grid row plyr]
  (let [fn-idxs #(range (* row grid-width) (* (inc row) grid-width))]
  (winning-line? fn-idxs grid plyr)))

(defn winning-col?
  [grid col plyr]
  (let [fn-idxs #(filter (fn [i] (= col (mod i grid-width))) (range grid-el-count))]
  (winning-line? fn-idxs grid plyr)))

(defn winning-diag?
  [grid plyr]
  (let [fn-idxs-top-l (fn [] (map #(+ % (* % grid-width)) (range grid-width)))
        fn-idxs-top-r (fn [] (map #(+ (- (dec grid-width) %) (* % grid-width)) (range grid-width)))]
  (or (winning-line? fn-idxs-top-l grid plyr) (winning-line? fn-idxs-top-r grid plyr))))

(defn winner?
  [grid plyr]
  (let [winning-rows (map #(winning-row? grid % plyr) (range 0 grid-width))
        winning-cols (map #(winning-col? grid % plyr) (range 0 grid-width))]
    (or (some true? winning-rows)
        (some true? winning-cols)
        (winning-diag? grid plyr))))

(defn game-ended?
  [grid]
  (= 0 (count (filter #(= :- %) grid))))

(defn print-grid
  ([grid] (print-grid grid 0))
  ([grid idx]
    (when (seq grid)
      (when (= 0 (mod idx grid-width)) (newline)) ; print newline if new row
      (print (format "(%d)%s " idx (first grid))) ; print X, O or - at this position
      (recur (rest grid) (inc idx)))))

; TODO Handle invalid input and retry
(defn read-and-apply-move
  [grid plyr]
  (println (format "\n\nYour move, %s" plyr))
  (move grid (Integer/parseInt (read-line)) plyr))

; TODO Ask for opinions on whether to separate gameplay / winner logic from readline / println code.
(defn play
  [grid plyr]
  (print-grid grid)
  (let [new-grid (read-and-apply-move grid plyr)]
   (cond
    (winner? new-grid plyr) (println (format "%s wins!" plyr))
    (game-ended? new-grid) (println "Game is drawn: no heroes, no zeroes.")
    :else (recur new-grid (if (= :X plyr) :O :X)))))
