(ns noughts-crosses.game
  (:gen-class))

(def grid-width  3)
(def grid-el-count (* grid-width grid-width))

(def initial-grid             ; TODO Create grid dynamically based on size
  [:- :- :-   ; 0, 1, 2
   :- :- :-   ; 3, 4, 5
   :- :- :-]) ; 6, 7, 8

(defn move
  [grid pos plyr]
  {:pre [(< -1 pos grid-el-count), (= :- (nth grid pos))]}
  (assoc grid pos plyr))

(defn grid-full?
  [grid]
  (not-any? #{:-} grid))

(defn rows
  [grid]
  (partition grid-width grid))

(defn cols
  [grid]
  (->> (rows grid)
       (apply mapv vector)))

(defn diag-from-top-l
  [grid]
  (->> (partition-all (inc grid-width) grid)
       (map first)
       (vector)))

(defn diag-from-top-r
  [grid]
  (->> (rows grid)
       (map reverse)
       (flatten)
       (diag-from-top-l)))

(defn all-lines
  [grid]
  (->> (concat (diag-from-top-l grid) (diag-from-top-r grid))
       (concat (cols grid))
       (concat (rows grid))))

(defn line-winner?
  [line]
  (reduce #(when (= %1 %2) %1) line)) ; FIXME Don't return :- if line empty

(defn winner?
  [grid plyr]
  (->> (all-lines grid)
       (map #(line-winner? %))
       (some #(= % plyr))))
