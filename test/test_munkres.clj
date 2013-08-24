(ns test-munkres
  (:require [clojure.test :refer :all])
  (:require [munkres :refer :all]))


(deftest test-maximal-assignment
         (are [agents tasks f result] (= (solve agents tasks f) result)

              [:agent1 :agent2] [:task1 :task2] [[1 2] [3 4]]
              {:weight 5.0
               :assignments {:agent1 :task1
                         :agent2 :task2}}))
