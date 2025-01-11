project-root/
├── rest/
│ └── src/main/java/com/example/calculator/rest/
│ ├── RestApplication.java
│ ├── controller/
│ │ └── CalculatorController.java
│ ├── config/
│ │ └── KafkaProducerConfig.java
│ └── service/
│ └── (future service classes)
│
└── calculator/
└── src/main/java/com/example/calculator/calc/
├── CalculatorApplication.java
├── config/
│ └── KafkaConsumerConfig.java
└── service/
└── (future calculator service classes)

Why this organization?

- Separation of Concerns:
  - REST module handles API endpoints
  - Calculator module handles computation logic
  - config package contains configuration classes
- Modularity:
  - REST module handles API endpoints
  - Calculator module handles computation logic
- Package Naming:
  - Using com.example.calculator as base package
  - Subpackages rest and calc to clearly separate modules
  - Further subpackages for specific responsibilities
