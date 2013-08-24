(ns munkres
  "The Hungarian method is a combinatorial optimization algorithm that solves the assignment problem.")


(defn maximum-matching
  "Returns the maximal assignment."
  ([^"[[D" weights n]
   (let [assignment (AssignmentProblemDense. weights)]
     {:assignments (for [i (range n)]
                     (.sol assignment i))
      :weight (.weight assignment)}))
  ([agents tasks matrix]
   (let [ac (count agents)
         tc (count tasks)
         n (max ac tc)
         weights (make-array Double/TYPE n n)]
     ; initialise a matrix for the solver
     ; pad with zeroes when dimensions differ
     (dotimes [i n]
       (dotimes [j n]
         (aset-double (aget weights i) j
                      (double
                       (if (and (< i ac) (< j tc))
                         (get-in matrix [i j])
                         0)))))
     (maximum-matching weights n))))

(defn solve
  "Solve a matching of agents to tasks,
  where the weight for an assignment is calculated by f."
  [agents tasks matrix]
  (let [solution (maximum-matching agents tasks matrix)
        matches (solution :assignments)
        matching-tasks (map #(nth tasks % nil) matches)
        assignments (zipmap agents matching-tasks)]
    {:assignments assignments
     :weight (solution :weight)}))

(defn weight-matrix
  "Calculates a weight matrix if you have a function to calculate weights for agent task pairs."
  [agents tasks f]
  (into [] (for [a agents]
             (into [] (for [t tasks]
                        (f a t))))))
