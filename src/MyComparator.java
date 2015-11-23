import java.util.Comparator;

public class MyComparator implements Comparator<Neighbour> {

	@Override
	public int compare(Neighbour n1, Neighbour n2) {
		if (n1.getDist() > n2.getDist()) {
			return 1;
		} else if (n1.getDist() < n2.getDist()) {
			return -1;
		}
		return 0;
	}
}
