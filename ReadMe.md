# E-commerce UI Automation Test Project

## Project Overview
This project automates UI and API tests for an e-commerce platform (`n11.com` UI, and a Mock API service).
- UI tests cover login, product search, filtering, adding to cart on multiple browsers.
- API tests verify token generation, invoice retrieval, and sending invoice requests.

Both UI and API tests are written in Java using Selenium, Cucumber BDD, RestAssured, and JUnit.  
Tests run parametrically and support parallel execution with failure screenshots for UI tests.

---

## Features
- Parametric browser selection (Chrome, Firefox, Safari) for UI tests via feature files
- Parallel test execution for both UI and API tests
- Automatic screenshot capture on UI test failure
- Reports generated in HTML, JSON, and JUnit XML formats
- Environment variables used for sensitive data (login credentials, API tokens)
- API tests include POST and GET requests with headers, query parameters, and JSON bodies
- Clean, maintainable, reusable code with proper waits and exception handling
---

## Technologies Used

- Java 11+
- Selenium WebDriver
- Cucumber (io.cucumber)
- RestAssured for API testing
- JUnit
- WebDriverManager
- Maven for build and dependency management

---

## Prerequisites

- Java Development Kit (JDK) 11 or higher installed
- Maven installed and added to system PATH
- Browsers installed (Chrome, Firefox, Safari for Mac users)
- Environment variables set for credentials and tokens:

```bash
export MY_APP_USERNAME=your_email@example.com
export MY_APP_PASSWORD=your_secure_password
export API_TOKEN=your_api_token_if_needed
```
## How to Run Tests

 ### Clone this repository:
- git clone https://github.com/MuhammedAlperenYilmaz/smartShopQA
- cd smartShopQA
### Set environment variables for your credentials.

### Run tests with Maven (runs all scenarios in parallel on browsers defined in the feature file):
- mvn clean test

 ## Reports will be generated at:
- target/cucumber-reports.html (HTML report)
- target/cucumber.json (JSON report)
- target/cucumber.xml (JUnit XML report)

 ## Screenshot on Failure
- If a test scenario fails, a screenshot of the browser at failure time is automatically captured and attached to the Cucumber report for easier debugging.

## Repository Structure
src/
├─ main/
│   └─ java/ (optional app code)
├─ test/
│   ├─ java/
│   │   └─ StepDefinition/ (step definition classes)
│   │   └─ Runner/ (test runner classes)
│   └─ resources/
│       └─ Features/ (feature files)
pom.xml
README.md
 ## Notes
-Tests are designed for https://www.n11.com/ and may require updates if the site changes
-Safari browser support requires macOS environment
-You can add more browsers by extending the switch-case in the driver setup method

## Contact
For questions or contributions, please open an issue or submit a pull request on GitHub.
