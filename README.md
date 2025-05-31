# Breakable Toy - Backend (Spring Boot)

This is the backend for the **Breakable Toy** application, built with Java and Spring Boot. It exposes RESTful endpoints for managing products, categories, and stock, and is designed to integrate with a React frontend.

## Technologies Used

- Java 17
- Spring Boot 3.3
- Maven
- Jackson (for JSON handling)
- Dependency Injection with `@Service` and `@RestController`

## Requirements

- Java 17
- Maven (`mvn`)

## How to Run

1. Clone the repository:

```bash
git clone https://github.com/your-username/backend-breakable-toy.git
cd backend-breakable-toy
```

2. Run the application:

```bash
./mvnw spring-boot:run
```

> By default, the backend runs on: `http://localhost:9090`

## Available Endpoints

| Method | Endpoint                     | Description                                |
|--------|------------------------------|--------------------------------------------|
| GET    | `/getProducts`               | Returns all products                       |
| GET    | `/getCategories`             | Returns unique categories                  |
| POST   | `/getProductsFiltered`       | Filters products by name, category, stock  |
| POST   | `/products`                  | Inserts a new product                      |
| PUT    | `/products/{id}`             | Updates an existing product                |
| DELETE | `/products/{id}`             | Deletes a product                          |
| POST   | `/products/{id}/outofstock`  | Toggles product stock to 0 or 10           |
| PUT    | `/products/{id}/instock`     | Toggles product stock to 0 or 10           |

## Notes

- Products are loaded from a local `resources/data/products.json` file.
- No database connection is used; all data is stored in memory.
- Stock can be toggled between 0 and 10 as a simple simulation.
- The `/getProductsFiltered` endpoint expects a `SearchObject` with:
  - `name`: product name (string)
  - `category`: category (`"AC"` for all)
  - `availability`: `"in"`, `"out"`, or `"all"`

## Frontend Integration

This backend is designed to work directly with a React frontend application that consumes the provided endpoints.

> Ensure it runs on port `8080` for correct integration with the frontend.
> Front end application https://github.com/alerameli-E/breakable-toy---frontend

## Author

Developed by Alerameli E.
