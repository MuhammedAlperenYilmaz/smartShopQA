
Feature: Invoice API Test Suite

  Scenario: Get Token from API
    Given I send a POST request to "/token" with headers "user" as "testuser" and "pass" as "testpass"
    Then the response status code should be 200

  Scenario: View Invoice by barcode
    When I send a GET request to "/viewInvoice" with query param "barcode" as "123456789"
    Then the response should contain "success" as true

  Scenario: Send Invoice with token
    Given I send a POST request to "/sendInvoice" with header "token" as "abc12345" and body:
      """
      {
        "Barcode": "123456789"
      }
      """
    Then the response should contain "success" as true

