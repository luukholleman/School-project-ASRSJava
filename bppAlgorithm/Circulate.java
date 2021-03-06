
package bppAlgorithm;

import java.util.ArrayList;
import productInfo.Product;

/**
 * Een algoritme die altijd om de beurt een bin kiest
 * 
 * @author Bas
 */
public class Circulate implements BPPAlgorithm {
	/**
	 * De naam van het algoritme
	 */
	private static final String NAME = "Circulate";
	
	/**
	 * De bin waar als laatst iets is in gelegd.
	 */
	private Bin lastBin = null;
	
	/**
	 * Berekend waar het product nog in past
	 * 
	 * @param Product, ArrayList<Bin>
	 * @return Bin
	 */
	
	public String getName(){
		return NAME;
	}
	
	@Override
	public Bin calculateBin(Product product, ArrayList<Bin> bins) {
		
		//Als dit de eerste keer is dat deze methode is opgeroepen, return de eerste bin waar hij in past.
		if(lastBin == null)
			for(Bin bin : bins)
				if(bin.getSize() >= product.getSize()){
					lastBin = bin;
					return lastBin;
				}
		
		//Probeer hem net zo vaak in de volgende bin te stoppen als dat er bins zijn, als het dan nog niet lukt, past hij nergens.
		for(int i = 0 ; i < bins.size() ; i++){
			
			//Als de vorige bin niet de laatste bin was in de array, probeer hem dan in de volgende bin te stoppen.
			if(bins.indexOf(lastBin) < bins.size()-1)
				lastBin = bins.get(bins.indexOf(lastBin)+1);
				if(lastBin.getSize()-lastBin.getFilled() >= product.getSize())
					return lastBin;
			
			
			//Als het wel de laatste bin in de array is, probeer hem dan in de eerste bin te stoppen.
			else{
				lastBin = bins.get(0);
				if(lastBin.getSize()-lastBin.getFilled() >= product.getSize())
					return lastBin;
			}
		}
		
		//Als er geen bin is gevonden, return null.
		return null;
	}
}
