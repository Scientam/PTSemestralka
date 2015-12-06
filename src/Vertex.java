import java.util.Random;

public class Vertex {
	
	/** vytvori promnenou ktera slouzi k vytvareni nahodnych velicin */
	Random r = new Random();	
	/** deklaruje promenou pro identifikaci vrcholu */
	protected int key;
	/** promenna uchovajici x souradnici objektu */
	protected double xAxis;											
	/** promenna uchovavajici y souradnici obejektu */
	protected double yAxis;											
	/** promenna uchovavajici pocet sousedu objektu, tj.objekty spojene cestou */
	protected int neighbourCount;					
	/** deklaruje promennou uchovavajici pocet predchucu vrcholu */ 
	public int[] predecessor;		
	/** deklaruje promennou uchovavajici vzdalenost vrcholu */
	protected double distance;
	/**  promena uchovavajicí pole sousedu*/
	protected Neighbour[] neighbour;
	
																				//konstruktory
	/**
	 * Konstruktor vrcholu
	 * @param key
	 * @param color
	 */
	 Vertex(int key, double xAxis, double yAxis, int neighbourCount) {
			this.key = key;
			this.xAxis = xAxis;
			this.yAxis = yAxis;
			this.neighbourCount = neighbourCount;
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
	 
	 	
	 public int[] getPredecessor() {
		 return predecessor;
	 }
	
}
