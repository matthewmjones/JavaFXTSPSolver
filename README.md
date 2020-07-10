# Travelling Salesman Problem Solver
This project is part of an outreach programme about algorithms and the traveling salesman problem. To use clone the project and build using `mvn javafx:run`.

It's not meant to be a super-fast solver or anything like that - get the amazing [Concorde TSP Solver](http://www.math.uwaterloo.ca/tsp/concorde.html) if you want that. 

## Using the programme
The GUI is fairly self-explanatory. You can either load a TSP problem from the file menu, or generate one randomly of varying size (up to 200 cities). Then simply pick an algorithm from the following:
- Nearest Neighbour
- Simulated Annealing
- Genetic
- 2-opt (improvment algorithm only)
- 3-opt (improvment algorithm only)

Finally click on Solve to run the algorithm.

In Edit > Settings there are various settings available to fine-tune Simulated Annealing and Genetic algorithm. You can also click on the Slow tick-box to slow things down a bit.

Enjoy!
