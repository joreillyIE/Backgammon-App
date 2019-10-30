/*
*	Authors: 
*	Joanne Reilly 	@joreillyIE
*	Dakota Owen		@DJOwenNetwork
*/
public class BackgammonViewer
{
	public static void main(String[] args)
	{  
		BackgammonFrame frame = new BackgammonFrame();//Creates backgammon frame
		frame.getPlayerInfo();
		frame.setUpBoard();
		
		//frame.moveCheckersTest(); 
		
		frame.setPlayerOrder();
		
		while(true) {
			frame.rollDie(); 
			frame.processTurn();
			//end when game ends
		}
		
	}
}