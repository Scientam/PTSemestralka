import java.util.Random;

/**
 * Trida pro reprezentaci vrcholu
 * @author Scien_000
 */

public class Vertex {
	
	/* deklarace promennych */
	Random r = new Random();	
	/** deklaruje promenou pro identifikaci vrcholu */
	protected int key;
	/** promenna uchovajici x souradnici objektu */
	protected double xAxis;											
	/** promenna uchovavajici y souradnici obejektu */
	protected double yAxis;											
	/** promenna uchovavajici pocet sousedu objektu, tj.objekty spojene cestou */
	protected int neighbourCount;				
	/** deklaruje promenou pro algoritmy hledani nejkratsich cest */
	protected char color;		
	/** deklaruje promennou uchovavajici pocet predchucu vrcholu */ 
	public int[] predecessor;		
	/** deklaruje promennou uchovavajici vzdalenost vrcholu */
	protected double distance;
	protected Neighbour[] neighbour;
	
																				//konstruktory
	/**
	 * Konstruktor vrcholu
	 * @param key
	 * @param color
	 */
	 Vertex(int key, double xAxis, double yAxis, int neighbourCount, char color) {
			this.key = key;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.neighbourCount = neighbourCount;
			this.color = color;
		    this.neighbour = new Neighbour[neighbourCount];
		}

	
	 																									/* gettery */
	 public int getKey() {
		 return key;
	 }
	  
	 public double getXAxis() {
		 return xAxis;
	 }
	  
	 public double getYAxis() {
		 return yAxis;
	 }

	 public int getNeighbourCount() {
		 return neighbourCount;
	 }
	 
	 public char getColor() {
		 return color;
	 }
	 	
	 public int[] getPredecessor() {
		 return predecessor;
	 }
	 
	                                                             //metody
	/**
	* Metoda pro tisk vrcholu
	*/
	public void printVertex() {
		System.out.print(key+" ");
	}
}
