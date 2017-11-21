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
    private int airGum;   //Movement Limit
    private int emergencyAir; //Extra movement
    private int children;
        
    /**
     * Create the game and initialise its internal map.
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        airGum = 1;
        emergencyAir = 2;
        children = 3;
    }
   
    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room silverDeposit1, silverDeposit2, silverDeposit3, MineShaftRoom, silverDeposit4;
        Room junctionSector1, junctionSector2, junctionSector3, junctionSector4;
        Room deepMines1, deepMines2, deepMines3, deepMines4, deepMines5, deepMines6;
      
        // create the rooms
        MineShaftRoom = new Room("in the main room where you woke up in");
        silverDeposit1 = new Room("in snowy forest");
        silverDeposit2 = new Room("on snowy mountain peak");
        silverDeposit3 = new Room("in a freezer");
        silverDeposit4 = new Room("by a frozen lake");
        junctionSector1 = new Room("jungle 1");
        junctionSector2 = new Room("jungle 2");
        junctionSector3 = new Room("jungle 3");
        junctionSector4 = new Room("jungle 4");
        deepMines1 = new Room("desert 1");
        deepMines2 = new Room("desert 2");
        deepMines3 = new Room("desert 3");
        deepMines4 = new Room("desert 4");
        deepMines5 = new Room("desert 5");
        deepMines6 = new Room("desert 6");
        
        // initialise room exits
        silverDeposit1.setExit("east", silverDeposit2);
        silverDeposit1.setExit("south", MineShaftRoom);
        silverDeposit1.setExit("north", silverDeposit3);

        silverDeposit2.setExit("west", silverDeposit1);
        silverDeposit2.setExit("north", silverDeposit4);
        
        silverDeposit3.setExit("east", silverDeposit4);
        silverDeposit3.setExit("west", junctionSector1);
        
        silverDeposit4.setExit("west", silverDeposit3);
        silverDeposit4.setExit("south", silverDeposit2);

        MineShaftRoom.setExit("east", silverDeposit1);
        MineShaftRoom.setExit("west", junctionSector4);
        MineShaftRoom.setExit("south", deepMines2);

        junctionSector4.setExit("south", MineShaftRoom);
        junctionSector4.setExit("east", junctionSector3);
        junctionSector4.setExit("north", junctionSector1);
        
        junctionSector3.setExit("east", junctionSector4);
        junctionSector3.setExit("north", junctionSector2);
        
        junctionSector2.setExit("east", junctionSector1);
        junctionSector2.setExit("south", junctionSector3);
        
        junctionSector1.setExit("west", junctionSector2);
        junctionSector1.setExit("east", silverDeposit3);
        
        deepMines1.setExit("west", junctionSector3);
        deepMines1.setExit("east", deepMines2);
        deepMines1.setExit("south", deepMines4);
        
        deepMines2.setExit("north", MineShaftRoom);
        deepMines2.setExit("west", deepMines1);
        deepMines2.setExit("east", deepMines3);
        deepMines2.setExit("south", deepMines5);
        
        deepMines3.setExit("west", deepMines2);
        deepMines3.setExit("east", silverDeposit2);
        deepMines3.setExit("south", deepMines6);
        
        deepMines4.setExit("north", deepMines1);
        deepMines4.setExit("east", deepMines5);
        
        deepMines5.setExit("west", deepMines4);
        deepMines5.setExit("north", deepMines2);
        deepMines5.setExit("east", deepMines6);
        
        deepMines6.setExit("west", deepMines5);
        deepMines6.setExit("north", deepMines3);

        currentRoom = MineShaftRoom;  // start game silverDeposit1
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean lost = false;        
        boolean finished = false;
        boolean won = false;
        while (!finished && !lost && !won) {
            Command command = parser.getCommand();
            finished = processCommand(command);
            lost = isGameOver();
        }
        if(isGameOver())
        {
            System.out.println("You died. Well....");
        }
        if(isGameWon())
        {
            System.out.println("Congrats everyone escaped!")
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("You are searching for children trapped inside this mine");
        System.out.println("You must find all the " + children + " children and escape.");
        System.out.println("You can find treasure chests that may help you.");
        System.out.println("Use the pathways in order to go from room to room.");
        System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }
    
    private boolean isGameOver()
    {
        if(emergencyAir == 0)
            return true;
        else
            return false;
    }
    
    private boolean isGameWon()
    {
        if(children == 0)
            return true;
        else
            return false;
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
                
            case LOOK:
                startLook(command);
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
        System.out.println("You still need to find " + children + " more\n");
        System.out.println("Your command words are:");
        parser.showCommands();
        System.out.println("go is use to move from room to room. EX: go east.");
        System.out.println("Each time you move to a room you have to use air gum so hurry");
        System.out.println("look is used to get a description of the room.");
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
            if(airGum > 0)
            {
                airGum--;
                System.out.println("Air gum left " + airGum);
            }
            if(airGum == 0)
            {
                emergencyAir--;
                System.out.println("No more air gum left\n" + emergencyAir + " minute(s) left until suffocation!");
            }
        }
    }
    
    private void startLook(Command command)
    {
        System.out.println(currentRoom.getLongDescription());
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
