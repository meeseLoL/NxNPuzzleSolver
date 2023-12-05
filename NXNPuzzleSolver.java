/**
 * NXNPuzzleSolver - A Java program to solve N x N puzzles using Breadth-First Search.
 *
 * 
 * @author Justin Ballard
 * @author Morgan Von Thaden
 * @author Sam Farnsley
 */


import java.util.LinkedList;
import java.util.Arrays;

public class NXNPuzzleSolver {

    /**
    * A representative vertex of a puzzle state. Constructs an inverse lineage back to the starting position.
    *
    *
    */
    public static class Node {
        int[] state;
        Node parent;
        int zeroIndex;

        /**
         * Constructor for creating a new puzzle node.
         *
         * @param state      The state of the puzzle.
         * @param zeroIndex  Index of the zero (empty space) in the puzzle state.
         * @param parent     Parent node in the puzzle tree.
         */
        public Node(int[] state, int zeroIndex, Node parent) {
            this.state = state;
            this.parent = parent;
            this.zeroIndex = zeroIndex;
        }
    }

    // Internal solver data
    private Node root;  // The root node.
    private LinkedList<int[]> visited = new LinkedList<>();  // The visited list, for when printout occurs.
    // Here lies a very misguided attempt to convert something into more efficency without realizing just what it entailed.
       
         /**
         * Constructor for creating a new puzzle solver.
         *
         * @param state      The initial state of the puzzle.
         */
    public NXNPuzzleSolver(int[] state) {
        int index = getZeroIndex(state);
        this.root = new Node(state, index, null);
    }

    /**
     * Get the index of the zero (empty space) in the puzzle state.
     *
     * @param state The puzzle state.
     * @return Index of the zero.
     */
    public int getZeroIndex(int[] state) {
        for (int i = 0; i < state.length; i++) {
            if (state[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Check if the puzzle is solved.
     *
     * @param state The puzzle state.
     * @return True if the puzzle is solved, false otherwise.
     */
    public boolean checkSolved(int[] state) {
        for (int i = 0; i < state.length - 1; i++) {
            if (state[i] > state[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Swaps two elements in a given array, based on the provided indices.
     *
     * @param state The array to alter.
     * @param i     Index of the first element to swap.
     * @param j     Index of the second element to swap.
     */
    void swap(int[] state, int i, int j) {
        int temp = state[i];
        state[i] = state[j];
        state[j] = temp;
    }

    /**
     * Check if a state has been visited before.
     *
     * @param toAdd State to check.
     * @return True if the state has been visited, false otherwise.
     */
    boolean checkVisited(int[] toAdd) {
        for (int[] visitedState : visited) {
            if (Arrays.equals(visitedState, toAdd)) {
                return true;
            }
        }
        return false;
    }
   
   
    /**
     * A size optimization method.
     *
     * @param queue The queue LinkedList from the calling method.
     * @param n The value to adjust the second swapping indice with.
     * @param node The node to base the new state on.
     * @param zeroIndex The index of the zero on this current state.
     */

   private void processIndex(LinkedList<Node> queue, int n, Node node, int zeroIndex) {
      int[] newState = Arrays.copyOf(node.state, node.state.length); // Create a placeholder array that shall contain the output.
      swap(newState, zeroIndex, zeroIndex + n);                      // Swap the array elements accordingly
      if (!checkVisited(newState)) {                                 // If the new state is unique..
         queue.add(new Node(newState, zeroIndex + n, node));         // Add a new node to the queue.
      }
   }
   
    /**
     * Solve the puzzle using Breadth-First Search.
     *
     * @return The solved puzzle node.
     */
   public Node solvePuzzle() {
      // Throw out invalid puzzles, based on whether or not they are a square.
      int n;
      if (Math.sqrt(root.state.length) % 1 == 0) {
         n = (int) Math.sqrt(root.state.length);
      } else
         return null;
      
      // Initialization
      LinkedList<Node> queue = new LinkedList<>();
      queue.add(root);
      

      while (!queue.isEmpty()) {
         Node node = queue.poll();
         visited.add(node.state);
         
         // Have we solved it yet?
         if (checkSolved(node.state)) {
            return node;
         }

         int zeroIndex = node.zeroIndex;
         
         // Attempt to move the pieces around.
         // Move Up
         if (zeroIndex - n >= 0) {
            processIndex(queue, -n, node, zeroIndex);
         }

         // Move Down
         if (zeroIndex + n < node.state.length) {
            processIndex(queue, n, node, zeroIndex);
         }

         // Move Left
         if (zeroIndex % n != 0) {
            processIndex(queue, -1, node, zeroIndex);
         }

         // Move Right
         if ((zeroIndex + 1) % n != 0) {
            processIndex(queue, 1, node, zeroIndex);
         }
      }

      return null; // This puzzle was left unsolved.
    }

    /**
     * Print the puzzle state in a table format.
     *
     * @param state The puzzle state.
     */
    public void printState(int[] state) {
        int n = (int) Math.sqrt(state.length);
        for (int i = 0; i < state.length; i++) {
            System.out.print(state[i] + "\t");
            if ((i + 1) % n == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }

    /**
     * Print the solution path from the initial state to the solved state.
     *
     * @param node The solved puzzle node.
     */
    public void printSolution(Node node) {
        LinkedList<Node> path = new LinkedList<>();
        while (node != null) {
            path.addFirst(node);
            node = node.parent;
        }

        for (Node step : path) {
            printState(step.state);
        }
    }

    //main method for testing
    public static void main(String[] args) {
        int[] puzzle1 = {1, 2, 3, 0, 4, 5, 6, 7, 8};
        int[] puzzle2 = {2, 4, 3, 0, 1, 5, 6, 7, 8};
        int[] puzzle3 = {5, 1, 2, 3, 0, 4, 6, 7, 8};
        int[] puzzle4 = {5, 1, 2, 3, 7, 4, 6, 0, 8};
        int[] puzzle5 = {2, 1, 4, 0, 3, 5, 6, 7, 8};
        int[] puzzle6 = {3, 2, 1, 0};
        // int[] puzzle7 = {2, 11, 18, 24, 4, 34, 12, 1, 35, 14, 26, 15, 29, 33, 30, 10, 19, 13, 22, 5, 21, 20, 17, 0, 25, 7, 31, 28, 8, 16, 3, 27, 23, 6, 32, 9}; // COMICALLY COMPLEX STRESS TEST

        NXNPuzzleSolver solver1 = new NXNPuzzleSolver(puzzle1);
        NXNPuzzleSolver solver2 = new NXNPuzzleSolver(puzzle2);
        NXNPuzzleSolver solver3 = new NXNPuzzleSolver(puzzle3);
        NXNPuzzleSolver solver4 = new NXNPuzzleSolver(puzzle4);
        NXNPuzzleSolver solver5 = new NXNPuzzleSolver(puzzle5);
        NXNPuzzleSolver solver6 = new NXNPuzzleSolver(puzzle6);
        // NXNPuzzleSolver solver7 = new NXNPuzzleSolver(puzzle7);

        System.out.println("Solution for Puzzle 1:");
        solver1.printSolution(solver1.solvePuzzle());

        System.out.println("Solution for Puzzle 2:");
        solver2.printSolution(solver2.solvePuzzle());

        System.out.println("Solution for Puzzle 3:");
        solver3.printSolution(solver3.solvePuzzle());

        System.out.println("Solution for Puzzle 4:");
        solver4.printSolution(solver4.solvePuzzle());

        System.out.println("Solution for Puzzle 5:");
        solver5.printSolution(solver5.solvePuzzle());

        System.out.println("Solution for Puzzle 6:");
        solver6.printSolution(solver6.solvePuzzle());
        
        /*
        // Stress test rig. This takes a very, very long time to execute. To be fair, it is a 6x6 puzzle.
        // Last recorded time: UNKNOWN.
        long sysStart = System.nanoTime();
        System.out.println("Solution for Puzzle 7:");
        solver6.printSolution(solver7.solvePuzzle());
        System.out.printf("%f.3f minutes.\n", (double) (System.nanoTime() - sysStart) / 6e+10);
        */
    }
}