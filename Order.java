package project212.phase2.AVL;
import java.time.LocalDate;
public class Order {

	    private int orderID;
	    private int customerID;
	    private String prod_IDs;
	    private double totalPrice;
	    private LocalDate orderDate;
	    private String status;
	    private LinkedList<Integer> productIDs;

	    public Order(int orderId, int customerId, String prod_Ids, double totalPrice, LocalDate orderDate, String status) {
	        this.orderID = orderId;
	        this.customerID = customerId;
	        this.prod_IDs = prod_Ids;
	        this.totalPrice = totalPrice;
	        this.orderDate = orderDate;
	        this.status = status;
	        this.productIDs = new LinkedList<>();
	        addIds(prod_Ids);
	    }

	    // Time Complexity: O(n)
	    public void addIds(String Ids) {
	        String a[] = Ids.split(";");
	        for (int i = 0; i < a.length; i++)
	            productIDs.addLast(Integer.parseInt(a[i].trim()));
	    }

	    // Time Complexity: O(1)
	    public void addId(int id) { productIDs.addLast(id); }

	    // Time Complexity: O(1)
	    public void UpdateOrder (Order ord) {
	        this.orderID = ord.orderID;
	        this.customerID =  ord.customerID;
	        this.prod_IDs =  ord.prod_IDs;
	        this.totalPrice =  ord.totalPrice;
	        this.orderDate =  ord.orderDate;
	        this.status =  ord.status;
	        this.productIDs =  ord.productIDs;
	    }

	    // Getters - Time Complexity: O(1)
	    public int getOrderId() { return orderID; }
	    public int getCustomerId() { return customerID; }
	    public String getProd_Ids() { return prod_IDs; }
	    public LinkedList<Integer> getProductIds() { return productIDs; }
	    public double getTotalPrice() { return totalPrice; }
	    public LocalDate getOrderDate() { return orderDate; }
	    public String getStatus() { return status; }

	    public void setStatus(String status) { this.status = status; }

	    public String toString() {
	        return "Order ID: " + orderID + "\nCustomer ID: " + customerID +
	               "\nProduct IDs: " + productIDs.toString() + "\nTotal Price: $" + totalPrice +
	               "\nDate: " + orderDate + "\nStatus: " + status;
	    }
	}