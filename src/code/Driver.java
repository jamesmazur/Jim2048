package code;

public class Driver {

    public static int GRID_SIZE;
    public static int WIN_THRESHOLD; 
   
    private static void badArgumentsQuit() {
        System.out.println("Improper arguments.  The arguments should match one of the following patterns:");
        System.out.println("[-h]");
        System.out.println("[--help]");
        System.out.println("[-s gridSize] [-t winThreshold]");
        System.out.println("[-t winThreshold] [-s gridSize]");
        System.exit(1);
    }
    
    public static void main(String[] args) {
        GRID_SIZE = 4;
        WIN_THRESHOLD = 2048;
        // Make sure the arguments follow the proper pattern.
        if (args.length == 1) {
            if (args[0].equals("-h") || args[0].equals("--help")) {
                System.out.println("The possible arguments for Jimmy's 2048 Game are:");
                System.out.println("'-h' or '--help' by itself, which displays this message.");
                System.out.println("'-s' followed by a grid size, which must be at least 2.");
                System.out.println("'-t' followed by a win threshold, which must be at least 8.  " +
                        "This is the tile value you must reach to win the game.");
                System.exit(0);
            } else {
                badArgumentsQuit();
            }
        } else if (args.length >= 2) {
            try {
                if (args[0].equals("-s")) {
                    GRID_SIZE = Integer.parseInt(args[1]);
                } else if (args[0].equals("-t")) {
                    WIN_THRESHOLD = Integer.parseInt(args[1]);
                } else {
                    badArgumentsQuit();
                }
                if (args.length == 4 && !args[2].equals(args[0])) {
                    if (args[2].equals("-s")) {
                        GRID_SIZE = Integer.parseInt(args[3]);
                    } else if (args[2].equals("-t")) {
                        WIN_THRESHOLD = Integer.parseInt(args[3]);
                    } else {
                        badArgumentsQuit();
                    }
                } else if (args.length != 2) {
                    badArgumentsQuit();
                }
            } catch (NumberFormatException e) {
                System.out.println("Couldn't parse arguments.  " +
                        "Make sure you enter actual numbers for the grid size and/or win threshold.");
                System.exit(1);
            }
        }
        
        // Error check the actual numbers passed in.
        if (GRID_SIZE < 2) {
            System.out.println("Sorry, your grid size must be at least 2.");
            System.exit(1);
        }
        if (WIN_THRESHOLD < 8) {
            System.out.println("Sorry, your win threshold must be at least 8.");
            System.exit(1);
        }
        
        // Start the game!
        Model model = new Model();
        new StandardView(model);
    }

}
