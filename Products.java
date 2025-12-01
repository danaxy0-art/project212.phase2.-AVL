package project212.phase2.AVL;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.Stack;

public class Products {

    private AVL<Product> products;
    private String filePath;

    public Products(AVL<Product> input_products) {
        products = input_products;
    }

    public Products() {
        products = new AVL<>();
    }

    public void setFilePath(String path) {
        this.filePath = path;
    }

    public AVL<Product> get_Products() {
        return products;
    }

    public Product SearchProductById(int id) {
        if (products.empty()) return null;
        return products.findKey(id) ? products.retrieve() : null;
    }

    public void addProduct(Product p) {
        boolean inserted = products.insert(p.getProductId(), p);
        if (inserted) {
            if (main.VERBOSE) System.out.println("Product added: " + p.getName());
            saveProducts(filePath);
        }
    }

    public void removeProduct(int id) {
        boolean removed = products.removeKey(id);
        if (removed) {
            if (main.VERBOSE) System.out.println("Product removed: " + id);
            saveProducts(filePath);
        }
    }

    public void updateProduct(int id, Product p) {
        Product old = SearchProductById(id);
        if (old == null) {
            if (main.VERBOSE) System.out.println("Product does not exist");
        } else {
            old.UpdateProduct(p);
            saveProducts(filePath);
        }
    }

    public void displayOutOfStock() {
        System.out.println("Out of stock products:");
        if (products.empty()) {
            System.out.println("no products exist");
        } else {
            inOrderOutOfStock(products.getRoot());
        }
    }

    private void inOrderOutOfStock(BSTNode<Product> p) {
        if (p == null) return;
        inOrderOutOfStock(p.left);
        if (p.data.getStock() == 0) {
            System.out.println("key = " + p.key);
            System.out.println(p.data);
        }
        inOrderOutOfStock(p.right);
    }

    public void displayAllProducts() {
        System.out.println("All Products");
        if (products.empty()) {
            System.out.println("no products exist");
            return;
        }
        inOrderAllProducts(products.getRoot());
    }

    private void inOrderAllProducts(BSTNode<Product> p) {
        if (p == null) return;
        inOrderAllProducts(p.left);
        System.out.println(p.data.toString());
        p.data.displayReviews();
        inOrderAllProducts(p.right);
    }

    public void assign(Review r) {
        Product p = SearchProductById(r.getProductID());
        if (p != null) p.addReview(r);
    }

    public static Product convert_String_to_product(String Line) {
        String[] a = Line.split(",", 4);
        return new Product(
                Integer.parseInt(a[0].trim()),
                a[1].trim(),
                Double.parseDouble(a[2].trim()),
                Integer.parseInt(a[3].trim())
        );
    }

    public void loadProducts(String fileName) {
        try {
            filePath = fileName;
            Scanner read = new Scanner(new File(fileName));

            if (read.hasNextLine()) read.nextLine();

            while (read.hasNextLine()) {
                String line = read.nextLine().trim();
                if (!line.isEmpty()) {
                    Product p = convert_String_to_product(line);
                    products.insert(p.getProductId(), p);
                }
            }
            read.close();

            if (main.VERBOSE) System.out.println("Products loaded.");

        } catch (Exception e) {
            System.out.println("Error reading products: " + e.getMessage());
        }
    }
    public void saveProducts(String productsCsv) {
        if (productsCsv == null || productsCsv.isEmpty()) return;

        try (PrintWriter pw = new PrintWriter(new FileWriter(productsCsv))) {

            pw.println("productId,name,price,stock");

            if (!products.empty()) {
                Stack<BSTNode<Product>> stack = new Stack<>();
                BSTNode<Product> current = products.getRoot();

                while (current != null || !stack.isEmpty()) {

                    while (current != null) {
                        stack.push(current);
                        current = current.left;
                    }

                    current = stack.pop();
                    Product p = current.data;

                    pw.println(
                            p.getProductId() + "," +
                            p.getName() + "," +
                            p.getPrice() + "," +
                            p.getStock()
                    );

                    current = current.right;
                }
            }

            if (main.VERBOSE) 
                System.out.println("Products saved successfully to: " + productsCsv);

        } catch (Exception e) {
            System.out.println("Error saving products: " + e.getMessage());
        }
    }
}
