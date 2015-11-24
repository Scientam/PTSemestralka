import java.util.HashSet;
import java.util.Set;

/**
 * Trida pro reprezentaci grafu
 * @author Scien_000
 */
public class Graph {
	
	/** tato promenna uchovava vrcholy nasi grafove striktury*/
	private static Vertex[] vertexes;					
	/** tato promenna uchovava informaci o tom zda spoluvrcholy sousedi*/
	private int[][] adjMatrix;
	/** tato promenna uchovava konecny pocet vrcholu*/
	private static int vertexCount;	
	/** tato promenna uchovava pocet vytvorenych vrcholu*/
	private int vertexCreated = 0;	
	/** promenna uchovavajici informaci o to zda je graf orientovany */
	private boolean connected;			
	private static int[][] shortestPath;
	
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
  	 * Metoda pro vlozeni hrany grafu
  	 * @param start
  	 * @param end
  	 */
  	public	void addEdge(int start, int end) {
  		for (int m = 0; m < vertexCount; m++) {
  			if (vertexes[m].key == start) {
  				for (int n = 0; n < vertexCount; n++) {
  					if (vertexes[n].key == end) {
  						adjMatrix[m][n] = 1;
  						if(connected == false){
  							adjMatrix[n][m] = 1;
  						}
  					}
  				}
  			}
  		}
  	}
	
  	/**
  	 * Metoda pro vypsani matice sousednosti na obrazovku 
  	 */
  	public void printAdjMatrix() {
  		System.out.println("\nMatice sousednosti\n");
  			for (int m = 0; m < vertexCount; m++) {
  				for (int n = 0; n < vertexCount; n++) {
  					System.out.print(adjMatrix[m][n]+" ");
  				}
  				System.out.println();
  			}
  			System.out.println();
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
  	public static int[][] floydWarshall(int[][] d) {
  	    int[][] p = constructInitialMatixOfPredecessors(d);
  	    for (int k = 0; k < d.length; k++) {
  	        for (int i = 0; i < d.length; i++) {
  	            for (int j = 0; j < d.length; j++) {
  	                if (d[i][k] == Integer.MAX_VALUE || d[k][j] == Integer.MAX_VALUE) {
  	                    continue;                  
  	                }
  	                
  	                if (d[i][j] > d[i][k] + d[k][j]) {
  	                    d[i][j] = d[i][k] + d[k][j];
  	                    p[i][j] = p[k][j];
  	                }

  	            }
  	        }
  	    }
  	    return p;
  	}

  	/**
  	 * Constructs matrix P0
  	 * @param d matrix of lengths
  	 * @return P0
  	 */
  	private static int[][] constructInitialMatixOfPredecessors(int[][] d) {
  	    int[][] p = new int[d.length][d.length];
  	    for (int i = 0; i < d.length; i++) {
  	        for (int j = 0; j < d.length; j++) {
  	            if (d[i][j] != 0 && d[i][j] != Integer.MAX_VALUE) {
  	                p[i][j] = i;
  	            } else {
  	                p[i][j] = -1;
  	            }
  	        }
  	    }
  	    return p;
  	}


  	
}