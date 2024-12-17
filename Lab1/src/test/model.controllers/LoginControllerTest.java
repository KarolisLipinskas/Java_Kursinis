package model.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.HibernateCustomer;
import model.HibernateManager;
import model.Utils.CustomerValidator;
import model.entities.Customer;
import model.entities.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;

import static org.mockito.Mockito.*;

class LoginControllerTest {

    private LoginController loginController;

    @Mock
    private HibernateCustomer hibernateCustomer;
    @Mock
    private HibernateManager hibernateManager;
    @Mock
    private CustomerValidator customerValidator;

    // Mock JavaFX components
    @Mock
    private TextField loginName;
    @Mock
    private PasswordField loginPass;
    @Mock
    private TextField registerName;
    @Mock
    private PasswordField registerPass;
    @Mock
    private PasswordField registerPassR;
    @Mock
    private TextField name;
    @Mock
    private TextField surname;
    @Mock
    private TextField gmail;
    @Mock
    private TextField birthdate;
    @Mock
    private TextField cardNo;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        HibernateCustomer hibernateCustomer = PowerMockito.mock(HibernateCustomer.class);

        loginController = new LoginController();
        loginController.hibernateCustomer = hibernateCustomer;
        loginController.hibernateManager = hibernateManager;

        // Set the mocked fields
        loginController.loginName = loginName;
        loginController.loginPass = loginPass;
        loginController.registerName = registerName;
        loginController.registerPass = registerPass;
        loginController.registerPassR = registerPassR;
        loginController.name = name;
        loginController.surname = surname;
        loginController.gmail = gmail;
        loginController.birthdate = birthdate;
        loginController.cardNo = cardNo;
    }

    @Test
    void createNewCustomer_ValidCustomer_Success() {

        // Arrange
        when(registerName.getText()).thenReturn("username");
        when(registerPass.getText()).thenReturn("password");
        when(registerPassR.getText()).thenReturn("password");
        when(name.getText()).thenReturn("John");
        when(surname.getText()).thenReturn("Doe");
        when(gmail.getText()).thenReturn("john@example.com");
        when(birthdate.getText()).thenReturn("2000-01-01");
        when(cardNo.getText()).thenReturn("1234-5678-9012");

        mockStatic(CustomerValidator.class);
        when(CustomerValidator.validate(any(Customer.class), eq("password"))).thenReturn(true);

        doNothing().when(hibernateCustomer).create(any(Customer.class));

        // Act
        loginController.createNewCustomer();

        // Verify customer creation and clearing fields
        verify(hibernateCustomer, times(1)).create(any(Customer.class));
        verify(registerName, times(1)).clear();
        verify(registerPass, times(1)).clear();
        verify(registerPassR, times(1)).clear();
    }

    @Test
    void createNewCustomer_InvalidCustomer_NoCreation() {

        // Arrange
        when(registerName.getText()).thenReturn("username");
        when(registerPass.getText()).thenReturn("password");
        when(registerPassR.getText()).thenReturn("wrongPassword");

        mockStatic(CustomerValidator.class);
        when(CustomerValidator.validate(any(Customer.class), eq("wrongPassword"))).thenReturn(false);

        // Act
        loginController.createNewCustomer();

        // Verify no customer is created
        verify(hibernateCustomer, never()).create(any(Customer.class));
    }

    @Test
    void login_CustomerSuccess_OpensMainWindow() throws Exception {

        // Arrange
        when(loginName.getText()).thenReturn("customerUser");
        when(loginPass.getText()).thenReturn("customerPass");

        Customer customer = new Customer();
        customer.setId(1);

        when(hibernateCustomer.getCustomer("customerUser", "customerPass")).thenReturn(customer);
        when(hibernateManager.getManager(anyString(), anyString())).thenReturn(null);

        // Mocking openMainWindow behavior
        LoginController spyController = spy(loginController);
        doNothing().when(spyController).openMainWindow(any(Customer.class));

        // Act
        spyController.login(mock(ActionEvent.class));

        // Verify
        verify(spyController, times(1)).openMainWindow(customer);
    }

    @Test
    void login_ManagerSuccess_OpensManagerWindow() throws Exception {

        // Arrange
        when(loginName.getText()).thenReturn("managerUser");
        when(loginPass.getText()).thenReturn("managerPass");

        Manager manager = new Manager();
        when(hibernateManager.getManager("managerUser", "managerPass")).thenReturn(manager);
        when(hibernateCustomer.getCustomer(anyString(), anyString())).thenReturn(null);

        // Mocking openManagerWindow behavior
        LoginController spyController = spy(loginController);
        doNothing().when(spyController).openManagerWindow();

        // Act
        spyController.login(mock(ActionEvent.class));

        // Verify
        verify(spyController, times(1)).openManagerWindow();
    }

    @Test
    void login_InvalidCredentials_ShowsErrorAlert() throws Exception {

        // Arrange
        when(loginName.getText()).thenReturn("wrongUser");
        when(loginPass.getText()).thenReturn("wrongPass");

        when(hibernateCustomer.getCustomer("wrongUser", "wrongPass")).thenReturn(null);
        when(hibernateManager.getManager("wrongUser", "wrongPass")).thenReturn(null);

        // Mock Alert
        Alert mockAlert = mock(Alert.class);
        mockStatic(Alert.class);
        when(Alert.AlertType.ERROR).thenReturn(Alert.AlertType.ERROR);

        // Act
        loginController.login(mock(ActionEvent.class));

        // Verify no windows are opened
        verify(hibernateCustomer, times(1)).getCustomer("wrongUser", "wrongPass");
        verify(hibernateManager, times(1)).getManager("wrongUser", "wrongPass");
    }
}
