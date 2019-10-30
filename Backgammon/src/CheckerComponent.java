import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;

public class CheckerComponent extends JComponent
{
	private static final int CHECKER = 35;
	public Color[] checkerColors = {Color.black, Color.white};
	public Player player;
	public int posX;
	public int posY;
	
	public CheckerComponent() { }
    public CheckerComponent(int x, int y, Player p)
    {
        posX = x;
        posY = y;
        player = p;
    }
	
	public void paintComponent(Graphics g)
	{
		//Draws checker piece
		Graphics2D g2 = (Graphics2D) g;
		Ellipse2D.Double ellipse = new Ellipse2D.Double(posX,posY,CHECKER,CHECKER); 
		g2.draw(ellipse);
		g2.setColor(player.getColour());
		g2.fill(ellipse);
	}
	
}