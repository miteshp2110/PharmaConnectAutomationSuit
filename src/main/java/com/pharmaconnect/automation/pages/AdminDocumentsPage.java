package com.pharmaconnect.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class AdminDocumentsPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ── Navigation ────────────────────────────────────────────────
    private By logoutBtn = By.cssSelector("button.pc-logout");

    // ── Page Header ───────────────────────────────────────────────
    private By pageTitle    = By.cssSelector(".page-header h1");
    private By pageSubtitle = By.cssSelector(".page-header p");
    private By pendingCount = By.cssSelector("span.pending-count");

    // ── Document Cards ────────────────────────────────────────────
    private By allCards     = By.cssSelector(".doc-card");
    private By emptyState   = By.cssSelector(".empty-state");

    // ── Card Inner Elements ───────────────────────────────────────
    private By docType      = By.cssSelector(".doc-type");
    private By pharmacyName = By.cssSelector(".ph-name");
    private By statusChip   = By.cssSelector(".status-chip");
    private By approveBtn   = By.cssSelector("button.btn-approve");
    private By rejectBtn    = By.cssSelector("button.btn-reject");

    // ── Meta rows inside card ─────────────────────────────────────
    private By metaRows = By.cssSelector(".meta-row");

    // ── Toast ─────────────────────────────────────────────────────
    private By toastSuccess = By.cssSelector(".toast-success");
    private By toastError   = By.cssSelector(".toast-error");

    public AdminDocumentsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    // ── Navigation ────────────────────────────────────────────────
    public void navigateTo(String baseUrl) {
        driver.get(baseUrl + "/admin/documents");
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutBtn)).click();
    }

    // ── Page Header ───────────────────────────────────────────────
    public String getPageTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle))
                .getText().trim();
    }

    public String getPageSubtitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageSubtitle))
                .getText().trim();
    }

    public String getPendingCountText() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(pendingCount))
                    .getText().trim();
        } catch (TimeoutException e) {
            return "0 pending";
        }
    }

    public int getPendingCount() {
        try {
            return Integer.parseInt(getPendingCountText().split(" ")[0]);
        } catch (NumberFormatException e) {
            return 0;
        }
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

    // ── Card Field Getters ────────────────────────────────────────
    public String getDocTypeAt(int index) {
        return getFreshCards().get(index).findElement(docType).getText().trim();
    }

    public String getPharmacyNameAt(int index) {
        return getFreshCards().get(index).findElement(pharmacyName).getText().trim();
    }

    public String getStatusAt(int index) {
        return getFreshCards().get(index).findElement(statusChip).getText().trim();
    }

    public String getMetaValueByKey(int cardIndex, String key) {
        List<WebElement> rows = getFreshCards().get(cardIndex).findElements(metaRows);
        for (WebElement row : rows) {
            String rowKey = row.findElement(By.cssSelector(".meta-key")).getText().trim();
            if (rowKey.equalsIgnoreCase(key)) {
                return row.findElement(By.cssSelector(".meta-val")).getText().trim();
            }
        }
        return "";
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

    public boolean isApproveButtonEnabledAt(int index) {
        try {
            return getFreshCards().get(index).findElement(approveBtn).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isRejectButtonEnabledAt(int index) {
        try {
            return getFreshCards().get(index).findElement(rejectBtn).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    // ── Toast ─────────────────────────────────────────────────────
    public String getToastSuccessMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastSuccess))
                    .getText().trim();
        } catch (TimeoutException e) {
            return "No success toast";
        }
    }

    public String getToastErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(toastError))
                    .getText().trim();
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
}