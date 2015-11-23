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
	ArrayList<Vertex> edges = new ArrayList<Vertex>();
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
	static double[][] distance;
	/** vytvori pole do ktereho se bude ukladat pst vrcholu na ceste z vrcholu i do vrcholu j*/
	static int[][] shortestPath;
	static int counter;

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		
		System.out.println("Nacist ze souboru/Generovat nove hodnoty: 0/1");
		input = sc.nextInt();
		if(input == 1) {
			System.out.println("Zadej pocet planet");
			planetsCount = sc.nextInt();
			distance = new double[factoriesCount+neighbourCountF][factoriesCount+neighbourCountF];
			shortestPath = new int[factoriesCount+neighbourCountF][factoriesCount+neighbourCountF]; 		
	
		
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
		
		
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			new Mapa(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV);
		
			for (int i = 0; i < shortestPath.length; i++) {
				shortestPath[i] = Graph.doDijkstra(distance, i);
			}
			
		}else{
			counter = fromFileVrcholy();
			fromFileHrany();
			planetsCount = counter - factoriesCount;
			distance = new double[factoriesCount+neighbourCountF][factoriesCount+neighbourCountF];
			shortestPath = new int[factoriesCount+neighbourCountF][factoriesCount+neighbourCountF]; 
			/** zavola metodu, ktera nazorne vykresli galaxii, tj. plnety,  centraly a cesty mezi nimi */
			new Mapa(factoriesCount, planetsCount, neighbourCountF, neighbourCountP, entitiesV);		
		}
		
	
	//*********************************************************************************SIMULACE********************************************************************************************************/	
		
	//*****************************************************************************KONEC_SIMULACE******************************************************************************************************/	
	
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
