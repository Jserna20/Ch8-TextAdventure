/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling and David J. Barnes
 * @version 2011.08.10
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room snowyForest, snowyMountainPeak, freezerRoom, mainRoom, frozenLakeside;
        Room j1, j2, j3, j4, d1, d2, d3, d4, d5, d6;
      
        // create the rooms
        snowyForest = new Room("In snowy forest");
        snowyMountainPeak = new Room("On snowy mountain peak");
        freezerRoom = new Room("In a freezer");
        mainRoom = new Room("The main room where I woke up in");
        frozenLakeside = new Room("By a frozen lake");
        j1 = new Room("jungle 1");
        j2 = new Room("jungle 2");
        j3 = new Room("jungle 3");
        j4 = new Room("jungle 4");
        d1 = new Room("desert 1");
        d2 = new Room("desert 2");
        d3 = new Room("desert 3");
        d4 = new Room("desert 4");
        d5 = new Room("desert 5");
        d6 = new Room("desert 6");
        
        // initialise room exits
        snowyForest.setExit("east", snowyMountainPeak);
        snowyForest.setExit("south", mainRoom);
        snowyForest.setExit("north", freezerRoom);

        snowyMountainPeak.setExit("west", snowyForest);
        snowyMountainPeak.setExit("north", frozenLakeside);
        
        freezerRoom.setExit("east", frozenLakeside);
        freezerRoom.setExit("west", j1);
        
        frozenLakeside.setExit("west", freezerRoom);
        frozenLakeside.setExit("south", snowyMountainPeak);

        mainRoom.setExit("east", snowyForest);
        mainRoom.setExit("west", j4);
        mainRoom.setExit("south", d2);

        j4.setExit("south", mainRoom);
        j4.setExit("east", j3);
        j4.setExit("north", j1);
        
        j3.setExit("east", j4);
        j3.setExit("north", j2);
        
        j2.setExit("east", j1);
        j2.setExit("south", j3);
        
        j1.setExit("west", j2);
        j1.setExit("east", freezerRoom);
        
        d1.setExit("west", j3);
        d1.setExit("east", d2);
        d1.setExit("south", d4);
        
        d2.setExit("north", mainRoom);
        d2.setExit("west", d1);
        d2.setExit("east", d3);
        d2.setExit("south", d5);
        
        d3.setExit("west", d2);
        d3.setExit("east", snowyMountainPeak);
        d3.setExit("south", d6);
        
        d4.setExit("north", d1);
        d4.setExit("east", d5);
        
        d5.setExit("west", d4);
        d5.setExit("north", d2);
        d5.setExit("east", d6);
        
        d6.setExit("west", d5);
        d6.setExit("north", d3);

        currentRoom = mainRoom;  // start game snowyForest
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("You are trapped in a place known as the Nightmare Labyrinth!");
        System.out.println("You must defeat 6 enemies in order to escape.");
        System.out.println("You can find treasure chests that may help you.");
        System.out.println("Use the teleporters in order to go from room to room.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        CommandWord commandWord = command.getCommandWord();

        switch (commandWord) {
            case UNKNOWN:
                System.out.println("I don't know what you mean...");
                break;

            case HELP:
                printHelp();
                break;

            case GO:
                goRoom(command);
                break;

            case QUIT:
                wantToQuit = quit(command);
                break;
        }
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You still need to defeat X number of enemies");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to go in one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no teleporter!");
        }
        else {
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
    }

    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            System.out.println("Are you sure you want to quit? \nYou will have to start over.");
            
            return true;  // signal that we want to quit
        }
    }
}
