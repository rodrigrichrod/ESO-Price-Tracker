package pages;

public class Items {

	
	private String url;
	private String name;
	private double price;
	private String id;
	public Items(String name, String id,double price) {
		super();
		this.id = id;
		this.url = "https://us.tamrieltradecentre.com/pc/Trade/SearchResult?ItemID=" + id + "&";
		this.name = name;
		this.price = price;
		
	}
	public String getUrl() {
		return url;
	}
	public String getName() {
		return name;
	}
	public double getPrice() {
		return price;
	}
	public String getId() {
		return id;
	}
	
	
	
}
