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
	public String getName() {
		return name;
	}

	@Override
	public Bin calculateBin(Product product, ArrayList<Bin> bins) {
		// Sorteer de bins in op volgorde van veel inhoud naar weinig inhoud
		Collections.sort(bins, new Comparator<Bin>() {
			public int compare(Bin one, Bin two) {
				return ((Integer) (one.getSize() - one.getFilled()))
						.compareTo(two.getSize() - two.getFilled());
			}
		});
		// Return de bin die de minste ruimte heeft
		Bin bin = bins.get(bins.size() - 1);
		return bin;
	}
}
