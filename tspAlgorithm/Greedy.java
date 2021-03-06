/**
 * @author Luuk Holleman
 * @date 15 april
 */
package tspAlgorithm;

import java.util.ArrayList;

import productInfo.Location;
import productInfo.Product;

public class Greedy extends TSPAlgorithm {
	private static final int INFINITY = 99999;
	public static String name = "Greedy";
	/**
	 * de gegeven product, alleen de producten die nog niet in de route staatn
	 * dus aan het eind is deze arraylist leeg
	 */
	private ArrayList<Product> products = new ArrayList<Product>();

	// de locatie van de robot, de robot start op 0, 0
	private Location location = new Location(0, 0);

	/**
	 * De berekende route, wordt gevuld met objecten uit products
	 */
	private ArrayList<Product> route = new ArrayList<Product>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public ArrayList<Product> calculateRoute(ArrayList<Product> products,
			int numberOfRobots, int currentRobot) {
		this.products = splitOrder(products, numberOfRobots, currentRobot);
		
		location = new Location((20 * currentRobot) - 1, 0);

		this.route = new ArrayList<Product>();

		while (nextNode(this.products));

		return this.route;
	}

	/**
	 * Berekent het dichtsbijzijnde product verwijderd het product uit products
	 * zodat het niet meer meegenomen wordt
	 * 
	 * @param products
	 * @return product
	 */
	private boolean nextNode(ArrayList<Product> products) {
		// de arraylist is leeg, niks te berekenen. return false
		if (products.size() == 0)
			return false;
		float minDistance = INFINITY;
		Product minProduct = null;

		// we lopen nu elk product af en berekenen de afstand. de kortste wordt
		// opgeslagen
		for (Product product : products) {
			float distance = location.getDistanceTo(product.getLocation());

			// de andere statement is als de net berekende distance korter is
			// dan de vorige
			if (distance <= minDistance) {
				minDistance = distance;
				minProduct = product;
			}
		}

		// de robot is nu op de locatie van dit product, update de locatie
		location = minProduct.getLocation();

		// we hebben het dichtsbijzijnde product, voeg hem toe aan de route,
		// verwijderen van de nog te berekenen producten
		route.add(minProduct);
		products.remove(minProduct);

		// we hebben een node! return true
		return true;

	}

}
