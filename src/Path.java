import java.util.Random;

public class Path {
	
	/** Promenna nebezpeci. True nebo false. */
	boolean dangerous;
	Vertex from;
    Vertex to;
	double distance;
	

	/**
	 * Urci nebezpeznost cesty. Sance 20 %.
	 * @param dangerous
	 */
	public Path(Vertex from, Vertex to, double distance) {
		this.from=from;
		this.to=to;
		this.distance=distance;
		Random r = new Random();
		int danger = r.nextInt(5);
		if (danger == 4){
			this.dangerous = true;
		}
		else{ this.dangerous = false; }			
	}
	
}
