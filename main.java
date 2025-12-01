package project212.phase2.AVL;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.Stack;

public class main {

    public static boolean VERBOSE = false;



    // Lists 
    private static AVL<Customer> customers_list;
    private static AVL<Order>    orders_list;
    private static AVL<Product>  products_list;
    private static AVL<Review>   reviews_list;

    //  Managers
    private static Reviews   all_Reviews;
    private static Customers all_Customers;
    private static Orders    all_Orders;
    public static Products  all_products;

    private static final Scanner input = new Scanner(System.in);

    //CSV paths
    private static final String BASE_PATH     = "C:\\Users\\danam\\Desktop\\project212\\phase2\\";
    private static final String PRODUCTS_CSV  = BASE_PATH + "prodcuts.csv";   
    private static final String CUSTOMERS_CSV = BASE_PATH + "customers.csv";
    private static final String ORDERS_CSV    = BASE_PATH + "orders.csv";
    private static final String REVIEWS_CSV   = BASE_PATH + "reviews.csv";

	public static final String products = null;
	

    //Auto-load once
    private static boolean dataLoaded = false;
    private static void ensureLoaded() {
        if (!dataLoaded) {
            Load_all(); 
            dataLoaded = true;
        }
    }

    // Constructor
    public main() {
        customers_list = new AVL<>();
        orders_list    = new AVL<>();
        products_list  = new AVL<>();
        reviews_list   = new AVL<>();

        all_products   = new Products(products_list);
        all_Customers  = new Customers(customers_list);
        all_Orders     = new Orders(customers_list, orders_list);
        all_Reviews    = new Reviews(reviews_list, products_list, customers_list);
    }

    // Load from CSVs  
    public static void Load_all() {
        all_products.loadProducts(PRODUCTS_CSV);
        all_Customers.loadCustomers(CUSTOMERS_CSV);
        all_Orders.loadOrders(ORDERS_CSV);
        all_Reviews.load_revews(REVIEWS_CSV); 
    }

    //Safe add wrappers
    public static void add_Customer(Customer c) { ensureLoaded(); all_Customers.addCustomer(c); }
    public static void add_Product(Product p)   { ensureLoaded(); all_products.addProduct(p);  }
    public static void add_Order(Order o)       { ensureLoaded(); all_Orders.addOrder(o);      }
    public static void add_Review(Review r)     { ensureLoaded(); all_Reviews.addReview(r);    }

    // Show top 3 products
    public void displayTop3Products() {
        ensureLoaded();

        if (products_list == null || products_list.empty()) {
            System.out.println("No products available.");
            return;
        }

        Product max1 = null, max2 = null, max3 = null;

        Stack<BSTNode<Product>> stack = new Stack<>();
        BSTNode<Product> current = products_list.getRoot();

        while (current != null || !stack.isEmpty()) {

            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            Product cur = current.data;

            double rating = cur.getAverageRating();

            if (max1 == null || rating > max1.getAverageRating()) {
                max3 = max2;
                max2 = max1;
                max1 = cur;
            } else if (max2 == null || rating > max2.getAverageRating()) {
                max3 = max2;
                max2 = cur;
            } else if (max3 == null || rating > max3.getAverageRating()) {
                max3 = cur;
            }

            current = current.right;
        }

        System.out.println("\nTop Products by Average Rating:");
        int rank = 1;

        if (max1 != null)
            System.out.println(rank++ + ". Product ID: " + max1.getProductId()
                    + " | Name: " + max1.getName()
                    + " | Avg Rating: " + String.format("%.2f", max1.getAverageRating()));

        if (max2 != null)
            System.out.println(rank++ + ". Product ID: " + max2.getProductId()
                    + " | Name: " + max2.getName()
                    + " | Avg Rating: " + String.format("%.2f", max2.getAverageRating()));

        if (max3 != null)
            System.out.println(rank++ + ". Product ID: " + max3.getProductId()
                    + " | Name: " + max3.getName()
                    + " | Avg Rating: " + String.format("%.2f", max3.getAverageRating()));

        System.out.println("-----------------------------------");
    }

    // Common high-rated products
    public static void showCommonHighRatedProducts(int customerId1, int customerId2) {
        ensureLoaded();
        System.out.println("Common Products Reviewed by Both Customers (Avg > 4):");

        if (products_list == null || products_list.empty()) {
            System.out.println("No products available.");
            return;
        }
        if (reviews_list == null || reviews_list.empty()) {
            System.out.println("No reviews available.");
            return;
        }

        boolean found = false;

        Stack<BSTNode<Product>> productStack = new Stack<>();
        BSTNode<Product> currentProduct = products_list.getRoot();

        while (currentProduct != null || !productStack.isEmpty()) {

            while (currentProduct != null) {
                productStack.push(currentProduct);
                currentProduct = currentProduct.left;
            }

            currentProduct = productStack.pop();
            Product p = currentProduct.data;

            boolean reviewedByFirst  = false;
            boolean reviewedBySecond = false;

            Stack<BSTNode<Review>> reviewStack = new Stack<>();
            BSTNode<Review> currentReview = reviews_list.getRoot();

            while (currentReview != null || !reviewStack.isEmpty()) {

                while (currentReview != null) {
                    reviewStack.push(currentReview);
                    currentReview = currentReview.left;
                }

                currentReview = reviewStack.pop();
                Review r = currentReview.data;

                if (r.getProductID() == p.getProductId()) {
                    if (r.getCustomerID() == customerId1) reviewedByFirst  = true;
                    if (r.getCustomerID() == customerId2) reviewedBySecond = true;
                }

                currentReview = currentReview.right;
            }

            if (reviewedByFirst && reviewedBySecond && p.getAverageRating() > 4.0) {
                System.out.println("Product: " + p.getName()
                        + " | Avg Rating: " + String.format("%.2f", p.getAverageRating()));
                found = true;
            }

            currentProduct = currentProduct.right;
        }

        if (!found) System.out.println("No common high-rated products found.");
    }

    // helpers
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (input.hasNextInt()) {
                int v = input.nextInt();
                input.nextLine();
                return v;
            } else {
                System.out.println("Invalid input. Please enter a whole number.");
                input.nextLine();
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            if (input.hasNextDouble()) {
                double v = input.nextDouble();
                input.nextLine();
                return v;
            } else {
                System.out.println("Invalid input. Please enter a decimal number (e.g., 12.5).");
                input.nextLine();
            }
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return input.nextLine().trim();
    }

    private static boolean isValidName(String s) {
        if (s == null) return false;
        s = s.trim();
        if (s.isEmpty() || s.length() > 50) return false;
        return s.matches("[\\p{IsArabic}A-Za-z\\s\\-']+");
    }

    private static String readName(String prompt) {
        while (true) {
            System.out.print(prompt);
            String name = input.nextLine().trim().replaceAll("\\s+", " ");
            if (isValidName(name)) return name;
            System.out.println("Invalid name. Use letters only (Arabic/English). Spaces, '-' and ' are allowed.");
        }
    }

    private static LocalDate readDateFlexible(String prompt) {
        String[] patterns = {
            "yyyy-MM-dd", "yyyy-M-d", "yyyy-M-dd", "yyyy-MM-d",
            "yyyy/MM/dd", "yyyy/M/d", "yyyy/M/dd", "yyyy/MM/d"
        };
        while (true) {
            String s = readLine(prompt).trim();
            for (String p : patterns) {
                try {
                    java.time.format.DateTimeFormatter f =
                        java.time.format.DateTimeFormatter.ofPattern(p);
                    return LocalDate.parse(s, f);
                } catch (Exception ignore) {}
            }
            System.out.println("Is not valid");
        }
    }

    private static int readRating(String prompt) {
        while (true) {
            int r = readInt(prompt);
            if (r >= 1 && r <= 5) return r;
            System.out.println("Rating must be between 1 and 5. Try again.");
        }
    }

    private static int readNewProductId(String prompt) {
        ensureLoaded();
        while (true) {
            System.out.print(prompt);
            if (input.hasNextInt()) {
                int id = input.nextInt(); 
                input.nextLine();

                if (all_products != null && all_products.SearchProductById(id) != null) {
                    System.out.println("Product ID " + id + " Is exist before.");
                    continue;
                }
                if (id >= 101 && id <= 150) {
                    System.out.println("ID is within 101–150 (reserved range). Allowed because it does not currently exist.");
                    return id;
                }
                return id; 
            } else {
                System.out.println("Invalid input. Please enter a numeric ID.");
                input.nextLine();
            }
        }
    }

    private static int readExistingProductId(String prompt) {
        ensureLoaded();
        while (true) {
            System.out.print(prompt);
            if (input.hasNextInt()) {
                int id = input.nextInt(); 
                input.nextLine();
                if (all_products == null || all_products.SearchProductById(id) == null) {
                    System.out.println("Product ID " + id + " Is not exist.");
                    continue;
                }
                return id;
            } else {
                System.out.println("Invalid input. Please enter a numeric Product ID.");
                input.nextLine();
            }
        }
    }

    private static String readValidProductIds(String prompt) {
        ensureLoaded();
        while (true) {
            String s = readLine(prompt).trim();
            if (s.isEmpty()) { 
                System.out.println("Enter at least one product ID."); 
                continue; 
            }
            String[] parts = s.split(";");
            boolean ok = true;
            for (String part : parts) {
                try {
                    int id = Integer.parseInt(part.trim());
                    if (all_products == null || all_products.SearchProductById(id) == null) { 
                        ok = false; 
                        break; 
                    }
                } catch (NumberFormatException ex) { 
                    ok = false; 
                    break; 
                }
            }
            if (!ok) {
                System.out.println("Try again ^-^");
                continue;
            }
            return s;
        }
    }

    private static int readUniqueCustomerId(String prompt) {
        ensureLoaded();
        while (true) {
            int id = readInt(prompt);
            if (all_Customers != null && all_Customers.searchById(id) != null) {
                System.out.println("Customer with ID " + id + " already exists. Enter another ID.");
                continue;
            }
            return id;
        }
    }

    private static int readExistingCustomerId(String prompt) {
        ensureLoaded();
        while (true) {
            int id = readInt(prompt);
            if (all_Customers == null || all_Customers.searchById(id) == null) {
                System.out.println(" Customer ID " + id + " not found. Enter an existing customer ID.");
                continue;
            }
            return id;
        }
    }

    private static int readUniqueOrderId(String prompt) {
        ensureLoaded();
        while (true) {
            int id = readInt(prompt);
            if (all_Orders != null && all_Orders.searchOrderById(id) != null) {
                System.out.println("Order with ID " + id + " already exists. Enter another ID.");
                continue;
            }
            return id;
        }
    }

    private static int readUniqueReviewId(String prompt) {
        ensureLoaded();
        while (true) {
            int id = readInt(prompt);
            if (all_Reviews != null && all_Reviews.SearchReviewById(id) != null) {
                System.out.println("Review with ID " + id + " already exists. Enter another ID.");
                continue;
            }
            return id;
        }
    }

    // main
    public static void main(String[] args) {
        main e1 = new main();
        ensureLoaded(); 
        int choice;

        do {
            System.out.println("1: Show loaded file paths");
            System.out.println("2: Add Product");
            System.out.println("3: Add Customer");
            System.out.println("4: Add Order");
            System.out.println("5: Add Review");
            System.out.println("6: List all customers");
            System.out.println("7: Show top 3 products by average rating");
            System.out.println("8: Display all orders");
            System.out.println("9: Display all orders between 2 dates");
            System.out.println("10: Show common high-rated products for 2 customers");
            System.out.println("11: Exit");

            choice = readInt("Enter your choice: ");

            switch (choice) {
                case 1: {
                    ensureLoaded();
                    System.out.println("\nLoaded files:");
                    System.out.println("  Products : " + PRODUCTS_CSV);
                    System.out.println("  Customers: " + CUSTOMERS_CSV);
                    System.out.println("  Orders   : " + ORDERS_CSV);
                    System.out.println("  Reviews  : " + REVIEWS_CSV);
                    System.out.println("-----------------------------------\n");
                    break;
                }

                case 2: { 
                    int id      = readNewProductId("Enter NEW Product ID (<101 OR >150; if 101–150 it must NOT exist now): ");
                    String name = readName("Enter Product Name: ");
                    double price= readDouble("Enter Price: ");
                    int qty     = readInt("Enter Quantity: ");
                    Product p   = new Product(id, name, price, qty);
                    add_Product(p);
                    System.out.println("Product added successfully.");
                    break;
                }

                case 3: { 
                    int id      = readUniqueCustomerId("Enter Customer ID [new]: ");
                    String name = readName("Enter Customer Name: ");
                    String email= readLine("Enter Customer Email: ");
                    Customer c  = new Customer(id, name, email);

                    add_Customer(c);
                    System.out.println("Customer added successfully.");
                    break;
                }

                case 4: {
                    
                }

                    int oid      = readUniqueOrderId("Enter Order ID [new]: ");
                    int cid      = readExistingCustomerId("Enter Customer ID [existing]: ");
                    String prod  = readValidProductIds("Enter Product IDs (semicolon-separated, must exist): ");
                    
                    String[] prodList = prod.split(";");
                    boolean outOfStock = false;
                    for (String pIdStr : prodList) {
                        int pid = Integer.parseInt(pIdStr.trim());
                        Product p = all_products.SearchProductById(pid);
                        if (p.getStock() == 0) {
                            System.out.println("\nProduct " + pid + " is OUT OF STOCK.");
                            outOfStock = true;
                        }
                    }
                    if (outOfStock) break; // ارجع للقائمة الرئيسية بدون متابعة باقي البيانات

                    // إذا كل المنتجات متوفرة
                    double total = readDouble("Enter Total Price: ");
                    LocalDate date = readDateFlexible("Enter Order Date (e.g., 2025-2-3): ");
                    String status = readLine("Enter Status (Pending/Processing/Shipped/Delivered/Cancelled/Returned): ");

                    Order o = new Order(oid, cid, prod, total, date, status);
                    add_Order(o);
                    System.out.println("Order added successfully.");
                    break;
                
                case 5: { 
                    int rid     = readUniqueReviewId("Enter Review ID [new]: ");
                    int pid     = readExistingProductId("Enter Product ID [existing]: ");
                    int cid1     = readExistingCustomerId("Enter Customer ID [existing]: ");
                    int rating  = readRating("Enter Rating (1–5): ");
                    String comment = readLine("Enter Comment: ");
                    Review r = new Review(rid, pid, cid1, rating, comment);
                    add_Review(r);
                    System.out.println("Review added successfully.");
                    break;
                }

                case 6:{
                    ensureLoaded();
                    all_Customers.displayAll();
                    break;
                }

                case 7:{
                    e1.displayTop3Products();
                    break;
                }

                case 8:{
                    ensureLoaded();
                    all_Orders.displayAllOrders();
                    break;
                }
                case 9:{
                    ensureLoaded();
                    System.out.println("Display all orders between 2 dates:");

                    LocalDate startDate = readDateFlexible("Enter start date (e.g., 2025-2-1): ");
                    LocalDate endDate   = readDateFlexible("Enter end date   (e.g., 2025-2-9): ");

                    Stack<BSTNode<Order>> stack = new Stack<>();
                    BSTNode<Order> current = all_Orders.get_Orders().getRoot(); 

                    boolean any = false;
                    System.out.println("Orders between " + startDate + " and " + endDate + ":");
                    while (current != null || !stack.isEmpty()) {

                        while (current != null) {
                            stack.push(current);
                            current = current.left;
                        }

                        current = stack.pop();
                        Order o1 = current.data;

                        if (!o1.getOrderDate().isBefore(startDate) && !o1.getOrderDate().isAfter(endDate)) {
                            System.out.println("OrderID: " + o1.getOrderId()
                                + " | CustomerID: " + o1.getCustomerId()
                                + " | Products: " + o1.getProd_Ids()
                                + " | TotalPrice: " + o1.getTotalPrice()
                                + " | Date: " + o1.getOrderDate()
                                + " | Status: " + o1.getStatus());
                            any = true;
                        }

                        current = current.right;
                    }

                    if (!any) System.out.println("No results.");
                    System.out.println("-----------------------------------");
                    break;
                }

                case 10: {
                    int c1 = readExistingCustomerId("Enter first customer ID [existing]: ");
                    int c2 = readExistingCustomerId("Enter second customer ID [existing]: ");
                    showCommonHighRatedProducts(c1, c2);
                    break;
                }

                case 11:{
                    System.out.println("Goodbye! ^-^");
                    break;
                }

                default:{
                    System.out.println("Unknown choice.");
                    break;
                }
            }
        } while (choice != 11);

        input.close();
    }
}
