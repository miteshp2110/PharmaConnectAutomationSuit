package com.pharmaconnect.automation.stepdefs;

import io.cucumber.java.After;
import io.cucumber.java.Before;

// ✅ Hooks class does NOT extend anything
// ✅ Step definition classes do NOT extend anything
// Both are picked up automatically by Cucumber via the glue path

public class Hooks {

    @Before
    public void setUp() {
        CucumberBaseTest.initDriver();
    }

    @After
    public void tearDown() {
        CucumberBaseTest.quitDriver();
    }
}