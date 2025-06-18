Feature: Portfolio value calculation

  Scenario: Calculate and print portfolio value with mocked data
    Given the database contains test stocks, securities, and quantities
    And the market data provider returns fixed prices
    When the portfolio scheduler runs
    Then the console output should contain the portfolio value table
    And the output should include "AAPL"