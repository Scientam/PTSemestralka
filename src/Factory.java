import java.util.ArrayList;

public class Factory extends Vertex{
	private ArrayList<Integer> distance;
	  																		/* konstruktor */	     
	  /**
	   * Vytvori centralu se zadanym id a souradnicemi, poctem sousedu
	   * @param id 
	   * @param xSour, ySour
	   * @param pocetSousC
	   */ 
	   public Factory(int id, double xAxis, double yAxis, int neighbourCount, char color) {
		   super(id, xAxis, yAxis, neighbourCount, color);
	  }
	   
}
