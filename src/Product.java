public class Product {
	private int id;
	private String description;
	private Float price;
	private String status;
	private int size;
	private Location location;

	public Product(int id, String description, Float price, int size,
			Location location) {
		this.id = id;
		this.description = description;
		this.price = price;
		this.status = "";
		this.size = size;
		this.location = location;
	}

	public int getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public Float getPrice() {
		return price;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSize() {
		return size;
	}

	public Location getLocation() {
		return location;
	}
}
