Feature: User Login
  As a registered user
  I want to log in with my credentials
  So that I can access my role-specific dashboard

  Background:
    Given I navigate to the login page

  Scenario: Patient logs in with valid credentials
    When I select the "User" login tab
    And I enter email "testone@pc.com" and password "password"
    And I click the Login button
    Then I should be redirected to "/search"

  Scenario: Admin logs in with valid credentials
    When I select the "Admin" login tab
    And I enter email "admin123@pharmaconnect.com" and password "password"
    And I click the Login button
    Then I should be redirected to "/admin/sellers"

  Scenario: Login with invalid password shows error
    When I select the "User" login tab
    And I enter email "testone@pc.com" and password "wrongpass"
    And I click the Login button
    Then I should see the error "Invalid credentials. Please try again."

  Scenario: Login with empty fields shows alert
    When I click the Login button without entering credentials
    Then an alert or validation message should be displayed