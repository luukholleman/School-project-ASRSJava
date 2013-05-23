package taskSimulate;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import order.Location;
import order.Product;

public class OrderPickingTaskSimulation extends JPanel {
	public static final int WAREHOUSE_Y = 10;
	private static final int LINE_INDENT = 12;
	private static final int DOT_INDENT = 7;
	private static final int CELL_SIZE = 25;
	private ArrayList<TravelingSalesmanProblem> problems;
	private int currentProblem = 0;

	public OrderPickingTaskSimulation(
			ArrayList<TravelingSalesmanProblem> problems) {
		super();

		this.problems = problems;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.BLACK);

		// Hier wordt het magazijn getekend in 20x10
		for (int y = 0; y < WAREHOUSE_Y; y++) {
			for (int x = 0; x < 20; x++) {
				g.drawRect((x * CELL_SIZE) + CELL_SIZE, (y * CELL_SIZE),
						CELL_SIZE, CELL_SIZE);
			}
		}

		Location lastLocation = new Location(-1, 0);
		int totalDistance = 0;
		int distanceIndent = 25;
		g.setColor(Color.blue);
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(2));
		int count = 0;

		for (ArrayList<Product> robot : problems.get(currentProblem)
				.getProblem()) {
			g.fillRect(
					lastLocation.x * CELL_SIZE + DOT_INDENT + CELL_SIZE,
					(WAREHOUSE_Y - 1 - lastLocation.y) * CELL_SIZE + DOT_INDENT,
					10, 10);
			for (Product product : robot) {
				Location productLocation = product.getLocation();
				g.fillRect(productLocation.x * CELL_SIZE + DOT_INDENT
						+ CELL_SIZE, (WAREHOUSE_Y - 1 - productLocation.y)
						* CELL_SIZE + DOT_INDENT, 10, 10);
				g2D.drawLine(lastLocation.x * CELL_SIZE + LINE_INDENT
						+ CELL_SIZE, (WAREHOUSE_Y - 1 - lastLocation.y)
						* CELL_SIZE + LINE_INDENT, productLocation.x
						* CELL_SIZE + LINE_INDENT + CELL_SIZE,
						(WAREHOUSE_Y - 1 - productLocation.y) * CELL_SIZE
								+ LINE_INDENT);
				totalDistance += productLocation.getDistanceTo(lastLocation);
				lastLocation = productLocation;
			}
			g2D.drawLine(lastLocation.x * CELL_SIZE + LINE_INDENT + CELL_SIZE,
					(WAREHOUSE_Y - 1 - lastLocation.y) * CELL_SIZE
							+ LINE_INDENT, (-1 + count * 21) * CELL_SIZE
							+ LINE_INDENT + CELL_SIZE, (WAREHOUSE_Y - 1 - 0)
							* CELL_SIZE + LINE_INDENT);
			totalDistance += lastLocation.getDistanceTo(new Location((-1 + count * 21), 0));
			count++;
			g.drawString(Integer.toString(totalDistance), distanceIndent, 260);
			g.setColor(Color.red);
			distanceIndent += 25*10;
			lastLocation = new Location(20, 0);
		}
		g.setColor(Color.black);
		g.drawString(Integer.toString(currentProblem), distanceIndent, 260);
	}

	public void nextProblem() {
		if (currentProblem < problems.size() - 1)
			currentProblem++;
		repaint();
	}

	public void previousProblem() {
		if (currentProblem > 0)
			currentProblem--;
		repaint();
	}
}
