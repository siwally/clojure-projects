(ns noughts-crosses.game
  (:gen-class))

(def grid-width  3)
(def grid-el-count (* grid-width grid-width))
;; hmm .. this is just (count grid), right? .. i'd do it inline where needed

;; i wouldn't put this in a fn.. probly just a def, although i guess i would
;; also put it in ox.cli and leave this namespace pure / generic
(defn initial-grid
  []
  [:- :- :-   ; 0, 1, 2
   :- :- :-   ; 3, 4, 5
   :- :- :-]) ; 6, 7, 8

(defn move
  [grid pos plyr]
  {:pre [(< -1 pos grid-el-count), (= :- (nth grid pos))]}
  (assoc grid pos plyr))

(defn grid-full?
  [grid]
  (every? #(not= :- %) grid))

(defn winning-line?
  [idxs grid plyr]
  (every? #(= plyr (nth grid %)) idxs))

(defn winning-row?
  [grid plyr]
  ;; i'd freely use the thread last macro in these
  (->> grid-el-count
       range
       (partition-all grid-width)
       (map #(winning-line? % grid plyr))
       (some true?)))

(defn winning-col?
  [grid plyr]
  (let [col-idxs (fn [col ;; <- is this more of a col-idx?
                     ] (->> grid-el-count
                               range
                               (filter (fn [idx] (= col (mod idx grid-width))))))]
    (->> grid-width
         range
         (map #(winning-line? (col-idxs %) grid plyr))
         (some true?))))


(defn winning-diag?
  [grid plyr]
  (let [idxs-from-top-l (->> grid-el-count
                             range
                             (partition-all (inc grid-width))
                             (map first))
        idxs-from-top-r (->> grid-el-count
                             (range (dec grid-width))
                             (partition (dec grid-width))
                             (map first))]
    (or (winning-line? idxs-from-top-l grid plyr)
        (winning-line? idxs-from-top-r grid plyr))))

;; even if you want to continue using indexes, i'd separate out the logic to
;; extract the indexes from the logic to score them. Now every fn knows how to
;; do two things: extract the rows/cols/diags and decide whether there's a
;; winning one. Some of the logic is duplicated (e.g. (map winning-line?) (some true?))

;e.g.:
(def cols-idxs ,,,)
(def rows-idxs ,,,)
(def diags-idxs ,,,)
(defn get-line
  [idxs grid]
  (map #(nth grid %) idxs))

(defn winner?
  [grid plyr]
  (or (winning-row? grid plyr)
      (winning-col? grid plyr)
      (winning-diag? grid plyr)))

;; TODO Potentially simplify winning-col, either with partioning or group-by for col mod
;; Something like (map #(map first (partition-all grid-width (range % grid-el-count))) (range grid-width))?

;; ^^^^^^^^^^^^^^ .. i'm not sure i love the passing around of indexes.. they couple
;; these fns to the concrete implementation of the grid and they make a lot of
;; the small utilities non-reusable. i'd rather work with the concepts of rows,
;; cols, and diagonals.

;; i'd start by defining your "accessors": rows, cols and diagonals

(defn grid-rows
  [grid-width grid]
  (partition grid-width grid))

(defn grid-cols
  [grid-width grid]
  (->> grid
       ;; i wouldn't worry about efficiency at all here.. there's always time to
       ;; optimize later
       (grid-rows grid-width)
       (apply map vector)))

(grid-rows 2 [:a :b
              :c :d])
;; => ((:a :b) (:c :d))

(grid-cols 3 [:a :b :c
              :d :e :f
              :g :h :i])

;; as I was about to define diagonals, the issue of non-square boards arised. so
;; now we get an extra fn:
(defn grid-square?
  [grid-width grid]
  (= (count (grid-rows grid-width grid))
     (count (grid-cols grid-width grid))))

(grid-square? 2 [:a :a
                 :a :a])
;; => true

(grid-square? 2 [:a :b
                 :c :d
                 :e :f])
;; => false

(defn grid-diags
  [grid-width grid]
    (when (grid-square? grid-width grid)
      (let [rows               (grid-rows grid-width grid)
            diag-from-top-left (fn [rows]
                                 (->> rows
                                      (map (fn [idx row] (nth row idx))
                                           (range))))]
        [(diag-from-top-left rows)
         (diag-from-top-left (map reverse rows))])))

(grid-diags 3 [:a :b :c
               :d :e :f
               :g :h :i])
;; => [(:a :e :i) (:c :e :g)]

;; non-square grid doesn't have a diagonal
(grid-diags 2 [:a :b
               :c :d
               :e :f])
;; => nil

;; just some utils for what's coming

(defn single-element? [coll]
  (= 1 (count coll)))

(defn every= [coll]
  (->> coll set single-element?))

(every= [:a :b])
;; => false
(every= [:a :a])
;; => true

(defn line-full?
  [line]
  (not-any? (partial = :-) line))

;; Once your accessors are done, we can define a generic line-winner
(defn line-winner
  [line]
  (when (and (line-full? line)
             (every= line))
    (first line)))

(line-winner [:X :X :X])
;; => :X
(line-winner [:X :O :X])
;; => nil
(line-winner [:- :- :-])
;; => nil

;; ... and we define a grid-winner in those terms
(defn grid-winner
  [grid-width grid]
  (let [rows    (grid-rows grid-width grid)
        cols    (grid-cols grid-width grid)
        diags   (grid-diags grid-width grid)
        winners (->> (concat (map line-winner rows)
                             (map line-winner cols)
                             ;; note map over nil is just nil so we don't need
                             ;; to worry
                             (map line-winner diags))
                     set
                     (remove nil?))]
    (when (single-element? winners)
      (first winners))))

;; non-square grid
(grid-winner 2 [:a :b
                :c :d
                :e :f])
;; => nil

;; no win
(grid-winner 2 [:a :b
                :c :d])
;; => nil

;; row win
(grid-winner 2 [:a :a
                :c :d])
;; => :a

;; col win
(grid-winner 2 [:a :b
                :a :d])
;; => :a

;; diag win
(grid-winner 2 [:a :b
                :c :a])
;; => :a

(grid-winner 2 [:a :b
                :b :d])
;; => :b

;; row win in non-square grid
(grid-winner 2 [:a :a
                :b :c
                :d :e])
;; => :a

;; col win in non-square grid
(grid-winner 2 [:a :b
                :a :c
                :a :e])
;; => :a

;; ...then the winner? fn becomes:
(defn winner?*
  [plyr grid-width grid]
  (= plyr (grid-winner grid-width grid)))
