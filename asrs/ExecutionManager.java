package asrs;

import tspAlgorithm.TSPAlgorithm;
import bppAlgorithm.BPPAlgorithm;

public class ExecutionManager {
	private Main main;
	private Order order;
//	private BinManager binManager;
//	private Warehouse warehouse;
//	private BinPacking binPacking;
	private TSPAlgorithm tspAlgorithm;
	private BPPAlgorithm bppAlgorithm;
	private Integer width;
	private Integer height;
	private Boolean useDetectedSize;

	public ExecutionManager(Main main, Order order, //BinManager binManager,
//			Warehouse warehouse, BinPacking binPacking,
			TSPAlgorithm tspAlgorithm, BPPAlgorithm bppAlgorithm,
			Integer width, Integer height, Boolean useDetectedSize) {

	}
	
	public Byte detectedProduct(Byte red, Byte green, Byte blue){
		/**
		 * Oproepen door bppArduino, kleur naar grootte omzetten.
		 * byte wordt binnummer?
		 */
		return null;
	}
	
	public void pickedUpProduct(Integer robotId){
		/**
		 * Geeft volgende locatie in een byte[]
		 */
	}
	
	public void deliveredProduct(Integer robotId){
		/**
		 * iets over het product afleveren
		 */
	}
}
