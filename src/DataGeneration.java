import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DataGeneration {
	static Scanner sc = new Scanner(System.in);
	/** pole se vzdalenostmi entit */
	private static int[][] distance;		
	/** x-ove a y-ove souradnice objektu */
	private static double xAxis, yAxis;
	/** pomocna promenna uchovavajici aktualni vzdalenost*/
	private static double actDist;
	/** */
	private static List<Neighbour> neigh = new ArrayList<Neighbour>();
	/** */
	private static Random r = new Random();
	/** */
	private static int danger;
	/** */
	
	 
	
	/**
	 * Tato metoda vytvori centraly na pozadovanych(vyhodnych) souradnicich
	 * @param factoriesCount
	 * @param planetsCount
	 * @param entity
	 * @param adjFactoriesCount
	 * @return
	 */
	 public static List<Vertex> factoriesDistribution(int factoriesCount, int planetsCount, int neighbourCountF, List<Vertex> entitiesV) {
		 distance = new int[planetsCount+factoriesCount][planetsCount+factoriesCount];     		//deklaruje velikost matice uchvavajici vzdalenost objektu
		 
		 for (int i = 0; i < factoriesCount; i++) {													// vytvori centraly rozlozene pravidelne na kruznici se stredem ve stredu souradneho systemu	  
			 xAxis = Math.floor(200*Math.cos(Math.toRadians(72*i))+400) ;                                  		// vypocte x-ovou souradnici
			 yAxis = Math.floor(-1*200*Math.sin(Math.toRadians(72*i))+400);                             		// vypocte y-ovou souradnici
			 Factory centrala = new Factory(i, xAxis, yAxis, neighbourCountF);              	// vytvori objekt centrala s pozadovanymi parametry
			 entitiesV.add(centrala);                                                         		// prida objekt centraly do AL entity
		  }
		
		 for (int i = 0; i < factoriesCount; i++) {	
			 for(int j = 0; j < factoriesCount; j++) {													// spocte zda  nove vytvarena planeta je dostatecne daleko od vsech vytvorenych entit
				actDist = Math.sqrt( Math.pow( (entitiesV.get(j).getXAxis() - entitiesV.get(i).getXAxis()), 2) + Math.pow((entitiesV.get(j).getYAxis() - entitiesV.get(i).getYAxis()), 2) );
				distance[i][j] = (int)actDist;											// matice distance je symetricka
		 		distance[j][i] = (int)actDist;		
			 }	
		 }
		 return entitiesV;
	  }
	 
	 
	 /**
	 * Generuje nahodne rozlozeni planet na zadanem uzemi.
	 * @param factoriesCount
	 * @param planetsCount
	 * @param entity
	 * @param neighbourCountP
	 * @return
	 * @throws IOException 
	 */
	 public static List<Vertex> planetsDistribution(int factoriesCount, int planetsCount,int neighbourCountP, List<Vertex> entitiesV) throws IOException {
		 int boundX = 800+2;
		 int boundY = 800+2;													   // nasteaveni mezi
		 ArrayList<Double> auxDist = new ArrayList<Double>();		   							   // pomocny ArrayList, ktery slouzi k ukladani vzdalenosti planet a nasledne kontroly splneni podminky min. vzdalenosti 2	
		 Random rX = new Random();
		 Random rY = new Random();
	
		 int i = factoriesCount;
		 while(i < factoriesCount+planetsCount) {																	// cyklus, ktery bezi, tak dlouho, dokud nejsou vytvoreny vsechny pozadovane planety
			 auxDist.clear();																// vycisti AL s pomocnymi vzdalenostmi, aby se nepletli s novymi(AL by se zvetoval do nekonecna)
			 if (i == factoriesCount) {
				 xAxis = 400;													    	// vygeneruje nahodne X, Y souradnice v zadanem rozmezi
		 		 yAxis = 400; 	
		 		 for(int j = 0; j < entitiesV.size(); j++) {												// spocte zda  nove vytvarena planeta je dostatecne daleko od vsech vytvorenych entit
		 			 actDist = Math.sqrt( Math.pow( (entitiesV.get(j).getXAxis() - xAxis), 2) + Math.pow((entitiesV.get(j).getYAxis() - yAxis), 2) );
			 		 auxDist.add(actDist);																// ulozi vzdalenost prave vytvarene planety od entity j do pomocneho AL
			 		 distance[i][j] = (int)actDist;											// matice distance je symetricka
			 		 distance[j][i] = (int)actDist;	 
		 		 }
			 } else {
				 xAxis = rX.nextInt(boundX);													    	// vygeneruje nahodne X, Y souradnice v zadanem rozmezi
		 		 yAxis = rY.nextInt(boundY); 	
		 		 for(int j = 0; j < entitiesV.size(); j++) {												// spocte zda  nove vytvarena planeta je dostatecne daleko od vsech vytvorenych entit
		 			 actDist = Math.sqrt( Math.pow( (entitiesV.get(j).getXAxis() - xAxis), 2) + Math.pow((entitiesV.get(j).getYAxis() - yAxis), 2) );
			 		 auxDist.add(actDist);																// ulozi vzdalenost prave vytvarene planety od entity j do pomocneho AL
			 		 distance[i][j] = (int)actDist;											// matice distance je symetricka
			 		 distance[j][i] = (int)actDist; 
		 		 }																			
		 	 }	
	 		 actDist = Collections.min(auxDist); 													// vybere nejmensi vzdalenost prave vytvarene planety a nejblizsi entity
	 		 if(actDist > 2) { 																		// overi, zda dana planeta je dostatecne daleko, pokud ano, tak ji vytvori a prida do AL entit
	 		 		Planet pl = new Planet(i, xAxis, yAxis, neighbourCountP);
	 		 		entitiesV.add(pl);
	 		 		i++;
	 		 }
		  }	
	 	  return entitiesV;
	  }
	 
	 
	 /**
	  * 
	  * @return
	  */
	 public static int[][] getDistance() {
		 return distance;
	 }
	 
	 
	 /**
	  * Metoda slouzici k nalezeni sousedu daneho vrcholu
	  * @param factoriesCount
	  * @param neighbourCountF
	  * @param neighbourCountP
	  * @param entitiesV
	 * @throws IOException 
	  */
	 public static List<Vertex> neighbour(int factoriesCount, int neighbourCountF, int neighbourCountP, List<Vertex> entitiesV) throws IOException {
		 for (int i = 0; i < entitiesV.size(); i++) {												//vytvori Al pomocnych objektu slozicich k hledani sousedu
			 neigh.add(new Neighbour(i, 100000));
		 }
		 
		 for (int i = 0; i < entitiesV.size(); i++) {			 
			 for (int j = 0; j < entitiesV.size(); j++) {
				 neigh.get(j).setDist(distance[i][j]);												// priradi i-temu prvku AL vzdalenost i-teho prvku od j-teho
				 neigh.get(j).setIndex(j);
			 }
	
			Collections.sort(neigh, new MyComparator());										// seradi AL entit podle vzdalenosti
	
			if(i < factoriesCount) {
				for (int j = 0; j < neighbourCountF; j++) {
					entitiesV.get(i).neighbour[j] = neigh.get(j+1).clone();								// priradi nejblizsich k(20) prvku(sousedu)
					danger = r.nextInt(5);
					if (danger == 4){entitiesV.get(i).neighbour[j].setDanger(true);	}
					else{ entitiesV.get(i).neighbour[j].setDanger(false); }
				}
			} else {
				for (int j = 0; j < neighbourCountP; j++) {		
					entitiesV.get(i).neighbour[j] = neigh.get(j+1).clone();								// priradi nejblizsich k(5) prvku(sousedu)
					danger = r.nextInt(5);
					if (danger == 4){entitiesV.get(i).neighbour[j].setDanger(true);	}
					else{ entitiesV.get(i).neighbour[j].setDanger(false); }
				}
			}
		}
		 return entitiesV;	 
	 } 
	 
	 
	 /**
	  * 
	  * @param entitiesV
	  * @param distance
	  * @return
	  */
	 public static int[][] realDistance(List<Vertex> entitiesV, int[][] distance) {
		 int[][] realDistance = new int[entitiesV.size()][entitiesV.size()];
		 
		 for (int i = 0; i < entitiesV.size(); i++) {
			 for (int j = 0; j < entitiesV.size(); j++) {															// vzdalenost z vrcholu do sebe sama je 0
				 if(i == j) {
					 realDistance[i][j] = 0;
					continue ;
				 }
				 
				 for(int k = 0; k < entitiesV.get(i).neighbour.length; k++) {
					 if (entitiesV.get(i).neighbour[k].getDist() == distance[i][j]) {			// .neighbour[k].index == d
						realDistance[i][j] = distance[i][j];
						break;
					}else{
						realDistance[i][j] = 10000;				// Integer.MAX_VALUE/2-100000
					}
				 }
			}
			
		}
		 return realDistance;
	 }
	 
	 
	 /**
	  * 
	  * @param entitiesV
	  * @param floydWarshallP
	  * @return
	  */
	 public static List<Vertex> assignNeighbour(List<Vertex> entitiesV, int[][] floydWarshallP) {
		 for (int i=0; i<floydWarshallP.length; i++) {
				entitiesV.get(i).predecessor = new int[entitiesV.size()];                    //urci velikost pole predchudcu(pocet planet+central)
				for (int j=0; j<floydWarshallP.length; j++) {
					entitiesV.get(i).predecessor[j]=floydWarshallP[i][j];
				}							
         }		 
		 return entitiesV;
	 }
	 
	 
	 /**
	  * 
	  * @param paths
	  * @param entitiesV
	  * @return
	  */
	 public static List<Integer>[][] createPaths(List<Integer>[][] paths, List<Vertex> entitiesV) {
		 for (int i = 0; i < paths.length; i++) {
		        for (int j = 0; j < paths.length; j++) {
		        	if (i!=j) {                                                             // abychom nehledali nejkratsi cestu z vrcholu u do vrcholu u
		        		paths[i][j] = Graph.getShortestPathTo(i, j, entitiesV);           
		        	}		        	
				}	
			}
		
		    /*
		    System.out.println("Cesta z vrcholu 5 do vrcholu 7: ");
		    for (int i = 0; i < paths[5][7].size(); i++) {
		    	System.out.print(paths[5][7].get(i)+" ");
			}*/
		 return paths;
	 }
	 
	 
	 /**
	  * 
	  * @param entitiesV
	  * @param planetL
	  * @return
	  */
	 public static List<Planet> createPlanetL (List<Vertex> entitiesV, List<Planet> planetL) {
		 Planet planet;
		 planetL = new ArrayList<Planet>();
		 for (int i = 0; i < 5; i++){
			 planet = new Planet(i, entitiesV.get(i).getXAxis(), entitiesV.get(i).getYAxis(), entitiesV.get(i).getNeighbourCount());
			 planetL.add(planet);
		 }
		 for (int i = 5; i < entitiesV.size(); i++){
				planet = (Planet) entitiesV.get(i);			//volani planety
				planetL.add(planet);
			} 
		 return planetL;
	 }
	 
	 public static List<Starship> createStarshipL (List<Vertex> entitiesV, List<Starship> starshipL) {
		 int factoryId;
		 Starship starship;
		 starshipL = new ArrayList<Starship>();
		 for (int i = 0; i < (entitiesV.size()-5)/100 * 40; i++){
			 	factoryId = entitiesV.get(r.nextInt(4)).getKey();                    													// urceni centraly, ktera obednavku vyridi
				starship = new Starship(i, 25, 5000000, factoryId, factoryId);     												    //volani lode, musi se doresit ID
				starshipL.add(starship);
			} 
		 return starshipL;
	 }
	 
	 
	 /**
	  * 
	  * @param day
	  * @param entitiesV
	  * @param planetL
	  * @return
	  */
	 public static List<Planet> createOrder (int day, int maxD, List<Vertex> entitiesV, List<Planet> planetL) {
		 int choice;
		 int orderID;
		 int orderDrugCount;
	
		 System.out.println("Chcete zadat vlastní objednavku? (0 - NE/1 - ANO): ");
		 choice = sc.nextInt();
		 while (choice == 1) {
			 System.out.println("Zadej objednavku ve tvaru (id_planety pocet_leku): ");
			 orderID = sc.nextInt();
			 orderDrugCount = sc.nextInt();
			 if (planetL.get(orderID).getAnswered()==false) {
				 planetL.get(orderID).setOrder(orderDrugCount);    // vytvori objednavku, jeji velikost zavisi na poctu obyvatel planety
				 planetL.get(orderID).setAnswered(true);
			 }
			 System.out.println("Chcete zadat vlastni objednavku? (0 - NE/1 - ANO): ");
			 choice = sc.nextInt();
		  } 
		  WorkWithFile.printOrder(day, maxD, entitiesV, planetL);
		 return planetL;
	 }
	 
	 public static List<Planet> orderExecution (int day, List<Starship> starshipL,List<Planet> planetL) {
		/* BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("OrderFulfillment.txt"));
			/**
			 * Pokud lod doleti na planetu, vylozi zasoby podle objednavky.
			 * Pote se priradi dalsi planeta (pripadne centrala).
			 
		 for (int i = 0; i < planetL.size(); i++) {
			if (starshipL.get(i).getDistance() <= 25.0) {
				starshipL.get(i).setDistance(0.0);																						 // lod doletela na planetu
				starshipL.get(i).setCapacity(starshipL.get(i).getCapacity() - planetL.get(starshipL.get(i).getTargetP()+5).getOrder());    // vylozeni nakladu
				bw.write("Lodi s id: " + starshipL.get(i).getId() + " zbyva doletet: " + starshipL.get(i).getDistance());
				bw.newLine();
				//duvod, proc se po vylozeni nevypisuje, kolik se vylozilo, protoze se hleda objednavka centraly, ktera neexistuje
				bw.write("Lod s id: " + starshipL.get(i).getId() + " vylozila na planete "+planetL.get(i).getId()+", " + planetL.get(starshipL.get(i).getTargetP()).getOrder() + " jednotek nakladu.");
				bw.newLine();
				starshipL.get(i).setSourceP(starshipL.get(i).getTargetP());																// lod se vraci na domovskou centralu						
				starshipL.get(i).setTargetP(starshipL.get(i).getNumF());			
			} else {
				starshipL.get(i).setDistance(starshipL.get(i).getDistance() - 25.0);					//vzdalenost se snizuje kazdy den o 25 LY
				bw.write("Lodi s id: " + starshipL.get(i).getId() + " zbyva doletet na planetu "+planetL.get(i).getId()+", "+ starshipL.get(i).getDistance()+" svìtelných let.");
				bw.newLine();
			}
			bw.newLine();
		 }	
		   bw.close();
		} catch (IOException e) { e.printStackTrace(); }	*/ 		 
		 return planetL;
	 }
	  
}
