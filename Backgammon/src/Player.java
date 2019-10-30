import java.awt.Color;
import java.lang.reflect.Field;

public class Player {
	
	//ask players for their name and colour
	//result from first roll for turn order? 
	//roll and move around the board. rolls are automatic. 
	//enter moves as 'starting pip', 'ending pip'. Return an error for invalid moves. 
	//if there's an error, allow them to enter a new move. 
	//rolls don't have to avoid opponents blocks yet, or check that the move is valid.
	//'next' ends turn.  
	//pip numbers should be correct for the current player. 
	//quit terminates the program. 
	
	private String name;
	private String colourString; 
	private Color colour;
	private boolean turn;//true for player1, false for 2
	
	public void setName(String string) {
		name = string; 
	}
	
	public String getName() {
		return name; 
	}
	
	public void setColour(String string) {
		
		colourString = string; 
		try {
		    Field field = Class.forName("java.awt.Color").getField(string);
		    colour = (Color)field.get(null);
		} catch (Exception e) {
		    colour = Color.black;
		}
		
	}
	
	public Color getColour() {
		return colour;
	}
	
	public String getColourString() {
		return colourString; 
	}
	
	public void setTurn(boolean t) {
		turn = t;
	}
	
	public boolean getTurn() {
		return turn;
	}

}
