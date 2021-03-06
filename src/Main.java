import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
/**
 * @authors Karel Sobehart, Petr Tobias
 * @version 1.0
 */
public class Main {
	
	static Scanner sc = new Scanner(System.in);
    //*********************************************************************************************promenne pro generovani**********************************************************************
	/** vytvori ArrayList do ktereho se budou ukladat objekty Vertex jako vrcholy grafu */
	static List<Vertex> entitiesV = new ArrayList<Vertex>();		
	/** vytvori promennou, urcujici zda se data budou generovat nebo nacitat ze souboru*/
	static int input;
	/** vytvori promennou ktera urcuje pocet central v galaxii */
	static int factoriesCount = 5;
	/** vytvori promennou ktera urcuje pocet sousedu kazde centraly */
	static int neighbourCountF = 20; 
	/** vytvori pronmenou ktera urcuje pocet planet v galaxii */
	static int planetsCount;
	/** vytvori promennou ktera urcuje pocet sousedu kazde planety(v zadani 5) */
	static int neighbourCountP = 5;
	/** vytvori promennou, ktera uchovava vzdalenosti mezi všemi vrcholy */
	static int[][] distance;
	/** vytvori promennou, ktera uchovava vzdalenosti mezi vrcholy mezi kterými existuje hrana */
	static int[][] realDistance;
	/** vytvori promennou, ktera uchovava nejkratsi vzdalenosti mezi vrcholy */
	static int[][] floydWarshall;
	/** vytvori promennou, ktera uchovava nejkratsi vzdalenosti(cesty) mezi vrcholy */
	static int[][] floydWarshallP;
	/** vytvori promennou, ktera uchovava posloupnosti vrcholu tvorici nejkratsi cestu z vrcholu u do vrcholu v */
	static List<Integer>[][] paths;
	//************************************************************************************************promenne pro simulaci*************************************************************************
	/** promenna urcujuci pocet dni po ktere pobezi simulace */
	static int maxD;
	/** promenna urcujici zda se bude zadavat objednavka rucne */
	static int choice;
	/** promenna urcijici jakouobjednavku prave vyrizujeme */
	static int orderID = 0;
	/** promenna urcujici kolik leku se ma objednat */
	static int orderDrugCount = 0;
	/** promenna reprezentujici objekt lod */
	static Starship starship;
	/** AL uchovavajici objekt planet */
	static List<Planet> planetL;
	/** AL uchovavajici objekt lod*/
	static List<Starship> starshipL;
	/** vytvori promennou, ktera slouzi pro generování náhodné veličiny*/
	static Random r = new Random();
	/** konstanta uchovavajici plny naklad*/
	static final int CAPACITY = 5000000;

	/**
	 * @param args
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {		
		System.out.println("Nacist ze souboru/Generovat nove hodnoty: 0/1");
		input = sc.nextInt();
		if(input == 1) {
			System.out.println("Zadej pocet planet");
			planetsCount = sc.nextInt();
			distance = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			realDistance = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			floydWarshall = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			floydWarshallP = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			paths = new ArrayList[factoriesCount+planetsCount][factoriesCount+planetsCount];
			
			/* zavola metodu, ktera vytvori centraly */
			entitiesV = DataGeneration.factoriesDistribution(factoriesCount, planetsCount, neighbourCountF, entitiesV);
			/* zavola metodu, ktera vytvori planety */
			entitiesV = DataGeneration.planetsDistribution(factoriesCount, planetsCount, neighbourCountP, entitiesV);	
			/* vytvori textovy soubor, do ktereho se vypisi parametry vrcholu */
			WorkWithFile.printVertex(entitiesV);
			/* spocte vzdalenosti mezi vsemi vrcholy a ulozi do matice */
			distance = DataGeneration.getDistance();												
			/* vytvori textovy soubor, do ktereho se vypise matice vzdalenosti */
			WorkWithFile.printMatrix(distance, entitiesV.size(), "Distance.txt");
			/* zavola metodu, ktera najde sousedy, tedy vytvori hrany */
			DataGeneration.neighbour(factoriesCount, neighbourCountF, neighbourCountP, entitiesV);  
			/* vytvori textovy soubor, do ktereho se vypisi hrany */
			WorkWithFile.printNeigbour(entitiesV, factoriesCount, planetsCount);
			/* spocte vzdalenosti mezi vsemi vrcholy mezi kterymi existuje hrana a ulozi do matice */
			realDistance = DataGeneration.realDistance(entitiesV, distance);
			WorkWithFile.printMatrix(realDistance, entitiesV.size(), "RealDistance.txt");
			/* zavola metodu, ktera najde nejkratsi cesty, tj. hodnoty */
			floydWarshall= Graph.floydWarshallM(realDistance, true); 	
			/* vytvori textovy soubor, do ktereho se vypisi nejkratsi cesty, tj. hodnoty */
			WorkWithFile.printMatrix(floydWarshall, entitiesV.size(), "FWShortestPath.txt");
			/* zavola metodu, ktera najde nejkratsi cesty, tj. predchudce */
			floydWarshallP = Graph.getPathMatrix(); 										
			/* vytvori textovy soubor, do ktereho se vypisi nejkratsi cesty, tj. predchudci */
			WorkWithFile.printMatrix(floydWarshallP, entitiesV.size(), "FWPath.txt");
			/* ulozeni pole predchudcu kazdeho vrcholu */
			DataGeneration.assignNeighbour(entitiesV, floydWarshallP);
			 new DrawMapG(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, paths);
			if (entitiesV.size()<=1000) {
				/* vytvoreni listu cest z kazdeho vrcholu do kazdeho */
			    DataGeneration.createPaths(paths, entitiesV);
				/* zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			   new DrawMap(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, paths);	
			}
			
	    //***********************************************************************************NACITANI_ZE_SOUBORU*************************************************************************************/
		} else { 	
			entitiesV = WorkWithFile.fromFileVrcholy(factoriesCount);
			WorkWithFile.fromFileHrany(entitiesV, factoriesCount, planetsCount);
			
			planetsCount = entitiesV.size() - factoriesCount;
			realDistance = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			floydWarshall = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			floydWarshallP = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			paths = new ArrayList[factoriesCount+planetsCount][factoriesCount+planetsCount];
			
			WorkWithFile.fromFileRealDistance(realDistance);
			WorkWithFile.fromFileFWShortestPath(floydWarshall);
			WorkWithFile.fromFileFWPath(floydWarshallP);
			DataGeneration.assignNeighbour(entitiesV, floydWarshallP);
			new DrawMapG(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, paths);
			if (entitiesV.size()<=1000) {
				DataGeneration.createPaths(paths, entitiesV);
				new DrawMap(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, paths);	
			}	
		}
	//*********************************************************************************SIMULACE********************************************************************************************************/	
		BufferedWriter bw = new BufferedWriter(new FileWriter("OrderFulfillment.txt"));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter("Order.txt"));
	    starship = null;
	    planetL = new ArrayList<>();
	    starshipL = new ArrayList<>();
	    int target = 0;
	   
	    /**
		 * Cyklus spousti simulaci kazdy den a generuje objednavky kazdych 30 dni. Pracuje se pouze s ArrayListem. Informace o objednavkach se ukladaji
		 * do textoveho souboru.
		 */

	    System.out.println("Zadej pocet dni po ktere bude bezet simulace (rok ma 360 dni): ");
	    maxD = sc.nextInt();
		for (int d = 0; d < maxD; d++) { 
			
			bw.write("---------------------------------------------------------------------------------------------");
			bw.newLine();
			bw.write("Zacal " + (d+1) + ". den.");
			bw.newLine();
			bw.write("---------------------------------------------------------------------------------------------");
			bw.newLine();
			
			if (d == 0) {
				planetL=DataGeneration.createPlanetL(entitiesV, planetL);
				starshipL=DataGeneration.createStarshipL(entitiesV, starshipL);
			}
				
		//****************************************************vytvoreni objednavek******************************************************************/

			if (d % 30 == 0){
				for (int i = 0; i < planetL.size(); i++){
					if (planetL.get(i).isStatus()){
						planetL.get(i).setPopulCount(planetL.get(i).getPopulCount() - planetL.get(i).getOrder());
					}
					if (planetL.get(i).getPopulCount() < 40000){
						planetL.get(i).setStatus(false);
						planetL.get(i).setTempStatus(false);
					}else{
					planetL.get(i).setStatus(true);
					planetL.get(i).setTempStatus(true);
					}
				}
				
				
				for (int i = 0; i < starshipL.size(); i++){
					starshipL.get(i).setIsInUse(true);
				}
			
			planetL=DataGeneration.createOrder(d, entitiesV, planetL);
			WorkWithFile.printOrder(d, entitiesV, planetL);
			}
			
		//*****************************************************vyrizovani objednacek****************************************************************/
			
			for (int i = 0; i < starshipL.size(); i++) {											

				if (d % 30 == 0) {
					
					starshipL.get(i).setTargetP(-1);
					for (int c = 0; c < 20; c++){
						target = entitiesV.get(starshipL.get(i).getNumF()).neighbour[c].getIndex();
						if ((target > factoriesCount) && (planetL.get(target).isTempStatus())){							 
							DataGeneration.nextTarget(target, factoriesCount, i, planetL, starshipL, distance);
							break;
						}
					}
					if (starshipL.get(i).getTargetP() == -1){
						for (int j = 5; j < entitiesV.size(); j++){
							for (int c = 0; c < 5; c++){
								target = entitiesV.get(j).neighbour[c].getIndex();
								if ((target > factoriesCount) && (planetL.get(target).isTempStatus())){
									DataGeneration.nextTarget(target, factoriesCount, i, planetL, starshipL, distance);
									break;
								}
							}
						if (starshipL.get(i).getTargetP() != -1){
							break;
						}
						}
					}
				}	

				/**
				 * Pokud lod doleti na planetu, vylozi zasoby podle objednavky.
				 * Pote se priradi dalsi planeta (pripadne centrala).
				 */
				if (starshipL.get(i).getDistance() <= 25) {
					starshipL.get(i).setDistance(0);
					if (starshipL.get(i).getTargetP() < 5){
						if (starshipL.get(i).getIsInUse()){
					DataGeneration.orderExecution(i, starshipL);
				}
				else{
					bw.write("Lod s id: " + starshipL.get(i).getId() + " dorazila na zakladnu a ceka na dalsi mesic.");
					bw.newLine();
					continue;
				}
					}						
					
					else{
						//DataGeneration.orderExecution2(i, planetL, starshipL);
						starshipL.get(i).setCapacity(starshipL.get(i).getCapacity() - planetL.get(starshipL.get(i).getTargetP()).getOrder());    // vylozeni nakladu
						planetL.get(starshipL.get(i).getTargetP()).setStatus(false);
						bw.write("Lodi s id: " + starshipL.get(i).getId() + " zbyva doletet: " + starshipL.get(i).getDistance());
						bw.newLine();
						bw.write("Lod s id: " + starshipL.get(i).getId() + " vylozila na planete "+starshipL.get(i).getTargetP()+", " + planetL.get(starshipL.get(i).getTargetP()).getOrder() + " jednotek nakladu.");
						bw.newLine();
					}
					
					if (starshipL.get(i).getTargetP() == -1){
						DataGeneration.returnShip(i, starshipL, distance);
						continue;
					}
					else{						
					starshipL.get(i).setSourceP(starshipL.get(i).getTargetP());
					}
					starshipL.get(i).setTargetP(-1);
					target = 0;
					for (int c = 0; c < 5; c++){
					target = entitiesV.get(starshipL.get(i).getSourceP()).neighbour[c].getIndex();
						if ((target > factoriesCount) && (planetL.get(target).isTempStatus())){
							DataGeneration.nextTarget(target, factoriesCount, i, planetL, starshipL, distance);
							break;
							
						}
					}
					if (starshipL.get(i).getTargetP() == -1){	
						for (int c = 0; c < 20; c++){
							target = entitiesV.get(starshipL.get(i).getNumF()).neighbour[c].getIndex();
							if ((target > factoriesCount) && (planetL.get(target).isTempStatus())){
								DataGeneration.nextTarget(target, factoriesCount, i, planetL, starshipL, distance);
								break;
							
							}
						}
						if (starshipL.get(i).getTargetP() == -1){
							for (int j = 5; j < entitiesV.size(); j++){
								for (int c = 0; c < 5; c++){
									target = entitiesV.get(j).neighbour[c].getIndex();
									if ((target > factoriesCount) && (planetL.get(target).isTempStatus())){
										DataGeneration.nextTarget(target, factoriesCount, i, planetL, starshipL, distance);
									break;
									}
								}
							if (starshipL.get(i).getTargetP() != -1){
								break;
							}
							}
						}
						if (starshipL.get(i).getTargetP() == -1){
							DataGeneration.returnShip(i, starshipL, distance);
						}
					}
					
					} else {
					starshipL.get(i).setDistance(starshipL.get(i).getDistance() - 25);					//vzdalenost se snizuje kazdy den o 25 LY
					bw.write("Lodi s id: " + starshipL.get(i).getId() + " zbyva doletet na planetu "+starshipL.get(i).getTargetP()+", "+ starshipL.get(i).getDistance()+" svetelnych let.");
					bw.newLine();
				}

				/**
				 * Pokud lod nema nalozen dostatek jednotek na pokryti objednavky dalsi planety, vraci se na centralu.
				 */

				if (starshipL.get(i).getSourceP() == -1 || starshipL.get(i).getTargetP() == -1){
					continue;
				}else{
					if (starshipL.get(i).getCapacity() < planetL.get(starshipL.get(i).getTargetP()).getOrder()){
					starshipL.get(i).setSourceP(starshipL.get(i).getTargetP());
					starshipL.get(i).setTargetP(starshipL.get(i).getNumF());																		
					starshipL.get(i).setDistance(distance[starshipL.get(i).getSourceP()][starshipL.get(i).getTargetP()]);
					bw.write("Lod s id: " + starshipL.get(i).getId() +  " se vratila na zakladnu  ve vzdalenosti: " + starshipL.get(i).getDistance() + " doplnit naklad.");
					bw.newLine();
					}
				}
			bw.newLine();
			}
//*****************************************************************************KONEC_SIMULACE******************************************************************************************************/	
		}
		bw.close();
		System.out.println("Program skoncil.");
	}

}