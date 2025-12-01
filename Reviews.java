package project212.phase2.AVL;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class Reviews {

    private AVL<Review> reviews;
    private Products all_products;
    private Customers all_Customers;
    private String filePath;

    public void setFilePath(String path) { this.filePath = path; }

    public Reviews(AVL<Review> reviews,AVL<Product> input_products, AVL<Customer> input_customers) {
        this.reviews = reviews;
        all_products = new Products(input_products);
        all_Customers = new Customers(input_customers);
    }

    public Reviews() {
        reviews = new AVL<>();
        all_products = new Products();
        all_Customers = new Customers();
    }

    public AVL<Review> get_all_Reviews() { return reviews; }
    public Products get_all_Products() { return all_products; }

    public Review SearchReviewById(int id) {
        return reviews.findKey(id) ? reviews.retrieve() : null;
    }

    public void assign_to_product(Review r) {
        Product p = all_products.SearchProductById(r.getProductID());
        if (p != null) p.addReview(r);
    }

    public void assign_to_customer(Review r) {
        Customer c = all_Customers.searchById(r.getCustomerID());
        if (c != null) c.addReview(r);
    }

    public void addReview(Review r) {
        boolean inserted = reviews.insert(r.getReviewID(), r);

        if (inserted) {
            if (main.VERBOSE) System.out.println("Review added: " + r.getReviewID());
            assign_to_product(r);
            assign_to_customer(r);
            saveAll();
        } else {
            if (main.VERBOSE) System.out.println("Review already exists");
        }
    }

    public void updateReview(int id, Review p) {
        Review old = SearchReviewById(id);
        if (old != null) {
            old.UpdateReview(p);
            saveAll();
        } else if (main.VERBOSE) {
            System.out.println("Review does not exist");
        }
    }

    public void displayAllReviews() {
        if (reviews.empty()) {
            System.out.println("No reviews available");
            return;
        }

        System.out.println("All Reviews:");
        System.out.println("============================================");

        Stack<BSTNode<Review>> stack = new Stack<>();
        BSTNode<Review> current = reviews.getRoot();

        while (current != null || !stack.isEmpty()) {

            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            System.out.println(current.data);

            current = current.right;
        }

        System.out.println("============================================");
    }

    public static Review convert_String_to_Review(String line) {
        String[] a = line.split(",", 5);
        return new Review(
            Integer.parseInt(a[0].trim()),
            Integer.parseInt(a[1].trim()),
            Integer.parseInt(a[2].trim()),
            Integer.parseInt(a[3].trim()),
            a[4].trim()
        );
    }

    public void load_revews(String fileName) {
        try {
            filePath = fileName;
            Scanner read = new Scanner(new File(fileName));

            if (read.hasNextLine()) read.nextLine();

            while (read.hasNextLine()) {
                String line = read.nextLine().trim();
                if (!line.isEmpty()) {
                    Review r = convert_String_to_Review(line);
                    reviews.insert(r.getReviewID(), r);
                }
            }

            read.close();

            if (main.VERBOSE) System.out.println("Reviews loaded.");

        } catch (Exception e) {
            System.out.println("Error reading reviews: " + e.getMessage());
        }
    }

    private void saveAll() {
        if (filePath == null || filePath.isEmpty()) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath))) {

            pw.println("reviewId,productId,customerId,rating,comment");

            Stack<BSTNode<Review>> stack = new Stack<>();
            BSTNode<Review> current = reviews.getRoot();

            while (current != null || !stack.isEmpty()) {

                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

                current = stack.pop();
                Review r = current.data;

                pw.println(
                    r.getReviewID() + "," +
                    r.getProductID() + "," +
                    r.getCustomerID() + "," +
                    r.getRating() + "," +
                    r.getComment()
                );

                current = current.right;
            }

        } catch (Exception e) {
            System.out.println("Error saving reviews: " + e.getMessage());
        }
    }

    public void saveReviews(String reviewsCsv) {
        if (reviews == null || reviews.empty()) {
            System.out.println("No reviews to save.");
            return;
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(reviewsCsv))) {
            Stack<BSTNode<Review>> stack = new Stack<>();
            BSTNode<Review> current = reviews.getRoot();

            while (current != null || !stack.isEmpty()) {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

                current = stack.pop();
                Review r = current.data;

                
                pw.println(r.getReviewID() + "," 
                        + r.getProductID() + "," 
                        + r.getCustomerID() + "," 
                        + r.getRating() + "," 
                        + r.getComment());

                current = current.right;
            }

            System.out.println("Reviews saved successfully to: " + reviewsCsv);
        } catch (Exception e) {
            System.out.println("Error saving reviews: " + e.getMessage());
        }
    }

}
