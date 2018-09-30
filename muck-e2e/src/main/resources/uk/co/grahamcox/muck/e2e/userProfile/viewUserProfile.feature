Feature: Viewing the User Profile

  Background:
    Given I view the home page
    And I authenticate with "google"

  Scenario: Viewing the core user profile data
    When I view the user profile
    Then the user profile details are:
      | Name          | Test User        |
      | Email Address | test@example.com |
