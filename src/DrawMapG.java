import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.List;

public class DrawMapG extends JFrame{

	private static final long serialVersionUID = 1L;
	/** vytvori promenou ktera urcuje pocet central v galaxii */
	int factoriesCount;
	/** vytvori promenou ktera urcuje pocet planet v galaxii */
	int planetsCount;
	/** vytvori promenou ktera urcuje pocet sousedu kazde centraly */
	int neighbourCountF;
	/** vytvori promenou ktera urcuje pocet sousedu kazde planety */
	int neighbourCountP;
	/** vytvori ArrayList do ktereho se budou ukladat objekty Vertex jako vrcholy grafu */
	List<Vertex> entities;
	/** vytvori promennou, ktera uchovava posloupnosti vrcholu tvorici nejkratsi cestu z vrcholu u do vrcholu v */
	List<Integer>[][] paths;


	/**
	 * 
	 * @param argFactoriesCount
	 * @param argPlanetsCount
	 * @param argNeighbourCountF
	 * @param argNeighbourCountP
	 * @param argAr
	 * @param argPaths
	 */
	public DrawMapG(int argFactoriesCount, int argPlanetsCount, int argNeighbourCountF, int argNeighbourCountP, List<Vertex> argEntities, List<Integer>[][] argPaths) {
		this.factoriesCount = argFactoriesCount;
		this.planetsCount = argPlanetsCount;
		this.neighbourCountF = argNeighbourCountF;
		this.neighbourCountP = argNeighbourCountP;
		this.entities = argEntities;
		this.paths=argPaths;
	
		this.setTitle("MapaGalaxie");
		this.setSize(800, 800);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	
	/**
	 * 
	 */
	public void paint(Graphics g) {
		super.paint(g);
		paint2D((Graphics2D)g);
	}
	
	/**
	 *  Metoda sloužící ke grafickému znázornìní situace v galaxii.
	 * @param g2
	 */
	public void paint2D(Graphics2D g2) {
																								/*KRESLENI VRCHOLU*/
		g2.setColor(Color.BLUE);
		for(int i = factoriesCount; i < entities.size(); i++) {
			g2.fill(new Ellipse2D.Double(entities.get(i).getXAxis()+100, entities.get(i).getYAxis()+130 , 4, 4));
		}		
		
		g2.setColor(Color.GREEN);
		for(int j = 0; j < factoriesCount; j++) {
			g2.fill(new Ellipse2D.Double(entities.get(j).getXAxis()+100, entities.get(j).getYAxis()+130, 7, 7));
		}		
		
		g2.setColor(Color.RED);
		g2.fill(new Ellipse2D.Double(400 + 100, 400 + 130, 10, 10));		
																								 /*KRESLENI CEST*/
		for (int i = 0; i < entities.size(); i++) {	
			if (i==5) {g2.setColor(Color.RED);}
			else{g2.setColor(Color.BLACK);}
			
			if(i < factoriesCount) {
				for (int j = 0; j < neighbourCountF; j++) {
					int id = entities.get(i).neighbour[j].getIndex();	
					g2.draw(new Line2D.Double(entities.get(i).getXAxis()+2+100, entities.get(i).getYAxis()+2+130, entities.get(id).getXAxis()+2+100, entities.get(id).getYAxis()+2+130) );
				}				
			}		
			else{					
				for (int j = 0; j < neighbourCountP; j++) {
					int id = entities.get(i).neighbour[j].getIndex();	
					g2.draw(new Line2D.Double(entities.get(i).getXAxis()+2+100, entities.get(i).getYAxis()+2+130, entities.get(id).getXAxis()+2+100, entities.get(id).getYAxis()+2+130) );
				}
			} 
		}
	}
	
}
