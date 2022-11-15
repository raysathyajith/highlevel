package highlevel;

import static org.testng.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Date;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.time.*;

import com.github.javafaker.Faker;

public class CalendarAutomation{

	private WebDriver driver;
	BaseClass bc;
	WebDriverWait wait;
	
	By email = By.xpath("//input[@id='email']");
	By password = By.xpath("//input[@id='password']");
	By signin = By.xpath("//button[text()='Sign in']");
	By bookAppointmentButton = By.xpath("//button/*[text()='Book Appointment']");
	By calendarAppFrame = By.xpath("//iframe[@name='calendar-app']");
	By calendarDropdown = By.xpath("//*[text()='Calendar']/following-sibling::*//input[@class='n-base-selection-input' ]");
	By timezones = By.xpath("//*[text()='Recommended Timezones']/parent::*//*[@class='n-base-select-option__content']");
	By slot = By.xpath("//*[text()='Slot']/parent::div//*[@class='n-base-selection-input']");
	By timeSlotTitle = By.xpath("//*[text()='Slot']/parent::div//div[@class='n-base-selection-input__content']");
	By rowData = By.xpath("//tr[@role='row']");

	private String url = "https://app.gohighlevel.com";
	private String email_id = "sathyajithrayn@outlook.com";
	private String pwd = "Test@123";
	// private String api_key = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2NhdGlvbl9pZCI6IjZTejNNak9XdE1maXc3RVE4YmU3IiwiY29tcGFueV9pZCI6Ill1VFVabFV0cndCdHZtZ0J5WkRXIiwidmVyc2lvbiI6MSwiaWF0IjoxNjYwMzA1NTYyMzI1LCJzdWIiOiJaZGRXbUhaOG55RlpUbFN4b2t0diJ9.i6PhhT5yp240YlKoDWSjI2wvU8DK6md2JU2wqIT42rA";


	public void launchDriver() {
		HashMap<String, Object> prefs = new HashMap<String, Object>();
		ChromeOptions options = new ChromeOptions();
		prefs.put("profile.default_content_setting_values.notifications", 2);
		options.setExperimentalOption("prefs", prefs);
		driver = new ChromeDriver(options);
		bc = new BaseClass(driver);
		wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}

	public void loginSite() {
		driver.get(url);
		bc.fill(email, email_id);
		bc.fill(password, pwd);
		bc.clickElement(signin);
	}
	
	public void clickMenu(String menu) {
		bc.waitForLoader();
		bc.clickElement(bc.dynamicLocator("text()", menu));
	}
	
	public void goToBookAppointment() {
		bc.waitForLoader();
		bc.clickElement(bc.dynamicLocator("text()", "Book Appointment"));
		wait.until(ExpectedConditions.presenceOfElementLocated(calendarAppFrame));
		driver.switchTo().frame("calendar-app");
	}
	
	public By getSlotsxPath(String title) {
		return By.xpath("//*[@class='n-base-select-option__content' and text()='"
				+ title + "']/ancestor::*[@class='v-vl-visible-items']//*[@class='n-base-select-option__content']");
	}
	
	public By getDayXpath(int date) {
		return By.xpath("//*[@class='vdpCell selectable']//*[text()='" + date + "']");
	}
	
	public HashMap<String, String> createContact() {		
		bc.clickElement(bc.dynamicLocator("text()", "Add New"));
		bc.waitForElement(bc.dynamicLocator("text()", "Add New Contact"));
		Faker faker = new Faker();
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Name",faker.name().fullName());
		map.put("Phone",faker.numerify("+9163########"));
		map.put("Email",faker.bothify("????##@gmail.com"));
		bc.fill(bc.fieldsInCreateContact("Name"), map.get("Name"));
		bc.fill(bc.fieldsInCreateContact("Phone"), map.get("Phone"));
		bc.fill(bc.fieldsInCreateContact("Email"), map.get("Email"));
		bc.clickElement(bc.dynamicLocator("@id", "save-contact-button"));
		bc.waitForElement(bookAppointmentButton);
		return map;
	}
	
	public void chooseCalendar(String calendar) {
		bc.waitForLoader();
		bc.waitForElement(bookAppointmentButton);
		bc.clickElement(calendarDropdown);
		bc.clickElement(bc.dynamicLocator("text()", calendar));
	}
	
	public String chooseRandomTimeZone() {
		bc.clickElement(bc.dynamicLocator("@id", "select-timezone-dropdown"));
		List<WebElement> timezonelist = driver.findElements(timezones);
		List<String> zonelist = new ArrayList<String>();
		for (WebElement e : timezonelist) {
			zonelist.add(e.getText());
		}
		int rnd = new Random().nextInt(zonelist.size());
		String timeZonetoPick = zonelist.get(rnd);
		bc.clickElement(bc.dynamicLocator("text()", timeZonetoPick));
		return timeZonetoPick;
	}
	
	public int chooseDifferentDate() {
		bc.clickElement(bc.dynamicLocator("@id", "date-picker-standard"));
		SimpleDateFormat formatter = new SimpleDateFormat("dd");
		Date date = new Date();
		int currentDate = Integer.parseInt(formatter.format(date));
		int nextDay = currentDate + 1;
		bc.clickElement(getDayXpath(nextDay));
		return nextDay;
	}
	
	public String chooseRandomTimeSlot() {
		bc.clickElement(slot);
		String title = bc.getText(timeSlotTitle);
		List<WebElement> slots = driver.findElements(getSlotsxPath(title));
		List<String> slotlist = new ArrayList<String>();
		for (WebElement e : slots) {
			slotlist.add(e.getText());
		}
		int rndslotlist = new Random().nextInt(slotlist.size());
		String preferredSlot = slotlist.get(rndslotlist);
		bc.clickElement(bc.dynamicLocator("text()", preferredSlot));
		return preferredSlot;
	}
	
	public String getNotificationText() {
		return bc.getText(bc.dynamicLocator("@class", "notif-container"));
	}
	
	public HashMap<String, String> fetchTimeZoneforBookedAppointment(String contact) {
		bc.clickElement(bc.dynamicLocator("text()", "Appointment Report"));
		bc.waitForLoader();
		List<WebElement> rows = driver.findElements(rowData);
		HashMap<String, String> rowDetails = new HashMap<String, String>();
		for(WebElement e: rows) {
			if(e.getText().contains(contact)) {
				List<WebElement> cols = e.findElements(By.tagName("td"));
				rowDetails.put("Calendar", cols.get(0).getText());
				rowDetails.put("Requested Time", cols.get(1).getText());
				break;
			}
		}
		return rowDetails;
	}
	
	
	public String convertTimeZone(String timeZone, int date, String time) {
		String zone = timeZone.split(" ")[1];
		String[] times = time.split(" ");
		String ap = times[1]== "pm" ? "p" : "a";
		String DATE_FORMAT = date + "-M-yyyy "+times[0]+":00 " + ap;
		LocalDateTime ldt = LocalDateTime.now();
		String dateToBeConverted = ldt.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
		String DATE_FORMATT = "dd-M-yyyy h:mm:ss a";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATT);
		formatter = formatter.withLocale( Locale.US );
		LocalDateTime l_givenTime = LocalDateTime.parse(dateToBeConverted.toUpperCase(), formatter);
		ZoneId givenZoneId = ZoneId.of(zone);
		ZonedDateTime givenZonedDateTime = l_givenTime.atZone(givenZoneId);
		ZoneId asiasZoneId = ZoneId.of("Asia/Kolkata");
        ZonedDateTime convertedDateTime = givenZonedDateTime.withZoneSameInstant(asiasZoneId);
        return convertedDateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy, hh:mm a"));
	}

	@BeforeTest
	public void setup() {
		launchDriver();
		loginSite();
	}

	@Test
	public void TC_01_createAppointmentandValidateTimeZone() {
		clickMenu("Calendars");
		goToBookAppointment();
		HashMap<String, String> contact = createContact();
		chooseCalendar("Dentist calendar");
		String timeZonePickedd = chooseRandomTimeZone();
		int date = chooseDifferentDate();
		String timeSlot = chooseRandomTimeSlot();
		bc.clickElement(bookAppointmentButton);
		driver.switchTo().defaultContent();
		assertEquals(getNotificationText(), "Appointment Booked!");
		clickMenu("Reporting");
		HashMap<String, String> details = fetchTimeZoneforBookedAppointment(contact.get("Name"));
		assertEquals(details.get("Calendar"), "Dentist calendar");
		assertEquals(details.get("Requested Time"), convertTimeZone(timeZonePickedd, date, timeSlot));
	}
	
	@AfterTest
	public void quit() {
		driver.quit();
	}
}
