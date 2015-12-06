public class Starship {	
	/** promenna uchovavajici idetifikacni cislo lodi */
	private  int id;
	/** promenna uchovavajici rychlost lodi */
	private  int velocity;
	/** promenna uchovavajici kapacitu lodi */
	private  int capacity;
	/** promenna uchovavajici id cetraly pod kterou lod spada */
	private  int numF;
	/** promenna uchovavajici id planety, ze ktere lod leti */
	private int sourceP;
	/** promenna uchovavajici id planety, na kterou lod prave miri */
	private  int targetP;
	/** promìnná uchovavajicí vzdalenost, kterou musi lod uletet */
	private int distance;
	/** promenna uchovavajici informaci o tom zda je dana lod prave k dispozici */
	private boolean isInUse;
	
																							/* konstruktor */
	/**
	 * konstruktor
	 * @param id
	 * @param velocity
	 * @param capacity
	 */
	public Starship(int id, int velocity,int capacity, int numF, int argSourceP)  {
		this.id = id;
		this.velocity = velocity;
		this.capacity = capacity;
		this.numF = numF;
		this.sourceP = argSourceP;
		this.isInUse = true;
	}
	
																								/* settery */

	
	public void setId(int argId) {
		this.id = argId;
	}	


	public void setSourceP(int argSourceP) {
		this.sourceP = argSourceP;
	}


	public void setTargetP(int argTargetP) {
		 this.targetP = argTargetP;
	}
	
	
	public void setIsInUse(boolean argIsInUse) {
		 this.isInUse = argIsInUse;
	}
	
	
	public void setCapacity(int argCapacity) {
		this.capacity = argCapacity;
	}
	

	public void setVelocity(int argVelocity) {
		this.velocity = argVelocity;
	}
	
	
	public void setNumF(int argNumF) {
		this.numF = argNumF;
	}
	
	
	public void setDistance(int argDistance) {
		this.distance = argDistance;
	}
																										/* gettery */

	 public int getId() {
		 return id;
	 }
	 
	  
	 public int getVelocity() {
		 return velocity;
	 }
	 
	 
	 public int getCapacity() {
		 return capacity;
	 }
	 
	 
	 public int getNumF() {
		 return numF;
	 }
	 
	 
	 public int getSourceP() {
			return sourceP;
	}
	 
	 
	 public int getTargetP() {
		 return targetP;
	 }
	 

	 public int getDistance() {
		 return distance;
	 }
	 
	 
	 public boolean getIsInUse() {
		 return isInUse;
	 }
	 
}
