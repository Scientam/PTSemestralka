import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Data {
	/** pole se vzdalenostmi entit */
	private static double[][] distance;		
	/** pole se n nejmensimi vzdalenostmi mezi centralou a jejimi sousedy */
	private static double[][] adjDist;				
	/** pole s n Id nejblizsich sousedu centraly */
	private static int[][] adjId;
	/** x-ove a y-ove souradnice objektu */
	private static double xAxis, yAxis;
	/** pomocna promenna uchovavajici aktualni vzdalenost*/
	private static double actDist = 0;
	private static ArrayList<Neighbour> neighbour = new ArrayList<Neighbour>();
	 
	
	/**
	 * Tato metoda vytvori centraly na pozadovanych(vyhodnych) souradnicich
	 * @param factoriesCount
	 * @param planetsCount
	 * @param entity
	 * @param adjFactoriesCount
	 * @return
	 */
	 public static ArrayList<Vertex> factoriesDistribution(int factoriesCount, int planetsCount, int neighbourCountF, ArrayList<Vertex> entitiesV) {
		 distance = new double[planetsCount+factoriesCount][planetsCount+factoriesCount];     		//deklaruje velikost matice uchvavajici vzdalenost objektu
		 
		 for (int i = 0; i < factoriesCount; i++) {													// vytvori centraly rozlozene pravidelne na kruznici se stredem ve stredu souradneho systemu	  
			 xAxis = Math.floor(200*Math.cos(Math.toRadians(72*i))+400) ;                                  		// vypocte x-ovou souradnici
			 yAxis = Math.floor((-1*200*Math.sin(Math.toRadians(72*i)))+400);                             		// vypocte y-ovou souradnici
			 Factory centrala = new Factory(i, xAxis, yAxis, neighbourCountF, 'B');              	// vytvori objekt centrala s pozadovanymi parametry
			 entitiesV.add(centrala);                                                         		// prida objekt centraly do AL entity
		  }
		
		 for (int i = 0; i < factoriesCount; i++) {	
			 for(int j = 0; j < factoriesCount; j++) {													// spocte zda  nove vytvarena planeta je dostatecne daleko od vsech vytvorenych entit
				actDist = Math.sqrt( Math.pow( (entitiesV.get(j).getXAxis() - entitiesV.get(i).getXAxis()), 2) + Math.pow((entitiesV.get(j).getYAxis() - entitiesV.get(i).getYAxis()), 2) );
				distance[i][j] = actDist;											// matice distance je symetricka
		 		distance[j][i] = actDist;
				/*if (i != j) {
					entitiesV.get(i).neighbour[j] = entitiesV.get(j);									// centrale i priradi za sousedy vsechny ostatni centraly	
				}	*/ 			
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
	 public static ArrayList<Vertex> planetsDistribution(int factoriesCount, int planetsCount,int neighbourCountP, ArrayList<Vertex> entitiesV) throws IOException {
		 int boundX = 800+2,  boundY = 800+2;													   // nasteaveni mezi
		 ArrayList<Double> auxDist = new ArrayList<Double>();		   							   // pomocny ArrayList, ktery slouzi k ukladani vzdalenosti planet a nasledne kontroly splneni podminky min. vzdalenosti 2	
		 Random rX = new Random(), rY = new Random();
	
		 int i = factoriesCount;
		 while(i < factoriesCount+planetsCount) {																	// cyklus, ktery bezi, tak dlouho, dokud nejsou vytvoreny vsechny pozadovane planety
			 auxDist.clear();																		// vycisti AL s pomocnymi vzdalenostmi, aby se nepletli s novymi(AL by se zvetoval do nekonecna)
			 xAxis = rX.nextInt(boundX);													    	// vygeneruje nahodne X, Y souradnice v zadanem rozmezi
	 		 yAxis = rY.nextInt(boundY); 	
	 		 for(int j = 0; j < entitiesV.size(); j++) {												// spocte zda  nove vytvarena planeta je dostatecne daleko od vsech vytvorenych entit
	 			 actDist = Math.sqrt( Math.pow( (entitiesV.get(j).getXAxis() - xAxis), 2) + Math.pow((entitiesV.get(j).getYAxis() - yAxis), 2) );
		 		 auxDist.add(actDist);																// ulozi vzdalenost prave vytvarene planety od entity j do pomocneho AL
		 		 distance[i][j] = actDist;											// matice distance je symetricka
		 		 distance[j][i] = actDist;	
		 	 }	
	 		 actDist = Collections.min(auxDist); 													// vybere nejmensi vzdalenost prave vytvarene planety a nejblizsi entity
	 		 if(actDist > 2) { 																		// overi, zda dana planeta je dostatecne daleko, pokud ano, tak ji vytvori a prida do AL entit
	 		 		Planet pl = new Planet(i, xAxis, yAxis, neighbourCountP, 'B');
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
	 public static double[][] getDistance() {
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
	 @SuppressWarnings("unchecked")
	 public static ArrayList<Vertex> neighbour(int factoriesCount, int neighbourCountF, int neighbourCountP, ArrayList<Vertex> entitiesV) throws IOException {
		 for (int i = 0; i < entitiesV.size(); i++) {												//vytvori Al pomocnych objektu slozicich k hledani sousedu
			 neighbour.add(new Neighbour(i, 100000));
		 }
		 
		 for (int i = 0; i < entitiesV.size(); i++) {			 
			 for (int j = 0; j < entitiesV.size(); j++) {
				 neighbour.get(j).dist=distance[i][j];												// priradi i-temu prvku AL vzdalenost i-teho prvku od j-teho
				 neighbour.get(j).index=j;
			 }	
			 if (i==5) {
			 	System.out.println("Kontrola neserazenosti");
			 	for (int j = 0; j < entitiesV.size(); j++) {
			 		System.out.println(neighbour.get(j).dist+" "+neighbour.get(j).index);
			 	}
			 }
			 Collections.sort(neighbour, new MyComparator());										// seradi AL entit podle vzdalenosti
			 if (i==5) {
				 System.out.println();
				 System.out.println("Kontrola serazenosti");
				 for (int j = 0; j < entitiesV.size(); j++) {
					 System.out.println(neighbour.get(j).dist+" "+neighbour.get(j).index);
				 }
			 }
	
			if(i < factoriesCount) {
				for (int j = 0; j < neighbourCountF; j++) {
					entitiesV.get(i).neighbour[j] = neighbour.get(j+1).clone();								// priradi nejblizsich k(20) prvku(sousedu)
				}
			} else {
				for (int j = 0; j < neighbourCountP; j++) {		
					if (i==5) {
						System.out.println("j je: "+j+" a vzdalenost je: "+neighbour.get(j+1).dist+" a index je: "+neighbour.get(j+1).index);
					}
					entitiesV.get(i).neighbour[j] = neighbour.get(j+1).clone();								// priradi nejblizsich k(5) prvku(sousedu)
				}
			}
		}
		 return entitiesV;	 
	 } 
	 
}


