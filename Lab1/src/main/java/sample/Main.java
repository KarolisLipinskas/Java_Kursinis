package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Cart;
import model.Customer;
import model.Product;

import java.time.LocalDate;

/**
 * Start the app
 */
public class Main /*extends Application*/ {

    /*@Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }*/

    public static void main(String[] args) {
        //launch(args);

        Product bike = new Product(1, "bike", 525.99, 3);
        Product brakes = new Product(2, "brakes", 50.00, 2);
        Product fork = new Product(3, "Fork", 299.99, 3);

        Customer customer = new Customer(1, "C1", "Pass", "Name", "Surname", LocalDate.parse("2020-01-01"), "1111");

        Cart cart1 = new Cart(1);
        cart1.addProduct(bike);
        cart1.addProduct(fork);

        Cart cart2 = new Cart(2);
        cart2.addProduct(brakes);

        customer.addCart(cart1);
        cart1.setStatus("paid");

        customer.addCart(cart2);

        System.out.println(customer);
    }
}
