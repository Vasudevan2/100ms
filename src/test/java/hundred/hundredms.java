package hundred;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.appium.java_client.android.AndroidDriver;

public class hundredms {
	private AndroidDriver driver;

    @BeforeClass
    public void setup() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "YourDeviceName");
        capabilities.setCapability("appPackage", "live.hms.app2");
        capabilities.setCapability("appActivity", "com.your.app.activity"); 

        driver = new AndroidDriver(new URL("http://localhost:4723/wd/hub"), capabilities);
    }

    @Test
    public void automateFlow() {
    	WebElement previewButton = driver.findElement(By.id("com.your.app:id/previewButton")); 
        previewButton.click();

       
        WebElement joinButton = driver.findElement(By.id("com.your.app:id/joinButton"));
        joinButton.click();

        
        String roomCode = "yourRoomCode"; 
        String token = getAuthToken(roomCode);

        
   WebElement videoElement = driver.findElement(By.id("com.your.app:id/videoView")); 
        assert videoElement.isDisplayed();
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }}

private String getAuthToken(String roomCode) {
    	 String tokenUrl = "https://api.100ms.live/v2/token"; 
    	    String authToken = null;

    	    try {
    	        
    	        URL url = new URL(tokenUrl);
    	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

    	        
    	        connection.setRequestMethod("POST");
    	        connection.setRequestProperty("Content-Type", "application/json");
    	        connection.setDoOutput(true);

    	       
    	        String jsonInputString = "{\"room_code\": \"" + roomCode + "\"}";

    	      
    	        try (OutputStream os = connection.getOutputStream()) {
    	            byte[] input = jsonInputString.getBytes("utf-8");
    	            os.write(input, 0, input.length);
    	        }

    	        
    	        int responseCode = connection.getResponseCode();
    	        if (responseCode == HttpURLConnection.HTTP_OK) { 
    	            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
    	            StringBuilder response = new StringBuilder();
    	            String responseLine;

    	            while ((responseLine = br.readLine()) != null) {
    	                response.append(responseLine.trim());
    	            }

    	            
    	            authToken = response.toString().split("\"token\":\"")[1].split("\"")[0];
    	        } else {
    	            System.out.println("Failed to retrieve token: " + responseCode);
    	        }
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }

    	    return authToken;
    	}
}
