import java.util.ArrayList;
import java.util.List;

public class Graph {		
	public static int[][] p;
		  	
  	
  	/**
  	 * Floyd-Warshall algorithm. Finds all shortest paths among all pairs of nodes
  	 * @param d matrix of distances (Integer.MAX_VALUE represents positive infinity)
  	 * @return matrix of predecessors
  	 */
  	public static int[][] floydWarshallM(int[][] m, boolean argBool) {
  		if(argBool){p = constructInitialMatrixOfPredecessors(m);}  		
  	    for (int k = 0; k < m.length; k++) {
			for (int i = 0; i < m.length; i++) {
				for (int j = 0; j < m.length; j++) {
					// to keep track.;
					if ( m[i][j] > m[i][k] + m[k][j]) {
						m[i][j] = m[i][k] + m[k][j];
						p[i][j] = p[k][j];
					}
				}
			}
		}
		return m;
  	}
  	
  	
  	/**
	 * Constructs matrix P0
	 * @param d matrix of lengths
	 * @return P0
	 */
	private static int[][] constructInitialMatrixOfPredecessors(int[][] d) {
	    int[][] initial = new int[d.length][d.length];
	    for (int i = 0; i < d.length; i++) {
	        for (int j = 0; j < d.length; j++) {
	            if (d[i][j] != 0 && d[i][j] != Integer.MAX_VALUE) {
	            	initial[i][j] = i;
	            } else {
	            	initial[i][j] = -1;
	            }
	        }
	    }
	    return initial;
	}
	
  	
  	/**
  	 * 
  	 * @return
  	 */
  	public static int[][] getPathMatrix(){
  		return p;
  	}
  	
  	
  	/**
  	 * Metoda, která nalezne nejkratsi cestu z prave zpracovavaneho vrcholu
	 * do libovolneho zadaneho vrcholu
  	 * @param source
  	 * @param target
  	 * @param entitiesV
  	 * @return
  	 */
  	public static List<Integer> getShortestPathTo(int argSource, int argTarget, List<Vertex> entitiesV) {
  		List<Integer> path = new ArrayList<Integer>();
  		List<Integer> seq = new ArrayList<Integer>();
  		int source = argSource;
  		int target = argTarget;
  		int stop = 1;
  		path.add(source);
  		
  		while (stop==1){                                     //pozn. source=radek, taget=sloupec
  			seq.add(target);
  			if (entitiesV.get(source).predecessor[target]==-1){         // osetreni chyby
  				stop=-1;
  			    System.out.println("Tohle se nemelo stat.");
  			}
  			else if (entitiesV.get(source).predecessor[target]==source){
  		    	for(int i=seq.size()-1; i != -1; i--){
  		    		path.add(seq.get(i));
  		    	}
  		    	stop=-1;
  		    }
  		    else{ 
  		    	target = entitiesV.get(source).predecessor[target];
  		    }
        }
  		return path;
  	}
 	
}