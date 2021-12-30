So the first 2 attempts did not work fast enough. Going all in on the third attempt.
Going to apply the following optimizations. Still iterating over all possible valid states.
As we do not want to mis any combination. But not following states that have no chance of winning.

- encoded state
  - reduce the size of the state so we can compare faster.
  - encode map with pods into a 64bit long.
    - 11*3=33 bits hallway (empty(0), exit(1), A(2), B(3), C(4), D(5))
    - 8*3=24 bits rooms (empty(0) A(2), B(3), C(4), D(5))
    - 57 bits
- heuristics
  - evaluate states that are more likely to win.
  - by ranking moving into the final spot the highest.
  - ?moving out of the way for a higher number?
- depth first (enough for part one)
  - evaluating states with the highest heuristic value first.
- remove loosing states (enough for part one)
  - if energy of a state is higher or equal than the lowest final energy measured. 
  - remove that state.
