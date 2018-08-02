Feature: Healthchecks

  @ignore
  Scenario: Health Checks

    When I get the health of the system
    Then the system is healthy
