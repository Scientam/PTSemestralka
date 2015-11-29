import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Trida pro reprezentaci grafu
 * @author Karel Sobehart
 */
public class Graph {
    /** tato promenna uchovava vrcholy nasi grafove striktury */
	private static Vertex[] vertexes;					
	/** tato promenna uchovava informaci o tom zda spoluvrcholy sousedi */
	private int[][] adjMatrix;
	/** tato promenna uchovava konecny pocet vrcholu */
	private static int vertexCount;	
	/** promenna uchovavajici informaci o to zda je graf orientovany */
	private boolean connected;			
	public static int[][] p;
		
	                                                              /* konstruktor */	   
	/**
	 * Konstruktor grafu
	 * @param pocVr
	 */
	Graph(int vertexCount, boolean connected) {
    this.vertexCount = vertexCount;												//inicializuje pocet vrcholu			
    this.adjMatrix = new int[vertexCount][vertexCount];							//inicializuje velikost matice sousednosti
	this.vertexes = new Vertex[vertexCount];									//inicializuje velikost pole vrcholu
	this.connected = connected;													//inicializuje typ grafu
	
	for (int i = 0; i < vertexes.length; i++) {									//pro jistotu pole vunulluje
    	vertexes[i] = null;
    }
  }
	
  	/**
  	 * Metoda pro ziskani Id vrcholu
  	 * @param key
  	 * @return
  	 */
  	public static int getId(int key) {
  		int pom = -1;
  		for (int i = 0; i < vertexCount; i++) {
  			if (vertexes[i].key == key) {
  				pom = i;
  			}
  		}
  		return pom;
  	}
	
  	
  	/**
  	 * Dijkstruv algoritmus
  	 * @param d matice delek (Integer.MAX_VALUE pokud hrana mezi uzly neexistuje)
  	 * @param from uzel ze ktereho se hledaji nejkratsi cesty
  	 * @return strom predchudcu (z ciloveho uzlu znaci cestu do uzlu from)
  	 */
  	public static int[] doDijkstra(int[][] d, int from) {
    Set<Integer> set = new HashSet<Integer>();											// vytvori prioritni frontu
    set.add(from);

    boolean[] closed = new boolean[d.length];											// vytvori pole urcujici zda je dany vrchol uz zpracovany
    int[] distances = new int[d.length];												// vytvori pole se vzdalenostmi/priority
    for (int i = 0; i < d.length; i++) {
        if (i != from) {
            distances[i] = Integer.MAX_VALUE;											// vsechny vrcholy krome pocatecniho maji vzdalenost inf
        } else {
            distances[i] = 0;															// pocatecni vrchol ma vzdalenost 0
        }
    }

    int[] predecessors = new int[d.length];												// vytvori seznam predchudcu, nas pozadovany vystup
    predecessors[from] = -1;

    while (!set.isEmpty()) {
        //najdi nejblizsi dosazitelny uzel
        int minDistance = Integer.MAX_VALUE;
        int node = -1;
        for(Integer i : set){
            if(distances[i] < minDistance){
                minDistance = distances[i];
                node = i;
            }
        }

        set.remove(node);
        closed[node] = true;
        
        //zkrat vzdalenosti
        for (int i = 0; i < d.length; i++) {
            //existuje tam hrana
            if (d[node][i] != Integer.MAX_VALUE) {
                if (!closed[i]) {
                    //cesta se zkrati
                    if (distances[node] + d[node][i] < distances[i]) {
                        distances[i] = distances[node] + (int)d[node][i];
                        predecessors[i] = node;
                        set.add(i); // prida uzel mezi kandidaty, pokud je jiz obsazen, nic se nestane
                    }
                }
            }
        }
    }
    return predecessors;
  	}
  	
  	
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
					// or not to keep track.
					//M[i][j] = min(M[i][j], M[i][k] + M[k][j]);
				}
			}
		}
		return m;
  	}
  	
  	public static int[][] getPathMatrix(){
  		return p;
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
	 * Metoda, která nalezne nejkratsi cestu z prave zpracovavaneho vrcholu
	 * do libovolneho zadaneho vrcholu
	 * @param target
	 * @return
	 */
  	public static ArrayList<Integer> getShortestPathTo(int source, int target, ArrayList<Vertex> entitiesV) {
  		ArrayList<Integer> path = new ArrayList<Integer>();
  		ArrayList<Integer> seq = new ArrayList<Integer>();
  		int stop = 1;
  		path.add(source);
  		
  		while (stop==1){                                     //pozn. source=radek, taget=sloupec
  			seq.add(target);
  			if (entitiesV.get(source).predecessor[target]==-1){         // osetreni chyby
  				stop=-1;
  			    System.out.println("Tohle se nemelo stat.");
  			}
  			else if (entitiesV.get(source).predecessor[target]==source){
  		    	for(int i=seq.size()-1; i != 0; i--){
  		    		path.add(seq.get(i));
  		    	}
  		    	stop=-1;
  		    }
  		    else{ 
  		    	target = entitiesV.get(source).predecessor[target];
  		    }
        }
  		// path.add(entitiesV.get(target).predecessor[source]);
  		
  		/* for (int vertex=target; vertex != source; vertex=entitiesV.get(source).getPredecessor()[source] ) {
            path.add(vertex);    
        } */  	
  		//Collection.reverse(path);           //vzhledem k neorentaci hran, asi nebude treba
  		return path;
  	}
 	
}