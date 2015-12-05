public class Planet extends Vertex {
	


	/** promenna uchovavajici informaci o tom zda se na planetu budou nadale dovazet leky  */
	private boolean status;
	/** promenna uchovavajici informaci o poctu obyvatel */
	private int populCount;
	/** promenna uchovajici informaci o vlastni produkci leku*/
	private int drugProduction;
	/** promenna uchovavajici objednavku*/
	private int order;
	/** */
	private int id;
	private boolean answered;
	
	  															/* konstruktor */	     
	/**
	* Vytvori planetu se zadanym id a souradnicemi, poctem sousedu a vygeneruje populaci podle normalniho rozdeleni
	* pravdepodobnosti se stredem 3000000 a odchylkou 2900000 (nad 100000 obyvatel). Pravdepodobnost vygenerovani
	* cisla mimo rozsah je teoreticky 0,3 %.
	* @param id 
	* @param xSour, ySour
	* @param pocetSousC
	*/ 
	public Planet(int id, double xAxis, double yAxis, int neighbourCount) {
		super(id, xAxis, yAxis, neighbourCount);
		this.id=id;
		status = true;
		double population = r.nextGaussian() * 2900000/3 + 3000000;
		populCount = (int) Math.round(population);								
	}	  
	
	
	/**
	 * Tato metoda urcuje vlastni produkci leku na planete. Tato metoda urcuje vlastni produkci leku na planete. Nejprve vygeneruje procentualni pokryti
	 * a z nej se potom vypocita konecny pocet leku. Vyrobi se pouze cele baleni leku, proto cele cislo.
	 * @param populCount
	 * @return
	 */
	public int drugProduction(int populCount) {
		double percentage = 0.2 + (0.8 - 0.2) * r.nextDouble();
		double drugs = Math.round(percentage*100.0)/100.0;
		drugProduction = (int) Math.round(drugs * populCount);
		//System.out.println(drugProduction);
  		return drugProduction;
  	}
	
	
	/**
	 * Tato metoda zjisti zda je dostatek leku, pokud ne, snizi prislusny pocet obyvatel
	 * @param populCount
	 * @return
	 */
	public int enoughDrugProduction(int populCount) {
		if(drugProduction < populCount){
			this.populCount = (int) drugProduction - populCount;
		}
  		return this.populCount;
  	}
	
	
	/**
	 * Tato metoda vraci informaci o to zda se na planetu budou nadale dovazet leky
	 * @param populCount
	 * @return
	 */
	public boolean planetStatus(int populCount) {
		if(populCount < 40000){
			status = false;
		}
  		return status;
  	}
	
	
	/**
	 * 
	 * @param populCount
	 * @param drugProduction
	 * @return
	 */
	public int order(int populCount, int drugProduction){
		order = populCount - drugProduction;
		return order;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int getId() {
		return this.id;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int getPopulCount() {
		return this.populCount;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public int getOrder() {
	    return this.order;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean getAnswered() {
	    return this.answered;
	}
	
	/**
	 * 
	 * @param order
	 */
	public void setOrder(int argOrder) {
		this.order = argOrder;
	}
	
	
	/**
	 * 
	 * @param argId
	 */
	public void setId(int argId) {
		this.id = argId;
	}
	
	
	/**
	 * 
	 * @param argAnswered
	 */
	public void setAnswered(boolean argAnswered) {
		this.answered = argAnswered;
	}
	
	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}
	
}