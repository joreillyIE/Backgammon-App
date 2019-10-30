import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Positions {
	
	public int posX;
	public int posY;
	public enum type {
	    DEFAULT,
	    BAR,
	    PIP
	  }
	public type posType;
	public CheckerComponent checker;
	public TextComponent text;
	public boolean taken;
	
	public Positions() { 
		
	}
	
	public Positions(int x, int y) {
		posX = x;
		posY = y;
		taken = false;
		checker = null;
		text = null;
		posType = type.DEFAULT;
		
	}
	
	public CheckerComponent addChecker(Player player) {
		if(posType == type.PIP) {
			return null;
		}
		else {
			checker = new CheckerComponent(posX, posY, player);
			setTaken(true);
		}
		return checker;
	}
	
	public CheckerComponent removeChecker() {
		CheckerComponent c = checker;
		checker = null;
		setTaken(false); 
		return c;
	}
	
	public TextComponent addText(int i) {
		if(posType == type.DEFAULT)
		{
			return null;
		}
		else if(posType == type.BAR)
		{
			text = new TextComponent(posX+12, posY+22, Integer.toString(i), Color.black);
		}
		else
		{
			text = new TextComponent(posX, posY, Integer.toString(i), Color.white);
		}
		
		return text;
	}
	
	public TextComponent addText(String s) {
		if(posType == type.DEFAULT)
		{
			return null;
		}
		else
		{
			text = new TextComponent(posX, posY, s, Color.black);
		}
		return text;
	}
	
	public TextComponent removeText() {
		TextComponent t = text;
		text = null;
		return t;
	}

	public Player getPlayer() {
		if(posType == type.PIP)
		{
			return null;
		}
		else if(checker == null)
		{
			return null;
		}
		else
		{
			return checker.player;
		}
		
	}
	
	public boolean getTaken() {
		return taken; 
	}
	
	public void setTaken(boolean b) {
		taken = b; 
	}
	
	public TextComponent setPip()
	{
		posType = type.PIP;
		setTaken(true);
		return text;
	}
	
	public void setBar()
	{
		posType = type.BAR;
	}
	
}
