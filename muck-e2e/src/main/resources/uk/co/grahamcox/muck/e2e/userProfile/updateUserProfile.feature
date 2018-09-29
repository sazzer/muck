Feature: Updating the User Profile

  Background:
    Given I view the home page
    And I authenticate with "google"
    And I view the user profile

  Scenario Outline: Successfully changing the user profile: <Comment>

    When I update the user profile with details:
      | Name          | <New Name>  |
      | Email Address | <New Email> |
    Then the user profile is updated successfully
    And the user profile details are:
      | Name          | <New Name>  |
      | Email Address | <New Email> |
    And I am logged in as "<New Name>"

    Examples:
      | New Name  | New Email        | Comment      |
      | New User  | test@example.com | Change name  |
      | Test User | new@user.com     | Change email |
      | New User  | new@user.com     | Change both  |
