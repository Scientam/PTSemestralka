
public class Neighbour {

	protected int index;
	protected double dist;
	
	Neighbour(int index, double dist){
		this.index=index;
		this.dist=dist;		
	}
																	//gettery a settery
	public double getIndex() {
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
	
	public Neighbour clone() {
		return new Neighbour(this.index, this.dist);
	}	
}
