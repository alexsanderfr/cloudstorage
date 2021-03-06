package com.udacity.superduperdrive;

import com.github.javafaker.Faker;
import com.udacity.superduperdrive.model.Credential;
import com.udacity.superduperdrive.model.page.HomePage;
import com.udacity.superduperdrive.model.page.LoginPage;
import com.udacity.superduperdrive.model.page.ResultPage;
import com.udacity.superduperdrive.model.page.SignupPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CredentialTests {

    @LocalServerPort
    private int port;
    private WebDriver driver;
    private WebDriverWait wait;
    private String baseURL;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }


    @BeforeEach
    public void beforeEach() {
        baseURL = "http://localhost:" + port;
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, 30);
        signupAndLogin();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testCreateCredential() {
        driver.get(baseURL + "/home");
        HomePage homePage = new HomePage(driver);
        Faker faker = new Faker();
        Credential credential = new Credential();
        credential.setUrl(faker.internet().url());
        credential.setUsername(faker.name().username());
        credential.setPassword(faker.lorem().word());
        homePage.createCredential(wait, credential);
        ResultPage resultPage = new ResultPage(driver);
        wait.until(ExpectedConditions.titleIs("Result"));
        assertTrue(resultPage.isSuccessShown());
        driver.get(baseURL + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
        String credentialUsernameElementId = String.format("%s-username", credential.getUsername());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(credentialUsernameElementId)));
        WebElement credentialUsernameElement = driver.findElement(By.id(credentialUsernameElementId));
        assertEquals(credential.getUsername(), credentialUsernameElement.getAttribute("innerHTML"));
    }

    @Test
    public void testEditCredential() {
        driver.get(baseURL + "/home");
        HomePage homePage = new HomePage(driver);
        Faker faker = new Faker();
        Credential credential = new Credential();
        credential.setUrl(faker.internet().url());
        credential.setUsername(faker.name().username());
        credential.setPassword(faker.lorem().word());
        homePage.createCredential(wait, credential);
        ResultPage resultPage = new ResultPage(driver);
        wait.until(ExpectedConditions.titleIs("Result"));
        assertTrue(resultPage.isSuccessShown());
        driver.get(baseURL + "/home");
        Credential newCredential = new Credential();
        newCredential.setUrl(faker.internet().url());
        newCredential.setUsername(faker.name().username());
        newCredential.setPassword(faker.lorem().word());
        homePage.editCredential(wait, newCredential, credential.getUsername());
        wait.until(ExpectedConditions.titleIs("Result"));
        assertTrue(resultPage.isSuccessShown());
        driver.get(baseURL + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
        String credentialUsernameElementId = String.format("%s-username", newCredential.getUsername());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(credentialUsernameElementId)));
        WebElement credentialUsernameElement = driver.findElement(By.id(credentialUsernameElementId));
        assertEquals(newCredential.getUsername(), credentialUsernameElement.getAttribute("innerHTML"));

    }

    @Test
    public void testCredentialPasswordVisibility() {
        driver.get(baseURL + "/home");
        HomePage homePage = new HomePage(driver);
        Faker faker = new Faker();
        Credential credential = new Credential();
        credential.setUrl(faker.internet().url());
        credential.setUsername(faker.name().username());
        credential.setPassword(faker.lorem().word());
        homePage.createCredential(wait, credential);
        ResultPage resultPage = new ResultPage(driver);
        wait.until(ExpectedConditions.titleIs("Result"));
        assertTrue(resultPage.isSuccessShown());
        driver.get(baseURL + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
        String credentialUsernameElementId = String.format("%s-username", credential.getUsername());
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(credentialUsernameElementId)));
        WebElement credentialUsernameElement = driver.findElement(By.id(credentialUsernameElementId));
        assertEquals(credential.getUsername(), credentialUsernameElement.getAttribute("innerHTML"));
        String credentialPasswordElementId = String.format("%s-password", credential.getUsername());
        WebElement credentialPasswordElement = driver.findElement(By.id(credentialPasswordElementId));
        assertNotEquals(credential.getPassword(), credentialPasswordElement.getAttribute("innerHTML"));
        String credentialEditElementId = String.format("%s-edit", credential.getUsername());
        wait.until(ExpectedConditions.elementToBeClickable(By.id(credentialEditElementId))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("credential-password")));
        WebElement passwordVisible = driver.findElement(By.id("credential-password"));
        assertEquals(credential.getPassword(), passwordVisible.getAttribute("value"));
    }

    @Test
    public void testDeleteCredential() {
        driver.get(baseURL + "/home");
        HomePage homePage = new HomePage(driver);
        Faker faker = new Faker();
        Credential credential = new Credential();
        credential.setUrl(faker.internet().url());
        credential.setUsername(faker.name().username());
        credential.setPassword(faker.lorem().word());
        homePage.createCredential(wait, credential);
        ResultPage resultPage = new ResultPage(driver);
        wait.until(ExpectedConditions.titleIs("Result"));
        assertTrue(resultPage.isSuccessShown());
        driver.get(baseURL + "/home");
        homePage.deleteCredential(wait, credential.getUsername());
        wait.until(ExpectedConditions.titleIs("Result"));
        assertTrue(resultPage.isSuccessShown());
        driver.get(baseURL + "/home");
        wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
        String credentialUsernameElementId = String.format("%s-username", credential.getUsername());
        boolean isPresent = driver.findElements(By.id(credentialUsernameElementId)).size() > 0;
        assertFalse(isPresent);
    }

    public void signupAndLogin() {
        driver.get(baseURL + "/signup");
        SignupPage signupPage = new SignupPage(driver);
        Faker faker = new Faker();
        String username = faker.name().username();
        String password = faker.lorem().word();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        signupPage.signup(firstName, lastName, username, password);
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(username, password);
    }
}
