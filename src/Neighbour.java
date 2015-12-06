
public class Neighbour {

	/** promena ktera urcuje identifikacni cislo objektu */
	private int index;
	/** promena ktera urcuje vzdalenost mezi objekty */
	private double dist;
	/** promena ktera urcuje zda je cesta mezi vrcholy bezpecna */
	private boolean dangerous;
	
	/**
	 * konstruktor
	 * @param index
	 * @param dist
	 */
	Neighbour(int index, double dist){
		this.index=index;
		this.dist=dist;		
	}
	
																	//gettery a settery
	public int getIndex() {
		return index;
	}
	
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	
	public double getDist() {
		return dist;
	}
	
	
	public void setDist(double dist) {
		this.dist = dist;
	}
	
	
	public boolean getDanger() {
		return dangerous;
	}
	
	
	public void setDanger(boolean argDanger) {
		this.dangerous = argDanger;
	}
	
	
    /**
     * Metoda ktera vytvori a vrati klon dane instance.
     */
	public Neighbour clone() {
		return new Neighbour(this.index, this.dist);
	}
	
}
