package project212.phase2.AVL;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class Customers {
    private AVL<Customer> customers;
    private String filePath;

    public Customers() {
        customers = new AVL<>();
    }

    public Customers(AVL<Customer> input_customers) {
        customers = input_customers;
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

    public AVL<Customer> get_customers() {
        return customers;
    }

    public Customer searchById(int id) {
        if (customers.empty()) return null;
        return customers.findKey(id) ? customers.retrieve() : null;
    }

    public void addCustomer(Customer c) {
        boolean inserted = customers.insert(c.getCustomerId(), c);

        if (inserted) {
            if (main.VERBOSE) System.out.println("Customer added: " + c.getName());
            saveAll();
        } else {
            if (main.VERBOSE) System.out.println("Customer ID already exists!");
        }
    }

    public void displayAll() {
        System.out.println("=== All customers ===");
        if (customers.empty()) {
            System.out.println("No customers exist");
            return;
        }
        inOrder_all_customers(customers.getRoot());
    }

    private void inOrder_all_customers(BSTNode<Customer> c) {
        if (c == null) return;
        inOrder_all_customers(c.left);
        c.data.display();
        inOrder_all_customers(c.right);
    }

    public static Customer convert_String_to_Customer(String Line) {
        String[] a = Line.split(",");
        return new Customer(
                Integer.parseInt(a[0].trim()),
                a[1].trim(),
                a[2].trim()
        );
    }

    public void loadCustomers(String fileName) {
        try {
            filePath = fileName;
            Scanner read = new Scanner(new File(fileName));

            if (read.hasNextLine()) read.nextLine();

            while (read.hasNextLine()) {
                String line = read.nextLine().trim();
                if (line.isEmpty()) continue;

                Customer c = convert_String_to_Customer(line);
                customers.insert(c.getCustomerId(), c);
            }

            read.close();

            if (main.VERBOSE) System.out.println("Customers loaded.");

        } catch (Exception e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }
    }

    private void saveAll() {
        if (filePath == null || filePath.isEmpty()) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            pw.println("customerId,name,email");

            if (!customers.empty()) {
                Stack<BSTNode<Customer>> stack = new Stack<>();
                BSTNode<Customer> current = customers.getRoot();

                while (current != null || !stack.isEmpty()) {

                    while (current != null) {
                        stack.push(current);
                        current = current.left;
                    }

                    current = stack.pop();
                    Customer c = current.data;

                    pw.println(
                        c.getCustomerId() + "," +
                        c.getName() + "," +
                        c.getEmail()
                    );

                    current = current.right;
                }
            }

        } catch (Exception e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

    public void saveCustomers(String customersCsv) {
        if (customers == null || customers.empty()) {
            System.out.println("No customers to save.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(customersCsv))) {
            Stack<BSTNode<Customer>> stack = new Stack<>();
            BSTNode<Customer> current = customers.getRoot();

            while (current != null || !stack.isEmpty()) {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

                current = stack.pop();
                Customer c = current.data;

                // كتابة العميل بصيغة CSV:
                // customerId,name,email
                pw.println(c.getCustomerId() + "," 
                        + c.getName() + "," 
                        + c.getEmail());

                current = current.right;
            }

            System.out.println("Customers saved successfully to: " + customersCsv);
        } catch (Exception e) {
            System.out.println("Error saving customers: " + e.getMessage());
        }
    }

}
