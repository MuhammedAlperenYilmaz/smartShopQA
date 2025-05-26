Feature: Add product to cart from the lowest rated seller

  Scenario Outline: User logs in and adds a product from the lowest rated seller to the cart
    Given User opens the "<browser>" browser and goes to the login page
    And User enters valid credentials and submits
    When User searches for "<keyword>"
    And User sets price range from "<minPrice>" to "<maxPrice>"
    And User closes the popup if it appears
    Then User scrolls to bottom and clicks a random product from bottom row
    And User adds product to cart from the lowest rated seller
    And Checking that the product has been added to the cart

    Examples:
      | browser | keyword      | minPrice | maxPrice |
      | chrome  | Cep telefonu | 15000    | 20000    |
      | safari  | Cep telefonu | 15000    | 20000    |
      | firefox | Cep telefonu | 15000    | 20000    |