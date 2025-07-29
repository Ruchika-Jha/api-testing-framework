# API Test Automation Framework

A comprehensive REST API testing framework built with Java, TestNG, RestAssured, and Maven. This framework provides a robust foundation for API testing with features like data-driven testing, parallel execution, comprehensive reporting, and CI/CD integration.

## 🚀 Features

- **REST API Testing**: Complete support for all HTTP methods (GET, POST, PUT, DELETE, PATCH)
- **Data-Driven Testing**: JSON-based test data management with dynamic data generation
- **Parallel Execution**: Multi-threaded test execution for faster feedback
- **Comprehensive Reporting**: ExtentReports integration with detailed test results
- **Configuration Management**: Environment-specific configurations
- **Request/Response Logging**: Detailed API request and response logging
- **Docker Support**: Containerized test execution
- **CI/CD Ready**: Jenkins, GitHub Actions integration
- **Page Object Model**: Clean, maintainable code structure

## 📁 Project Structure

```
API-TESTING-FRAMEWORK/
├── src/
│   ├── main/
│   │   └── java/com/testlead/automation/
│   │       ├── base/
│   │       │   ├── BaseApiClient.java
│   │       │   └── BaseTest.java
│   │       ├── clients/
│   │       │   ├── UserApiClient.java
│   │       │   └── ProductApiClient.java
│   │       ├── config/
│   │       │   └── ConfigManager.java
│   │       ├── constants/
│   │       │   └── ApiEndpoints.java
│   │       ├── listeners/
│   │       │   ├── TestListener.java
│   │       │   └── RequestResponseLogger.java
│   │       ├── models/
│   │       │   ├── User.java
│   │       │   ├── Product.java
│   │       │   └── ApiResponse.java
│   │       ├── testdata/
│   │       │   ├── UserTestData.java
│   │       │   └── ProductTestData.java
│   │       └── utils/
│   │           ├── APIUtils.java
│   │           ├── DataUtils.java
│   │           ├── JsonUtils.java
│   │           ├── DataFaker.java
│   │           └── ReportUtils.java
│   └── test/
│       └── java/com/testlead/automation/tests/
│           ├── authentication/
│           │   └── AuthenticationTest.java
│           ├── integration/
│           │   └── IntegrationTest.java
│           ├── products/
│           │   └── ProductCrudTest.java
│           ├── smoke/
│           │   └── SmokeTest.java
│           └── users/
│               └── UserCrudTest.java
├── testdata/
│   ├── requests/
│   │   ├── user-requests.json
│   │   └── product-requests.json
│   └── schemas/
│       ├── user-schema.json
│       └── product-schema.json
├── resources/
│   ├── config/
│   │   ├── qa.properties
│   │   ├── dev.properties
│   │   └── prod.properties
├── reports/
├── logs/
├── docker/
├── pom.xml
├── testng.xml
├── regression-testng.xml
├── docker-compose.yml
├── Dockerfile
└── README.md
```

## 🛠️ Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker (optional, for containerized execution)

## ⚙️ Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/your-repo/api-testing-framework.git
cd api-testing-framework
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Configure Environment
Edit the properties files in `src/main/resources/`:
- `qa.properties` - QA environment configuration
- `dev.properties` - Development environment configuration
- `prod.properties` - Production environment configuration

### 4. Update Base URL
Update the `base.url` property in the respective environment file:
```properties
base.url=https://your-api-endpoint.com
```

## 🏃 Running Tests

### Command Line Execution

#### Run All Tests
```bash
mvn clean test
```

#### Run Specific Test Suite
```bash
mvn clean test -Dsuite=regression-testng.xml
```

#### Run Tests for Specific Environment
```bash
mvn clean test -Denvironment=qa
```

#### Run Tests in Parallel
```bash
mvn clean test -Dparallel=true -DthreadCount=3
```

#### Run Specific Test Class
```bash
mvn clean test -Dtest=UserCrudTest
```

#### Run Tests with Specific Profile
```bash
mvn clean test -Psmoke
mvn clean test -Pregression
```

### Docker Execution

#### Build and Run with Docker Compose
```bash
docker-compose up --build
```

#### Run Specific Test Suite in Docker
```bash
docker-compose run api-tests mvn test -Dsuite=smoke-testng.xml
```

## 📊 Test Reporting

### ExtentReports
- HTML reports are generated in the `reports/` directory
- Reports include detailed test execution results, screenshots, and logs
- Access reports by opening the generated HTML file in a browser

### Console Output
- Real-time test execution status
- Detailed request and response logging
- Performance metrics

## 🔧 Configuration

### Environment Configuration
The framework supports multiple environments through properties files:

```properties
# Base Configuration
base.url=https://api.example.com
api.version=v1
api.timeout=30000

# Authentication
auth.enabled=true
auth.username=test_user
auth.password=test_password

# Test Data
test.data.cleanup=true
test.data.generate.random=true

# Logging
log.level=INFO
log.request.response=true
```

### TestNG Configuration
Customize test execution through TestNG XML files:

```xml
<suite name="API Test Suite" parallel="tests" thread-count="3">
    <parameter name="environment" value="qa"/>
    <test name="Smoke Tests">
        <classes>
            <class name="com.testlead.automation.tests.smoke.SmokeTest"/>
        </classes>
    </test>
</suite>
```

## 📝 Writing Tests

### Example Test Case
```java
@Test(description = "Create new user")
public void testCreateUser() {
    // Arrange
    User testUser = UserTestData.createValidUser();
    
    // Act
    ApiResponse response = userApiClient.createUser(testUser);
    
    // Assert
    Assert.assertEquals(response.getStatusCode(), 201);
    Assert.assertNotNull(response.getBody());
    
    User createdUser = JsonUtils.fromJson(response.getBody(), User.class);
    Assert.assertEquals(createdUser.getEmail(), testUser.getEmail());
}
```

### Test Data Management
```java
public class UserTestData {
    public static User createValidUser() {
        User user = new User();
        user.setFirstName(DataFaker.getFirstName());
        user.setLastName(DataFaker.getLastName());
        user.setEmail(DataFaker.getEmail());
        user.setPassword("SecurePassword123!");
        return user;
    }
}
```

## 🚀 CI/CD Integration

### Jenkins Pipeline
```groovy
pipeline {
    agent any
    stages {
        stage('Test') {
            steps {
                sh 'mvn clean test -Denvironment=qa'
            }
            post {
                always {
                    publishHTML([
                        allowMissing: false,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'reports',
                        reportFiles: '*.html',
                        reportName: 'API Test Report'
                    ])
                }
            }
        }
    }
}
```

### GitHub Actions
```yaml
name: API Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run tests
        run: mvn clean test -Denvironment=qa
      - name: Upload reports
        uses: actions/upload-artifact@v2
        with:
          name: test-reports
          path: reports/
```

## 🧪 Test Categories

### Smoke Tests
Basic functionality validation:
- API connectivity
- Core endpoints accessibility
- Basic CRUD operations

### Regression Tests
Comprehensive test coverage:
- Full CRUD operations
- Data validation
- Error handling
- Performance benchmarks

### Integration Tests
End-to-end workflows:
- Multi-step business processes
- Data consistency across endpoints
- Cross-functional scenarios

## 📈 Best Practices

### Code Organization
- Follow Page Object Model pattern
- Separate test data from test logic
- Use meaningful test and method names
- Implement proper exception handling

### Test Data Management
- Use dynamic test data generation
- Implement proper test data cleanup
- Separate test data by environment
- Use JSON files for complex test data

### Assertion Strategy
- Use specific assertions
- Validate both positive and negative scenarios
- Check response status codes, headers, and body
- Implement response time validations

### Logging and Reporting
- Log all API requests and responses
- Include meaningful test descriptions
- Capture screenshots for failures
- Provide detailed error messages

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

For questions and support:
- Create an issue in the GitHub repository
- Contact the development team
- Check the documentation wiki

## 🔄 Changelog

### Version 1.0.0
- Initial framework setup
- Basic CRUD test implementation
- ExtentReports integration
- Docker support
- CI/CD pipeline setup

---

**Happy Testing! 🚀**