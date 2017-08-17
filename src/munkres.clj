(ns munkres
  "The Hungarian method is a combinatorial optimization algorithm that solves the assignment problem."
  (:require [clojure.core.matrix :as mat]))

(defn weight-matrix
  "Calculates a weight matrix if you have a function to calculate weights for agent-task pairs."
  [f agents tasks]
  (into [] (for [a agents]
             (into [] (for [t tasks]
                        (f a t))))))

(defn- to-weight-matrix
  "If given a function, converts it to a weight matrix."
  [maybe-f agents tasks]
  (if (fn? maybe-f)
    (weight-matrix maybe-f agents tasks)
    maybe-f))

(defn- double-array-2d [matrix]
  "Creates a double[][] from the given matrix."
  (into-array (Class/forName "[D")
              (map double-array matrix)))

(defn- assignments
  "Extracts the assignments from an AssignmentProblemDense instance."
  [assign-prob agents tasks]
  (->> (range (count agents))
    (map #(.sol assign-prob %))
    (map #(nth tasks % nil))
    (zipmap agents)
    (filter (comp some? val))
    (into {})))

(defn minimize-weight
  "Assigns tasks to agents minimizing the weight. `weights` can either
   be a weight matrix or a function that takes agent-task pairs."
  [weights agents tasks]
  (-> weights
    (to-weight-matrix agents tasks)
    (mat/reshape (repeat 2 (max (count agents)
                                (count tasks))))
    (double-array-2d)
    (AssignmentProblemDense.)
    (as-> x {:assignments (assignments x agents tasks)
             :weight (.weight x)})))
