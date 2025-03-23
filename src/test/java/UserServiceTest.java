import com.revature.models.User;
import com.revature.repos.UserDAOImpl;
import com.revature.repos.interfaces.UserDAO;
import com.revature.services.UserService;
import com.revature.util.PasswordUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;

import static org.mockito.Mockito.when;

public class UserServiceTest {

    private UserService userService;
    private UserDAO mockDAO;

    @Before
    public void setup() {
        mockDAO = Mockito.mock(UserDAO.class);
        userService = new UserService(mockDAO);
    }

    // Validate the email
    @Test
    public void validEmailShouldReturnTrue() {
        String testEmail = "valid.email@gmail.com";
        boolean result = userService.validateEmail(testEmail);
        Assert.assertTrue(result);
    }

    @Test
    public void invalidEmailShouldReturnFalse() {
        String testEmail = "@gmail.com";
        boolean result = userService.validateEmail(testEmail);
        Assert.assertFalse(result);
    }

    // Validates the password
    @Test
    public void validPasswordShouldReturnTrue() {
        String testPassword = "ValidPassword1";
        boolean result = userService.validatePassword(testPassword);
        Assert.assertTrue(result);
    }

    @Test
    public void tooShortPasswordShouldReturnFalse() {
        String testPassword = "Short1";
        boolean result = userService.validatePassword(testPassword);
        Assert.assertFalse(result);
    }

    @Test
    public void allLowercasePasswordShouldReturnFalse() {
        String testPassword = "password";
        boolean result = userService.validatePassword(testPassword);
        Assert.assertFalse(result);
    }

    @Test
    public void allCapsPasswordShouldReturnFalse() {
        String testPassword = "PASSWORD";
        boolean result = userService.validatePassword(testPassword);
        Assert.assertFalse(result);
    }

    // The Email is available
    @Test
    public void takenEmailShouldReturnFalse() {
        String testEmail = "aranda.cruz.agustin@gmail.com";
        User mockedUser = new User("test", "test", testEmail, "Password");

        when(mockDAO.getUserByEmail(testEmail)).thenReturn(mockedUser);

        boolean isUsernameAvailable = userService.isEmailAvailable(testEmail);
        Assert.assertFalse(isUsernameAvailable);
    }

    @Test
    public void availableEmailShouldReturnTrue() {
        String testEmail = "ThisIsAEmailTest@gmail.com";

        when(mockDAO.getUserByEmail(testEmail)).thenReturn(null);

        boolean isUsernameAvailable = userService.isEmailAvailable(testEmail);
        Assert.assertTrue(isUsernameAvailable);
    }

    // Test loging
    @Test
    public void loginWithNullUserShouldReturnNull() {
        String email = "bryanSerfozo@gmail.com";
        String password = "password";

        when(mockDAO.getUserByEmail(email)).thenReturn(null);

        User returnedUser = userService.loginUser(email, password);
        Assert.assertNull(returnedUser);
    }

    @Test
    public void incorrectPasswordShouldReturnNullOnLogin() {
        String email = "aranda.cruz.agustin@gmail.com";
        String correctPassword = "password";
        String incorrectPassword = "notRight";

        String hashedPassword = PasswordUtil.hashPassword(correctPassword);
        String hashedIncorrectPassword = PasswordUtil.hashPassword(incorrectPassword);
        User user = new User("test", "test", email, hashedPassword);

        when(mockDAO.getUserByEmail(email)).thenReturn(user);

        User returnedUser = userService.loginUser(email, hashedIncorrectPassword);
        Assert.assertNull(returnedUser);
    }

    @Test
    public void correctLoginInfoShouldReturnUser() {
        // Arrange
        String email = "user@example.com";
        String password = "password123";

        String hashedPassword = PasswordUtil.hashPassword(password);

        User user = new User("John", "Doe", email, hashedPassword);
        when(mockDAO.getUserByEmail(email)).thenReturn(user);
        User returnedUser = userService.loginUser(email, password);
        Assert.assertEquals(user, returnedUser);
    }



}
