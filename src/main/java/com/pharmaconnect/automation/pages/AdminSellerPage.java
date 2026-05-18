package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AdminSellerPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Navigation ────────────────────────────────────────────────
    private By logo      = By.cssSelector("a.pc-logo");
    private By logoutBtn = By.cssSelector("button.pc-logout");

    // ── Page Header ───────────────────────────────────────────────
    private By pageBadge    = By.cssSelector(".pc-page-badge");
    private By pageTitle    = By.cssSelector(".pc-page-title");
    private By pageSubtitle = By.cssSelector(".pc-page-subtitle");

    // ── Tabs ──────────────────────────────────────────────────────
    private By allTabs              = By.cssSelector("button.asl-tab");
    private By pendingTab           = By.cssSelector("button.asl-tab:nth-child(1)");
    private By allPharmaciesTab     = By.cssSelector("button.asl-tab:nth-child(2)");
    private By pendingTabCount      = By.cssSelector("button.asl-tab:nth-child(1) .asl-tab-count");

    // ── Filter Chips ──────────────────────────────────────────────
    private By allFilterChips = By.cssSelector(".asl-fchip");

    // ── Search ────────────────────────────────────────────────────
    private By searchInput = By.cssSelector("input.asl-search");

    // ── Pharmacy Cards ────────────────────────────────────────────
    private By allCards     = By.cssSelector(".asl-card");
    private By emptyState   = By.cssSelector(".pc-state, .empty-state");

    // ── Card Inner Elements ───────────────────────────────────────
    private By pharmacyName  = By.cssSelector("h3.asl-ph-name");
    private By pharmacyAddr  = By.cssSelector(".asl-ph-addr");
    private By statusBadge   = By.cssSelector(".asl-badges .pc-chip");
    private By ownerName     = By.cssSelector(".asl-owner-name");
    private By ownerEmail    = By.xpath(".//span[@class='asl-ir-key' and text()='Email']/following-sibling::span[@class='asl-ir-val']");
    private By pharmacyPhone = By.xpath(".//span[@class='asl-ir-key' and text()='Phone']/following-sibling::span[@class='asl-ir-val']");
    private By registeredDate = By.xpath(".//span[@class='asl-ir-key' and text()='Registered']/following-sibling::span[@class='asl-ir-val']");
    private By pharmacyIdHint = By.cssSelector(".asl-id-hint");
    private By approveBtn    = By.cssSelector("button.pc-btn-primary");
    private By rejectBtn     = By.cssSelector("button.pc-btn-danger");
    // ── All Pharmacies Tab — Filter Chips ─────────────────────────
    private By filterChipAll        = By.xpath("//button[contains(@class,'asl-fchip') and normalize-space(text()[1])='All']");
    private By filterChipVerified   = By.xpath("//button[contains(@class,'asl-fchip') and normalize-space(text()[1])='Verified']");
    private By filterChipUnverified = By.xpath("//button[contains(@class,'asl-fchip') and normalize-space(text()[1])='Unverified']");
    private By filterChipInactive   = By.xpath("//button[contains(@class,'asl-fchip') and normalize-space(text()[1])='Inactive']");

    // ── All Pharmacies Tab — Card State ───────────────────────────
    private By inactiveTag    = By.cssSelector(".asl-inactive-tag");
    private By deactivateBtn  = By.cssSelector("button.pc-btn-danger");   // "Deactivate"
    private By reactivateBtn  = By.cssSelector("button.pc-btn-outline");   // "Reactivate"
    private By verifyOnlyBtn  = By.cssSelector("button.pc-btn-primary");   // "Verify" (All Pharmacies tab)

    // ── Toast ─────────────────────────────────────────────────────
    private By toastSuccess = By.cssSelector(".toast-success");
    private By toastError   = By.cssSelector(".toast-error");

    public AdminSellerPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ── Navigation ────────────────────────────────────────────────
    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/admin/sellers");
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }

    public void clickLogo() {
        wait.until(ExpectedConditions.elementToBeClickable(logo)).click();
    }

    // ── Page Header ───────────────────────────────────────────────
    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle)).getText();
    }

    public String getPageBadge() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageBadge)).getText();
    }

    // ── Tabs ──────────────────────────────────────────────────────
    public void clickPendingTab() {
        wait.until(ExpectedConditions.elementToBeClickable(pendingTab)).click();
    }

    public void clickAllPharmaciesTab() {
        wait.until(ExpectedConditions.elementToBeClickable(allPharmaciesTab)).click();
    }

    public boolean isPendingTabActive() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pendingTab))
                .getAttribute("class").contains("active");
    }

    public boolean isAllPharmaciesTabActive() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(allPharmaciesTab))
                .getAttribute("class").contains("active");
    }

    public int getPendingTabCount() {
        String text = wait.until(
                ExpectedConditions.visibilityOfElementLocated(pendingTabCount)).getText().trim();
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public int getTabCount() {
        return driver.findElements(allTabs).size();
    }

    // ── Search ────────────────────────────────────────────────────
    public void searchPharmacy(String name) {
        WebElement input = wait.until(
                ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(name);
    }

    public void clearSearch() {
        driver.findElement(searchInput).clear();
    }

    // ── Cards ─────────────────────────────────────────────────────
    private List<WebElement> getFreshCards() {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(allCards));
    }

    public int getCardCount() {
        return driver.findElements(allCards).size();
    }

    public boolean isEmptyStateVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(emptyState))
                    .isDisplayed();
        } catch (TimeoutException e) {
            return false;
        }
    }

    public void waitForCards() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(allCards));
    }

    // ── Card Field Getters ────────────────────────────────────────
    public String getPharmacyNameAt(int index) {
        return getFreshCards().get(index).findElement(pharmacyName).getText();
    }

    public String getPharmacyAddressAt(int index) {
        return getFreshCards().get(index).findElement(pharmacyAddr).getText().trim();
    }

    public String getStatusBadgeAt(int index) {
        try {
            return getFreshCards().get(index).findElement(statusBadge).getText().trim();
        } catch (Exception e) {
            return "VERIFIED";
        }
    }

    public String getOwnerNameAt(int index) {
        return getFreshCards().get(index).findElement(ownerName).getText();
    }

    public String getOwnerEmailAt(int index) {
        return getFreshCards().get(index).findElement(ownerEmail).getText();
    }

    public String getPharmacyIdAt(int index) {
        return getFreshCards().get(index).findElement(pharmacyIdHint)
                .getText().replace("ID: ", "").trim();
    }

    // ── Actions ───────────────────────────────────────────────────
    public void clickApproveAt(int index) {
        getFreshCards().get(index).findElement(approveBtn).click();
    }

    public void clickRejectAt(int index) {
        getFreshCards().get(index).findElement(rejectBtn).click();
    }

    public boolean isApproveButtonVisibleAt(int index) {
        try {
            return getFreshCards().get(index).findElement(approveBtn).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRejectButtonVisibleAt(int index) {
        try {
            return getFreshCards().get(index).findElement(rejectBtn).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Find Card by Pharmacy Name ────────────────────────────────
    public int findCardIndexByName(String name) {
        List<WebElement> cards = driver.findElements(allCards);
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).findElement(pharmacyName)
                    .getText().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }

    public int findCardIndexByEmail(String email) {
        List<WebElement> cards = driver.findElements(allCards);
        for (int i = 0; i < cards.size(); i++) {
            try {
                String cardEmail = cards.get(i).findElement(ownerEmail).getText();
                if (cardEmail.equalsIgnoreCase(email)) return i;
            } catch (Exception ignored) {}
        }
        return -1;
    }

    // ── Toast ─────────────────────────────────────────────────────
    public String getToastSuccessMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastSuccess))
                    .getText();
        } catch (TimeoutException e) {
            return "No success toast";
        }
    }

    public String getToastErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastError))
                    .getText();
        } catch (TimeoutException e) {
            return "No error toast";
        }
    }

    public boolean isRedirected(String url) {
        try {
            return wait.until(ExpectedConditions.urlContains(url));
        } catch (TimeoutException e) {
            return false;
        }
    }
    public void clickFilterAll() {
        wait.until(ExpectedConditions.elementToBeClickable(filterChipAll)).click();
    }

    public void clickFilterVerified() {
        wait.until(ExpectedConditions.elementToBeClickable(filterChipVerified)).click();
    }

    public void clickFilterUnverified() {
        wait.until(ExpectedConditions.elementToBeClickable(filterChipUnverified)).click();
    }

    public void clickFilterInactive() {
        wait.until(ExpectedConditions.elementToBeClickable(filterChipInactive)).click();
    }

    public int getFilterChipCount(String filterName) {
        // Returns the number shown in the chip badge
        By chipSpan = By.xpath(
                "//button[contains(@class,'asl-fchip') and normalize-space(text()[1])='"
                        + filterName + "']/span");
        try {
            return Integer.parseInt(
                    wait.until(ExpectedConditions.visibilityOfElementLocated(chipSpan))
                            .getText().trim());
        } catch (Exception e) {
            return 0;
        }
    }

    public boolean isCardInactive(int index) {
        WebElement card = getFreshCards().get(index);
        // Either card has asl-inactive class, or contains the inactive tag span
        boolean hasClass = card.getAttribute("class").contains("asl-inactive");
        boolean hasTag   = !card.findElements(inactiveTag).isEmpty();
        return hasClass || hasTag;
    }

    public boolean isDeactivateButtonVisibleAt(int index) {
        try {
            return getFreshCards().get(index).findElement(deactivateBtn).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isReactivateButtonVisibleAt(int index) {
        try {
            return getFreshCards().get(index).findElement(reactivateBtn).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isVerifyButtonVisibleAt(int index) {
        try {
            return getFreshCards().get(index).findElement(verifyOnlyBtn).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }



    public void clickVerifyAt(int index) {
        getFreshCards().get(index).findElement(verifyOnlyBtn).click();
    }

    public int findFirstActiveVerifiedCardIndex() {
        // Active verified card: has pc-chip-green badge but NOT asl-inactive class
        List<WebElement> cards = driver.findElements(allCards);
        for (int i = 0; i < cards.size(); i++) {
            WebElement card = cards.get(i);
            boolean isVerified = !card.findElements(
                    By.cssSelector(".pc-chip-green")).isEmpty();
            boolean isInactive = card.getAttribute("class").contains("asl-inactive");
            if (isVerified && !isInactive) return i;
        }
        return -1;
    }

    public int findInactiveCardIndex() {
        List<WebElement> cards = driver.findElements(allCards);
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getAttribute("class").contains("asl-inactive")) return i;
        }
        return -1;
    }
    public void clickDeactivateAt(int index) {
        getFreshCards().get(index).findElement(deactivateBtn).click();
        // ✅ Accept the confirmation alert that appears
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    public void clickReactivateAt(int index) {
        getFreshCards().get(index).findElement(reactivateBtn).click();
        // ✅ Accept the confirmation alert that appears
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }
}