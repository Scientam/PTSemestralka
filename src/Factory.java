public class Factory extends Vertex {
	  /**
	   * Vytvori centralu se zadanym id a souradnicemi, poctem sousedu.
	   * @param id 
	   * @param xSour, ySour
	   * @param pocetSousC
	   */ 
	   public Factory(int index, double xAxis, double yAxis, int neighbourCount) {
		   super(index, xAxis, yAxis, neighbourCount);
	  }
	   
}
