Feature: Listing the Authentication Services

  Scenario: Getting the list of Authentication Services

    When I get the list of active authentication services
    Then I get an OK response
    And I get an application/hal+json document
    And the returned authentication services are:
      | Name         | google                              |
      | Redirect URI | /api/authentication/external/google |
