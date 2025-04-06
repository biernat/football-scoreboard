# Live Football World Cup Score Board

A simple Java library to manage live football game scores. It allows you to:

- Start new games
- Update scores
- Finish games
- View a summary of current games ordered by total score and recency

## 📆 Assumptions

- Supported teams are predefined using a static list in an enum (`Team`).
- Only one active game is allowed between any two teams at a time.

## 📆 CI Workflow

The project includes a GitHub Actions CI workflow that builds and tests the project on every push or pull request to the
`main` branch.

## 📦 Build & Test

This project uses Maven and Java 17.

To compile:

```bash
mvn clean compile
```

To run tests:

```bash
mvn test
```

## 📂 Project Structure

```
src/
├── main/
│   └── java/at/biern/scoreboard/
│       ├── domain/
│       ├── exception/
│       ├── repository/
│       └── service/
└── test/
│   └── java/at/biern/scoreboard/
│       ├── domain/
│       ├── repository/
│       └── service/
```

## 📧 Contact

Created by biernat – feel free to reach out!

