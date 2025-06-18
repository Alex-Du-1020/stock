package com.example.stock.steps;

import com.example.stock.model.Security;
import com.example.stock.model.SecurityQuantity;
import com.example.stock.model.Stock;
import com.example.stock.repository.SecurityQuantityRepository;
import com.example.stock.repository.SecurityRepository;
import com.example.stock.repository.StockRepository;
import com.example.stock.service.PortfolioScheduler;
import com.example.stock.util.MarketDataProvider;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class PortfolioSchedulerSteps {

    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private SecurityRepository securityRepository;
    @Autowired
    private SecurityQuantityRepository securityQuantityRepository;
    @Autowired
    private PortfolioScheduler portfolioScheduler;

    @MockBean
    private MarketDataProvider marketDataProvider;

    private String consoleOutput;

    @Before
    public void setup() {
        securityQuantityRepository.deleteAll();
        securityRepository.deleteAll();
        stockRepository.deleteAll();
    }

    @Given("the database contains test stocks, securities, and quantities")
    public void the_database_contains_test_data() {
        // Stock
        Stock stock = new Stock();
        stock.setId("AAPL");
        stockRepository.save(stock);

        // Security
        Security security = new Security();
        security.setTicker("AAPL");
        security.setType("STOCK");
        security.setStock(stock);
        securityRepository.save(security);

        // SecurityQuantity
        SecurityQuantity sq = new SecurityQuantity();
        sq.setSecurity(security);
        sq.setQuantity(1000);
        securityQuantityRepository.save(sq);
    }

    @And("the market data provider returns fixed prices")
    public void the_market_data_provider_returns_fixed_prices() {
        when(marketDataProvider.getCurrentPrice(anyString())).thenReturn(123.45);
    }

    @When("the portfolio scheduler runs")
    public void the_portfolio_scheduler_runs() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
        try {
            portfolioScheduler.calculatePortfolioValue();
        } finally {
            System.setOut(originalOut);
        }
        consoleOutput = outContent.toString();
    }

    @Then("the console output should contain the portfolio value table")
    public void the_console_output_should_contain_the_portfolio_value_table() {
        assertTrue(consoleOutput.contains("TICKER"));
        assertTrue(consoleOutput.contains("QUANTITY"));
        assertTrue(consoleOutput.contains("VALUE"));
    }

    @And("the output should include {string}")
    public void the_output_should_include(String ticker) {
        assertTrue(consoleOutput.contains(ticker));
    }
}