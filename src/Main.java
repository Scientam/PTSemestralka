import java.io.BufferedReader;
import java.io.BufferedWriter;
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
	//verze 27.11 1814
	static Scanner sc = new Scanner(System.in);
    //*********************************************************************************************promenne pro generovani****************************************************
	/** vytvori ArrayList do ktereho se budou ukladat objekty Entita jako vrcholy grafu */
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
	private static int centralaID;
	/** */
	private static int starshipID;
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
			floydWarshall = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			floydWarshallP = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			paths = new ArrayList[factoriesCount+planetsCount][factoriesCount+planetsCount];
			
		
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
		
			/*Dijsktra 
			  for (int i = 0; i < floydWarshall.length; i++) {
				  floydWarshall[i] = Graph.doDijkstra(distance, i);
			}*/
			 
			/** zavola metodu, ktera najde nejkratsi cesty, tj. predchudce */
			floydWarshallP = Graph.floydWarshallP(distance); 										
			/** vytvori textovy soubor, do ktereho se vypisi nejkratsi cesty, tj. predchudci */
			BufferedWriter bw4 = new BufferedWriter(new FileWriter("FWPath.txt"));					
			for (int i = 0; i < floydWarshallP.length; i++) {												
				for (int j = 0; j < floydWarshallP.length; j++) {
					bw4.write(floydWarshallP[i][j]+"\t");					
				}			
				bw4.newLine();
			}
			bw4.close();
			
			/** zavola metodu, ktera najde nejkratsi cesty, tj. hodnoty */
			floydWarshall= Graph.floydWarshallM(distance); 									       
			/** vytvori textovy soubor, do ktereho se vypisi nejkratsi cesty, tj. hodnoty */
			BufferedWriter bw5 = new BufferedWriter(new FileWriter("FWShortestPath.txt"));			
			for (int i = 0; i < floydWarshall.length; i++) {										
					for (int j = 0; j < floydWarshall.length; j++) {
						bw5.write(floydWarshall[i][j]+"\t");					
					}			
				bw5.newLine();
			}
			bw5.close();
			
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
		   /* for (int i = 0; i < paths.length; i++) {
		        for (int j = 0; j < paths.length; j++) {		        	
		        	paths[i][j] = Graph.getShoortestPathTo(i, j, entitiesV);                             //OutOfMemoryError
				}	
		        System.out.print("Uz jse dodelal prvek: "+i);
			}*/
			
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			new DrawMap(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV);
			
	    // negeneruje, ale vytvari pomoci txt	
		}else{ 
			counter = fromFileVrcholy();
			fromFileHrany();
			planetsCount = counter - factoriesCount;
			distance = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			new DrawMap(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV);		
		}
		
	
	//*********************************************************************************SIMULACE********************************************************************************************************/	
		
	    BufferedWriter bw6 = new BufferedWriter(new FileWriter("Order.txt"));
		for (int d = 0; d < 1; d++){ 			//poèet dní, kdy simulace pobìží (pozdìji pùjde nastavit více dní uživatelem)
			bw6.write("Objednavky pro "+(d+1)+". den: ");
			bw6.newLine();
			bw6.write("---------------------------------------------------------------------------------------------");
			bw6.newLine();
			
			//****************************************************vytvoreni objednavek******************************************************************/
			for (int i=0; i<planetsCount; i++) {			                            //cyklus pobìží pro všechny planety
			    Planet planet = (Planet) entitiesV.get(i+factoriesCount);			//volani planety							
			    planet.setOrder(planet.order(planet.getPopulCount(), planet.drugProduction(planet.getPopulCount())));    // vytvori objednavku, jeji velikost zavisi na poctu obyvatel planety
				bw6.write("Planeta s id: "+planet.getId()+" objednava takovyto pocet leku: "+planet.getOrder());
				bw6.newLine();
					
			//*****************************************************vyrizovani objednacek****************************************************************/
			
				// pokud bude cas, udelal bych, aby to bralo nejbzsi centralu
				centralaID = entitiesV.get(r.nextInt(4)).getKey();                   // urceni centraly, ktera obednavku vyridi
				Starship starship = new Starship(i, 25, CAPACITY, centralaID);      //volani lode, musi se doresit ID
				starship.setTargetP(planet.getKey());
				//lod doleti na planetu
				starship.setCapacity(starship.getCapacity() - planet.getOrder());
				// Pridal bych nehjakou podminku na vzdalenost, aby to nebralo, planety na druhe strane galaxie
				
				/**for (int j=0; j<planetsCount; j++) {
					if (i!=j && (entitiesV.get(j+factoriesCount).getOrder() < starship.getCapacity()) ) {
					    while (planets.get(i+1).getOrder() < starship.getCapacity()){
						    starship.setTargetP(planets.get(i+1).getKey());
						    //lod doleti na dalsi planetu
						    starship.setCapacity(starship.getCapacity() - planet.getOrder());
						} 
				    }	
				} */
				
				starship.setTargetP(centralaID);
				starship.setCapacity(CAPACITY);                                         //lod doleti do centraly, tj. doplni zasoby
			}
		    bw6.close();
							
							//int oneDayPath = s.getVel();			//lod za jeden den urazi 25 LY
							//Scanner sc2 = new Scanner(new File("seznamVzdalSeraz.txt"));
							//sc2.skip("Planeta c.1: ");
							//while(sc2.hasNextDouble()){
							
							//double nextPlanet = sc2.nextDouble(); 			//zatím to neète to, co má
							//System.out.println(nextPlanet);
							//if (nextPlanet > oneDayPath){
							//	nextPlanet = nextPlanet - oneDayPath;
							//	System.out.println("Lod je stale na ceste");
							//else{
							//podle ID planety se zmìní starshipID
							//starshipID = entities.get(i).getId();
							//int cargo = s.getCap();
							//if(cargo > p.drugProduction(p.getPopulCount())){				//funkcni pouze pokud je populCount public ve tride Planet
							//cargo = cargo - p.enoughDrugProduction(p.getPopulCount());			//vylozi se naklad podle potreby
							//}
							//else{
								//navrat do centraly				//pozdeji proste poleti na dalsi planetu
								//starshipID = centralaID			//dokud simulace trva jeden den, lod na dalsi planety pokracovat nemuze, program zatim nepokracuje
							}
		
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
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException a) {
		}
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
						//entitiesV.get(counter).neighbour[i].index = index;
						//entitiesV.get(counter).neighbour[i].dist = vzdalenost;					
					}
						
				} else {
					for (int i = 0; i < neighbourCountP; i++) {
						index = Integer.valueOf(parseLine[i]);
						vzdalenost = Double.valueOf(parseLine2[(int)index]);
						entitiesV.get(counter).neighbour[i] = new Neighbour(index, vzdalenost);
						//System.out.println("nacet jsem: " + index + " vzdal : " + vzdalenost);
					}
				}
				counter++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("File nanalezen");
		} catch (IOException a) {
			System.out.println("IOexception");
		}
	}
}
