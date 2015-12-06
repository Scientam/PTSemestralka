import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkWithFile {
//********************************************************************************************VYPIS_DO_TXT*********************************************************************************************	
	/**
	 * 
	 * @param matrix
	 * @param size
	 * @param name
	 */
	public static void printMatrix(int[][] matrix, int size, String name) {
		 BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter(name));
			for (int i = 0; i<size; i++) {
				for (int j = 0; j<size; j++) {
					bw.write((int)Math.floor(matrix[i][j])+"\t");
				}
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {e.printStackTrace();}			
	 }
	
	
	/**
	 * 
	 * @param entitiesV
	 */
	public static void printVertex(List<Vertex> entitiesV) {
		 BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("Vertexes.txt"));
			for (int i = 0; i<entitiesV.size(); i++) {
				bw.write(entitiesV.get(i).key+"\t"+entitiesV.get(i).xAxis+"\t"+entitiesV.get(i).yAxis+"\t"+entitiesV.get(i).neighbourCount);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {e.printStackTrace();}			
	 }
	
	
	/**
	 * 
	 * @param entitiesV
	 * @param factoriesCount
	 * @param planetsCount
	 */
	public static void printNeigbour(List<Vertex> entitiesV, int factoriesCount, int planetsCount) {
		 BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("Edges.txt"));					
			for (int i = 0; i<entitiesV.size(); i++) {											    
				if (i<factoriesCount) {                                                             // zpracovava centraly
					for (int j = 0; j<entitiesV.get(0).neighbourCount; j++) {
						bw.write(entitiesV.get(i).neighbour[j].getIndex()+"\t");					
					}
				}else{
					for (int j = 0; j<entitiesV.get(5).neighbourCount; j++) {                                       // zpracovava planety
						bw.write(entitiesV.get(i).neighbour[j].getIndex()+"\t");	
					}	
				}
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {e.printStackTrace();}			
	 }
	
	
	public static void printOrder(int day, List<Vertex> entitiesV, List<Planet> planetL) {
		BufferedWriter bw;
		int production;
		try {
			bw = new BufferedWriter(new FileWriter("Order.txt"));
			bw.write("---------------------------------------------------------------------------------------------");
			bw.newLine();
			if ((day % 30) == 0) {
			bw.write("Objednavky pro "+(day/30+1)+". mesic: ");
			bw.newLine();
			bw.write("---------------------------------------------------------------------------------------------");
			bw.newLine();
			
			
			for (int i = 5; i < entitiesV.size(); i++) {			                            //cyklus pobezi pro vseechny planety
				if (planetL.get(i).getAnswered()==false) {
					production = planetL.get(i).drugProduction( planetL.get(i).getPopulCount() );
					planetL.get(i).setOrder( planetL.get(i).order( planetL.get(i).getPopulCount(), production ) );    // vytvori objednavku, jeji velikost zavisi na poctu obyvatel planety
				}	
				bw.write("Planeta s id: "+(planetL.get(i).getId())+" objednava takovyto pocet leku: "+planetL.get(i).getOrder());
				bw.newLine();
			}
				bw.write("---------------------------------------------------------------------------------------------");
				bw.newLine();
			}
			bw.close();
		}catch (IOException e) {e.printStackTrace();}	
		
	}

	//********************************************************************************************NACITANI_Z_TXT*********************************************************************************************	

	/**
	 * 
	 * @param factoriesCount
	 * @return
	 */
	public static List<Vertex> fromFileVrcholy(int factoriesCount) {
		BufferedReader br;
		/** vytvori ArrayList do ktereho se budou ukladat objekty Vertex jako vrcholy grafu */
		ArrayList<Vertex> entitiesV = new ArrayList<Vertex>();	
		int counter=0;
		try {
			br = new BufferedReader(new FileReader("Vertexes.txt"));
			String radka;			
			while((radka = br.readLine()) != null){
				String[] parseLine = radka.split("\t");
				counter++;
				
				if (counter < factoriesCount) {
					 Factory centrala = new Factory(Integer.valueOf(parseLine[0]), Double.valueOf(parseLine[1]), Double.valueOf(parseLine[2]), Integer.valueOf(parseLine[3]));              	// vytvori objekt centrala s pozadovanymi parametry
					 entitiesV.add(centrala);    
				} else {
					 Planet pl = new Planet(Integer.valueOf(parseLine[0]), Double.valueOf(parseLine[1]), Double.valueOf(parseLine[2]), Integer.valueOf(parseLine[3]));
	 		 		 entitiesV.add(pl);
				}
			}
			br.close();
		} 
		catch (FileNotFoundException e) {e.printStackTrace();} 
		catch (IOException a) {System.out.println("IOexception");}
		return entitiesV;
	}
	
	
	/**
	 * 
	 * @param entitiesV
	 * @param factoriesCount
	 * @param planetsCount
	 */
	public static void fromFileHrany(List<Vertex> entitiesV, int factoriesCount, int planetsCount) {
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
					for (int i = 0; i <entitiesV.get(0).neighbourCount; i++) {
						index = Integer.valueOf(parseLine[i]);
						vzdalenost = Double.valueOf(parseLine2[index]);
						entitiesV.get(counter).neighbour[i] = new Neighbour(index, vzdalenost);					
					}
						
				} else {
					for (int i = 0; i < entitiesV.get(5).neighbourCount; i++) {
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
	 * 
	 * @param realDistance
	 */
	public static void fromFileRealDistance(int[][] realDistance) {
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
	 * 
	 * @param floydWarshall
	 */
	public static void fromFileFWShortestPath(int[][] floydWarshall) {
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
	 * 
	 * @param floydWarshallP
	 */
	public static void fromFileFWPath(int[][] floydWarshallP) {
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
