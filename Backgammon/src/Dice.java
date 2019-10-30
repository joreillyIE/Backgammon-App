import java.util.Random;

public class Dice {
	
	private int dice1; 
	private int dice2; 
	
	public void roll() {
		dice1 = new Random().nextInt(6)+1; 
		dice2 = new Random().nextInt(6)+1; 
	}
	
	public int getDice1() {
		return dice1; 
	}
	
	public int getDice2() {
		return dice2; 
	}
	
	public int getSum() {
		return dice1+dice2; 
	}
	
	public boolean isDouble() {
		boolean isDouble = false; 
		if (getDice1() == getDice2()) {
			isDouble = true; 
		}
		return isDouble; 
	}
	
}
