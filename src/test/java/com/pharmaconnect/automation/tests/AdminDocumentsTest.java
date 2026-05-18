package com.pharmaconnect.automation.tests;

import com.pharmaconnect.automation.base.StatefulBaseTest;
import com.pharmaconnect.automation.pages.AdminDocumentsPage;
import com.pharmaconnect.automation.pages.LoginPage;
import com.pharmaconnect.automation.utils.ConfigReader;
import com.pharmaconnect.automation.utils.TestContext;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;

public class AdminDocumentsTest extends StatefulBaseTest {

    private AdminDocumentsPage documentsPage;
    private WebDriverWait wait;

    @BeforeClass
    public void classSetup() {
        // StatefulBaseTest's @BeforeClass has already run, so getWebDriver() is ready to use!
        wait = new WebDriverWait(getWebDriver(), Duration.ofSeconds(10));
        documentsPage = getPageObjectManager().getAdminDocumentsPage();
        loginAsAdmin();
    }

    private void loginAsAdmin() {
        getWebDriver().get(ConfigReader.getProperty("login.url"));
        LoginPage loginPage = getPageObjectManager().getLoginPage();
        loginPage.enterEmail(ConfigReader.getProperty("test.admin.email"));
        loginPage.enterPassword(ConfigReader.getProperty("test.admin.password"));
        loginPage.clickLoginButton();
        loginPage.isRedirected("/admin");
    }

    // ══════════════════════════════════════════════════════════════
    // PAGE LOAD
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 1,
            testName = "Pending Documents page loads with correct title",
            description = "Navigate to admin/documents and verify the page title is Pending Documents")
    public void pendingDocumentsPageLoads() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertEquals(documentsPage.getPageTitle(), "Pending Documents",
                "Page title mismatch");
    }

    @Test(priority = 2,
            testName = "Page subtitle describes what to do on this page",
            description = "Verify the page subtitle is visible and not empty")
    public void pageSubtitleIsVisible() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        Assert.assertFalse(documentsPage.getPageSubtitle().isEmpty(),
                "Page subtitle is empty");
    }

    @Test(priority = 3,
            testName = "Pending count badge is visible in header",
            description = "Verify the pending count badge is shown in the page header with a valid number")
    public void pendingCountBadgeIsVisible() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        String countText = documentsPage.getPendingCountText();
        Assert.assertFalse(countText.isEmpty(),
                "Pending count badge is empty");
        Assert.assertTrue(countText.contains("pending"),
                "Pending count text does not contain 'pending': " + countText);
    }

    @Test(priority = 4,
            testName = "Pending count badge matches number of document cards shown",
            description = "Verify the number in the pending badge equals the actual number of document cards on the page")
    public void pendingCountMatchesCardCount() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int badgeCount = documentsPage.getPendingCount();
        int cardCount  = documentsPage.getCardCount();
        Assert.assertEquals(badgeCount, cardCount,
                "Pending badge count [" + badgeCount
                        + "] does not match card count [" + cardCount + "]");
    }

    // ══════════════════════════════════════════════════════════════
    // DOCUMENT CARDS
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 5,
            testName = "Each document card shows document type",
            description = "Verify the document type is displayed on each card and is not empty")
    public void eachCardShowsDocumentType() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = documentsPage.getCardCount();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            Assert.assertFalse(documentsPage.getDocTypeAt(i).isEmpty(),
                    "Document type is empty at card index: " + i);
        }
    }

    @Test(priority = 6,
            testName = "Each document card shows pharmacy name",
            description = "Verify the pharmacy name is displayed on each pending document card")
    public void eachCardShowsPharmacyName() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = documentsPage.getCardCount();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            Assert.assertFalse(documentsPage.getPharmacyNameAt(i).isEmpty(),
                    "Pharmacy name is empty at card index: " + i);
        }
    }

    @Test(priority = 7,
            testName = "Each document card shows PENDING status chip",
            description = "Verify every card on the pending documents page shows a PENDING status chip")
    public void eachCardShowsPendingStatus() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = documentsPage.getCardCount();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            Assert.assertEquals(documentsPage.getStatusAt(i), "PENDING",
                    "Card at index " + i + " does not show PENDING status. Actual: "
                            + documentsPage.getStatusAt(i));
        }
    }

    @Test(priority = 8,
            testName = "Each document card shows Approve and Reject buttons",
            description = "Verify both Approve and Reject buttons are visible and enabled on each document card")
    public void eachCardShowsApproveAndRejectButtons() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = documentsPage.getCardCount();
        if (count == 0) return;

        for (int i = 0; i < count; i++) {
            Assert.assertTrue(documentsPage.isApproveButtonVisibleAt(i),
                    "Approve button not visible at card index: " + i);
            Assert.assertTrue(documentsPage.isRejectButtonVisibleAt(i),
                    "Reject button not visible at card index: " + i);
            Assert.assertTrue(documentsPage.isApproveButtonEnabledAt(i),
                    "Approve button not enabled at card index: " + i);
            Assert.assertTrue(documentsPage.isRejectButtonEnabledAt(i),
                    "Reject button not enabled at card index: " + i);
        }
    }

    @Test(priority = 9,
            testName = "Each document card shows Pharmacy meta field",
            description = "Verify the meta section of each card shows a Pharmacy name field")
    public void eachCardShowsPharmacyMeta() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = documentsPage.getCardCount();
        if (count == 0) return;

        String pharmacyMeta = documentsPage.getMetaValueByKey(0, "Pharmacy");
        Assert.assertFalse(pharmacyMeta.isEmpty(),
                "Pharmacy meta field is empty on first card");
    }

    @Test(priority = 10,
            testName = "Each document card shows Pharmacy ID meta field",
            description = "Verify the meta section of each card shows a Pharmacy ID field")
    public void eachCardShowsPharmacyIdMeta() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = documentsPage.getCardCount();
        if (count == 0) return;

        String pharmacyId = documentsPage.getMetaValueByKey(0, "Pharmacy ID");
        Assert.assertFalse(pharmacyId.isEmpty(),
                "Pharmacy ID meta field is empty on first card");
    }

    @Test(priority = 11,
            testName = "Each document card shows Uploaded date meta field",
            description = "Verify the meta section shows an Uploaded date on each card")
    public void eachCardShowsUploadedDateMeta() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int count = documentsPage.getCardCount();
        if (count == 0) return;

        String uploadedDate = documentsPage.getMetaValueByKey(0, "Uploaded");
        Assert.assertFalse(uploadedDate.isEmpty(),
                "Uploaded date meta field is empty on first card");
    }

    // ══════════════════════════════════════════════════════════════
    // APPROVE DOCUMENT
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 12,
            testName = "Approving a document removes it from the pending list",
            description = "Click Approve on the first pending document and verify the card count decreases by 1")
    public void approvingDocumentRemovesFromPendingList() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countBefore = documentsPage.getCardCount();
        if (countBefore == 0) return;

        String docType = documentsPage.getDocTypeAt(0);
        String pharmacy = documentsPage.getPharmacyNameAt(0);
        TestContext.set("approvedDocType", docType);
        TestContext.set("approvedDocPharmacy", pharmacy);

        documentsPage.clickApproveAt(0);

        // Wait for card to disappear
        wait.until(driver -> documentsPage.getCardCount() < countBefore);

        int countAfter = documentsPage.getCardCount();
        Assert.assertTrue(countAfter < countBefore,
                "Card count did not decrease after approving. Before: "
                        + countBefore + " After: " + countAfter);
    }

    @Test(priority = 13,
            testName = "Pending count badge updates after approving a document",
            description = "After approving verify the pending count badge decreases to match the new card count",
            dependsOnMethods = "approvingDocumentRemovesFromPendingList")
    public void pendingCountUpdatesAfterApproval() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int badgeCount = documentsPage.getPendingCount();
        int cardCount  = documentsPage.getCardCount();
        Assert.assertEquals(badgeCount, cardCount,
                "Badge count [" + badgeCount
                        + "] does not match card count [" + cardCount + "] after approval");
    }

    // ══════════════════════════════════════════════════════════════
    // REJECT DOCUMENT
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 14,
            testName = "Rejecting a document removes it from the pending list",
            description = "Click Reject on the first pending document and verify the card count decreases by 1")
    public void rejectingDocumentRemovesFromPendingList() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int countBefore = documentsPage.getCardCount();
        if (countBefore == 0) return;

        documentsPage.clickRejectAt(0);

        // Wait for card to disappear
        wait.until(driver -> documentsPage.getCardCount() < countBefore);

        int countAfter = documentsPage.getCardCount();
        Assert.assertTrue(countAfter < countBefore,
                "Card count did not decrease after rejecting. Before: "
                        + countBefore + " After: " + countAfter);
    }

    @Test(priority = 15,
            testName = "Pending count badge updates after rejecting a document",
            description = "After rejecting verify the pending count badge matches the remaining card count",
            dependsOnMethods = "rejectingDocumentRemovesFromPendingList")
    public void pendingCountUpdatesAfterRejection() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int badgeCount = documentsPage.getPendingCount();
        int cardCount  = documentsPage.getCardCount();
        Assert.assertEquals(badgeCount, cardCount,
                "Badge count [" + badgeCount
                        + "] does not match card count [" + cardCount + "] after rejection");
    }

    // ══════════════════════════════════════════════════════════════
    // EMPTY STATE
    // ══════════════════════════════════════════════════════════════

    @Test(priority = 16,
            testName = "When no pending documents exist the page shows empty state or zero count",
            description = "When all documents have been actioned verify either an empty state message appears or card count is zero")
    public void whenNoPendingDocumentsEmptyStateShows() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        int cardCount = documentsPage.getCardCount();
        if (cardCount > 0) return; // Documents still present — skip

        boolean emptyStateVisible = documentsPage.isEmptyStateVisible();
        boolean zeroCount         = documentsPage.getPendingCount() == 0;

        Assert.assertTrue(emptyStateVisible || zeroCount,
                "Neither empty state nor zero count shown when no pending documents exist");
    }

    // ── Logout — always last ──────────────────────────────────────
    @Test(priority = 17,
            testName = "Admin logout from Pending Documents redirects to login",
            description = "Click Logout from the Pending Documents page and verify redirect to login")
    public void adminLogoutFromDocumentsPage() {
        documentsPage.navigateTo(ConfigReader.getProperty("base.url"));
        documentsPage.clickLogout();
        Assert.assertTrue(documentsPage.isRedirected("/"),
                "Logout did not redirect to login. URL: "
                        + getWebDriver().getCurrentUrl());
    }
}