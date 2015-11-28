import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
	//verze 27.11 1223
	static Scanner sc = new Scanner(System.in);
    //*********************************************************************************************promenne pro generovani****************************************************
	/** vytvori ArrayList do ktereho se budou ukladat objekty Entita jako vrcholy grafu */
	static ArrayList<Vertex> entitiesV = new ArrayList<Vertex>();		
	/** vytvori ArrayList do ktereho se budou ukladat objekty Entita jako hrany grafu */
	ArrayList<Path> paths = new ArrayList<Path>();
	/** vytvori pole do ktereho se bude ukladat pst vrcholu na ceste z vrcholu i do vrcholu j*/
	ArrayList<Integer> edges = new ArrayList<Integer>();
	//ArrayList<Vertex> edges = new ArrayList<Vertex>();
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
	
	static int counter;
	//*********************************************************************promenne pro simulaci**************************************************************************************************
	static ArrayList<Planet> planets = new ArrayList<Planet>();
	static Random r = new Random();
	private static int centralaID;
	private static int starshipID;
	/** konstanta uchovavajici plny naklad*/
	static final int CAPACITY = 5000000;

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
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
			
		
			/** zavola metodu, ktera vytvori centraly */
			entitiesV = DataGeneration.factoriesDistribution(factoriesCount, planetsCount, neighbourCountF, entitiesV);
			/** zavola metodu, ktera vytvori planety */
			entitiesV = DataGeneration.planetsDistribution(factoriesCount, planetsCount, neighbourCountP, entitiesV);	
			
			BufferedWriter bw1 = new BufferedWriter(new FileWriter("Vertexes.txt"));				// BW na vypis vrcholu(tj. central a planet)do textaku  
			for (int i = 0; i < entitiesV.size(); i++) {
				bw1.write(entitiesV.get(i).key+"\t"+entitiesV.get(i).xAxis+"\t"+entitiesV.get(i).yAxis+"\t"+entitiesV.get(i).neighbourCount+"\t"+entitiesV.get(i).color);
				bw1.newLine();							
			}
			bw1.close();
		
			distance = DataGeneration.getDistance();												// vrati matici vzdalenosti
			BufferedWriter bw2 = new BufferedWriter(new FileWriter("Distance.txt"));				// BW na vypis vzdalnesti vrcholu(tj. central a planet)do textaku   
			for (int i = 0; i < entitiesV.size(); i++) {
				for (int j = 0; j < entitiesV.size(); j++) {
					bw2.write(Math.floor(distance[i][j])+"\t");
				}
				bw2.newLine();
			}
			bw2.close();
		
			DataGeneration.neighbour(factoriesCount, neighbourCountF, neighbourCountP, entitiesV);  // najde nejblizsi sousedy(tj. vytvori hrany)
			BufferedWriter bw3 = new BufferedWriter(new FileWriter("Edges.txt"));					// BW na vypis do textaku 
			for (int i = 0; i<entitiesV.size(); i++) {											// vypise vsechny cesty, prvni prvek pocatecni vrchol, ostatni koncove
				if (i<factoriesCount) {
					for (int j = 0; j<neighbourCountF; j++) {
						bw3.write(entitiesV.get(i).neighbour[j].index+"\t");					
					}
				}else{
					for (int j = 0; j<neighbourCountP; j++) {
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
			 
			floydWarshallP= Graph.floydWarshallP(distance); 										// najde nejkratsi cesty
			BufferedWriter bw6 = new BufferedWriter(new FileWriter("FWPath.txt"));					// BW na vypis do textaku 
			for (int i = 0; i < floydWarshallP.length; i++) {												
				for (int j = 0; j < floydWarshallP.length; j++) {
					bw6.write(floydWarshallP[i][j]+"\t");					
				}			
				bw6.newLine();
			}
			bw6.close();
			
			/* FloydWarshall */
			floydWarshall= Graph.floydWarshallM(distance); 									        // najde hodnotu nekratsich cest
			BufferedWriter bw4 = new BufferedWriter(new FileWriter("FWShortestPath.txt"));			// BW na vypis do textaku 
			for (int i = 0; i < floydWarshall.length; i++) {										
					for (int j = 0; j < floydWarshall.length; j++) {
						bw4.write(floydWarshall[i][j]+"\t");					
					}			
				bw4.newLine();
			}
			bw4.close();
			
			
			
			
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			new DrawMap(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV);
			
	    // negeneruje, ale vytvari pomoci txt	
		}else{ // negeneruje, ale vytvari pomoci txt
			counter = fromFileVrcholy();
			fromFileHrany();
			planetsCount = counter - factoriesCount;
			distance = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			new DrawMap(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV);		
		}
		
	
	//*********************************************************************************SIMULACE********************************************************************************************************/	
		
		BufferedWriter bw5 = new BufferedWriter(new FileWriter("Order.txt"));
		for (int d = 0; d < 1; d++){ 			//poèet dní, kdy simulace pobìží (pozdìji pùjde nastavit více dní uživatelem)
			bw5.write("Objednavky pro "+(d+1)+". den: ");
			bw5.newLine();
			bw5.write("---------------------------------------------------------------------------------------------");
			bw5.newLine();
						for (int i = 0; i < planetsCount; i++){			//cyklus pobìží pro všechny planety
							Planet p = new Planet(entitiesV.get(i).getKey(), entitiesV.get(i).getXAxis(), entitiesV.get(i).getYAxis(), entitiesV.get(i).getNeighbourCount(), entitiesV.get(i).color);			//volani planety
							p.setOrder(p.order(p.getPopulCount(), p.drugProduction(p.getPopulCount())));
							planets.add(p);
							bw5.write("Planeta s id: "+p.getId()+" objednava takovyto pocet leku: "+planets.get(i).getOrder());
							bw5.newLine();
						}
						bw5.close();	
						for (int i = 0; i < planets.size(); i++){	
							
							centralaID = entitiesV.get(r.nextInt(4)).getKey();
							Starship s = new Starship(i, 25, CAPACITY, centralaID);      //volani lode, musi se doresit ID
							s.setTargetP(planets.get(i).getKey());
							//lod doleti na planetu
							s.setCapacity(s.getCap() - planets.get(i).getOrder());
							
								while (planets.get(i+1).getOrder() < s.getCap()){
									s.setTargetP(planets.get(i+1).getKey());
									//lod doleti na dalsi planetu
									s.setCapacity(s.getCap() - planets.get(i).getOrder());
								}
							
							
							s.setTargetP(centralaID);
							//lod doleti do centraly
							s.setCapacity(CAPACITY);
						}
							
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
	 * 
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
	 * 
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
