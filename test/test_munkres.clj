(ns test-munkres
  (:require [clojure.test :refer :all])
  (:require [munkres :refer :all]))

(defn- weight-f [agent task]
  (-> {:agent1 {:task1 2, :task2 1}
       :agent2 {:task1 3, :task2 4}}
    (get agent)
    (get task)))

(deftest weight-matrix-test
  (is (= (weight-matrix weight-f [] [])
         []))
  (is (= (weight-matrix weight-f [:agent1] [])
         [[]]))
  (is (= (weight-matrix weight-f [] [:task1])
         []))
  (is (= (weight-matrix weight-f
                        [:agent1 :agent2]
                        [:task1])
         [[2] [3]]))
  (is (= (weight-matrix weight-f
                        [:agent1]
                        [:task1 :task2])
         [[2 1]])))

(deftest minimize-weight-test
  (is (= (minimize-weight [] [] [])
         {:assignments {}, :weight 0.0}))
  (is (= (minimize-weight weight-f [] [])
         {:assignments {}, :weight 0.0}))
  (is (= (minimize-weight weight-f [:agent1] [])
         {:assignments {}, :weight 0.0}))
  (is (= (minimize-weight weight-f [] [:task1])
         {:assignments {}, :weight 0.0}))
  (is (= (minimize-weight weight-f
                          [:agent1 :agent2]
                          [:task1])
         {:assignments {:agent1 :task1},
          :weight 2.0}))
  (is (= (minimize-weight weight-f
                          [:agent1]
                          [:task1 :task2])
         {:assignments {:agent1 :task2},
          :weight 1.0}))
  (is (= (minimize-weight weight-f
                          [:agent1 :agent2]
                          [:task1 :task2])
         {:assignments {:agent1 :task2
                        :agent2 :task1}
          :weight 4.0}))
  (is (= (minimize-weight [[2 1] [3 4]]
                          [:agent1 :agent2]
                          [:task1 :task2])
         {:assignments {:agent1 :task2
                        :agent2 :task1}
          :weight 4.0}))
  (is (= (minimize-weight (fn [a b] 0) #{:agent} #{:task})
         {:assignments {:agent :task}, :weight 0.0})))

(deftest maximize-weight-test
  (is (= (maximize-weight [] [] [])
         {:assignments {}, :weight 0.0}))
  (is (= (maximize-weight weight-f [] [])
         {:assignments {}, :weight 0.0}))
  (is (= (maximize-weight weight-f [:agent1] [])
         {:assignments {}, :weight 0.0}))
  (is (= (maximize-weight weight-f [] [:task1])
         {:assignments {}, :weight 0.0}))
  (is (= (maximize-weight weight-f
                          [:agent1 :agent2]
                          [:task1])
         {:assignments {:agent2 :task1},
          :weight 3.0}))
  (is (= (maximize-weight weight-f
                          [:agent1 :agent2]
                          [:task1 :task2])
         {:assignments {:agent1 :task1
                        :agent2 :task2}
          :weight 6.0}))
  (is (= (str (:weight (maximize-weight [] [] [])))
         "0.0"))
  (is (= (maximize-weight (fn [a b] -5) [:agent] [:task])
         {:assignments {:agent :task}, :weight -5.0})))

(deftest testcase1
  (let [[as bs] (read-string (slurp "test/testcase1.edn"))]
    (is (= (Math/round (:weight (minimize-weight (fn [a b]
                                                   (Math/abs ^double (- a b)))
                                                 as bs)))
           695196))))

(run-tests)
