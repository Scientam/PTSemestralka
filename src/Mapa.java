import javax.swing.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;


public class Mapa extends JFrame{

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


	/**
	 * 
	 * @param planetsCount
	 * @param factoriesCount
	 * @param ar
	 * @param adjId
	 */
	public Mapa(int factoriesCount, int planetsCount, int neighbourCountF, int neighbourCountP, ArrayList<Vertex>ar) {
		this.factoriesCount = factoriesCount;
		this.planetsCount = planetsCount;
		this.neighbourCountF = neighbourCountF;
		this.neighbourCountP = neighbourCountP;
		this.ar = ar;
	
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
	
		System.out.println("Prave jsem vstoupil do malovani.");
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
	
		for (int i = 0; i < ar.size(); i++) {	
			g2.setColor(Color.BLACK);
			if(i < factoriesCount) {
				for (int j = 0; j < neighbourCountF; j++) {
					int id = ar.get(i).neighbour[j].index;	
					g2.draw(new Line2D.Double(ar.get(i).getXAxis()+2+100, ar.get(i).getYAxis()+2+130, ar.get(id).getXAxis()+2+100, ar.get(id).getYAxis()+2+130) );
				}				
			}		
			else{					
				for (int j = 0; j < neighbourCountP; j++) {
					int id = ar.get(i).neighbour[j].index;	
					if(i==5) {
						g2.setColor(Color.PINK);
						System.out.println("Y kam jdu: " + ar.get(id).key);
					}
					g2.draw(new Line2D.Double(ar.get(i).getXAxis()+2+100, ar.get(i).getYAxis()+2+130, ar.get(id).getXAxis()+2+100, ar.get(id).getYAxis()+2+130) );
				}
			}
		} 
	}
}
