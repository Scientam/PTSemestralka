
public class Neighbour {

	private int index;
	private double dist;
	private boolean dangerous;
	
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
	public Neighbour clone() {
		return new Neighbour(this.index, this.dist);
	}	
}
