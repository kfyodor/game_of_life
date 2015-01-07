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

(def spaceship
  #{[2 1] [5 1] [6 2] [2 3] [6 3] [3 4] [4 4] [5 4] [6 4]})

(defn init-state
  "Inits the board"
  ([w h]
   (init-state w h (random-cells w h)))
  ([w h c-coords]
   (zipmap c-coords (repeat 1))))

(defn die
  [state cell]
  (dissoc state cell))

(defn rise
  [state cell]
  (assoc state cell 1))

(defn neighbours-for
  [state cell]
  (let [[x y]   cell
        delta   [-1 0 1]]
    (for [dx delta
          dy delta
          :let [nx    (+ dx x)
                ny    (+ dy y)
                alive (if (state [nx ny]) 1 0)
                ncell [nx ny alive]]
          :when (not (= [dx dy] [0 0]))]
      ncell)))

(defn find-cells
  "Alive cells + cells that might get reborn
   plus counts of neighbours for each of them"
  [state]
  (let [cells (group-by identity
                        (mapcat (partial neighbours-for state)
                                (keys state)))]
    (for [[k v] cells]
      [k (count v)])))

(defn apply-rules
  "Applies GOL's rules"
  [state cell-with-count]
  (let [[cell count] cell-with-count
        [x y alive]  cell
        alive?       (= alive 1)]
    (if (or (and alive?
                 (or (= count 2)
                     (= count 3)))
            (and (not alive?)
                 (= count 3)))
      (assoc state [x y] 1)
      state)))

(defn step
  "Next generation"
  [state]
  (reduce apply-rules {} (find-cells state)))

(defn run-life
  [state]
  (iterate step state))
