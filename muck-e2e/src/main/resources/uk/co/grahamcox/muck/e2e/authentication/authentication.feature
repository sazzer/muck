Feature: Authenticating

  Scenario Outline: Authenticating with <Provider>
    Given I view the home page
    When I authenticate with <Provider>
    Then I am logged in as <User Name>

  Examples:
    | Provider | User Name |
    | google   | Test User |
