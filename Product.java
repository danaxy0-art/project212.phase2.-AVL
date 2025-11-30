package project212.phase2.AVL;


public class Product {
	 private int productId;
	    private String name;
	    private double price;
	    private int stock;
	    private LinkedList<Review> reviews;

	    public Product(int productId, String name, double price, int stock) {
	        this.productId = productId;
	        this.name = name;
	        this.price = price;
	        this.stock = stock;
	        this.reviews = new LinkedList<>();
	    }

	    public void UpdateProduct(Product p) {
	        this.productId = p.productId;
	        this.name = p.name;
	        this.price = p.price;
	        this.stock = p.stock;
	        this.reviews = p.reviews;
	    }

	    public int getProductId() { return productId; }
	    public String getName() { return name; }
	    public double getPrice() { return price; }
	    public int getStock() { return stock; }

	    public void setPrice(double price) { this.price = price; }
	    public void setStock(int stock) { this.stock = stock; }

	    public void addReview(Review review) { reviews.addLast(review); }

	    public double getAverageRating() {
	        if (reviews.empty()) return 0.0;
	        reviews.findfirst();
	        double sum = 0;
	        int count = 0;
	        while (true) {
	            sum += reviews.retrieve().getRating();
	            count++;
	            if (reviews.last()) break;
	            reviews.findenext();
	        }
	        return sum / count;
	    }

	    public void displayReviews() {
	        System.out.println("Reviews for " + name + ":");
	        if (reviews.empty()) {
	            System.out.println("  No reviews yet.");
	        } else {
	            reviews.findfirst();
	            while (true) {
	                System.out.println(reviews.retrieve().toString());
	                if (reviews.last()) break;
	                reviews.findenext();
	            }
	        }
	    }

	    public String toString() {
	        return "Product ID: " + productId + "\n" +
	               "Name: " + name + "\n" +
	               "Price: $" + price + "\n" +
	               "Stock: " + stock + "\n" +
	               "Average Rating: " + String.format("%.2f", getAverageRating());
	    }

	
	}
