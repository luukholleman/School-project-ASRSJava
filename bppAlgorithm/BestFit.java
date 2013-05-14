/**
 * @author Luuk Holleman
 * @date 16 april
 */
package bppAlgorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import order.Product;

public class BestFit implements BPPAlgorithm {
	public static String name = "Best Fit";

	@Override
	/**
	 * Geeft de naam van het algoritme aan de GUI
	 * @return name
	 */
	public String getName() {
		return name;
	}

	@Override
	/**
	 * Berekent in welke bin het gegeven product moet
	 * 
	 * @param product
	 * @param bins
	 * @return fittingBin
	 */
	public Bin calculateBin(Product product, ArrayList<Bin> bins) {
		//Kopieer de arraylist
		bins = new ArrayList<Bin>(bins);		
	
		// Sorteer de bins in op volgorde van veel inhoud naar weinig inhoud
		Collections.sort(bins, new Comparator<Bin>() {
			public int compare(Bin one, Bin two) {
				return ((Integer) (one.getSize() - one.getFilled()))
						.compareTo(two.getSize() - two.getFilled());
			}
		});
		//Controleer of het product past
		ArrayList<Bin> possibleBins = new ArrayList<Bin>();
		for(Bin bin : bins){
			if ((bin.getSize()-bin.getFilled()) >= product.getSize())
				possibleBins.add(bin);
		}
		//Return de bin die de minste ruimte heeft
		Bin fittingBin;
		if(possibleBins.isEmpty())
			fittingBin = null;
		else
			fittingBin = possibleBins.get(0);
		
		return fittingBin;
	}
}
