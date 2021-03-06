package gui;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import productInfo.Location;
import productInfo.Order;
import productInfo.Product;
import utilities.Database;
import utilities.DatabaseConnectionFailedException;

import asrsController.ExecutionManager;
import asrsController.WarehouseRobot;
import asrsController.Warehouse;

/**
 * De panel waarin het warenhuis wordt gesimuleerd
 * 
 * @author Bas
 */
public class OrderPickingPanel extends JPanel implements Runnable, Warehouse {
	/**
	 * De inspring van de getekende robot
	 */
	private static final int ROBOT_INDENT = 1;
	/**
	 * De tijdsduur van een frame
	 */
	private static final int FRAME_TIME = 25;
	/**
	 * De Y locatie van de bin packer
	 */
	private static final int BINPACKER_Y = 3;
	/**
	 * De X locatie van de bin packer
	 */
	private static final int BINPACKER_X = -2;
	/**
	 * De tijdsduur dat de robot stil staat nadat hij iets heeft opgepakt
	 */
	private static final int PAUSE_TIME_ON_PICKUP = 1000;
	/**
	 * De maximale lading van de robot
	 */
	private static final int LOAD_MAX = 3;
	/**
	 * De grootte van een product in het warenhuis
	 */
	private static final int PRODUCT_SIZE = 15;
	/**
	 * De inspring van een product
	 */
	private static final int PRODUCT_INDENT = 3;
	/**
	 * De grootte van een robot
	 */
	private static final int ROBOT_SIZE = 18;
	/**
	 * De X locatie van het rechter uiteinde van de rails
	 */
	private static final int RAILS_X = 260;
	/**
	 * De hoogte van de rails
	 */
	private static final int RAILS_Y = 420;
	/**
	 * De grootte van een markering van een bestemming
	 */
	private static final int DESTINATION_SIZE = 16;
	/**
	 * De inspring van een bestemming
	 */
	private static final int DESTINATION_INDENT = 2;
	/**
	 * De maximale lengte van het warenhuis
	 */
	private static final int WAREHOUSE_MAX_Y = 19;
	/**
	 * De grootte van elke cel in het warenhuis
	 */
	private static final int CELL_SIZE = 20;
	/**
	 * De maximale breedte van het warenhuis
	 */
	private static final int WAREHOUSE_MAX_X = 9;
	/**
	 * De grootte van de bin packer (vierkant)
	 */
	private static final int BINPACKER_SIZE = 60;
	/**
	 * De Y pixel locatie van de bin packer
	 */
	private static final int BINPACKER_DEPTH = 340;

	/**
	 * De ExecutionManager die alle data opslaat en simulatie ger elateerde
	 * berekeningen uitvoert
	 */
	private ExecutionManager executionManager;
	/**
	 * De thread die de animatie laat aflopen
	 */
	private Thread runner;
	/**
	 * In warehouse worden alle locaties van de producten in het warenhuis
	 * opgeslagen
	 */
	private ArrayList<Location> warehouse;
	/**
	 * Alle producten die moeten worden opgehaald
	 */
	private ArrayList<Product> products;
	/**
	 * Array met beide robots
	 */
	private WarehouseRobot robots[];
	/**
	 * De linker warehouse robot
	 */
	private WarehouseRobot robotLeft;
	/**
	 * De rechter warehouse robot
	 */
	private WarehouseRobot robotRight;

	/**
	 * constructor
	 * 
	 * @param BinPackingPanel
	 * @author Bas
	 */
	public OrderPickingPanel(BinPackingPanel bpPanel) {
		super();
		setSize(300, 500);

		// Beide robots worden aangemaakt en op hun begin plek gezet.
		robotLeft = new WarehouseRobot(getStartLocation(0), 0);
		robotLeft.setPixels(new Location(BINPACKER_SIZE + ROBOT_INDENT
				+ (robotLeft.getLocation().getX() * CELL_SIZE), ROBOT_INDENT
				+ ((WAREHOUSE_MAX_Y - robotLeft.getLocation().getY()) * CELL_SIZE)));

		robotRight = new WarehouseRobot(getStartLocation(1), 1);
		robotRight.setPixels(new Location(BINPACKER_SIZE + ROBOT_INDENT
				+ (robotRight.getLocation().getX() * CELL_SIZE), ROBOT_INDENT
				+ ((WAREHOUSE_MAX_Y - robotRight.getLocation().getY()) * CELL_SIZE)));
		// Beide robots worden in een array gezet om makkelijk aan te roepen.
		robots = new WarehouseRobot[2];
		robots[0] = robotLeft;
		robots[1] = robotRight;

		/*
		 * Alle producten worden uit het warenhuis gehaald zodat ze kunnen
		 * getoont worden.
		 */
		try {
			warehouse = Database.getAllOccupiedLocations();
		} catch (DatabaseConnectionFailedException e) {
			JOptionPane.showMessageDialog(this,
					"Kan geen verbinding maken met de database.");
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);

		// Hier wordt de BinPacking robot getekend (een blok).
		g.drawRect(0, BINPACKER_DEPTH, BINPACKER_SIZE, BINPACKER_SIZE);

		// Hier wordt het magazijn getekend in 10x20
		for (int y = 0; y <= WAREHOUSE_MAX_Y; y++) {
			for (int x = 0; x <= WAREHOUSE_MAX_X; x++) {
				g.drawRect(BINPACKER_SIZE + (x * CELL_SIZE), (y * CELL_SIZE),
						CELL_SIZE, CELL_SIZE);
			}
		}

		// Tekenen van de producten in het warenhuis
		drawWarehouseProducts(g);

		// Tekenen robots
		drawRobots(g);

		// Tekenen doel
		drawDestination(g);
	}

	/**
	 * Tekent de bestemming van elke robot in de vorm van een blauw blokje
	 * 
	 * @param Graphics
	 * @author Bas
	 */
	private void drawDestination(Graphics g) {
		g.setColor(Color.blue);
		for (WarehouseRobot robot : robots)
			if (robot.getDestination() != null)
				g.drawRect(
						BINPACKER_SIZE + DESTINATION_INDENT
								+ (robot.getDestination().getX() * CELL_SIZE),
						((WAREHOUSE_MAX_Y - robot.getDestination().getY()) * CELL_SIZE)
								+ DESTINATION_INDENT, DESTINATION_SIZE,
						DESTINATION_SIZE);
	}

	/**
	 * Tekent de robots
	 * 
	 * @param Graphics
	 * @author Bas
	 */
	private void drawRobots(Graphics g) {
		// De rails
		g.drawLine(0, RAILS_Y, RAILS_X, RAILS_Y);

		/*
		 * De robots en de ondersteuning worden alleen getekend als de robot
		 * bestaat
		 */
		for (WarehouseRobot robot : robots) {
			if (robot != null) {
				// Tekent de robot
				g.drawRect(robot.getPixels().getX(), robot.getPixels().getY(),
						ROBOT_SIZE, ROBOT_SIZE);

				// Tekent de linker ondersteuning
				g.drawLine(robot.getPixels().getX() - 1, robot.getPixels().getY(),
						robot.getPixels().getX() - 1, RAILS_Y);

				// Tekent de rechter ondersteuning
				g.drawLine(robot.getPixels().getX() + ROBOT_SIZE + 1,
						robot.getPixels().getY(), robot.getPixels().getX() + ROBOT_SIZE
								+ 1, RAILS_Y);

			}
		}

		// Inhoud van de robots tekenen
		for (WarehouseRobot robot : robots) {
			g.fillRect(robot.getLocation().getX() + 7 - (robot.getLoad() * 2),
					robot.getLocation().getY() + 7 - (robot.getLoad() * 2), LOAD_MAX
							+ (robot.getLoad() * 5), LOAD_MAX + (robot.getLoad() * 5));
		}
	}

	/**
	 * Tekent alle producten in het warenhuis
	 * 
	 * @param Graphics
	 * @author Bas
	 */
	private void drawWarehouseProducts(Graphics g) {
		for (Location location : warehouse) {
			Location loc = new Location(0, 0);

			// De y location wordt hier omgedraait zodat 0,0 links onderin zit.
			loc.setY(WAREHOUSE_MAX_Y - location.getY());
			loc.setX(location.getX());
			if (loc.getX() <= WAREHOUSE_MAX_X && loc.getY() <= WAREHOUSE_MAX_Y) {
				// Tekent het product
				g.fillRect(BINPACKER_SIZE + PRODUCT_INDENT
						+ (loc.getX() * CELL_SIZE), (loc.getY() * CELL_SIZE)
						+ PRODUCT_INDENT, PRODUCT_SIZE, PRODUCT_SIZE);
			}
		}
	}

	/**
	 * Begint de thread om de animatie te laten lopen
	 * 
	 * @author Bas
	 */
	public void start() {
		if (runner == null) {
			runner = new Thread(this);
			runner.start();
		}
	}

	/**
	 * Dit is de loop die afloopt om de animatie af te spelen
	 * 
	 * @param void
	 * @return void
	 * @author Bas
	 */
	@Override
	public void run() {
		// De producten die onderdeel zijn worden uit de order gehaald
		Order order = executionManager.getOrder();
		products = order.getProducts();

		// Haal de huidige thread op
		Thread thisThread = Thread.currentThread();
		while (runner == thisThread) {
			// Als de robots niet vol of klaar zijn, haal het volgende product
			// op.
			if (robots[0].getLoad() <= LOAD_MAX || robots[1].getLoad() <= LOAD_MAX) {
				for (WarehouseRobot robot : robots)
					if (robot.isFinished() == false)
						if (!robot.getProductsOnFork().isEmpty())
							executionManager.pickedUpProduct(robot.getId(),
									(byte) robot.getProductsOnFork().get(0)
											.getSize());
						else
							executionManager
									.pickedUpProduct(robot.getId(), (byte) 0);
				move();
				sleep(PAUSE_TIME_ON_PICKUP);

				/*
				 * Als de robot de producten heeft opgepakt, haal ze uit het
				 * warenhuis en leg ze op de robot of op de bin packer.
				 */
				warehouse.remove(robots[0].getDestination());
				warehouse.remove(robots[1].getDestination());
				
				for (WarehouseRobot robot : robots) {
					if (robot.getLocation().getX() == BINPACKER_X
							&& robot.getLocation().getY() == BINPACKER_Y)
						executionManager.deliveredProduct(robot.getId());
					
					else
						for (Product product : products)
							if (robot.getDestination() == product.getLocation())
								robot.getProductsOnFork().add(product);
				}

				/*
				 * Als een van de robots vol is, laat deze robot dan naar de bin
				 * packer bewegen.
				 */
			} else {
				if (robots[0].getLoad() <= LOAD_MAX)
					robots[0].setDestination(new Location(BINPACKER_X,
							BINPACKER_Y));
				if (robots[1].getLoad() <= LOAD_MAX)
					robots[1].setDestination(new Location(BINPACKER_X,
							BINPACKER_Y));
				move();
				// Leg zijn producten daarna op de bin packer.
				for (WarehouseRobot robot : robots)
					for (Product product : products)
						if (robot.getDestination() == product.getLocation())
							robot.getProductsOnFork().add(product);

			}
		}
	}

	/**
	 * Stopt de animatie
	 * 
	 * @author Bas
	 */
	public void stop() {
		if (runner != null) {
			runner = null;
			System.out.println("stopping Order Picker");
		}
	}

	/**
	 * Een functie om de robot van de huidige locatie naar het doel te bewegen.
	 * 
	 * @author Bas
	 */
	private void move() {
		// De beweging wordt verdeelt in stappen over de X en Y as.
		int stepx0 = robots[0].getDestination().getX() - robots[0].getLocation().getX();
		int stepy0 = robots[0].getDestination().getY() - robots[0].getLocation().getY();

		int stepx1 = robots[1].getDestination().getX() - robots[1].getLocation().getX();
		int stepy1 = robots[1].getDestination().getY() - robots[1].getLocation().getY();

		// De robot wordt naar het doel verplaatst
		robots[0].setLocation(robots[0].getDestination());
		robots[1].setLocation(robots[1].getDestination());

		// Hier wordt in 20 frames de animatie van de verplaatsing getekent
		for (int i = 0; i < CELL_SIZE; i++) {
			robots[0].getPixels().setX(robots[0].getPixels().getX() + stepx0);
			robots[0].getPixels().setY(robots[0].getPixels().getY() - stepy0);

			robots[1].getPixels().setX(robots[1].getPixels().getX() + stepx1);
			robots[1].getPixels().setY(robots[1].getPixels().getY() - stepy1);
			repaint();
			sleep();
		}
	}

	/**
	 * Stopt de animatie voor 25 miliseconden
	 * 
	 * @author Bas
	 */
	private void sleep() {
		try {
			Thread.sleep(FRAME_TIME);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Stopt de animatie voor een variabel aantal miliseconden
	 * 
	 * @param int
	 * @author Bas
	 */
	private void sleep(int pause) {
		try {
			Thread.sleep(pause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void retrieveProduct(Location location, int robotId) {
		robots[robotId].setDestination(location);
	}

	@Override
	public void bringToBinPacker(int robotId) {
		robots[robotId].setDestination(new Location(BINPACKER_X, LOAD_MAX));
	}

	@Override
	public void moveToStart(int robotId) {
		robots[robotId].setDestination(getStartLocation(robotId));
		move();
		sleep();
		robots[robotId].setFinished(true);
		if (robots[0].isFinished() == true && robots[1].isFinished() == true)
			stop();
	}

	@Override
	public int getNumberOfRobots() {
		return 2;
	}

	@Override
	public int getMaxLoad() {
		// Omdat we het terug brengen van producten anders behandellen, sturen
		// we een oneindig aantal maximum producten
		return WIDTH * HEIGHT;
	}

	@Override
	public Location getStartLocation(int robotId) {
		if (robotId == 0)
			return new Location(-1, 0);
		else if (robotId == 1)
			return new Location(10, 0);
		else
			return null;
	}

	/**
	 * EM setter
	 * 
	 * @param exectuionManager
	 * @author Bas
	 */
	public void setExecutionManager(ExecutionManager exectuionManager) {
		this.executionManager = exectuionManager;
	}
}
