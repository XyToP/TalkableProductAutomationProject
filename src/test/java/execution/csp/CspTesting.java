package execution.csp;

import api.objects.Site;
import api.scenarios.ViaAPI;
import common.cases.CommonScenarios;
import common.cases.functionalities.CspScenarios;
import execution.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import util.EnvFactory;
import util.PropertyLoader;
import util.TestDataGenerator;
import util.logging.Log;

    /*
    * Scenarios#1: Verify values on Person Lookup.
    *
    * 1. Make referral via API
    * 2. Search advocate
    * 3. Click details for the referral
    * 4. Verify paid AD status
    * 5. Make purchase under AD (via API) --> Purchase count increased
    * 6. Verify count of the purchases in the tab
    *
    *
    * Scenario#2: Approve pending referral.
    *
    * 1. Make referral via API
    * 2. Open CSP --> Pending referrals
    * 3. Approve referral
    * 4. Click 'See details' for approved referral
    * 5. Verify referral status.
    *
    *
    * Scenario#3: Void pending referral.
    *
    * 1. Make referral via API
    * 2. Open CSP --> Pending referrals
    * 3. Void referral
    * 4. Open Person Lookup
    * 5. Search advocate.
    * 6. Verify referral status.
    *
    *
    * Scenario#4: Blacklist by Email
    *
    * 1. Open CSP --> Blacklisting tab
    * 2. Clear all Blacklisted emails.
    * 3. Make referral via API.
    * 4. Click Blacklist by Email.
    * 5. Navigate to Blacklisted emails.
    * 6. Assert email in the Blacklist by Email.
    *
    *
    * Scenario#5: Anonymize person
    *
    * 1. Create Referral via API.
    * 2. Open advocate in Person lookup
    * 3. Anonymize advocate.
    * 4. Srearch advocate in Person Lookup --> Adovcate should not be found.
    *
    * */


@Listeners(util.Listeners.class)
public class CspTesting extends BaseTest{

    private Site site;

    @BeforeClass
    public void login(){
        CommonScenarios.acceptCookiesUsage();
        CommonScenarios.login(
                PropertyLoader.loadProperty("talkable.user.csp"),
                EnvFactory.getPassword()
        );
        site = CommonScenarios.getSiteIntegrationValues();
    }


    /*Scenarios#1
    * */
    @Test
    public void verifyPersonLookupPage(){
        String advocate = "advocate.auto+" + TestDataGenerator.getRandomId() + "@gmail.com";
        String friend = "friend.auto+" + TestDataGenerator.getRandomId() + "@gmail.com";

        ViaAPI.createReferral(site, advocate, friend);
        CommonScenarios.openCustomerServicePortal();
        CspScenarios.openPersonLookupPage();
        CspScenarios.searchPersonLookup(advocate);
        CspScenarios.openReferralDetailsFromPersonLookup(friend);
        Assert.assertEquals(
                CspScenarios.getAdvocateRewardStatusFromReferralDetails(),
                " Pending",
                "FAILED: Incorrect Advocate reward status in Person Lookup --> Referral Details. AD email <" + advocate + ">, FR email <" + friend + ">.");

        String purchasesCount = CspScenarios.getPurchasesRowsCountFromPersonLookup();
        Assert.assertEquals(purchasesCount, "1", "FAILED: Incorrect count of purchases");
        ViaAPI.makePurchaseWithRandomUUID(site, advocate);
        CspScenarios.openPurchasesTabOnPersonLookupPage();
        CspScenarios.openReferralsTabOnPersonLookupPage();
        driver.navigate().refresh();
        CspScenarios.openOffersTabOnPersonLookupPage();

        purchasesCount = CspScenarios.getPurchasesRowsCountFromPersonLookup();
        Assert.assertEquals(purchasesCount, "2", "FAILED: Incorrect count of purchases");

        Log.testPassed("Person Lookup page smoke scenario.");
    }


    /*Scenario#2
    * */
    @Test(expectedExceptions = AssertionError.class) //failed due to https://talkable.atlassian.net/browse/PR-9520
    public void approvePendingReferral(){
        String advocate = "advocate.auto+" + TestDataGenerator.getRandomId() + "@gmail.com";
        String friend = "friend.auto+" + TestDataGenerator.getRandomId() + "@gmail.com";

        ViaAPI.createReferral(site, advocate, friend);
        CommonScenarios.openCustomerServicePortal();
        CspScenarios.openPendingReferralsPage();
        CspScenarios.approvePendingReferral(advocate);

        Assert.assertEquals(
                CspScenarios.getReferralStatusFromReferralDetailsPage(),
                "Approved",
                "FAILED: Incorrect pending referral status for advocate = <" + advocate + ", friend = <" + friend + ">."
        );

        Log.testPassed("CSP --> Approve pending referral.");

    }

    /*Scenario#3
     * */
    @Test(expectedExceptions = AssertionError.class) //failed due to https://talkable.atlassian.net/browse/PR-9520
    public void voidPendingReferral(){
        String advocate = "advocate.auto+" + TestDataGenerator.getRandomId() + "@gmail.com";
        String friend = "friend.auto+" + TestDataGenerator.getRandomId() + "@gmail.com";

        ViaAPI.createReferral(site, advocate, friend);
        CommonScenarios.openCustomerServicePortal();
        CspScenarios.openPendingReferralsPage();
        CspScenarios.voidPendingReferral(advocate);

        Assert.assertEquals(
                CspScenarios.getReferralStatusFromReferralDetailsPage(),
                "Voided",
                "FAILED: Incorrect pending referral status for advocate = <" + advocate + ", friend = <" + friend + ">."
        );
        Log.testPassed("CSP --> Void pending referral.");

    }

    /*Scenario#4
     * */
    @Test
    public void blacklistByEmail(){
        String advocateEmail = "advocate.auto+" + TestDataGenerator.getRandomId() + "@gmail.com";
        String friend = "friend.auto+" + TestDataGenerator.getRandomId() + "@gmail.com";

        CommonScenarios.openCustomerServicePortal();
        CspScenarios.openBlacklistingPage();
        CspScenarios.updateBlacklistedEmailsList("");

        ViaAPI.createReferral(site, advocateEmail, friend);
        CspScenarios.searchAndAddPersonToBlacklistedEmail(advocateEmail);
        CspScenarios.openBlacklistingPage();

        Assert.assertEquals(
                CspScenarios.getBlacklistedEmailsList(),
                advocateEmail,
                "FAILED: Incorrect email in the Blacklisted Emails List.");
        Log.testPassed("CSP --> Person Lookup --> Blacklist by Email.");
    }


    /*Scenario#5
     * */
    @Test
    public void anonymizePerson(){
        String advocateEmail = "advocate.auto+" + TestDataGenerator.getRandomId() + "@gmail.com";
        String friend = "friend.auto+" + TestDataGenerator.getRandomId() + "@gmail.com";

        ViaAPI.createReferral(site, advocateEmail, friend);
        CommonScenarios.openCustomerServicePortal();
        CspScenarios.openPersonLookupPage();
        CspScenarios.searchAndAnonymizeFromPersonLookup(advocateEmail);
        try {
            CspScenarios.searchPersonLookup(advocateEmail);
            Assert.fail("FAILED: Anonymized person was found in person lookup. Email = " + advocateEmail);
        }catch (AssertionError e){
            Log.testPassed("CSP. Anonymized test.");
        }
    }


}
