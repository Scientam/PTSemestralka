import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
/**
 * @authors Karel Sobehart, Petr Tobias
 * @version 0.0512.1210
 */
public class Main {
	
	static Scanner sc = new Scanner(System.in);
    //*********************************************************************************************promenne pro generovani**********************************************************************
	/** vytvori ArrayList do ktereho se budou ukladat objekty Vertex jako vrcholy grafu */
	static List<Vertex> entitiesV = new ArrayList<Vertex>();		
	/** vytvori promennou, urcujici zda se data budou generovat nebo nacitat ze souboru*/
	static int input;
	/** vytvori promenou ktera urcuje pocet central v galaxii */
	static int factoriesCount = 5;
	/** vytvori promenou ktera urcuje pocet sousedu kazde centraly */
	static int neighbourCountF = 20; 
	/** vytvori promenou ktera urcuje pocet planet v galaxii */
	static int planetsCount;
	/** vytvori promenou ktera urcuje pocet sousedu kazde planety(v zadani 5) */
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
    //Dodelej prosim ty komenty a u simulace vic popisku, co k cemu slouzi
	/** */
	static int maxD;
	/** */
	static int choice;
	/** */
	static int orderID = 0;
	/** */
	static int orderDrugCount = 0;
	/** */
	static Starship starship;
	/** */
	static List<Planet> planetL;
	/** */
	static List<Starship> starshipL;
	/** vytvori promennou, ktera slouzi pro generování náhodné veličiny*/
	static Random r = new Random();
	/** vytvori promennou, ktera slouzi k identifikovani centraly */
	private static int factoryId;
	/** */
	//private static int starshipId;
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
			
			/** zavola metodu, ktera vytvori centraly */
			entitiesV = DataGeneration.factoriesDistribution(factoriesCount, planetsCount, neighbourCountF, entitiesV);
			/** zavola metodu, ktera vytvori planety */
			entitiesV = DataGeneration.planetsDistribution(factoriesCount, planetsCount, neighbourCountP, entitiesV);	
			/** vytvori textovy soubor, do ktereho se vypisi parametry vrcholu */
			WorkWithFile.printVertex(entitiesV);
			/** spocte vzdalenosti mezi vsemi vrcholy a ulozi do matice */
			distance = DataGeneration.getDistance();												
			/** vytvori textovy soubor, do ktereho se vypise matice vzdalenosti */
			WorkWithFile.printMatrix(distance, entitiesV.size(), "Distance.txt");
			/** zavola metodu, ktera najde sousedy, tedy vytvori hrany */
			DataGeneration.neighbour(factoriesCount, neighbourCountF, neighbourCountP, entitiesV);  
			/** vytvori textovy soubor, do ktereho se vypisi hrany */
			WorkWithFile.printNeigbour(entitiesV, factoriesCount, planetsCount);
			/** spocte vzdalenosti mezi vsemi vrcholy mezi kterymi existuje hrana a ulozi do matice */
			realDistance = DataGeneration.realDistance(entitiesV, distance);
			WorkWithFile.printMatrix(realDistance, entitiesV.size(), "RealDistance.txt");
			/** zavola metodu, ktera najde nejkratsi cesty, tj. hodnoty */
			floydWarshall= Graph.floydWarshallM(realDistance, true); 	
			/** vytvori textovy soubor, do ktereho se vypisi nejkratsi cesty, tj. hodnoty */
			WorkWithFile.printMatrix(floydWarshall, entitiesV.size(), "FWShortestPath.txt");
			/** zavola metodu, ktera najde nejkratsi cesty, tj. predchudce */
			floydWarshallP = Graph.getPathMatrix(); 										
			/** vytvori textovy soubor, do ktereho se vypisi nejkratsi cesty, tj. predchudci */
			WorkWithFile.printMatrix(floydWarshallP, entitiesV.size(), "FWPath.txt");
			/** ulozeni pole predchudcu kazdeho vrcholu */
			DataGeneration.assignNeighbour(entitiesV, floydWarshallP);
			/** vytvoreni listu cest z kazdeho vrcholu do kazdeho */
		    DataGeneration.createPaths(paths, entitiesV);
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
		    new DrawMapG(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, paths);
			new DrawMap(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, paths);	
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
			DataGeneration.createPaths(paths, entitiesV);
			new DrawMapG(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, paths);
			new DrawMap(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, paths);		
		}
	//*********************************************************************************SIMULACE********************************************************************************************************/	
		 	BufferedWriter bw = new BufferedWriter(new FileWriter("OrderFulfillment.txt"));
		    starship = null;
		    starshipL = new ArrayList<>();		 
		   
		    /**
			 * Cyklus spousti simulaci kazdy den a generuje objednavky kazdych 30 dni. Pracuje se pouze s ArrayListem. Informace o objednavkach se ukladaji
			 * do textoveho souboru.
			 */
		    System.out.println("Zadej pocet dni po ktere bude bezet simulace (rok ma 360 dni): ");
		    maxD = sc.nextInt();
			for (int d = 0; d < maxD; d++) { 
				if (d == 0) {planetL=DataGeneration.createPlanetL(entitiesV, planetL);}														// id planet jde od 5,...,entitiesV.size()
			//****************************************************vytvoreni objednavek******************************************************************/
				if (d % 30 == 0) {
					System.out.println("Chcete zadat vlastni objednavku? (0 - NE/1 - ANO): ");
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
					WorkWithFile.printOrder(d, entitiesV, planetL);
				}
			//*****************************************************vyrizovani objednacek****************************************************************/
				
				for (int i = 0; i < planetsCount; i++) {
					if (d == 0) {
					factoryId = entitiesV.get(r.nextInt(4)).getKey();                    													// urceni centraly, ktera obednavku vyridi
					starship = new Starship(i, 25, CAPACITY, factoryId, factoryId);     												    //volani lode, musi se doresit ID
					starshipL.add(starship);
					}
					if (d % 30 == 0) {
					starshipL.get(i).setTargetP(planetL.get(i+factoriesCount).getId());																	//lodi se priradi id dalsi planety
					starshipL.get(i).setDistance(floydWarshall[factoryId][starshipL.get(i).getTargetP()]);					//lodi se priradi vzdalenost, jakou ma uletet
					}
					
					/**
					 * Pokud lod doleti na planetu, vylozi zasoby podle objednavky.
					 * Pote se priradi dalsi planeta (pripadne centrala).
					 */
					if (starshipL.get(i).getDistance() <= 25.0) {
						starshipL.get(i).setDistance(0.0);																						 // lod doletela na planetu
						starshipL.get(i).setCapacity(starshipL.get(i).getCapacity() - planetL.get(starshipL.get(i).getTargetP()+factoriesCount).getOrder());    // vylozeni nakladu
						bw.write("Lodi s id: " + starshipL.get(i).getId() + " zbyva doletet: " + starshipL.get(i).getDistance());
						bw.newLine();
						//duvod, proc se po vylozeni nevypisuje, kolik se vylozilo, protoze se hleda objednavka centraly, ktera neexistuje
						bw.write("Lod s id: " + starshipL.get(i).getId() + " vylozila na planete "+planetL.get(i).getId()+", " + planetL.get(starshipL.get(i).getTargetP()).getOrder() + " jednotek nakladu.");
						bw.newLine();
						starshipL.get(i).setSourceP(starshipL.get(i).getTargetP());																// lod se vraci na domovskou centralu						
						starshipL.get(i).setTargetP(factoryId);			
					} else {
						starshipL.get(i).setDistance(starshipL.get(i).getDistance() - 25.0);					//vzdalenost se snizuje kazdy den o 25 LY
						bw.write("Lodi s id: " + starshipL.get(i).getId() + " zbyva doletet na planetu "+planetL.get(i).getId()+", "+ starshipL.get(i).getDistance()+" světelných let.");
						bw.newLine();
					}				
					// Pridal bych nejakou podminku na vzdalenost, aby to nebralo, planety na druhe strane galaxie
					/**
					 * Pokud lod nema nalozen dostatek jednotek na pokryti objednavky dalsi planety, vraci se na centralu.
					 */
					
					
					/** if (starshipL.get(i).getCapacity() < planetL.get(starshipL.get(i).getTargetP()).getOrder()){
						starshipL.get(i).setSourceP(starshipL.get(i).getTargetP());
						starshipL.get(i).setTargetP(factoryId);																		*******
						//ulozit vzdalenost k centrale do Distance																	*******	
																							}*/
					/**for (int j=0; j<planetsCount; j++) {
						if (i!=j && (entitiesV.get(j+factoriesCount).getOrder() < starship.getCapacity()) ) {
						    while (planets.get(i+1).getOrder() < starship.getCapacity()){
							    starship.setTargetP(planets.get(i+1).getKey());
							    //lod doleti na dalsi planetu
							    starship.setCapacity(starship.getCapacity() - planet.getOrder());
							} 
					    }	
					} */
					
					//starship.get(i).setTargetP(floydWarshall[planet.get(i).getId()][planet.get(i).getId()]);
				
				bw.newLine();
				}		
	//*****************************************************************************KONEC_SIMULACE******************************************************************************************************/	
			}
    bw.close();
    System.out.println("Program skoncil.");
	}

}
