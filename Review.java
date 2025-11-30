package project212.phase2.AVL;

public class Review {
	 private int reviewID;
	    private int productID;
	    private int customerID;
	    private int rating;
	    private String comment;

	    public Review(int reviewID, int productID, int customerID, int rating, String comment) {
	        this.reviewID = reviewID;
	        this.productID = productID;
	        this.customerID = customerID;
	        this.rating = rating;
	        this.comment = comment;
	    }

	    public void UpdateReview(Review q) {
	        this.reviewID = q.reviewID;
	        this.productID = q.productID;
	        this.customerID = q.customerID;
	        this.rating = q.rating;
	        this.comment = q.comment;
	    }

	    public int getRating() { return rating; }
	    public void setRating(int rating1) { this.rating = rating1; }
	    public String getComment() { return comment; }
	    public void setComment(String comment1) { this.comment = comment1; }
	    public int getReviewID() { return reviewID; }
	    public int getProductID() { return productID; }
	    public int getCustomerID() { return customerID; }

	    public String toString() {
	        return "Review ID:" + reviewID + "\n" +
	               "Product ID:" + productID + "\n" +
	               "Customer ID:" + customerID + "\n" +
	               "Rating:" + rating + "\n" +
	               "Comment:" + comment + "\n";
	    }
	}
