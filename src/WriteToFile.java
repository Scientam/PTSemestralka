import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteToFile {
	
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

}
