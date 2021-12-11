# eClassifieds Group (eCG), Coding challege

## Goal
To build a system that can perform sale transactions.

## Considerations
- Performance (Time complexity, Load Test),
- Optimizing Memory Usage

## Development Tools
- Maven
- Java 11
- Spring Boot

## Testing Tools
- JUnit
- JMeter (Performance Test)


## Setup
- To build and run the project: `mvn clean spring-boot:run`
- To verify performance test: `mvn clean verify`

## About Performance Test
To test the server's performance, I used JMeter GUI.

**Sample Test Plan setup**

I created two thread groups: one for sale api and another for statistics api. 

*Sale-API*

![alt text](./docs/2.png "sale-test-plan")

- Graph Result
![alt text](./docs/1.png "sale-graph-result")

- Summary Report
![alt text](./docs/3.png "sale-summary-report")

*Statistics-API*

![alt text](./docs/4.png "statistics-test-plan")

- Graph Result
  ![alt text](./docs/5.png "statistics-graph-result")

- Summary Report
  ![alt text](./docs/6.png "statistics-summary-report")
