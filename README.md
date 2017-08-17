# Munkres

The Hungarian method is a combinatorial optimization algorithm that solves the assignment problem O(N^3).
This library wraps java code from [Algorithms](http://algs4.cs.princeton.edu/65reductions)
for use from [Clojure](http://clojure.org)

## Usage
Add this to your project.clj `:dependencies`

`[munkres "0.2.0"]`

Require it from your source code

```clojure
(ns my-ns
  (:require [munkres :as m]))
```

And call it like so:

```clojure
(m/minimize-weight [[2 1] [3 4]]
                   [:agent1 :agent2]
                   [:task1 :task2])
```
Which will result in the assignments:
```clojure
{:assignments {:agent1 :task2
               :agent2 :task1}
 :weight 4.0}
```

See the docstrings for `minimize-weight` for further details.

## Building

Install [Leiningen](https://github.com/technomancy/leiningen) then

`lein javac && lein jar`
