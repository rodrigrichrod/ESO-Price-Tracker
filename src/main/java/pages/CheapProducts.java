package pages;

public class CheapProducts {
	private String name;
	private double goldPrice;
	private int timeElapsed;
	private String location;
	public CheapProducts(String name, double goldPrice, int timeElapsed, String location) {
		super();
		this.name = name;
		this.goldPrice = goldPrice;
		this.timeElapsed = timeElapsed;
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public double getGoldPrice() {
		return goldPrice;
	}
	public int getTimeElapsed() {
		return timeElapsed;
	}
	public String getLocation() {
		return location;
	}
	
	
	
	
}
