package project212.phase2.AVL;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.Stack;

public class Orders {

    private AVL<Order> all_orders;      
    private Customers all_Customers;    
    private String filePath;            

    static DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Orders(AVL<Customer> input_customers, AVL<Order> ordersInput) {
        all_Customers = new Customers(input_customers);
        this.all_orders = ordersInput;
    }

    public Orders() {
        all_Customers = new Customers();
        all_orders = new AVL<>();
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

    public AVL<Order> get_Orders() {
        return all_orders;
    }

    public Order searchOrderById(int id) {
        if (all_orders.empty()) return null;
        return all_orders.findKey(id) ? all_orders.retrieve() : null;
    }

    public void removeOrder(int id) {
        boolean removed = all_orders.removeKey(id);
        if (removed && main.VERBOSE) System.out.println("Order removed: " + id);
        if (removed) saveAll();
    }

    public void assign(Order ord) {
        Customer p = all_Customers.searchById(ord.getCustomerId());
        if (p != null) {
            p.addOrder(ord);
        } else if (main.VERBOSE) {
            System.out.println("Customer does not exist");
        }
    }

    public void addOrder(Order ord) {
        boolean inserted = all_orders.insert(ord.getOrderId(), ord);

        if (inserted) {
            if (main.VERBOSE) System.out.println("Order added: " + ord.getOrderId());
            assign(ord);
            saveAll();
        } else if (main.VERBOSE) {
            System.out.println("Order already exists");
        }
    }

    public static Order convert_String_to_order(String line) {
        String[] a = line.split(",", 6);

        int orderId     = Integer.parseInt(a[0].trim().replace("\"",""));
        int customerId  = Integer.parseInt(a[1].trim().replace("\"",""));
        String prodIds  = a[2].trim().replace("\"","");
        double total    = Double.parseDouble(a[3].trim().replace("\"",""));
        LocalDate date  = LocalDate.parse(a[4].trim().replace("\"",""), df);
        String status   = a[5].trim().replace("\"","");

        return new Order(orderId, customerId, prodIds, total, date, status);
    }

    public void loadOrders(String fileName) {
        try {
            filePath = fileName;
            Scanner read = new Scanner(new File(fileName));

            if (read.hasNextLine()) read.nextLine();

            while (read.hasNextLine()) {
                String line = read.nextLine().trim();
                if (line.isEmpty()) continue;

                Order ord = convert_String_to_order(line);
                all_orders.insert(ord.getOrderId(), ord);
            }

            read.close();

            if (main.VERBOSE) System.out.println("Orders loaded.");

        } catch (Exception e) {
            System.out.println("Error loading orders: " + e.getMessage());
        }
    }

    public void displayAllOrders() {
        if (all_orders.empty()) {
            System.out.println("No orders found!");
            return;
        }

        System.out.println("OrderID\tCustomerID\tProductIDs\tTotalPrice\tDate\t\tStatus");
        System.out.println("====================================================================");

        inOrderTraversal(all_orders.getRoot());
    }

    private void inOrderTraversal(BSTNode<Order> node) {
        if (node == null) return;

        inOrderTraversal(node.left);

        System.out.println(node.data);

        inOrderTraversal(node.right);
    }

    public void displayAllOrders_between2dates(LocalDate d1, LocalDate d2) {
        if (all_orders.empty()) {
            System.out.println("No orders found.");
            return;
        }

        System.out.println("Orders between " + d1 + " and " + d2 + ":");

        boolean any = false;

        Stack<BSTNode<Order>> stack = new Stack<>();
        BSTNode<Order> current = all_orders.getRoot();

        while (current != null || !stack.isEmpty()) {

            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();

            Order o = current.data;

            if (!o.getOrderDate().isBefore(d1) && !o.getOrderDate().isAfter(d2)) {
                System.out.println(o.getOrderId());
                any = true;
            }

            current = current.right;
        }

        if (!any) System.out.println("No results.");
        System.out.println("==============================================");
    }

    private void saveAll() {
        if (filePath == null || filePath.isEmpty()) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            pw.println("orderId,customerId,productIds,totalPrice,date,status");

            if (!all_orders.empty()) {
                Stack<BSTNode<Order>> stack = new Stack<>();
                BSTNode<Order> current = all_orders.getRoot();

                while (current != null || !stack.isEmpty()) {

                    while (current != null) {
                        stack.push(current);
                        current = current.left;
                    }

                    current = stack.pop();
                    Order o = current.data;

                    pw.println(
                        o.getOrderId() + "," +
                        o.getCustomerId() + "," +
                        o.getProd_Ids() + "," +
                        o.getTotalPrice() + "," +
                        o.getOrderDate() + "," +
                        o.getStatus()
                    );

                    current = current.right;
                }
            }

        } catch (Exception e) {
            System.out.println("Error saving orders: " + e.getMessage());
        }
    }
}