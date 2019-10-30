import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JComponent;

public class CubeComponent extends JComponent
{
	public int posX;
	public int posY;
	public int value;
	
	public CubeComponent() { }
    public CubeComponent(int x, int y, int z)
    {
        posX = x;
        posY = y;
        value = z;
    }
	
	public void paintComponent(Graphics g)
	{
		//Draws checker piece
		Graphics2D g2 = (Graphics2D) g;
		Graphics2D graphics2 = (Graphics2D) g;
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(posX, posY, 35, 35, 10, 10);
        graphics2.draw(roundedRectangle);
		g2.setColor(Color.WHITE);
		g2.fill(roundedRectangle);
		
		g2.setColor(Color.black);
        g2.setFont(new Font("Courier",Font.BOLD,16));
		g2.drawString(Integer.toString(value), posX+8, posY+23);
	}
	
}
