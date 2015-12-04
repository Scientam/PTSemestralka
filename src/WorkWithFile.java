import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WorkWithFile {
//********************************************************************************************VÝPIS_DO_TXT*********************************************************************************************	
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
	
	public static void printVertex(ArrayList<Vertex> entitiesV) {
		 BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("Vertexes.txt"));
			for (int i = 0; i<entitiesV.size(); i++) {
				bw.write(entitiesV.get(i).key+"\t"+entitiesV.get(i).xAxis+"\t"+entitiesV.get(i).yAxis+"\t"+entitiesV.get(i).neighbourCount+"\t"+entitiesV.get(i).color);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {e.printStackTrace();}			
	 }
	
	public static void printNeigbour(ArrayList<Vertex> entitiesV, int factoriesCount, int planetsCount) {
		 BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("Edges.txt"));					
			for (int i = 0; i<entitiesV.size(); i++) {											    
				if (i<factoriesCount) {                                                             // zpracovava centraly
					for (int j = 0; j<entitiesV.get(0).neighbourCount; j++) {
						bw.write(entitiesV.get(i).neighbour[j].index+"\t");					
					}
				}else{
					for (int j = 0; j<entitiesV.get(5).neighbourCount; j++) {                                       // zpracovava planety
						bw.write(entitiesV.get(i).neighbour[j].index+"\t");	
					}	
				}
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {e.printStackTrace();}			
	 }

	//********************************************************************************************NAÈÍTÁNÍ_Z_TXT*********************************************************************************************	

	public static ArrayList<Vertex> fromFileVrcholy(int factoriesCount) {
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
		return entitiesV;
	}
	
	
	public static void fromFileHrany(ArrayList<Vertex> entitiesV, int factoriesCount, int planetsCount) {
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
