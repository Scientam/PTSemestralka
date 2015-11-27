import java.util.Random;

public class Path {
	
	/** Promenna nebezpeci. True nebo false. */
	private boolean dangerous;
	private Random r = new Random();
	private Vertex from;
	private Vertex to;
	private double distance;
	

	/**
	 * Urci nebezpeznost cesty. Sance 20 %.
	 * @param dangerous
	 */
	public Path(Vertex from, Vertex to, double distance, boolean dangerous) {
		this.from=from;
		this.to=to;
		this.distance=distance;
		int danger = r.nextInt(5);
		if (danger == 4){
			this.dangerous = true;
		}
		else
			this.dangerous = false;
	}
}
