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

; TODO Potentially simplify winning-col, either with partioning or group-by for col mod
; Something like (map #(map first (partition-all grid-width (range % grid-el-count))) (range grid-width))?
