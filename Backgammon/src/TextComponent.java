import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import javax.swing.JComponent;

public class TextComponent extends JComponent{
	public int posX;
	public int posY;
	public int number;
	public String text;
	public Color color;
	
	public TextComponent() { }
    public TextComponent(int x, int y, String t, Color c)
    {
        posX = x;
        posY = y;
        text = t;
        color = c;
    }
    
    public TextComponent(int x, int y, int z, Color c)
    {
    	posX = x;
    	posY = y;
    	text = Integer.toString(z);
    	color = c;
    }
    
    public void setText(String s)
    {
    	text = s;
    }
	
	public void paintComponent(Graphics g)
	{
		//Draws text
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
        g2.setFont(new Font("Courier",Font.BOLD,16));
        g2.drawString(text, posX, posY);
	}
}