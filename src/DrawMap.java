import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

/**
 * 
 * @author Karel Sobehart, Tomas Matejka
 *
 */
public class DrawMap extends JFrame{

	/**
	 * 
	 */
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
	ArrayList<Vertex> entities;
	/** vytvori promennou, ktera uchovava posloupnosti vrcholu tvorici nejkratsi cestu z vrcholu u do vrcholu v */
	ArrayList<Integer>[][] paths;


	/**
	 * 
	 * @param argFactoriesCount
	 * @param argPlanetsCount
	 * @param argNeighbourCountF
	 * @param argNeighbourCountP
	 * @param argAr
	 * @param argPaths
	 */
	public DrawMap(int argFactoriesCount, int argPlanetsCount, int argNeighbourCountF, int argNeighbourCountP, ArrayList<Vertex>argEntities, ArrayList<Integer>[][] argPaths) {
		this.factoriesCount = argFactoriesCount;
		this.planetsCount = argPlanetsCount;
		this.neighbourCountF = argNeighbourCountF;
		this.neighbourCountP = argNeighbourCountP;
		this.entities = argEntities;
		this.paths=argPaths;
	
		this.setTitle("Mapa");
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
			if (i<factoriesCount) {g2.setColor(Color.RED);}
			else{g2.setColor(Color.BLACK);}
			
		
			/*Sousedi 
			if(i < factoriesCount) {
				for (int j = 0; j < neighbourCountF; j++) {
					int id = entities.get(i).neighbour[j].index;	
					g2.draw(new Line2D.Double(entities.get(i).getXAxis()+2+100, entities.get(i).getYAxis()+2+130, entities.get(id).getXAxis()+2+100, entities.get(id).getYAxis()+2+130) );
				}				
			}		
			else{					
				for (int j = 0; j < neighbourCountP; j++) {
					int id = entities.get(i).neighbour[j].index;	
					g2.draw(new Line2D.Double(entities.get(i).getXAxis()+2+100, entities.get(i).getYAxis()+2+130, entities.get(id).getXAxis()+2+100, entities.get(id).getYAxis()+2+130) );
				}
			} */
			
			/*Nejkratsi cesty */
			int from;
		    int to;
			for (int j = 0; j < entities.size(); j++) {
				if (paths[i][j] != null) {
				    for (int k = 0; k < paths[i][j].size()-1; k++){ 					
						from = paths[i][j].get(k);
						to = paths[i][j].get(k+1);
						g2.draw(new Line2D.Double(entities.get(from).getXAxis()+2+100, entities.get(from).getYAxis()+2+130, entities.get(to).getXAxis()+2+100, entities.get(to).getYAxis()+2+130) );
					}
					
				}
		    }
		}
	}
}
