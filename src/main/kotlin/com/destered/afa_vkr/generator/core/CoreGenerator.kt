package com.destered.afa_vkr.generator.core

object CoreGenerator {

    fun generateTestHeader(className: String) =
        """
import io.appium.java_client.AppiumDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.URL;
            
/*
Добавьте следующие зависимости:
 testImplementation 'io.appium:java-client:(9.2.2/или другую)'
 androidTestImplementation 'androidx.test.ext:junit:(1.1.2/или другую)'
 
*/
             
public class $className {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String deviceName= android.os.Build.MODEL;
        String verion = android.os.Build.VERSION.RELEASE;
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformVersion", verion );
        capabilities.setCapability("platformName","Android");
        capabilities.setCapability("automationName","UiAutomator2");
        capabilities.setCapability("browserName","Chromium");
        capabilities.setCapability("autoWebview","true");
        driver = new AppiumDriver(new URL("http://127.0.0.1:4723"), capabilities);
        driver.get("https://mail.ru");
    }
    
        """.trimIndent()

    fun generateTestFooter() = """
    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
    """.trimIndent()

}