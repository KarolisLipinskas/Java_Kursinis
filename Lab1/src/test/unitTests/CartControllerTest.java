package unitTests;

import static org.junit.Assert.*;
import static start.Main.isTest;

import model.controllers.CartController;
import model.entities.Cart;
import model.entities.Customer;
import model.entities.Product;
import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.List;

public class CartControllerTest {
    private CartController cartController;

    private Cart mockCart;

    @Before
    public void setUp() {
        isTest = true;

        cartController = new CartController();

        // Mock Table Data
        cartController.data = FXCollections.observableArrayList();

        // Mock Cart
        mockCart = new Cart();
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Product1", "TestProduct", 250.49, 2));
        productList.add(new Product("Product2", "TestProduct", 399.99, 3));
        mockCart.setProducts(productList);
        mockCart.setStatus("open");
    }

    @Test
    public void testLoadData_SuccessfulLoad() {
        // Call the method
        cartController.loadData(mockCart);

        // Assert the data content
        assertEquals(2, cartController.data.size());
        assertEquals("Product1", cartController.data.get(0).getName());
        assertEquals("Product2", cartController.data.get(1).getName());
        assertEquals("250.49", cartController.data.get(0).getPrice());
        assertEquals("399.99", cartController.data.get(1).getPrice());

        // Verify total price calculation
        assertEquals(650.48, mockCart.getPrice(), 0.00001);
    }

    @Test
    public void testLoadData_NullCart() {
        // Call the method with null cart
        cartController.loadData(null);

        // Verify no data is added
        assertTrue(cartController.data.isEmpty());
    }

    @Test
    public void testLoadData_EmptyProducts() {
        // Cart with no products
        mockCart.setProducts(new ArrayList<>());

        // Call the method
        cartController.loadData(mockCart);

        // Verify no data is added
        assertTrue(cartController.data.isEmpty());
    }

    @Test
    public void testInitData_NoCart() {
        cartController.initData("Test");

        // Verify no data is added
        assertTrue(cartController.data.isEmpty());
    }

    @Test
    public void testInitData_WithCart() {
        cartController.initData("Test", mockCart);

        // Assert the data content
        assertEquals(2, cartController.data.size());
        assertEquals("Product1", cartController.data.get(0).getName());
        assertEquals("Product2", cartController.data.get(1).getName());
        assertEquals("250.49", cartController.data.get(0).getPrice());
        assertEquals("399.99", cartController.data.get(1).getPrice());

        // Verify total price calculation
        assertEquals(650.48, mockCart.getPrice(), 0.00001);
    }

    @Test
    public void testGetLastCart_CartListIsNull() {
        // Arrange
        Customer customer = new Customer();
        customer.setCartList(null);

        // Act
        Cart result = cartController.getLastCart(customer);

        // Assert
        assertNull("Expected null when cart list is null.", result);
    }

    @Test
    public void testGetLastCart_CartListIsEmpty() {
        // Arrange
        Customer customer = new Customer();
        customer.setCartList(new ArrayList<>());

        // Act
        Cart result = cartController.getLastCart(customer);

        // Assert
        assertNull("Expected null when cart list is empty.", result);
    }

    @Test
    public void testGetLastCart_CartListHasCarts() {
        // Arrange
        Customer customer = new Customer();
        List<Cart> cartList = new ArrayList<>();
        Cart cart1 = new Cart();
        Cart cart2 = mockCart;

        cartList.add(cart1);
        cartList.add(cart2);
        customer.setCartList(cartList);

        // Act
        Cart result = cartController.getLastCart(customer);

        // Assert
        assertNotNull("Expected a non-null cart.", result);
        assertEquals("Expected the last cart to be returned.", cart2, result);
    }
}
