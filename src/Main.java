import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
/**
 * 
 * @authors Karel Sobehart, Petr Tobias
 *
 */
public class Main {
	/**
	 * @version 0.0212.1619
	 */
	static Scanner sc = new Scanner(System.in);
    //*********************************************************************************************promenne pro generovani****************************************************
	/** vytvori ArrayList do ktereho se budou ukladat objekty Vertex jako vrcholy grafu */
	static ArrayList<Vertex> entitiesV = new ArrayList<Vertex>();		
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
	/** vytvori promennou, ktera uchovava vzdalenosti mezi vrcholy */
	static int[][] distance;
	static int[][] realDistance;
	/** vytvori promennou, ktera uchovava nejkratsi vzdalenosti mezi vrcholy */
	static int[][] floydWarshall;
	/** vytvori promennou, ktera uchovava nejkratsi vzdalenosti(cesty) mezi vrcholy */
	static int[][] floydWarshallP;
	/** vytvori promennou, ktera uchovava posloupnosti vrcholu tvorici nejkratsi cestu z vrcholu u do vrcholu v */
	static ArrayList<Integer>[][] paths;
	/** vytvori promennou, ktera slozi pro nacitani ze souboru */
	static int counter;
	//*********************************************************************promenne pro simulaci**************************************************************************************************
    //Dodelej prosim ty komenty a u simulace vic popisku, co k cemu slouzi
	/** */
	static Random r = new Random();
	/** */
	private static int factoryId;
	/** */
	//private static int starshipId;
	/** konstanta uchovavajici plny naklad*/
	static final int CAPACITY = 5000000;

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
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
			
			//*************************************************************************GENEROVANI_DAT***********************************************************************
			/** zavola metodu, ktera vytvori centraly */
			entitiesV = DataGeneration.factoriesDistribution(factoriesCount, planetsCount, neighbourCountF, entitiesV);
			/** zavola metodu, ktera vytvori planety */
			entitiesV = DataGeneration.planetsDistribution(factoriesCount, planetsCount, neighbourCountP, entitiesV);	
			
			/** vytvori textovy soubor, do ktereho se vypisi parametry vrcholu */
			BufferedWriter bw1 = new BufferedWriter(new FileWriter("Vertexes.txt"));				// BW na vypis vrcholu(tj. central a planet)do textaku  
			for (int i=0; i<entitiesV.size(); i++) {
				bw1.write(entitiesV.get(i).key+"\t"+entitiesV.get(i).xAxis+"\t"+entitiesV.get(i).yAxis+"\t"+entitiesV.get(i).neighbourCount+"\t"+entitiesV.get(i).color);
				bw1.newLine();							
			}
			bw1.close();
		
			/** spocte vzdalensoti mezi vsemi vrcholy a ulozi do matice */
			distance = DataGeneration.getDistance();												
			/** vytvori textovy soubor, do ktereho se vypise matice vzdalenosti */
			BufferedWriter bw2 = new BufferedWriter(new FileWriter("Distance.txt"));				
			for (int i = 0; i<entitiesV.size(); i++) {
				for (int j = 0; j<entitiesV.size(); j++) {
					bw2.write(Math.floor(distance[i][j])+"\t");
				}
				bw2.newLine();
			}
			bw2.close();
		
			/** zavola metodu, ktera najde sousedy, tedy vytvori hrany */
			DataGeneration.neighbour(factoriesCount, neighbourCountF, neighbourCountP, entitiesV);  
			/** vytvori textovy soubor, do ktereho se vypisi hrany */
			BufferedWriter bw3 = new BufferedWriter(new FileWriter("Edges.txt"));					
			for (int i = 0; i<entitiesV.size(); i++) {											    
				if (i<factoriesCount) {                                                             // zpracovava centraly
					for (int j = 0; j<neighbourCountF; j++) {
						bw3.write(entitiesV.get(i).neighbour[j].index+"\t");					
					}
				}else{
					for (int j = 0; j<neighbourCountP; j++) {                                       // zpracovava planety
						bw3.write(entitiesV.get(i).neighbour[j].index+"\t");	
					}	
				}
				bw3.newLine();
			}
			bw3.close();
			
			realDistance = DataGeneration.realDistance(entitiesV, distance);
			BufferedWriter bw9 = new BufferedWriter(new FileWriter("RealDistance.txt"));				
			for (int i = 0; i<realDistance.length; i++) {
				for (int j = 0; j<realDistance.length; j++) {
					bw9.write((int)Math.floor(realDistance[i][j])+"\t");
				}
				bw9.newLine();
			}
			bw9.close();
		
			//*********************************************************************************HLEDANI_NEJKRATSICH_CEST********************************************************
			/*Dijsktra */
			  for (int i = 0; i < floydWarshall.length; i++) {
				  floydWarshallP[i] = Graph.doDijkstra(realDistance, i);
			} 

			 
			/** zavola metodu, ktera najde nejkratsi cesty, tj. hodnoty */
			floydWarshall= Graph.floydWarshallM(realDistance, true); 	
			/*for (int i = 0; i < entitiesV.size()-1; i++) {
				floydWarshall = Graph.floydWarshallM(floydWarshall, false);
			}*/
			/** vytvori textovy soubor, do ktereho se vypisi nejkratsi cesty, tj. hodnoty */
			BufferedWriter bw5 = new BufferedWriter(new FileWriter("FWShortestPath.txt"));			
			for (int i = 0; i < floydWarshall.length; i++) {										
					for (int j = 0; j < floydWarshall.length; j++) {
						bw5.write(floydWarshall[i][j]+"\t");					
					}			
				bw5.newLine();
			}
			bw5.close();
			
			/** zavola metodu, ktera najde nejkratsi cesty, tj. predchudce */
			//floydWarshallP = Graph.getPathMatrix(); 										
			/** vytvori textovy soubor, do ktereho se vypisi nejkratsi cesty, tj. predchudci */
			BufferedWriter bw4 = new BufferedWriter(new FileWriter("FWPath.txt"));					
			for (int i = 0; i < floydWarshallP.length; i++) {												
				for (int j = 0; j < floydWarshallP.length; j++) {
					bw4.write(floydWarshallP[i][j]+"\t");					
				}			
				bw4.newLine();
			}
			bw4.close();
			
			// ulozeni pole predchudcu kazdeho vrcholu
			for (int i = 0; i < floydWarshallP.length; i++) {
				entitiesV.get(i).predecessor = new int[entitiesV.size()];                    //urci velikost pole predchudcu(pocet planet+central)
				for (int j = 0; j < floydWarshallP.length; j++) {
					entitiesV.get(i).predecessor[j]=floydWarshallP[i][j];
					//if(i==2){System.out.print(entitiesV.get(2).predecessor[j]+" ");}
					//if(i==7){System.out.print(entitiesV.get(7).predecessor[j]+" ");}
				}							
            }
			
			
			// vytvoreni listu cest z kazdeho vrcholu do kazdeho
		    for (int i = 0; i < paths.length; i++) {
		        for (int j = 0; j < paths.length; j++) {
		        	if (i!=j) {                                                             // abychom nehledali nejkratsi cestu z vrcholu u do vrcholu u
		        		paths[i][j] = Graph.getShortestPathTo(i, j, entitiesV);           
		        	}		        	
				}	
		        System.out.println("Uz jsem dodelal prvek i: "+i);
			}
		    System.out.println("Uz jsem za forem");
			
		    //********************************************************************************VYKRESLENI_DAT*****************************************************************************************/
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			new DrawMap(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, paths);
		
		//*******************************************************************************************************************************************************************************************/	
	    //***********************************************************************************NACITANI_ZE_SOUBORU*************************************************************************************/
		//*******************************************************************************************************************************************************************************************/	
		} else { 
			//*************************************************************************"GENEROVANI_DAT"***********************************************************************	
			counter = fromFileVrcholy();
			fromFileHrany();
			planetsCount = counter - factoriesCount;
			
			realDistance = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			floydWarshall = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			floydWarshallP = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			paths = new ArrayList[factoriesCount+planetsCount][factoriesCount+planetsCount];
			
			fromFileRealDistance();
			fromFileFWShortestPath();
			fromFileFWPath();
			//*********************************************************************************"HLEDANI_NEJKRATSICH_CEST"********************************************************
			
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			new DrawMap(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, paths);		
		}
		
	
	//*********************************************************************************SIMULACE********************************************************************************************************/	
		
	    BufferedWriter bw6 = new BufferedWriter(new FileWriter("Order.txt"));
	    Scanner sc2 = new Scanner(new File("Distance.txt"));
	    Scanner sc3 = new Scanner(System.in);
	    sc2.skip("0.0");
	    int production = 0;
	    Planet p = null;
	    Starship s = null;
	    ArrayList<Starship> starship = new ArrayList<>();
	    ArrayList<Planet> planet = new ArrayList<>();
	    
	    /**
		 * Cyklus spousti simulaci kazdy den a generuje objednavky kazdych 30 dni. Pracuje se pouze s ArrayListem. Informace o objednavkach se ukladaji
		 * do textoveho souboru.
		 */
	    System.out.println("Zadej pocet dni po ktere bude bezet simulace: ");
	    int maxD = sc3.nextInt();
		for (int d = 0; d < maxD; d++){ 	
			bw6.write("---------------------------------------------------------------------------------------------");
			bw6.newLine();
			if ((d % 30) == 0){
			bw6.write("Objednavky pro "+(d/30+1)+". mesic: ");
			bw6.newLine();
			bw6.write("---------------------------------------------------------------------------------------------");
			bw6.newLine();
			}
			//****************************************************vytvoreni objednavek******************************************************************/
			if (d % 30 == 0 && d != 0){
				System.out.println("Chcete zadat vlastni objednavku? (0 - NE/1 - ANO): ");
				int choice = sc3.nextInt();
				if (choice == 0){
					continue;
				}
				else{
					System.out.println("Zadej objednavku ve tvaru (id_planety pocet_leku): ");
					String order = sc3.next();
					String[] parseLine = order.split(" ");
					int orderID = Integer.parseInt(parseLine[0]);
					int orderDrugCount = Integer.parseInt(parseLine[1]);
					 planet.get(orderID).setOrder(orderDrugCount);    // vytvori objednavku, jeji velikost zavisi na poctu obyvatel planety
						bw6.write("Planeta s id: "+planet.get(orderID).getKey()+" objednava takovyto pocet leku: "+planet.get(orderID).getOrder());
						bw6.newLine();
				}
				
			}
			for (int i = 0; i < planetsCount; i++) {			                            //cyklus pobÃ¬Å¾Ã­ pro vÅ¡echny planety
				if ((d % 30) == 0){	
				p = (Planet) entitiesV.get(i+factoriesCount);			//volani planety
				planet.add(p);
			    production = planet.get(i).drugProduction(planet.get(i).getPopulCount());
			    planet.get(i).setOrder(planet.get(i).order(planet.get(i).getPopulCount(), production));    // vytvori objednavku, jeji velikost zavisi na poctu obyvatel planety
				bw6.write("Planeta s id: "+planet.get(i).getKey()+" objednava takovyto pocet leku: "+planet.get(i).getOrder());
				bw6.newLine();
				factoryId = entitiesV.get(r.nextInt(4)).getKey();                   // urceni centraly, ktera obednavku vyridi
				sc2.skip("\t");
				s = new Starship(i, 25, CAPACITY, factoryId);      //volani lode, musi se doresit ID
				starship.add(s);
				starship.get(i).setSourceP(factoryId);
				starship.get(i).setTargetP(planet.get(i).getId()-5);								//lodi se priradi id dalsi planety
				starship.get(i).setDistance(Double.parseDouble(sc2.next()));									//lodi se priradi vzdalenost, jakou ma uletet
			}
			//*****************************************************vyrizovani objednacek****************************************************************/
			
				// pokud bude cas, udelal bych, aby to bralo nejblizsi centralu
				/**
				 * Pokud ma lod id centraly, vrati se na ni a doplni zasoby.
				 * Po doplneni zasob se vrati id planety, na kterou puvodne smerovala.
				 */
				/*if (starship.get(i).getTargetP() == factoryId){
					starship.get(i).setDistance(starship.get(i).getDistance() - 25.0);					//vzdalenost se snizuje kazdy den o 25 LY
					bw6.write("Lodi s id: " + starship.get(i).getId() + " zbyva doletet: " + starship.get(i).getDistance());
					bw6.newLine();
					if (starship.get(i).getDistance() < 25.0){
						starship.get(i).setDistance(0.0);
						starship.get(i).setSourceP(starship.get(i).getTargetP());
						starship.get(i).setCapacity(CAPACITY);
						starship.get(i).setTargetP(starship.get(i).getSourceP());
					}
				}*/
				/**
				 * Pokud lod doleti na planetu, vylozi zasoby podle objednavky.
				 * Pote se priradi dalsi planeta (pripadne centrala).
				 */
				if (starship.get(i).getDistance() < 25.0){
					starship.get(i).setDistance(0.0);
					starship.get(i).setCapacity(starship.get(i).getCapacity() - planet.get(starship.get(i).getTargetP()).getOrder());
					bw6.write("Lodi s id: " + starship.get(i).getId() + " zbyva doletet: " + starship.get(i).getDistance());
					bw6.newLine();
					bw6.write("Lod s id: " + starship.get(i).getId() + " vylozila " + planet.get(starship.get(i).getTargetP()).getOrder() + " jednotek nakladu.");
					bw6.newLine();
					starship.get(i).setSourceP(starship.get(i).getTargetP());
					starship.get(i).setTargetP(factoryId);
				}
				else{
					/**
					 * Lodi se odpocitava 25 LY z celkove cesty kazdy den.
					 */
				starship.get(i).setDistance(starship.get(i).getDistance() - 25.0);					//vzdalenost se snizuje kazdy den o 25 LY
				bw6.write("Lodi s id: " + starship.get(i).getId() + " zbyva doletet: " + starship.get(i).getDistance());
				bw6.newLine();
				}
				// Pridal bych nejakou podminku na vzdalenost, aby to nebralo, planety na druhe strane galaxie
				/**
				 * Pokud lod nema nalozen dostatek jednotek na pokryti objednavky dalsi planety, vraci se na centralu.
				 */
				if (starship.get(i).getCapacity() < planet.get(starship.get(i).getTargetP()).getOrder()){
					starship.get(i).setSourceP(starship.get(i).getTargetP());
					starship.get(i).setTargetP(factoryId);
					//ulozit vzdalenost k centrale do Distance
																						}
				/**for (int j=0; j<planetsCount; j++) {
					if (i!=j && (entitiesV.get(j+factoriesCount).getOrder() < starship.getCapacity()) ) {
					    while (planets.get(i+1).getOrder() < starship.getCapacity()){
						    starship.setTargetP(planets.get(i+1).getKey());
						    //lod doleti na dalsi planetu
						    starship.setCapacity(starship.getCapacity() - planet.getOrder());
						} 
				    }	
				} */
			
			bw6.newLine();
			}
		   
							
							//int oneDayPath = s.getVel();			//lod za jeden den urazi 25 LY
							//Scanner sc2 = new Scanner(new File("seznamVzdalSeraz.txt"));
							//sc2.skip("Planeta c.1: ");
							//while(sc2.hasNextDouble()){
							
							//double nextPlanet = sc2.nextDouble(); 			//zatÃ­m to neÃ¨te to, co mÃ¡
							//System.out.println(nextPlanet);
							//if (nextPlanet > oneDayPath){
							//	nextPlanet = nextPlanet - oneDayPath;
							//	System.out.println("Lod je stale na ceste");
							//else{
							//podle ID planety se zmÃ¬nÃ­ starshipID
							//starshipID = entities.get(i).getId();
							//int cargo = s.getCap();
							//if(cargo > p.drugProduction(p.getPopulCount())){				//funkcni pouze pokud je populCount public ve tride Planet
							//cargo = cargo - p.enoughDrugProduction(p.getPopulCount());			//vylozi se naklad podle potreby
							//}
							//else{
								//navrat do centraly				//pozdeji proste poleti na dalsi planetu
								//starshipID = centralaID			//dokud simulace trva jeden den, lod na dalsi planety pokracovat nemuze, program zatim nepokracuje
		
							//System.out.println(naklad);
							//System.out.println(p.populCount);
							//}
							
						//}
					//}
					//}
						
						//while (Starship.getCap() > 0){
						/*if (new Path(false) != null){
							int pirates = r.nextInt(9);
								if (pirates == 8){
									//Lod se vraci do materske centraly
									//capacity = 0;
								}
						}*/
						
						//lod poleti na nejblizsi planetu
						//if Planet.planetStatus == true
						//na planete vylozi naklad podle Planet.drugProduction
						//else planeta se preskoci a nasleduje na dalsi nejblizsi planetu
						//}
						//}
			

			
	//*****************************************************************************KONEC_SIMULACE******************************************************************************************************/	
	
	   
	    }
    bw6.close();
    System.out.println("Program skoncil.");
	}
	
	/**
	 * Metoda slouzici pro nacitani dat typu Vertex
	 * @return
	 */
	public static int fromFileVrcholy() {
		BufferedReader br;
		int counter=0;
		try {
			br = new BufferedReader(new FileReader("Vertexes.txt"));
			String radka;			
			while((radka = br.readLine()) != null){
				String[] parseLine = radka.split("\t");
				counter++;
				
				if (counter < factoriesCount) {
					 Factory centrala = new Factory(Integer.valueOf(parseLine[0]), Double.valueOf(parseLine[1]), Double.valueOf(parseLine[2]), Integer.valueOf(parseLine[3]), parseLine[4].charAt(0));              	// vytvori objekt centrala s pozadovanymi parametry
					 entitiesV.add(centrala);    
				} else {
					 Planet pl = new Planet(Integer.valueOf(parseLine[0]), Double.valueOf(parseLine[1]), Double.valueOf(parseLine[2]), Integer.valueOf(parseLine[3]), parseLine[4].charAt(0));
	 		 		 entitiesV.add(pl);
				}
			}
			br.close();
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException a) {System.out.println("IOexception");}
		return counter;
	}
	
	/**
	 * Metoda slouzici pro nacitani dat typu Edges
	 */
	public static void fromFileHrany() {
		BufferedReader br;
		BufferedReader br2;
		int counter=0;
		try {
			br = new BufferedReader(new FileReader("Edges.txt"));
			br2 = new BufferedReader(new FileReader("Distance.txt"));
			String radka;		
			String radka2;	
			while((radka = br.readLine()) != null){
				radka2 = br2.readLine();
				String[] parseLine = radka.split("\t");
				String[] parseLine2 = radka2.split("\t");
				
				int index;
				double vzdalenost;
				if (counter < factoriesCount) {
					for (int i = 0; i < neighbourCountF; i++) {
						index = Integer.valueOf(parseLine[i]);
						vzdalenost = Double.valueOf(parseLine2[index]);
						entitiesV.get(counter).neighbour[i] = new Neighbour(index, vzdalenost);					
					}
						
				} else {
					for (int i = 0; i < neighbourCountP; i++) {
						index = Integer.valueOf(parseLine[i]);
						vzdalenost = Double.valueOf(parseLine2[(int)index]);
						entitiesV.get(counter).neighbour[i] = new Neighbour(index, vzdalenost);
					}
				}
				counter++;
			}
			br.close();
		} 
		catch (FileNotFoundException e) {System.out.println("File nanalezen");} 
		catch (IOException a) {System.out.println("IOexception");}
	}
	
	/**
	 * Metoda slouzici pro nacitani dat typu RealDistance
	 */
	public static void fromFileRealDistance() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("RealDistance.txt"));		
			String radka;	
			int counter=0;
			while ((radka = br.readLine()) != null) {
				String[] parseLine = radka.split("\t");
				
				for (int i = 0; i < parseLine.length; i++) {
					realDistance[counter][i] = Integer.valueOf(parseLine[i]);
				}
				counter++;				
			}	
			br.close();
		} 
		catch (FileNotFoundException e) {System.out.println("File nanalezen");} 
		catch (IOException a) {System.out.println("IOexception");}		
	}
	
	
	/**
	 * Metoda slouzici pro nacitani dat typu FWShortestPath
	 */
	public static void fromFileFWShortestPath() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("FWShortestPath.txt"));		
			String radka;	
			int counter=0;
			while ((radka = br.readLine()) != null) {
				String[] parseLine = radka.split("\t");
				
				for (int i = 0; i < parseLine.length; i++) {
					floydWarshall[counter][i] = Integer.valueOf(parseLine[i]);
				}
				counter++;				
			}	
			br.close();
		} 
		catch (FileNotFoundException e) {System.out.println("File nanalezen");} 
		catch (IOException a) {System.out.println("IOexception");}		
	}
	
	
	/**
	 *  Metoda slouzici pro nacitani dat typu FWPath
	 */
	public static void fromFileFWPath() {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("FWPath.txt"));		
			String radka;	
			int counter=0;
			while ((radka = br.readLine()) != null) {
				String[] parseLine = radka.split("\t");
				
				for (int i = 0; i < parseLine.length; i++) {
					floydWarshallP[counter][i] = Integer.valueOf(parseLine[i]);
				}
				counter++;				
			}	
			br.close();
		} 
		catch (FileNotFoundException e) {System.out.println("File nanalezen");} 
		catch (IOException a) {System.out.println("IOexception");}		
	}
}
