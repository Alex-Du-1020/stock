# I build this application all in Cursor. For me, I just described the requirement, just a litte coding.

# Stock Portfolio Management System

A Spring Boot application for managing stock portfolios with real-time price simulation and portfolio value calculation.

## Features

- **H2 Database**: In-memory database with console access
- **JPA Entities**: Stock, Security, and SecurityQuantity management
- **Market Data Simulation**: Geometric Brownian Motion price simulation
- **Portfolio Scheduler**: Automated portfolio value calculation every 5 seconds
- **CSV Import**: Import position data from CSV files
- **Comprehensive Testing**: Unit tests, integration tests, and Cucumber BDD tests

## Technology Stack

- **Java 17**
- **Spring Boot 3.0.6**
- **Spring Data JPA**
- **H2 Database**
- **Gradle**
- **JUnit 5**
- **Cucumber (BDD Testing)**
- **Mockito**

## Project Structure

```
src/
├── main/
│   ├── java/com/example/stock/
│   │   ├── model/
│   │   │   ├── Stock.java
│   │   │   ├── Security.java
│   │   │   ├── SecurityQuantity.java
│   │   │   ├── Position.java
│   │   │   ├── PriceData.java
│   │   │   └── SecurityType.java
│   │   ├── repository/
│   │   │   ├── StockRepository.java
│   │   │   ├── SecurityRepository.java
│   │   │   └── SecurityQuantityRepository.java
│   │   ├── service/
│   │   │   ├── StockService.java
│   │   │   ├── SecurityService.java
│   │   │   ├── SecurityQuantityService.java
│   │   │   └── PortfolioScheduler.java
│   │   ├── util/
│   │   │   └── MarketDataProvider.java
│   │   ├── config/
│   │   │   └── ImportData.java
│   │   └── StockApplication.java
│   └── resources/
│       ├── application.properties
│       ├── schema.sql
│       └── static/
│           └── position.csv
└── test/
    ├── java/com/example/stock/
    │   ├── repository/
    │   ├── service/
    │   ├── config/
    │   ├── steps/
    │   └── CucumberTest.java
    └── resources/
        ├── application-test.properties
        └── features/
            └── portfolio_scheduler.feature
```

## Database Schema

### Tables

1. **STOCK**
   - `id` (VARCHAR) - Primary key

2. **SECURITY**
   - `ticker` (VARCHAR) - Primary key
   - `stock_id` (VARCHAR) - Foreign key to STOCK
   - `type` (VARCHAR) - STOCK, CALL, or PUT
   - `maturity` (VARCHAR) - Option maturity date
   - `strike` (DOUBLE) - Option strike price

3. **SECURITY_QUANTITY**
   - `id` (BIGINT) - Auto-increment primary key
   - `ticker` (VARCHAR) - Foreign key to SECURITY
   - `quantity` (INT) - Position size
   - `created_at` (TIMESTAMP) - Creation timestamp

## Getting Started

### Prerequisites

- Java 17 or higher
- Gradle 8.3 or higher

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd stock
   ```

2. **Build the project**
   ```bash
   ./gradlew clean build
   ```

3. **Run the application**
   ```bash
   ./gradlew bootRun
   ```

### Access Points

- **Application**: http://localhost:8080
- **H2 Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: `password`

## Main Functions

### 1. Portfolio Scheduler (`PortfolioScheduler.java`)

**Purpose**: Calculates and displays portfolio value every 5 seconds

**Key Features**:
- Fetches current stock prices from MarketDataProvider
- Retrieves all securities and their quantities
- Calculates security prices (stock price × strike for options)
- Displays formatted portfolio table with ticker, price, quantity, and value
- Shows total portfolio value

**Output Format**:
```
================================================================================
PORTFOLIO VALUE CALCULATION - 2024-01-15T10:30:45
================================================================================

1. CURRENT STOCK PRICES:
----------------------------------------
AAPL            : $102.45
TELSA           : $398.67
MSFT            : $98.23

2. SECURITY PRICES:
----------------------------------------
AAPL                    : $102.45 (Type: STOCK, Strike: N/A)
AAPL-OCT-2020-110-C     : $5.12 (Type: CALL, Strike: 0.05)
AAPL-OCT-2020-110-P     : $0.51 (Type: PUT, Strike: 0.005)

3. POSITION VALUES:
--------------------------------------------------------------------------------
TICKER                     | PRICE      | QUANTITY   | VALUE          
--------------------------------------------------------------------------------
AAPL                       | $  102.45  |       1000 | $    102450.00
AAPL-OCT-2020-110-C       | $    5.12  |     -20000 | $   -102400.00
AAPL-OCT-2020-110-P       | $    0.51  |      20000 | $     10200.00
--------------------------------------------------------------------------------

4. TOTAL PORTFOLIO VALUE:
----------------------------------------
TOTAL VALUE: $ 179715.00
================================================================================
```

### 2. Market Data Provider (`MarketDataProvider.java`)

**Purpose**: Simulates real-time stock price movements using Geometric Brownian Motion

**Features**:
- Random price updates every 0.5-2 seconds
- GBM formula: `S(t+dt) = S(t) * exp((μ - 0.5σ²)dt + σ√dt * N(0,1))`
- Parameters: μ (drift) = 0.0, σ (volatility) = 0.2
- Initial prices: AAPL=100.0, TELSA=400.0, MSFT=100.0

### 3. CSV Import (`ImportData.java`)

**Purpose**: Imports position data from CSV file on application startup

**Process**:
1. Reads `position.csv` from static resources
2. Parses symbol and position size
3. Creates SecurityQuantity records in database
4. Validates securities exist before creating quantities

**CSV Format**:
```csv
symbol,positionSize
AAPL,1000
AAPL-OCT-2020-110-C,-20000
AAPL-OCT-2020-110-P,20000
TELSA,-500
TELSA-NOV-2020-400-C,10000
TELSA-DEC-2020-400-P,-10000
```

## Testing

### Run All Tests
```bash
./gradlew test
```

### Run Specific Test Categories
```bash
# Repository tests
./gradlew test --tests "*RepositoryTest"

# Service tests
./gradlew test --tests "*ServiceTest"

# Cucumber tests
./gradlew test --tests "*CucumberTest*"
```

### Test Coverage
- **Unit Tests**: Model classes, services, repositories
- **Integration Tests**: Database operations, service layer
- **BDD Tests**: Cucumber scenarios for portfolio calculation

## Configuration

### Application Properties (`application.properties`)
```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Enable H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# JPA/Hibernate properties
spring.jpa.hibernate.ddl-auto=update
```

### Test Configuration (`application-test.properties`)
```properties
# Test Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=never
spring.h2.console.enabled=false
```

## API Endpoints

Currently, the application focuses on background processing and console output. Future versions may include REST endpoints for:
- Portfolio management
- Real-time price updates
- Position tracking
- Risk analysis

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Support

For questions or issues, please create an issue in the repository. 