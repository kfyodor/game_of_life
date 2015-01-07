(ns conways-game-of-life.life-test
  (:require [conways-game-of-life.life :refer :all]
            [clojure.test :refer :all]))

(def w 3)
(def h 3)
(def st (init-state w h [[0 1] [1 1] [2 1]]))

(deftest life-test
  (testing "Cell has neighbours"
    (is (= (neighbours-for st [1 1]) (sort '([0 0 0] [1 0 0] [2 0 0]
                                        [0 1 1] [2 1 1] [0 2 0]
                                        [1 2 0] [2 2 0])))))
  (testing "Cells with counts"
    (is (= (sort (find-cells st)) (sort '([[-1 0 0] 1]
                                   [[-1 1 0] 1]
                                   [[-1 2 0] 1]
                                   [[0 0 0] 2]
                                   [[1 0 0] 3]
                                   [[2 0 0] 2]
                                   [[0 1 1] 1]
                                   [[1 1 1] 2]
                                   [[2 1 1] 1]
                                   [[0 2 0] 2]
                                   [[1 2 0] 3]
                                   [[2 2 0] 2]
                                   [[3 0 0] 1]
                                   [[3 1 0] 1]
                                   [[3 2 0] 1])))))

  (testing "Creates next generation"
    (is (= (step st)) {[1 0] 1
                       [1 1] 1
                       [2 1] 1})

    (is (= (step (step st))
           {[0 1] 1 [1 1] 1 [2 1] 1}))))
