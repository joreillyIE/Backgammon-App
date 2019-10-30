import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.OverlayLayout;
import javax.swing.Timer;

public class BackgammonFrame extends JFrame
{
	private static final int FRAME_WIDTH = 1110;
	private static final int FRAME_HEIGHT = 548;
	private String input;
	private int userCommand;
	private int points;//points to play up to
	
	private boolean order; //True for player1's turn, False for player2's turn. 
	
	private BoardPanel board = new BoardPanel(); 
	private InfoPanel info = new InfoPanel(); 
	private CommandPanel command = new CommandPanel(); 
	private Dice dice = new Dice(); 
	private Player player1 = new Player(); 
	private Player player2 = new Player(); 
	
	private int[] possibleMoves1 = new int[50]; 
	private int[] possibleMoves2 = new int[50]; 
	int mcount = 0, matchCount = 0, scoreCounter[] = {0,0}, highPoint;
	int doublingValue = 1;//value of dice
	boolean doublingPlayer;//true for player1, false for player 2
	
	private boolean die1 = true, die2 = true; //true = still usable  
	private boolean doubleToggle = true; 
	int time;
	
	Timer timer = new Timer(30000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            board.updateTime();
        	refresh();
        }
     });
	
	   
	public BackgammonFrame()
	{
		setSize(FRAME_WIDTH, FRAME_HEIGHT);//size of frame
		setTitle("Backgammon - The Greatest Game of All Time");//Title
		
		board.setBackground(Color.CYAN); //Test background color for mapping purposes
		
		LayoutManager overlay = new OverlayLayout(board); 
		board.setLayout(overlay);
		
		add(board, BorderLayout.LINE_START);
		add(info, BorderLayout.LINE_END); 
		add(command, BorderLayout.PAGE_END);  
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false); 
		setVisible(true);
		
	}
	
	public void moveCheckersTest() {
		
		board.moveFromTo(13, 0, order); 
		refresh();
		board.moveFromTo(19, 0, order);
		refresh();
		
		int prev1 = 0;
		int prev2 = 0;
		
		
		for(int i = 1; i < 25; i++)
		{
			boolean x = board.moveFromTo(prev1, i, order);
			refresh();
			boolean y = board.moveFromTo(prev2, 25-i, order);
			refresh();
			
			if(x == true)
			{
				prev1 = i;
			}
			if(y == true)
			{
				prev2 = 25-i;
			}
		}
		
		board.moveFromTo(prev1, 13, order);
		refresh();
		board.moveFromTo(prev2, 19, order);
		refresh();
		
	}
	
	public void refresh() {
		board.revalidate();
		board.repaint();
	}

	//Check to see if a number is odd or not, false = even, true = odd
	public static boolean isOdd(int i) {
		while (i < 26)
			if (i % 2 == 1) {
				return true;
			}
			return false;
	}

	public void getPoints(){
		info.displayString("How many points would you like to play up to?");
		info.displayString("You can play to an odd number of points (1,3,5,7, up to 25!)");
		// Receive input from player
		command.inputString();
		input = command.getString().trim();
		// Exception
		try {
			points = Integer.parseInt(input);
			//making sure that the points entered is an odd number less than 26 (less than or equal to 25)
			while (!isOdd(points)) {
				info.clearTextArea();
				info.displayMessage(0);
				getPoints();

			}
				info.clearTextArea();
				info.displayString("Game total points: " + points + " point(s)");

			} catch(NumberFormatException e){
				//Throw exception
				info.displayMessage(0);
				getPoints();
				return;
			}


	}

	public void getPlayerInfo() {
		//announce the game, placed here since this method is always called first
		info.displayString("Welcome to Backgammon!");
		
		getPoints();
		
		//Set Player 1 name
		info.displayString("Enter Player 1's name:"); 
		command.inputString(); 
		input = command.getString().trim(); 
		if (input.equalsIgnoreCase("quit")) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else {
			player1.setName(input); 
			info.displayString("> "+input); 
		}
		
		//Set Player 1 colour
		info.displayString("Enter Player 1's colour:"); 
		command.inputString(); 
		input = command.getString().trim(); 
		if (input.equalsIgnoreCase("quit")) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else {
			//Test colour input
			
			player1.setColour(input.toLowerCase()); 
			info.displayString("> "+input); 
		}
		player1.setTurn(true);


		//Set Player 2 name 
		info.displayString("Enter Player 2's name:");
		command.inputString(); 
		input = command.getString().trim(); 
		if (input.equalsIgnoreCase("quit")) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else {
			player2.setName(input);
			info.displayString("> "+input);
		}

		nameSame();
		//Set Player 2 colour 
		info.displayString("Enter Player 2's colour:"); 
		command.inputString(); 
		input = command.getString().trim();
		if (input.equalsIgnoreCase("quit")) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		else {
			//Test colour input
			
			player2.setColour(input.toLowerCase()); 
			info.displayString("> "+input); 
		}
		colorSame();
		player2.setTurn(false);
	}
	
	public void setPlayerOrder() {
		int roll1, roll2; 
		dice.roll(); 
		roll1 = dice.getSum(); 
		dice.roll(); 
		roll2 = dice.getSum(); 
		if(roll1 == roll2) {
			setPlayerOrder(); 
		}
		else if (roll2 > roll1) {
			order = player2.getTurn(); 
			info.displayString(player1.getName() + " rolled: " + roll1); 
			info.displayString(player2.getName() + " rolled: " + roll2); 
			info.displayString(player2.getName() + " will go first.");
		}
		else {
			order = player1.getTurn(); 
			info.displayString(player1.getName() + " rolled: " + roll1); 
			info.displayString(player2.getName() + " rolled: " + roll2); 
			info.displayString(player1.getName() + " will go first.");
		}
	}
	
	public void setUpBoard() {
		board.addPositions();
		board.setBoard(player1, player2);
		board.addStartingCheckers(player1, player2);
		timer.start();
		refresh();
	}
	
	public void rollDie() {
		dice.roll(); 
		
		if(order) {
			info.displayString(player1.getName()+" ("+player1.getColourString()+") it is your turn."); 
		} else {
			info.displayString(player2.getName()+" ("+player2.getColourString()+") it is your turn."); 
		}
		
		info.displayString("Your rolls are: "+ dice.getDice1()+" & " +dice.getDice2());
	}
	
	public void processTurn() {
		
		legalChecks();
		
		if(bearingChecks()) { 
			if(!order) {
				for(int i = 6; i >= 1; i--) { //6-1 is player 2!!!
					if(dice.isDouble()) {
						//if(i-dice.getDice1() == 0 || (i <= highPoint && i-dice.getDice1() <=0)) {
						if(i-dice.getDice1() == 0 && board.boardPositions[i][1].getTaken()) {
							possibleMoves1[mcount] = i; 
							possibleMoves2[mcount] = -1; 
							mcount++; 
						}
					} else {
						//if(i-dice.getDice1() == 0 || (i <= highPoint && i-dice.getDice1() <=0)) {
						if(i-dice.getDice1() == 0 && die1 && board.boardPositions[i][1].getTaken()) {
							possibleMoves1[mcount] = i; 
							possibleMoves2[mcount] = -1; 
							mcount++; 
						}
						//if(i-dice.getDice1() == 0 || (i <= highPoint && i-dice.getDice1() <=0)) {
						if(i-dice.getDice2() == 0 && die2 && board.boardPositions[i][1].getTaken()) {
							possibleMoves1[mcount] = i; 
							possibleMoves2[mcount] = -1; 
							mcount++; 
						} 
					}
				}
			} else { 
				for(int i = 19; i <=24; i++) { //for player 1
					if(dice.isDouble()) {
						//if(i-dice.getDice1() == 0 || (i <= highPoint && i-dice.getDice1() <=0)) {
						if(i+dice.getDice1() == 25 && board.boardPositions[i][1].getTaken()) {
							possibleMoves1[mcount] = i; 
							possibleMoves2[mcount] = -1; 
							mcount++; 
						}
					} else {
						//if(i-dice.getDice1() == 0 || (i <= highPoint && i-dice.getDice1() <=0)) {
						if(i+dice.getDice1() == 25 && die1 && board.boardPositions[i][1].getTaken()) { 
							possibleMoves1[mcount] = i; 
							possibleMoves2[mcount] = -1; 
							mcount++; 
						}
						//if(i-dice.getDice1() == 0 || (i <= highPoint && i-dice.getDice1() <=0)) {
						if(i+dice.getDice2() == 25 && die2 && board.boardPositions[i][1].getTaken()) {
							possibleMoves1[mcount] = i; 
							possibleMoves2[mcount] = -1; 
							mcount++; 
						}
					}
				}
			}
		}
		

		//if there are no legal moves inform player and auto continue on next roll
		if(mcount==0) {
			info.clearTextArea(); 
			info.displayString("No possible moves are available."); 
			info.displayString("Your turn is over."); 
			order = !order; //set the turn to the next player 
			die1 = true; 
			die2 = true; 
			clearMovesArray();
		} else {
		
			//print options for player to choose from 
			for(int i = 0; i < mcount; i++) {
				if(possibleMoves1[i]==0) { //if moving from the bar
					info.displayString((i+1)+": Move from the Bar to Pip "+possibleMoves2[i]); 
				} 
				else if(possibleMoves1[i]==0 && hitCheck(possibleMoves2[i])) {//if moving from the bar would be a hit
					info.displayString((i+1)+": Move from the Bar to Pip "+possibleMoves2[i]+ "*"); 
				} 
				else if(possibleMoves2[i]==-1) {
					info.displayString((i+1)+": Bear off the board from Pip "+possibleMoves1[i]);
				}
				else if(hitCheck(possibleMoves2[i])) { //if the move is a hit
					info.displayString((i+1)+": Move Pip "+possibleMoves1[i]+" to Pip "+possibleMoves2[i]+ "*"); 
				} else { //if it's just a normal move
					info.displayString((i+1)+": Move Pip "+possibleMoves1[i]+" to Pip "+possibleMoves2[i]); 
				}
			}
		
			//receive input from player for corresponding number (change to letter later) for move they want
			command.inputString(); 
			input = command.getString().trim(); 
			
			//quit command 
			if (input.equalsIgnoreCase("quit")) {
				dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
			
			//cheat command
			if (input.equalsIgnoreCase("cheat")) {
				board.cheat(player1, player2);
				refresh();
				clearMovesArray();
				info.displayMessage(1);
				processTurn();
				return;
			}
			
			if(input.equalsIgnoreCase("double")) {
				if(!doubling())
				{
					info.displayMessage(2);
				}
				clearMovesArray();
				processTurn();
				return;
			}
			
			try {
				userCommand = Integer.parseInt(input); 
			} catch (NumberFormatException e) {
				//Throw exception
				info.displayMessage(0); 
				clearMovesArray();
				//reenter move message 
				processTurn(); 
				return; 
			}
			
			
			//display input after verifying it to be correct
			info.displayString("> "+input); 
			
			//check that its a valid input (in the range of options shown) 
			if(userCommand > mcount+1) {
				info.displayMessage(0); 
				clearMovesArray();
				processTurn(); 
			}
			
				if(possibleMoves2[userCommand-1]==-1) { //bear off move command
					board.moveFromTo(possibleMoves1[userCommand-1], -1, order); 
				}
				else if(hitCheck(possibleMoves2[userCommand-1])) { //hit move commands 
					//move command to bump checker to bar
					board.moveFromTo(possibleMoves2[userCommand-1], 0, order); 
					//move command from user
					board.moveFromTo(possibleMoves1[userCommand-1], possibleMoves2[userCommand-1], order); 
				} 
				else { //default move command
					board.moveFromTo(possibleMoves1[userCommand-1], possibleMoves2[userCommand-1], order); 
				}
			
			refresh(); 
			
			if(possibleMoves2[userCommand-1] == -1) { //check which dice is used to bear off 
				if(order) {
					if(25-possibleMoves1[userCommand-1] == dice.getDice1()) {
						die1 = false; 
					}
					if(25-possibleMoves1[userCommand-1] == dice.getDice2()) {
						die2 = false; 
					}
				}
				else { //check which dice is used for other moves 
					if(possibleMoves1[userCommand-1]-dice.getDice1() == 0) {
						die1 = false; 
					}
					if(possibleMoves1[userCommand-1]-dice.getDice2() == 0) {
						die2 = false; 
					}
				}
			} else if(Math.abs(possibleMoves1[userCommand-1] - possibleMoves2[userCommand-1]) == dice.getDice1()) {
				die1 = false; 
			} else if (Math.abs(possibleMoves1[userCommand-1] - possibleMoves2[userCommand-1]) == dice.getDice2()) {
				die2 = false; 
			}
			
			endChecks(); 
		}
	}

	public void legalChecks() {
		//check for all legal moves using moveChecks in a for loop 
		
		//check in the order according to...well order. 
		if(order) { //if player 1's turn 1-24 
			
			//if doubles only bother checking for one die 
			if(dice.isDouble()) {
				for (int i = 0; i < 25; i++) {
					if(moveChecks(i,i+dice.getDice1())) {
						possibleMoves1[mcount] = i; 
						possibleMoves2[mcount] = i+dice.getDice1(); 
						mcount++; 
					}
				}
			} else {
				for (int i = 0; i < 25; i++) {
					if(moveChecks(i,i+dice.getDice1())) {
						possibleMoves1[mcount] = i; 
						possibleMoves2[mcount] = i+dice.getDice1(); 
						mcount++; 
					}
					
					if(moveChecks(i,i+dice.getDice2())) {
						possibleMoves1[mcount] = i; 
						possibleMoves2[mcount] = i+dice.getDice2(); 
						mcount++; 
					}
				}
			}
		} else { //if player 2's turn 24-1 
			
			//if doubles only bother checking for one die 
			if(dice.isDouble()) {
				//check bar (have to do this by itself since player 2 goes backwards)
				if(moveChecks(0, 25-dice.getDice1())) {
					possibleMoves1[mcount] = 0; 
					possibleMoves2[mcount] = 25-dice.getDice1(); 
					mcount++; 
				}
				for (int i = 24; i >= 1; i--) {
					if(moveChecks(i,i-dice.getDice1())) {
						possibleMoves1[mcount] = i; 
						possibleMoves2[mcount] = i-dice.getDice1(); 
						mcount++; 
					}
				}
			} else {
				//check bar (have to do this by itself since player 2 goes backwards)
				if(moveChecks(0, 25-dice.getDice1())) {
					possibleMoves1[mcount] = 0; 
					possibleMoves2[mcount] = 25-dice.getDice1(); 
					mcount++; 
				} 
				if(moveChecks(0, 25-dice.getDice2())) {
					possibleMoves1[mcount] = 0; 
					possibleMoves2[mcount] = 25-dice.getDice2(); 
					mcount++; 
				}
				for (int i = 24; i >= 1; i--) {
					if(moveChecks(i,i-dice.getDice1())) {
						possibleMoves1[mcount] = i; 
						possibleMoves2[mcount] = i-dice.getDice1(); 
						mcount++; 
					}
					
					if(moveChecks(i,i-dice.getDice2())) {
						possibleMoves1[mcount] = i; 
						possibleMoves2[mcount] = i-dice.getDice2(); 
						mcount++; 
					}
				}
			} 
			
		}
	} 

	public void endChecks() {
		if(matchWinCheck()) { //if the game is over 
			if(scoreCounter[0] >= points || scoreCounter[1] >= points) { //if the entire game is over
				info.clearTextArea();
				timer.stop();
				info.displayString("Game Over!"); 
				info.displayString("Score: " + player1.getName() + ": " + scoreCounter[0]+ "    " + player2.getName() + ": " + scoreCounter[1]); 
				die1 = true; 
				die2 = true; 
				clearMovesArray();
				
				if(scoreCounter[0] > scoreCounter[1]) {
					info.displayString(player1.getName() + " is victorious!"); 
				} else {
					info.displayString(player2.getName() + " is victorious!"); 
				}
				
				newMatch(); 
				//will only get here if string from newMatch is a "yes" 
				
				matchCount=0;
				getPlayerInfo(); 
			} else {
				matchCount++; 
			}
			//no else statement here because if it gets here without the if statement or from newMatch(), than we are going to continue either way
			board.resetBoard();  
			board.addStartingCheckers(player1, player2); 
			refresh(); 
			
			//enter any button to continue, we don't actually have to do anything with the key entered. 
			command.inputString(); 
			
			info.clearTextArea(); 
			info.displayString("Match Over!"); 
			die1 = true; 
			die2 = true; 
			clearMovesArray();
			setPlayerOrder(); 
		}
		
		//repeat if there are still unused dice (be careful with doubles) 
		else if (die1 || die2 && !dice.isDouble()) {
			info.displayString("Go ahead and move again.");
			clearMovesArray();
			processTurn(); 
		}
		//doubles repeat 
		else if(dice.isDouble() && doubleToggle) {
			doubleToggle = false; 
			
			info.displayString("Go ahead and move again.");
			clearMovesArray();
			processTurn(); 
			
			info.displayString("Go ahead and move again.");
			clearMovesArray();
			processTurn(); 
			
			info.displayString("Go ahead and move again.");
			clearMovesArray();
			processTurn(); 
			doubleToggle = true; 
			
			info.clearTextArea(); 
			info.displayString("Your turn is over."); 
			order = !order; //set the turn to the next player 
			die1 = true; 
			die2 = true; 
			clearMovesArray();
		}
		//end turn
		else if (!die1 && !die2 && !dice.isDouble()) {
			info.clearTextArea(); 
			info.displayString("Your turn is over."); 
			order = !order; //set the turn to the next player 
			die1 = true; 
			die2 = true; 
			clearMovesArray(); 
		}
	}
	
	public void newMatch() {
		info.displayString("Would you like to play another match? (yes/no)"); 
		command.inputString(); 
		input = command.getString().trim(); 
		
		if(input.equalsIgnoreCase("no")){
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		} else if(!input.equalsIgnoreCase("yes")){
			info.displayString("Incorrect input."); 
			newMatch(); 
		}
	}
	
	public boolean moveChecks(int pip1, int pip2) {
		
		boolean check = true;
		
		//All of the below checks are else if's since only one needs to fail anyways
		
		//check pips entered bounds here.  Can't move beyond pip 24, but want to be able
		//to move off the bar, without accidentally moving onto it (when its not a hit) 
		if(pip1 > 24 || pip2 > 24 || pip1 < 0 || pip2 <= 0) {
			check = false; 
		}
		
		else if(pip1 != 0) {
			//check if given pip1 is empty
			if(!board.boardPositions[pip1][1].getTaken()) { 
				check = false;
			}
			
			//check if the checkers on pip1 is for this player
			else if(order && board.boardPositions[pip1][1].getPlayer()==player2) {
				check = false; 
			}
			else if(!order && board.boardPositions[pip1][1].getPlayer()==player1) {
				check = false; 
			}
			
			//check if the given pip2 is full
			else if(board.boardPositions[pip2][5].getTaken()) {
				check = false; 
			}
			
			//if pip2 has at least 2 checkers (so it won't be a hit) on pip2
			else if(board.findFree(pip2) >= 3) { //if the third point or higher is open
				//check if the checkers on pip2 is for this player
				if(order && board.boardPositions[pip2][1].getPlayer()==player2) {
					check = false; 
				}
				else if(!order && board.boardPositions[pip2][1].getPlayer()==player1) {
					check = false; 
				}
			}
		} else {
			if(order && !board.boardPositions[0][1].getTaken()) {
				check = false; 
			} else if(!order && !board.boardPositions[0][2].getTaken()) {
				check = false; 
			} else if(board.boardPositions[pip2][5].getTaken()) { //check if pip2 is full
				check = false; 
			} else if(board.findFree(pip2) >= 3) { //if the third point or higher is open (only 2 checkers actually there) 
				//check if the checkers on pip2 is for this player
				if(order && board.boardPositions[pip2][1].getPlayer()==player2) {
					check = false; 
				}
				else if(!order && board.boardPositions[pip2][1].getPlayer()==player1) {
					check = false; 
				}
			}
		}
		
		if(!dice.isDouble()) {
			//check if die1 has already been used 
			if(!die1 && Math.abs(pip1 - pip2) == dice.getDice1()) {
				check = false; 
			}
			
			//check if die2 has already been used 
			else if(!die2 && Math.abs(pip1 - pip2) == dice.getDice2()) {
				check = false; 
			}
		} 
		
		return check; 
		
	}

	public boolean bearingChecks() {
		
		boolean check = true; 
		
		//check for bearing off. 
		if(!order) {
			for(int i = 24; i >= 7; i--) { //player 2 home is 1-6
				if(board.boardPositions[i][1].getTaken()) {
					if(board.boardPositions[i][1].getPlayer()==player2) {
						check = false; 
					}
				}
			}
			//if there are any checkers for this player on the bar
			if(board.boardPositions[0][1].getTaken()) { //because if there's any checkers on this exact position they can only belong to player1
				check = false; 
			}
		} else {
			for(int i = 1; i <= 18; i++) { //player 1 home is 19-24
				if(board.boardPositions[i][1].getTaken()) {
					if(board.boardPositions[i][1].getPlayer()==player1) {
						check = false; 
					}
				}
			}
			//if there are any checkers for this player on the bar
			if(board.boardPositions[0][2].getTaken()) { //because if there's any checkers on this exact position they can only belong to player2 
				check = false; 
			}
		}
		
		return check; 
	}
	
	public boolean hitCheck(int a) {
		//if there is only one checker on pipA
		if(board.findFree(a)==2) { 
			//if the only checker on pipA belongs to the current player (to prevent you from hitting yourself) 
			if((board.boardPositions[a][1].getPlayer()==player1 && order) || (board.boardPositions[a][1].getPlayer()==player2 && !order)) {
				return false; 
			} else {
				return true; 
			}
		} else {
			return false; 
		}
	}
	
	public boolean matchWinCheck() {
		if(board.barCounter[0] > 0) {
			return false; 
		} else if(board.barCounter[1] > 0) {
			return false; 
		}
		for(int i = 1; i < 25; i++) {
			if(board.boardPositions[i][1].getTaken()) {
				if(order && board.boardPositions[i][1].getPlayer()==player1) { 
					//this is done on two separate lines to prevent a null return error
					return false; 
				} else if (!order && board.boardPositions[i][1].getPlayer()==player2) {
					return false; 
				}
			}
		}
		matchCount++; 
		if(order) {
			scoreCounter[0] += doublingValue;
			
		} else {
			scoreCounter[1] += doublingValue;
		}
		board.updateScore(scoreCounter);
		refresh();
		return true; 
	}

	public boolean doubling() {
		//under circumstances where doubling is illegal, return false
		if((doublingValue > 1 && doublingPlayer != order) || doublingValue == 64)
		{
			return false;
		}
		//other player should accept (continue) or decline (quit)
		info.clearTextArea();
		if(order) {
			info.displayString(player1.getName()+" ("+player1.getColourString()+") has proposed to double the stakes."); 
			info.displayString(player2.getName()+" ("+player2.getColourString()+"), would you like to accept? (yes/no)"); 
		} else {
			info.displayString(player2.getName()+" ("+player2.getColourString()+") has proposed to double the stakes."); 
			info.displayString(player1.getName()+" ("+player1.getColourString()+"), would you like to accept? (yes/no)"); 
		}
		
		command.inputString(); 
		input = command.getString().trim(); 
		
		if(input.equalsIgnoreCase("no")){
			//end match and allocate points to winner
			info.displayString("> "+input); 
			board.clearCheckers();
			endChecks();
		} else if(input.equalsIgnoreCase("yes")){
			//update cube and doubling player
			info.displayString("> "+input); 
			doublingPlayer = !order;
			doublingValue = doublingValue*2;
			board.updateDoublingCube(order, doublingValue);
			refresh();
			info.displayString("Stakes have been doubled.");
		}
		else {
			info.displayMessage(0);
		}
		return true;
	}
	
	public void clearMovesArray() {
		for (int i = 0; i < mcount; i++) {
			possibleMoves1[i] = 0; 
			possibleMoves2[i] = 0; 
		}
		mcount = 0; 
	}

	public void nameSame(){
		while (player1.getName().equals(player2.getName()))
		{
			info.displayString("Error... " + "'" + player1.getName() + "'" + " has already been chosen");
			info.displayString("Enter Player 2's name:");
			command.inputString();
			input = command.getString().trim();
			if (input.equalsIgnoreCase("quit")) {
				dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			}
			else {
				player2.setName(input);
				info.displayString("> "+input);
			}
		}
	}
	
	public void colorSame() {
		while (player1.getColour().equals(player2.getColour()))
		{
			info.displayString("Error... this color has already been chosen");
			info.displayString("Enter Player 2's colour:");
			command.inputString();
			input = command.getString().trim();
			if (input.equalsIgnoreCase("quit")) {
				dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			} else {
				player2.setColour(input.toLowerCase());
				info.displayString("> " + input);
			}
		}

	}
}