(ns conways-game-of-life.core
  [:require [quil.core :as q]])

(def grid-width 10)
(def width      500)
(def height     500)
(def cell-color {true 0x000000 false 200})

(defn setup []
  (q/smooth)
  (q/frame-rate 1)
  (q/background 0xffffff))

(defn init-random-state []
  (let [cells-count 1500]
    (loop [cells #{}]
      (let [new-cells (conj cells {:x      (rand-int (/ width grid-width))
                                   :y      (rand-int (/ height grid-width))
                                   :alive? true})]
        (if (= cells-count (count new-cells))
          new-cells
          (recur new-cells))))))

(def game-state (atom (init-random-state)))

(defn count-next-generation
  [state]
  (map (partial apply-rules state) state))


(defn find-neighbours-for
  [state point]
  (let [{:keys [x y]} point
        nx              [(inc x) (dec x)]
        ny              [(inc y) (dec y)]]
    (filter (fn [p]
              (and (some #(= % (:x p)) nx)
                   (some #(= % (:y p)) ny)
                   (:alive? p)))
            state)))

(defn apply-rules
  [state cell]
  (let [n-count (count (find-neighbours-for state cell))
        alive   (if (:alive? cell)
                  (not (or (< n-count 2) (> n-count 3)))
                  (= n-count 3))]
    (assoc cell :alive? alive)))

(defn next-generation! []
  (swap! game-state count-next-generation))

(defn draw []
  (q/background 0xffffff)
  (q/no-stroke)
  (doseq [point @game-state]
    (q/fill (cell-color (:alive? point)))
    (q/rect (* grid-width (:x point))
            (* grid-width (:y point))
            grid-width
            grid-width)))

(q/defsketch game-of-life
  :title "Conway's Game Of Life"
  :setup    setup
  :draw     #((draw) (next-generation!))
  :features [:keep-on-top]
  :size     [width height])
