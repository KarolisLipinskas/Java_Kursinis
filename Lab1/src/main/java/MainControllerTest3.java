import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.HibernateCart;
import model.HibernateCustomer;
import model.HibernateProduct;
import model.controllers.MainController;
import model.entities.Cart;
import model.entities.Customer;
import model.entities.Product;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainControllerTest3 {

    private MainController controller;

    @BeforeAll
    static void initJFX() {
        // Initialize JavaFX toolkit
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        controller = new MainController();

        // Initialize UI components manually
        controller.priceMin = new TextField();
        controller.priceMax = new TextField();
        controller.quantityField = new TextField();
        controller.types = new ComboBox<>();
        controller.table = new TableView<>();
        controller.name = new TableColumn<>();
        controller.type = new TableColumn<>();
        controller.quantity = new TableColumn<>();
        controller.warranty = new TableColumn<>();
        controller.price = new TableColumn<>();
        controller.customerId = new Label();

        // Initialize ObservableList for table
        controller.data = FXCollections.observableArrayList();

        // Mock initialization
        controller.hibernateProduct = new HibernateProduct(null); // Set null EntityManager for testing
        controller.hibernateCustomer = new HibernateCustomer(null);
        controller.hibernateCart = new HibernateCart(null);
    }

    @Test
    void testInitData() {
        controller.initData("123");
        assertEquals("123", controller.customerId.getText());
    }

    @Test
    void testCreateNewProduct() {
        // No actual persistence is done, but ensure method doesn't throw an exception
        assertDoesNotThrow(() -> controller.createNewProduct());
    }



    @Test
    void testLoadTable() {
        // Simulate products
        controller.hibernateProduct = new HibernateProduct(null) {
            @Override
            public List<Product> getAllProducts() {
                ObservableList<Product> products = FXCollections.observableArrayList();
                products.add(new Product("Bike v1", "Bike", 70.99, 3));
                return products;
            }
        };

        controller.loadTable("All", 0.0, 100.0);

        // Check that data is loaded into the table
        assertFalse(controller.data.isEmpty());
        assertEquals(1, controller.data.size());
        assertEquals("Bike v1", controller.data.get(0).getName());
    }


    @Test
    void testGetOpenCartWithNoCart() {
        Customer customer = new Customer();
        customer.setCartList(null);

        assertNull(controller.getOpenCart(customer));
    }

    @Test
    void testGetOpenCartWithOpenCart() {
        Customer customer = new Customer();
        Cart cart = new Cart("open", customer);
        customer.setCartList(FXCollections.observableArrayList(cart));

        Cart result = controller.getOpenCart(customer);
        assertNotNull(result);
        assertEquals("open", result.getStatus());
    }

    @Test
    void testFilterWithValidInput() {
        controller.priceMin.setText("10");
        controller.priceMax.setText("100");
        controller.types.setValue("All");

//        assertDoesNotThrow(() -> controller.filter(null));
 }


    @Test
    void testGetStage() {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/cart.fxml"));

//        assertThrows(IOException.class, () -> controller.getStage(loader, "Test Stage"));
}

    @Test
    void testGetOpenCartWithNoOpenCart() {
        // Arrange: Create a customer without an open cart
        controller = new MainController();
        Customer customer = new Customer("1", "Test Customer");
        customer.setCartList(new ArrayList<>());  // No cart available

        // Act: Try to get an open cart
        Cart cart = controller.getOpenCart(customer);

        // Assert: Verify that no open cart is returned
        assertNull(cart, "If no open cart exists, the method should return null.");
    }


    @Test
    void testGetOpenCartReturnsCorrectCart() {
        controller = new MainController();
        controller.data = FXCollections.observableArrayList();
        // Arrange: Create a customer with multiple carts
        Customer customer = new Customer("1", "John Doe");
        Cart cart1 = new Cart("closed", customer);
        Cart cart2 = new Cart("open", customer);
        customer.setCartList(List.of(cart1, cart2));

        // Act: Find the open cart
        Cart openCart = controller.getOpenCart(customer);

        // Assert: Verify the open cart is returned
        assertNotNull(openCart);
        assertEquals("open", openCart.getStatus());
    }

//
//    @Test
//    void testCloseStage() {
//        Stage mockStage = new Stage();
//        Scene scene = new Scene(new Label());
//        mockStage.setScene(scene);
//        controller.priceMax.setScene(scene);
//
//        assertDoesNotThrow(() -> controller.closeStage());
//    }
//
//
//
//
//    @Test
//    void testAddToCartWithValidQuantity() {
//        // Simulate UI values
//        controller.quantityField.setText("2");
//        controller.customerId.setText("123");
//
//        ProductTableParameters productParam = new ProductTableParameters("1", "Bike v1", "Bike", "inf", "2", "70.99");
//        controller.table.getItems().add(productParam);
//        controller.table.getSelectionModel().select(productParam);
//
//        // Simulate customer and product retrieval
//        controller.hibernateProduct = new HibernateProduct(null) {
//            @Override
//            public Product getProduct(int id) {
//                return new Product("Bike v1", "Bike", 70.99, 3);
//            }
//        };
//        controller.hibernateCustomer = new HibernateCustomer(null) {
//            @Override
//            public Customer getCustomer(String id) {
//                return new Customer();
//            }
//        };
//
//        // Act
//        assertDoesNotThrow(() -> controller.addToCart(null));
//    }
//
//
//
//    @Test
//    void testAddToCartWithValidQuantity() {
//        // Simulate UI values
//        controller.quantityField.setText("2");
//        controller.customerId.setText("123");
//
//        ProductTableParameters productParam = new ProductTableParameters("1", "Bike v1", "Bike", "inf", "2", "70.99");
//        controller.table.getItems().add(productParam);
//        controller.table.getSelectionModel().select(productParam);
//
//        // Simulate customer and product retrieval
//        controller.hibernateProduct = new HibernateProduct(null) {
//            @Override
//            public Product getProduct(int id) {
//                return new Product("Bike v1", "Bike", 70.99, 3);
//            }
//        };
//        controller.hibernateCustomer = new HibernateCustomer(null) {
//            @Override
//            public Customer getCustomer(String id) {
//                return new Customer();
//            }
//        };
//
//        // Act
//        assertDoesNotThrow(() -> controller.addToCart(null));
//    }
//
//        @Test
//    void testInitialize() {
//        controller.initialize(null, null);
//
//        // Check that the combo box is populated with expected values
//        ObservableList<String> expectedItems = FXCollections.observableArrayList("All", "Bike", "Brakes", "Fork", "Frame", "Handlebars", "Pedals", "Shock", "Wheels");
//        assertEquals(expectedItems, controller.types.getItems());
//    }

}
