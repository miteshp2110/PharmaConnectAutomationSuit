package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MyReservationsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Navigation ────────────────────────────────────────────────
    private By logo        = By.cssSelector("a.pc-logo");
    private By logoutBtn   = By.cssSelector("button.pc-logout");
    private By hamburgerMenu = By.cssSelector("button.pc-hamburger");

    // ── Page Header ───────────────────────────────────────────────
    private By pageTitle       = By.cssSelector(".page-header h1");
    private By pageSubtitle    = By.cssSelector(".page-header p");
    private By reservationCount = By.cssSelector("span.res-count");

    // ── Flash Messages ────────────────────────────────────────────
    private By flashError   = By.cssSelector(".flash.flash-error");
    private By flashSuccess = By.cssSelector(".flash.flash-success");

    // ── Empty State ───────────────────────────────────────────────
    private By emptyState      = By.cssSelector(".empty-state");
    private By emptyStateTitle = By.cssSelector(".empty-state h3");
    private By emptyStateMsg   = By.cssSelector(".empty-state p");
    private By searchNowLink   = By.cssSelector("a.btn-search-link");

    // ── Reservation Cards ─────────────────────────────────────────
    private By allCards      = By.cssSelector(".res-card");
    private By pendingCards  = By.cssSelector(".res-card.card-pending");
    private By claimedCards  = By.cssSelector(".res-card.card-claimed");
    private By expiredCards  = By.cssSelector(".res-card.card-expired");
    private By cancelledCards = By.cssSelector(".res-card.card-cancelled");

    // ── Card Inner Elements (scoped per card) ─────────────────────
    private By medicineName  = By.cssSelector(".med-name");
    private By pharmacyName  = By.cssSelector(".ph-name");
    private By statusChip    = By.cssSelector(".status-chip");
    private By qtyPill       = By.cssSelector(".detail-pill:nth-child(1) .d-text strong");
    private By reservedAtPill = By.cssSelector(".detail-pill:nth-child(2) .d-text strong");
    private By expiryPill    = By.cssSelector(".expiry-pill .d-text strong");
    private By cancelBtn     = By.cssSelector("button.btn-cancel");
    private By pickupHint    = By.cssSelector("p.pickup-hint");
    private By claimedMsg    = By.cssSelector(".claimed-msg");
    private By expiredMsg    = By.cssSelector(".expired-msg");

    public MyReservationsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ── Navigation ────────────────────────────────────────────────
    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/my-reservations");
    }

    public void clickLogo() {
        wait.until(ExpectedConditions.elementToBeClickable(logo)).click();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }

    public void clickSearchNowLink() {
        wait.until(ExpectedConditions.elementToBeClickable(searchNowLink)).click();
    }

    // ── Page Header ───────────────────────────────────────────────
    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public String getPageSubtitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageSubtitle)).getText();
    }

    public int getTotalReservationCount() {
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(reservationCount))
                .getText();                        // e.g. "3 total"
        return Integer.parseInt(text.split(" ")[0]);
    }

    // ── Flash Messages ────────────────────────────────────────────
    public String getFlashErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(flashError)).getText();
        } catch (TimeoutException e) {
            return "No error message displayed";
        }
    }

    public String getFlashSuccessMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(flashSuccess)).getText();
        } catch (TimeoutException e) {
            return "No success message displayed";
        }
    }

    // ── Empty State ───────────────────────────────────────────────
    public boolean isEmptyStateVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(emptyState))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getEmptyStateTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(emptyStateTitle)).getText();
    }

    // ── Card Counts ───────────────────────────────────────────────
    public void waitForCards() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(allCards));
    }

    public int getTotalCardCount() {
        return driver.findElements(allCards).size();
    }

    public int getPendingCardCount() {
        return driver.findElements(pendingCards).size();
    }

    public int getClaimedCardCount() {
        return driver.findElements(claimedCards).size();
    }

    public int getExpiredCardCount() {
        return driver.findElements(expiredCards).size();
    }

    public int getCancelledCardCount() {
        return driver.findElements(cancelledCards).size();
    }

    // ── Card Getter ───────────────────────────────────────────────
    public WebElement getCard(int index) {
        List<WebElement> cards = driver.findElements(allCards);
        return cards.get(index);
    }

    // ── Card Field Getters (by index) ─────────────────────────────
    public String getMedicineNameAt(int index) {
        return getCard(index).findElement(medicineName).getText();
    }

    public String getPharmacyNameAt(int index) {
        return getCard(index).findElement(pharmacyName).getText();
    }

    public String getStatusAt(int index) {
        return getCard(index).findElement(statusChip).getText().trim();
    }

    public String getQuantityAt(int index) {
        return getCard(index).findElement(qtyPill).getText().trim();
    }

    public String getReservedAtAt(int index) {
        return getCard(index).findElement(reservedAtPill).getText().trim();
    }

    public String getExpiryTimeAt(int index) {
        try {
            return getCard(index).findElement(expiryPill).getText().trim();
        } catch (Exception e) {
            return "No expiry shown";
        }
    }

    public String getPickupHintAt(int index) {
        try {
            return getCard(index).findElement(pickupHint).getText().trim();
        } catch (Exception e) {
            return "No pickup hint shown";
        }
    }

    public String getClaimedMessageAt(int index) {
        try {
            return getCard(index).findElement(claimedMsg).getText().trim();
        } catch (Exception e) {
            return "No claimed message shown";
        }
    }

    public String getExpiredMessageAt(int index) {
        try {
            return getCard(index).findElement(expiredMsg).getText().trim();
        } catch (Exception e) {
            return "No expired message shown";
        }
    }

    // ── Status Checks (by index) ──────────────────────────────────
    public boolean isCardPending(int index) {
        return getCard(index).getAttribute("class").contains("card-pending");
    }

    public boolean isCardClaimed(int index) {
        return getCard(index).getAttribute("class").contains("card-claimed");
    }

    public boolean isCardExpired(int index) {
        return getCard(index).getAttribute("class").contains("card-expired");
    }

    public boolean isCardCancelled(int index) {
        return getCard(index).getAttribute("class").contains("card-cancelled");
    }

    // ── Cancel Button ─────────────────────────────────────────────
    public boolean isCancelButtonVisibleAt(int index) {
        try {
            return getCard(index).findElement(cancelBtn).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickCancelAt(int index) {
        getCard(index).findElement(cancelBtn).click();
    }

    // ── Find Card by Medicine + Pharmacy ──────────────────────────
    public int findCardIndex(String medicine, String pharmacy) {
        List<WebElement> cards = driver.findElements(allCards);
        for (int i = 0; i < cards.size(); i++) {
            String name = cards.get(i).findElement(medicineName).getText();
            String pharm = cards.get(i).findElement(pharmacyName).getText();
            if (name.equalsIgnoreCase(medicine) && pharm.equalsIgnoreCase(pharmacy)) {
                return i;
            }
        }
        return -1;  // not found
    }

    public boolean isReservationPresent(String medicine, String pharmacy) {
        return findCardIndex(medicine, pharmacy) != -1;
    }

    public String getStatusOf(String medicine, String pharmacy) {
        int index = findCardIndex(medicine, pharmacy);
        if (index == -1) return "Reservation not found";
        return getStatusAt(index);
    }

    public String getQuantityOf(String medicine, String pharmacy) {
        int index = findCardIndex(medicine, pharmacy);
        if (index == -1) return "Reservation not found";
        return getQuantityAt(index);
    }

    public void cancelReservation(String medicine, String pharmacy) {
        int index = findCardIndex(medicine, pharmacy);
        if (index == -1) throw new RuntimeException("Reservation not found: " + medicine + " @ " + pharmacy);
        clickCancelAt(index);
    }

    // ── Wait for Status Change ────────────────────────────────────
    public void waitForStatusToBe(int index, String expectedStatus) {
        wait.until(driver -> getStatusAt(index).equalsIgnoreCase(expectedStatus));
    }

    public void waitForCancellation(String medicine, String pharmacy) {
        wait.until(driver -> {
            int index = findCardIndex(medicine, pharmacy);
            return index != -1 && getStatusAt(index).equalsIgnoreCase("CANCELLED");
        });
    }
    public boolean isRedirected(String url){
        try{
            return wait.until(ExpectedConditions.urlContains(url));
        } catch (TimeoutException e) {
            return false;
        }
    }
}