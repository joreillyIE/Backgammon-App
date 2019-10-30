import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.event.*;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.*;

public class BoardPanel extends JPanel
{
	private static final int BOARD_WIDTH = 720, BOARD_HEIGHT = 491;
	private static final int PIP_WIDTH = 50, CHECKER_HEIGHT = 18;
	private static final int BORDER_TOP = 31, BORDER_BOTTOM = 33, BORDER_LEFT = 23, BORDER_RIGHT = 63;
	
	private long gameStart;
	Positions [][] boardPositions = new Positions[25][6];
	int barCounter[] = {0,0};
	int bearCounter[] = {0, 0}; 
	CubeComponent cube = new CubeComponent(668,245,1);
	TextComponent time = new TextComponent(148,25,"0 hours 0 minutes",Color.black);
	TextComponent scores[] = {new TextComponent(),new TextComponent()};
	
	private BufferedImage boardImage; 
	
	public BoardPanel()
	{
		setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));//size of board panel
		
		try {
			boardImage = ImageIO.read(this.getClass().getResource("backgammonboard2.png"));  
		} catch (IOException ex) {
			System.out.println("Could not find the image file " + ex.toString());
		}
		setVisible(true);
		add(new TextComponent(20,25,"Match Length:",Color.BLACK));
		add(new TextComponent(380,25,"Match Score:",Color.black));
		add(new TextComponent(535,25,"-",Color.black));
	}
	
	public void paintComponent(Graphics g) 
	{ 
		super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(boardImage, 0, 0, BOARD_WIDTH, BOARD_HEIGHT, this);
        
	}
	
	public void updateDoublingCube(boolean order, int doubleValue)
	{
		remove(cube);
		if(order)//player2's side
		{
			add(cube = new CubeComponent(668,205,doubleValue));
		}
		else//player1's side
		{
			add(cube = new CubeComponent(668,285,doubleValue));
		}
	}
	
	public void updateTime()
	{
		remove(time);
		long currentTime = System.currentTimeMillis();
		long elapsedTime = currentTime - gameStart;
		String s = String.format("%d hours %d minutes", 
			    TimeUnit.MILLISECONDS.toHours(elapsedTime),
				TimeUnit.MILLISECONDS.toMinutes(elapsedTime) - 
			    TimeUnit.MINUTES.toMinutes(TimeUnit.MILLISECONDS.toHours(elapsedTime))
			);
		
		time = new TextComponent(148,25,s,Color.black);
		add(time);
	}
	
	public void updateScore(int [] scoreCounter)
	{
		remove(scores[0]);
		remove(scores[1]);
		scores[0].setText(String.format("%02d", scoreCounter[0]));
		scores[1].setText(String.format("%02d", scoreCounter[1]));
		add(scores[0]);
		add(scores[1]);
		
	}
	
	public void setBoard(Player player1, Player player2) {//for first match
		add(cube);
		add(time);
		gameStart = System.currentTimeMillis();
		add(scores[0] = new TextComponent(510,25,"00",player1.getColour()));
		add(scores[1] = new TextComponent(550,25,"00",player2.getColour()));
	}
	
	public void resetBoard() {//more matches
		//reset cube
		remove(cube);
		cube = new CubeComponent(668,245,1);
		add(cube);
		clearCheckers();
	}
	
	public void addStartingCheckers(Player player1, Player player2)
	{
		
		CheckerComponent c = new CheckerComponent();
		
		//Player1 checkers
		for(int x = 1; x < 6; x++)
		{
			c = boardPositions[12][x].addChecker(player1);
			c.setSize(new Dimension(35, 35));
			add(c);
			
			c = boardPositions[19][x].addChecker(player1);
			c.setSize(new Dimension(35, 35));
			add(c);
			
			if(x < 4)
			{
				c = boardPositions[17][x].addChecker(player1);
				c.setSize(new Dimension(35, 35));
				add(c);
			}
			if(x < 3)
			{
				c = boardPositions[1][x].addChecker(player1);
				c.setSize(new Dimension(35, 35));
				add(c);
			}
		}
		
		//Player2 checkers
		for(int x = 1; x < 6; x++)
		{
			c = boardPositions[13][x].addChecker(player2);
			c.setSize(new Dimension(35, 35));
			add(c);
			
			c = boardPositions[6][x].addChecker(player2);
			c.setSize(new Dimension(35, 35));
			add(c);
			
			if(x < 4)
			{
				c = boardPositions[8][x].addChecker(player2);
				c.setSize(new Dimension(35, 35));
				add(c);
			}
			if(x < 3)
			{
				c = boardPositions[24][x].addChecker(player2);
				c.setSize(new Dimension(35, 35));
				add(c);
			}

		}
	}
	
	public void addPositions()
	{
		//creates 1-5 x,y checker positions for pips 1-24
		for(int x = 1; x < 7; x++)
		{
			for(int y = 1; y < 6; y++)
			{
				boardPositions[x][y] = new Positions((BOARD_WIDTH - (BORDER_RIGHT + x*(PIP_WIDTH))),(BOARD_HEIGHT - (BORDER_BOTTOM + y*(CHECKER_HEIGHT))));
				boardPositions[25-x][y] = new Positions((BOARD_WIDTH - (BORDER_RIGHT + x*(PIP_WIDTH))),(BORDER_TOP + y*(CHECKER_HEIGHT)));
				boardPositions[13-x][y] = new Positions((BORDER_LEFT + (x-1)*(PIP_WIDTH)),(BOARD_HEIGHT - (BORDER_BOTTOM + y*(CHECKER_HEIGHT))));
				boardPositions[12+x][y] = new Positions((BORDER_LEFT + (x-1)*(PIP_WIDTH)),(BORDER_TOP + y*(CHECKER_HEIGHT)));
			}
			
		}
		
		//creates bar positions
		boardPositions[0][1] = new Positions(315,170);
		boardPositions[0][1].setBar();
		boardPositions[0][2] = new Positions(315, 250);
		boardPositions[0][2].setBar();
		
		//ignore
		boardPositions[0][0] = new Positions(0, 0);
		boardPositions[0][3] = new Positions(0, 0);
		
		//off board
		boardPositions[0][4] = new Positions(668, 140);
		boardPositions[0][4].setBar();
		boardPositions[0][5] = new Positions(668, 350);
		boardPositions[0][5].setBar();
		
		//pip numbers
		for(int x = 1; x < 7; x++)
		{
			boardPositions[x][0] = new Positions((BOARD_WIDTH - (BORDER_RIGHT - 12 + x*(PIP_WIDTH))), BOARD_HEIGHT);
			boardPositions[25-x][0] = new Positions((BOARD_WIDTH - (BORDER_RIGHT - 10 + x*(PIP_WIDTH))), BORDER_TOP+13);
			boardPositions[13-x][0] = new Positions((BORDER_LEFT + x*(PIP_WIDTH) - 40), BOARD_HEIGHT);
			boardPositions[12+x][0] = new Positions((BORDER_LEFT + x*(PIP_WIDTH) - 40), BORDER_TOP+13);
		}
		TextComponent T = new TextComponent();
		for(int x = 1; x < 25; x++)
		{
			boardPositions[x][0].setPip();
			T = boardPositions[x][0].addText(x);
			add(T);
		}
	}
	
	public boolean moveFromTo(int a, int b, boolean order)
	{
		
		//Conditions to return false
		
		/*if(boardPositions[a][1].getTaken() == false || boardPositions[b][5].getTaken() == true) {
			
			return false; 
		}*/
		
		int x, y;
		int i = (order) ? 1 : 0; //convert order into an int depending on its value for the barCounter array. this set is for placing checkers on the bar
		int i2 = (order) ? 0 : 1; //convert order into an int depending on its value for the barCounter array. this set is for removing checkers from the bar
		if(a == 0)//moving from bar
		{
			if(order)//player1
			{
				x = 1;
			} 
			else//player2
			{
				x = 2; 
			}
			y = findFree(b);
			Player p = boardPositions[0][x].getPlayer();
			
			if(barCounter[i2] > 1)//if there's more than one checker, change text 
			{
				remove(boardPositions[0][x].removeText());
				if(barCounter[i2] > 2)
				{
					add(boardPositions[0][x].addText(barCounter[i]-1));
				}
				
			}
			else//if there's one checker, remove checker
			{
				remove(boardPositions[0][x].removeChecker()); 
			}
			barCounter[i2]--;
			add(boardPositions[b][y].addChecker(p));
			
		}
		else if(b == 0)//moving to bar
		{
			y = findTop(a);
			if(order)//player1
			{
				x = 2;
			}
			else//player2
			{
				x = 1;
			}
			Player p = boardPositions[a][y].getPlayer();
			remove(boardPositions[a][y].removeChecker());
			
			if(barCounter[i] >= 1)//if there's already a checker, change text
			{
				remove(boardPositions[0][x].removeChecker());//quickfix
				if(barCounter[i] > 1)
				{
					remove(boardPositions[0][x].removeText()); 
				}
				add(boardPositions[0][x].addText(barCounter[i]+1));
				add(boardPositions[0][x].addChecker(p));//quickfix
			}
			else
			{
				add(boardPositions[0][x].addChecker(p));
			}
			barCounter[i]++;
		}
		else if(b == -1) { //bearing off the bar
			if(order) { //player 1
				x = 4; 
			} else {
				x = 5; 
			} 
			y = findTop(a); 
			Player p = boardPositions[a][y].getPlayer(); 
			remove(boardPositions[a][y].removeChecker()); 
			
			if(bearCounter[i] >= 1) {
				remove(boardPositions[0][x].removeChecker()); //quickfix
				if(bearCounter[i] > 1) {
					remove(boardPositions[0][x].removeText()); 
				}
				add(boardPositions[0][x].addText(bearCounter[i]+1)); 
				add(boardPositions[0][x].addChecker(p)); 
			} else {
				add(boardPositions[0][x].addChecker(p));
			}
			bearCounter[i]++; 
		}
		else {//move piece from pip a to pip b
			x = findTop(a);
			y = findFree(b);
			Player p = boardPositions[a][x].getPlayer();
			remove(boardPositions[a][x].removeChecker());
			
			add(boardPositions[b][y].addChecker(p));
		}
		return true;
	} 
	
	public int findTop(int a) {
		for(int i = 5; i >= 1; i--)
		{
			if(boardPositions[a][i].getTaken())
			{
				return i; 
			}
		} 
		return 5; 
	}
	
	public int findFree(int b) { 
		for(int i = 1; i < 6; i++)
			{
				if(!(boardPositions[b][i].getTaken()))
				{
					return i; 
				}
			} 
			return 1;
	}
	
	public void removeChecker(int a, int b) {
		if(boardPositions[a][b].getTaken())
		{
			remove(boardPositions[a][b].removeChecker());
		}
	}
	
	public void clearCheckers() {
		//have to deal with bar positions separately 
		for(int i = barCounter[0]; i > 0; i--) {
			removeChecker(0, 1); 
		}
		for(int i = barCounter[1]; i > 0; i--) {
			removeChecker(0, 2); 
		}
		
		for(int i = 1; i < 25; i++) {
			if(boardPositions[i][1].getTaken()) {
				for(int i2 = findTop(i); i2 >= 1; i2--) {
					removeChecker(i, i2); 
				}
			}
		}
		barCounter[0] = 0;
		barCounter[1] = 0;
		
		if(boardPositions[0][4].getTaken())
		{
			remove(boardPositions[0][4].removeChecker());
		}
		if(boardPositions[0][5].getTaken())
		{
			remove(boardPositions[0][5].removeChecker());
		}
		
		TextComponent t = new TextComponent();
		if((t = boardPositions[0][4].removeText()) != null)
		{
			remove(t);
		}
		if((t = boardPositions[0][5].removeText()) != null)
		{
			remove(t);
		}
		bearCounter[0] = 0;
		bearCounter[1] = 0;
	}
	
	public void cheat(Player p1, Player p2) {
		//clear bored completely
		CheckerComponent c;
		TextComponent t;
		for(int x = 1; x < 3; x++)//clears bar
		{
			barCounter[x-1] = 3;
			c = boardPositions[0][x].removeChecker();
			t = boardPositions[0][x].removeText();
			if(c != null)
			{
				remove(c);
				if(t != null)
				{
					remove(t);
				}
			}
		}
		
		for(int x = 1; x < 25; x++)//clears board checkers
		{
			if(boardPositions[x][1].getTaken())//if this pip is not empty
			{
				for(int y = findTop(x); y >= 1; y--)//find all the checkers on this pip
				{
					remove(boardPositions[x][y].removeChecker());//remove them
				}
			}
		}
		
		//two checkers at player 1's ace point
		c = boardPositions[24][1].addChecker(p1);
		c.setSize(new Dimension(35, 35));
		add(c);
		c = boardPositions[24][2].addChecker(p1);
		c.setSize(new Dimension(35, 35));
		add(c);
		//two checkers at player 2's ace point
		c = boardPositions[1][1].addChecker(p2);
		c.setSize(new Dimension(35, 35));
		add(c);
		c = boardPositions[1][2].addChecker(p2);
		c.setSize(new Dimension(35, 35));
		add(c);
		
		//add checkers to bar
		/*add(boardPositions[0][2].addText(3));
		add(boardPositions[0][2].addChecker(p1));
		add(boardPositions[0][1].addText(3));
		add(boardPositions[0][1].addChecker(p2));*/
		
		//add checkers off board
		add(boardPositions[0][5].addText(13));
		add(boardPositions[0][5].addChecker(p1));
		add(boardPositions[0][4].addText(13));
		add(boardPositions[0][4].addChecker(p2));
		
		bearCounter[0] = 13;
		bearCounter[0] = 13;
		
	}
}