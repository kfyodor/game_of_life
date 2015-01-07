(ns conways-game-of-life.life
  (:require [clojure.set :as set]
            [clojure.string :as s]))

(defn random-cells
  "Generates random 30% population"
  [w h]
  (let [population (int (* 0.3 w h))]
    (set
      (take population
            (shuffle
             (for [x (range w)
                   y (range h)] [x y]))))))

(def glider-gun
  " ........................O
    ......................O.O
    ............OO......OO............OO
    ...........O...O....OO............OO
    OO........O.....O...OO
    OO........O...O.OO....O.O
    ..........O.....O.......O
    ...........O...O
    ............OO

    Gosper's glider gun initial cells"
  #{[24 0] [22 1] [24 1]
    [12 2] [13 2] [20 2] [21 2] [34 2] [35 2]
    [11 3] [15 3] [20 3] [21 3] [34 3] [35 3]
    [0 4] [1 4] [10 4] [16 4] [20 4] [21 4]
    [0 5] [1 5] [10 5] [14 5] [16 5] [17 5] [22 5] [24 5]
    [10 6] [16 6] [24 6]
    [11 7] [15 7]
    [12 8] [13 8]})

(defn init-board
  "Inits the board"
  ([w h]
   (init-board w h (random-cells w h)))
  ([w h c-coords]
   (apply sorted-set
          (for [x (range w)
                y (range h)
                :let [has-cell (seq (set/select #(= [x y] %)
                                                c-coords))]]
            (if has-cell [x y true] [x y false])))))

(defn cell-alive?
  [cell]
  (last cell))

(defn neighbours
  "Shows neighbour cells coords"
  [cell]
  (let [[x y] cell]
    (set
      (for [nx    [-1 0 1]
            ny    [-1 0 1]
            :let  [n [(+ nx x) (+ ny y) true]]
            :when (not (= n cell))]
        n))))

(defn count-neighbours
  "Counts neighbours"
  [state cell]
  (count (set/intersection state
                           (neighbours cell))))

(defn apply-rules
  "Applies GOL's rules"
  [state cell]
  (let [cn    (count-neighbours state cell)
        alive (if (cell-alive? cell)
                  (or (= 2 cn) (= 3 cn))
                  (= 3 cn))]
    (conj (pop cell) alive)))

(defn step
  "Generation"
  [state]
  (apply sorted-set
         (map (partial apply-rules state) state)))

(defn run-life
  [board]
  (iterate step board))

(defn print-step
  [board]
  (println)
  (print (s/join "\n"
                 (for [cells (vals (group-by second
                                             board))]
                   (s/join
                    " "
                    (map #(if (last %) "o" ".")
                         cells)))))
  (println))
