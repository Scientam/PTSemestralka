public class Starship {
	
	/** promenna uchovavajici idetifikacni cislo lodi */
	private  int id;
	/** promenna uchovavajici rychlost lodi */
	private  int velocity;
	/** promenna uchovavajici kapacitu lodi */
	private  int capacity;
	/** promenna uchovavajici id cetraly pod kterou lod spada */
	private  int numF;
	/** promenna uchovavajici id planety na kterou lod prave miri */
	private  int targetP;
	/** promenna uchovavajici informaci o tom zda je dana lod prave k dispozici */
	private boolean isInUse;
	
															/* konstruktor */
	/**
	 * 
	 * @param id
	 * @param velocity
	 * @param capacity
	 */
	public Starship(int id, int velocity,int capacity, int numF)  {
		this.id = id;
		this.velocity = velocity;
		this.capacity = capacity;
		this.numF = numF;
		this.isInUse = true;
	}
	
														/* settery */
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
	
														/* gettery */
	/**
	 * 
	 * @return
	 */
	 public int getId() {
		 return id;
	 }
	  
	 /**
	  * 
	  * @return
	  */
	 public int getVel() {
		 return velocity;
	 }
	 
	 /**
	  * 
	  * @return
	  */
	 public int getCap() {
		 return capacity;
	 }
	 
	 /**
	  * 
	  * @return
	  */
	 public int getNumF() {
		 return numF;
	 }
	 
	 /**
	  * 
	  * @return
	  */
	 public int getTargetP() {
		 return targetP;
	 }
	 
	 /**
	  * 
	  * @return
	  */
	 public boolean getIsInUse() {
		 return isInUse;
	 }

}
