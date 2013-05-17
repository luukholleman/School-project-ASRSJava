package order;

/**
 * Deze classe bevant informatie over een locatie binnen het warenhuis
 * @author timpotze
 *
 */
public class Location {
	/**
	 * X coordinaat van location
	 */
	public int x;
	/**
	 * Y coordinaat van location
	 */
	public int y;

	
	/**
	 * Genereert een unieke hash voor een instantie van deze classe
	 * 
	 * Auto-generated
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}

	/**
	 * Vergelijk instantie van Location met andere instantie van Location
	 * 
	 * Atuo-generated
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	/**
	 * 
	 * @param x coordinaat
	 * @param y coordinaat
	 */
	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Vergelijk deze Location met toLocation
	 * @param toLocation Location om mee te vergelijken
	 * @return afstand tussen beide instanties
	 */
	public float getDistanceTo(Location toLocation) {
		
		//Pitaguras ofzo
		return (float) Math.sqrt(Math.pow(Math.abs(toLocation.x - x), 2)
				+ Math.pow(Math.abs(toLocation.y - y), 2));
	}
	
	/**
	 * Genereer een string die de instantie representeerd
	 */
	@Override
	public String toString() {
		return x + ", " + y;
	}
}
