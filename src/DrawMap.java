import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;


public class DrawMap extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** promenna uchovavajici pocet central*/
	int factoriesCount;
	/** promenna uchovavajici pocet planet*/
	int planetsCount;
	int neighbourCountF;
	int neighbourCountP;
	/** ArrayList uchovavajici vsechny objekty typu Entita*/
	ArrayList<Vertex> ar;
	int[][] shortestPath;


	/**
	 * 
	 * @param planetsCount
	 * @param factoriesCount
	 * @param ar
	 * @param adjId
	 */
	public DrawMap(int factoriesCount, int planetsCount, int neighbourCountF, int neighbourCountP, ArrayList<Vertex>ar, int[][] shortestPath) {
		this.factoriesCount = factoriesCount;
		this.planetsCount = planetsCount;
		this.neighbourCountF = neighbourCountF;
		this.neighbourCountP = neighbourCountP;
		this.ar = ar;
		this.shortestPath =  shortestPath;
	
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
		for(int i = factoriesCount; i < ar.size(); i++) {
			g2.fill(new Ellipse2D.Double(ar.get(i).getXAxis()+100, ar.get(i).getYAxis()+130 , 4, 4));
		}		
		
		g2.setColor(Color.GREEN);
		for(int j = 0; j < factoriesCount; j++) {
			g2.fill(new Ellipse2D.Double(ar.get(j).getXAxis()+100, ar.get(j).getYAxis()+130, 7, 7));
		}		
		
		g2.setColor(Color.RED);
		g2.fill(new Ellipse2D.Double(400 + 100, 400 + 130, 10, 10));
		
																										/*KRESLENI CEST*/

		for (int i = 0; i < shortestPath.length-1; i++) {	
			g2.setColor(Color.BLACK);
		
			/*Sousedi*/
			if(i < factoriesCount) {
				for (int j = 0; j < neighbourCountF; j++) {
					int id = ar.get(i).neighbour[j].index;	
					g2.draw(new Line2D.Double(ar.get(i).getXAxis()+2+100, ar.get(i).getYAxis()+2+130, ar.get(id).getXAxis()+2+100, ar.get(id).getYAxis()+2+130) );
				}				
			}		
			else{					
				for (int j = 0; j < neighbourCountP; j++) {
					int id = ar.get(i).neighbour[j].index;	
					g2.draw(new Line2D.Double(ar.get(i).getXAxis()+2+100, ar.get(i).getYAxis()+2+130, ar.get(id).getXAxis()+2+100, ar.get(id).getYAxis()+2+130) );
				}
			}
			
			/*Nejkratsi cesty
			for (int j = 0; j < shortestPath.length-1; j++) {
				//System.out.println("Hodnota j je: "+j);
					
				if ( (shortestPath[i][j] != -1 && shortestPath[i][j] != i) ) {
					System.out.println();
					//System.out.println("Hodnota j je: "+j+" a index je: "+shortestPath[i][j]);
					g2.draw(new Line2D.Double(ar.get(i).getXAxis()+2+100, ar.get(i).getYAxis()+2+130, ar.get(shortestPath[i][j]).getXAxis()+2+100, ar.get(shortestPath[i][j]).getYAxis()+2+130) );
					//g2.draw(new Line2D.Double(ar.get(shortestPath[i][j]).getXAxis()+2+100, ar.get(shortestPath[i][j]).getYAxis()+2+130, ar.get(j).getXAxis()+2+100, ar.get(j).getYAxis()+2+130) );
				}
				
			}*/
		} 
	}
}
