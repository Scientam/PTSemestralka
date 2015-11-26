import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.PlatformLoggingMXBean;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import org.omg.Messaging.SyncScopeHelper;

public class Main {
	
	private static int centralaID;
	private static int starshipID;
	
	static Scanner sc = new Scanner(System.in);
	static Random r = new Random();
	/** vytvori ArrayList do ktereho se budou ukladat objekty Entita jako vrcholy grafu */
	static ArrayList<Vertex> entitiesV = new ArrayList<Vertex>();		
	/** vytvori ArrayList do ktereho se budou ukladat objekty Entita jako hrany grafu */
	ArrayList<Path> paths = new ArrayList<Path>();	
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
	static int[][] FloydWarshall;
	ArrayList<Integer> edges = new ArrayList<Integer>();
	/** vytvori pole do ktereho se bude ukladat pst vrcholu na ceste z vrcholu i do vrcholu j*/
	static int[][] shortestPath;
	static int counter;
	/** ArrayList planet */
	static ArrayList<Planet> planets = new ArrayList<Planet>();
	/** konstanta uchovavajici plny naklad*/
	static final int capacity = 5000000;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		
		System.out.println("Nacist ze souboru/Generovat nove hodnoty: 0/1");
		input = sc.nextInt();
		if(input == 1) {
			System.out.println("Zadej pocet planet");
			planetsCount = sc.nextInt();
			distance = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			shortestPath = new int[factoriesCount+planetsCount][factoriesCount+planetsCount]; 
	
		
			/** zavola metodu, ktera vytvori centraly */
			entitiesV = Data.factoriesDistribution(factoriesCount, planetsCount, neighbourCountF, entitiesV);
			/** zavola metodu, ktera vytvori planety */
			entitiesV = Data.planetsDistribution(factoriesCount, planetsCount, neighbourCountP, entitiesV);	
			
			BufferedWriter bw1 = new BufferedWriter(new FileWriter("Vrcholy.txt"));				// BW na vypis do textaku vzdalenosti vrcholu
			for (int i = 0; i < entitiesV.size(); i++) {
				
					bw1.write(entitiesV.get(i).key+"\t"+entitiesV.get(i).xAxis+"\t"+entitiesV.get(i).yAxis+"\t"+entitiesV.get(i).neighbourCount+"\t"+entitiesV.get(i).color);
					bw1.newLine();	
						
			}
			bw1.close();
		
			distance = Data.getDistance();
			BufferedWriter bw2 = new BufferedWriter(new FileWriter("Vzdalenosti.txt"));				// BW na vypis do textaku 
			for (int i = 0; i < entitiesV.size(); i++) {
				for (int j = 0; j < entitiesV.size(); j++) {
					bw2.write(Math.floor(distance[i][j])+"\t");
				}
				bw2.newLine();
			}
			bw2.close();
		
			Data.neighbour(factoriesCount, neighbourCountF, neighbourCountP, entitiesV);
			BufferedWriter bw3 = new BufferedWriter(new FileWriter("Hrany.txt"));				// BW na vypis do textaku 
			for (int i = 0; i < entitiesV.size(); i++) {										// vypise vsechny cesty, prvni prvek pocatecni vrchol, ostatni koncove
				if (i<factoriesCount) {
					for (int j = 0; j < neighbourCountF; j++) {
						bw3.write(entitiesV.get(i).neighbour[j].index+"\t");					
					}
				}else{
					for (int j = 0; j < neighbourCountP; j++) {
						bw3.write(entitiesV.get(i).neighbour[j].index+"\t");	
					}	
				}
				bw3.newLine();
			}
			bw3.close();
		
			/*Dijsktra
			  for (int i = 0; i < shortestPath.length; i++) {
				shortestPath[i] = Graph.doDijkstra(distance, i);
			}*/
			 
			/* FloydWarshall */
			FloydWarshall= Graph.floydWarshall(distance);
			BufferedWriter bw4 = new BufferedWriter(new FileWriter("ShortestPath.txt"));				// BW na vypis do textaku 
			for (int i = 0; i < shortestPath.length; i++) {												// vypise vystup z Dijkstry
				
					for (int j = 0; j < shortestPath.length; j++) {
						bw4.write(shortestPath[i][j]+"\t");					
					}			
				bw4.newLine();
			}
			bw4.close();
			
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			new Mapa(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, shortestPath);
		
			
			
		}else{
			counter = fromFileVrcholy();
			fromFileHrany();
			planetsCount = counter - factoriesCount;
			distance = new int[factoriesCount+planetsCount][factoriesCount+planetsCount];
			shortestPath = new int[factoriesCount+planetsCount][factoriesCount+planetsCount]; 
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			new Mapa(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV, shortestPath);		
		}
		
	
	//**************************************************************SIMULACE***********************************************************************************************************
		for (int d = 0; d < 1; d++){ 			//poèet dní, kdy simulace pobìží (pozdìji pùjde nastavit více dní uživatelem)
			for (int i = 0; i < planetsCount; i++){			//cyklus pobìží pro všechny planety
				Planet p = new Planet(entitiesV.get(i).getKey(), entitiesV.get(i).getXAxis(), entitiesV.get(i).getYAxis(), entitiesV.get(i).getNeighbourCount(), entitiesV.get(i).color);			//volani planety
				p.setOrder(p.order(p.getPopulCount(), p.drugProduction(p.getPopulCount())));
				planets.add(p);
				
				System.out.println(planets.get(i).getOrder());
			}
					
			for (int j = 0; j < planets.size(); j++){	
				
				centralaID = entitiesV.get(r.nextInt(4)).getKey();
				Starship s = new Starship(j, 25, capacity, centralaID);      //volani lode, musi se doresit ID
				s.setTargetP(planets.get(j).getKey());
				//lod doleti na planetu
				s.setCapacity(s.getCap() - planets.get(j).getOrder());
				while (planets.get(j+1).getOrder() < s.getCap()){
					s.setTargetP(planets.get(j+1).getKey());
					//lod doleti na dalsi planetu
					s.setCapacity(s.getCap() - planets.get(j).getOrder());
				}
				s.setTargetP(centralaID);
				//lod doleti do centraly
				s.setCapacity(capacity);
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
	//**************************************************************KONEC SIMULACE*****************************************************************************************************

	System.out.println("Program skoncil.");
	}	
	
	public static int fromFileVrcholy() {
		BufferedReader br;
		int counter=0;
		try {
			br = new BufferedReader(new FileReader("Vrcholy.txt"));
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
	
	public static void fromFileHrany() {
		BufferedReader br;
		BufferedReader br2;
		int counter=0;
		try {
			br = new BufferedReader(new FileReader("Hrany.txt"));
			br2 = new BufferedReader(new FileReader("Vzdalenosti.txt"));
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
						System.out.println("nacet jsem: " + index + " vzdal : " + vzdalenost);
						//entitiesV.get(counter).neighbour[i].index = index;
						//entitiesV.get(counter).neighbour[i].dist = vzdalenost;
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
