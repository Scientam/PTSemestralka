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
	private double distance;
	/** promenna uchovavajici informaci o tom zda je dana lod prave k dispozici */
	private boolean isInUse;
	
															/* konstruktor */
	/**
	 * 
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

	
	public void setId(int id) {
		this.id = id;
	}	


	/**
	 * 
	 * @param sourceP
	 */
	public void setSourceP(int sourceP) {
		this.sourceP = sourceP;
	}


	/**
	 * 
	 * @param targetP
	 */
	public void setTargetP(int targetP) {
		 this.targetP = targetP;
	}
	
	
	/**
	 * 
	 * @param isInUse
	 */
	public void setIsInUse(boolean isInUse) {
		 this.isInUse = isInUse;
	}
	
	
	/**
	 * 
	 * @param capacity
	 */
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	/**
	 * 
	 * @param argVelocity
	 */
	public void setVelocity(int argVelocity) {
		this.velocity = argVelocity;
	}
	
	
	/**
	 * 
	 * @param argNumF
	 */
	public void setNumF(int argNumF) {
		this.numF = argNumF;
	}
	
	
	/**
	 * 
	 * @param distance
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}
	

																										/* gettery */
	/**
	 * 
	 * @return id
	 */
	 public int getId() {
		 return id;
	 }
	 
	  
	 /**
	  * 
	  * @return velocity
	  */
	 public int getVelocity() {
		 return velocity;
	 }
	 
	 
	 /**
	  * 
	  * @return capacity
	  */
	 public int getCapacity() {
		 return capacity;
	 }
	 
	 
	 /**
	  * 
	  * @return numF
	  */
	 public int getNumF() {
		 return numF;
	 }
	 
	 
	 /**
	  * 
	  * @return sourceP
	  */
	 public int getSourceP() {
			return sourceP;
	}
	 
	 
	 /**
	  * 
	  * @return targetP
	  */
	 public int getTargetP() {
		 return targetP;
	 }
	 
	 /**
	  * 
	  * @return distance
	  */
	 public double getDistance() {
		 return distance;
	 }
	 
	 
	 /**
	  * 
	  * @return isInUse
	  */
	 public boolean getIsInUse() {
		 return isInUse;
	 }
	 
}
