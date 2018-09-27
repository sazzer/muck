Feature: Supported Authentication Providers

  Scenario: List Supported Authentication Providers
    When I view the home page
    Then the supported authentication providers are:
      | google |
