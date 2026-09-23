# Food Delivery Web Application

## About the project

Food Delivery Web Application is a personal portfolio project by Vasylyna Shelepko, a Software Development graduate. It demonstrates a full-stack restaurant discovery and food ordering workflow. Customers can browse restaurants and menus, manage a cart, check out with a delivery address, and track orders. Administrators can manage restaurants, menu items, and order status.

This is a personal learning and portfolio project. It does not represent employment, client work, or production experience.

## Features

- Customer registration and login with BCrypt password hashing and signed JWT bearer tokens.
- CUSTOMER and ADMIN roles, with admin API authorization.
- Restaurant search, cuisine filtering, restaurant details, ratings, opening hours, and menu browsing.
- Admin restaurant and menu category/item create, read, update, and delete operations.
- Authenticated cart with quantity changes, server-calculated subtotal, delivery charge, and total. A cart holds items from one restaurant at a time.
- Checkout creates an address and order; order item name and price are snapshots so later menu edits do not rewrite past orders.
- Customer order history and details; admin order queue and validated status progression.
- Customer profile editing.
- Responsive React pages for home, sign in, registration, restaurant list/details, cart, checkout, order history/details, profile, and admin dashboard.
- Bean Validation, structured API errors, PostgreSQL constraints, Flyway migrations, JUnit/Mockito tests, Dockerfiles, and Docker Compose.

## Technologies

- Backend: Java 17, Spring Boot 3, Spring Web, Spring Data JPA, Spring Security, Bean Validation, JWT (JJWT), Flyway, Maven, PostgreSQL.
- Frontend: React 18, JavaScript, HTML, CSS, React Router, Axios, Vite.
- Development: Docker, Docker Compose, JUnit 5, Mockito, Git, GitHub.

## Architecture

The project is a monorepo. `backend/` is a layered Spring application organized by feature. Controllers handle HTTP and validation, services apply business rules and transactions, repositories handle persistence, and DTOs form API boundaries so JPA entities are not serialized directly. `backend/src/main/resources/db/migration` contains versioned Flyway schema changes; Hibernate validates the schema at startup.

`frontend/` is a separate single-page React app. Axios centralizes the API base URL and attaches the saved bearer token. React Router maps each customer/admin workflow to a page. Docker Compose runs PostgreSQL, the API, and the static frontend together.

## Database structure

Flyway migration `V1__create_core_schema.sql` creates:

- `app_users`: unique email, BCrypt hash, CUSTOMER/ADMIN role.
- `restaurants`: cuisine, constrained 0–5 rating, opening hours, image URL, and open state.
- `categories` and `menu_items`: restaurant menu hierarchy with unique category names per restaurant and non-negative prices.
- `addresses`: customer delivery address data.
- `carts` and `cart_items`: one cart per user, positive quantities, unique cart/item pairs.
- `customer_orders` and `order_items`: constrained order states and monetary fields; item name and unit price snapshots preserve purchase history.

Foreign keys, uniqueness constraints, check constraints, and indexes enforce core invariants in PostgreSQL. Removing a menu item clears it from carts and leaves historic order snapshots intact.

## API overview

| Method | Endpoint | Access | Purpose |
| --- | --- | --- | --- |
| POST | `/api/auth/register` | Public | Register CUSTOMER account |
| POST | `/api/auth/login` | Public | Authenticate and receive JWT |
| GET | `/api/restaurants?q=&cuisine=` | Public | Search/filter restaurants |
| GET | `/api/restaurants/{id}` | Public | Restaurant details |
| GET | `/api/restaurants/{id}/menu` | Public | Available menu items |
| GET/POST | `/api/cart`, `/api/cart/items` | Customer | View cart / add item |
| PUT/DELETE | `/api/cart/items/{menuItemId}` | Customer | Change quantity / remove item |
| POST | `/api/orders` | Customer | Checkout and create order |
| GET | `/api/orders`, `/api/orders/{id}` | Customer | Order history / details |
| GET/PUT | `/api/profile` | Customer | Read/update profile |
| GET/POST/PUT/DELETE | `/api/admin/restaurants[/{id}]` | Admin | Manage restaurants |
| GET/POST/PUT/DELETE | `/api/admin/restaurants/{id}/menu[/{itemId}]` | Admin | Manage menu items |
| GET | `/api/admin/orders` | Admin | View all orders |
| PATCH | `/api/admin/orders/{id}/status` | Admin | Advance order status |

Validation errors return HTTP 400 with field messages; missing resources return HTTP 404; duplicate registration returns HTTP 409. Order status transitions are checked by the service. Customer order queries are scoped to the authenticated account.

## Installation

Install Java 17+, Maven 3.9+, Node.js 20+ and npm. To run the application locally, install PostgreSQL 16+; Docker Desktop with Compose is an alternative that starts PostgreSQL for you. Git is needed for the GitHub steps below.

Copy `.env.example` to `.env` and replace the placeholder password. Generate a random JWT key instead of using the sample value. Do not commit `.env`.

PowerShell example for a random key:

```powershell
$bytes = [byte[]]::new(32)
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:JWT_SECRET = [Convert]::ToBase64String($bytes)
```

For Docker, put the resulting value in `.env` as `JWT_SECRET=...`. The example JWT value is only a placeholder and must be changed.

Registration intentionally creates CUSTOMER accounts only. To exercise the protected admin dashboard during local development, register your own account, then promote it directly in your local database:

```sql
UPDATE app_users SET role = 'ADMIN' WHERE email = 'your-email@example.com';
```

Sign out and sign in again so the next token includes the ADMIN role. Never expose an admin registration endpoint or promote an account in a database you do not control.

## Running locally

Create a local PostgreSQL database named `food_delivery` and a user with permission to use it, then set environment variables in the same PowerShell session. For example:

Run these SQL statements using `psql` as a local PostgreSQL administrator (choose your own password):

```sql
CREATE USER food_app WITH PASSWORD 'your-local-database-password';
CREATE DATABASE food_delivery OWNER food_app;
```

```powershell
$env:DATABASE_URL = 'jdbc:postgresql://localhost:5432/food_delivery'
$env:DATABASE_USERNAME = 'food_app'
$env:DATABASE_PASSWORD = 'your-local-database-password'
$env:JWT_SECRET = 'paste-your-generated-base64-secret'
$env:JWT_EXPIRATION_MS = '86400000'
```

Start the backend:

```powershell
cd backend
mvn spring-boot:run
```

In another terminal, start the React development server:

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`. The backend API is at `http://localhost:8080/api`. Flyway creates the schema on first backend startup. Vite uses `VITE_API_BASE_URL` when set; otherwise it defaults to the local API URL.

## Docker

From the repository root, copy and edit the env file, then start all services:

```powershell
Copy-Item .env.example .env
```

Edit `.env` to set a unique `POSTGRES_PASSWORD` and generated `JWT_SECRET`, then run:

```powershell
docker compose up --build
```

The frontend is served at `http://localhost:3000`, the API at `http://localhost:8080`, and PostgreSQL data is persisted in the `postgres_data` volume. Stop services with Ctrl+C; use `docker compose down` to stop and remove containers. `docker compose down -v` also deletes the database volume.

## Testing

Backend tests use JUnit 5, AssertJ, and Mockito. From `backend/`, run:

```powershell
mvn test
```

The test suite covers authentication registration/password hashing, restaurant search, menu listing, cart totals, and order checkout snapshots and totals. Test execution has not been verified in the authoring environment because Java and Maven were unavailable.

To build the backend artifact locally, run `mvn clean package` from `backend/`. To create a production frontend bundle, run `npm run build` from `frontend/`.

## Screenshots

Add screenshots here after running the application, for example:

- Home page
- Restaurant details and menu
- Cart and checkout
- Admin dashboard

## Future improvements

- Persist reusable customer address book and add delivery address selection.
- Add pagination, restaurant ratings/reviews, and richer cuisine filters.
- Add email verification and refresh-token/session revocation strategy.
- Add payment integration, delivery tracking, and notifications.
- Expand integration tests and add frontend tests/accessibility review.
- Publish a live demo and add real screenshots.

## Author

**Vasylyna Shelepko** — Software Development graduate.

This is a personal portfolio project created to demonstrate software development skills.

## Suggested commit history

1. `Initial project setup`
2. `Add database entities and migration`
3. `Implement authentication`
4. `Add restaurant API`
5. `Add menu functionality`
6. `Implement shopping cart`
7. `Implement order management`
8. `Add admin dashboard`
9. `Add frontend pages`
10. `Add tests`
11. `Add Docker configuration`
12. `Improve documentation`

## Upload to GitHub

Create an empty repository on GitHub (without generating a second README), then run these commands from the project root after installing Git and configuring your GitHub account:

```powershell
git init
git add .
git commit -m "Initial project setup"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/food-delivery-web-application.git
git push -u origin main
```

Replace `YOUR_USERNAME` with your GitHub username. Check `git status` and confirm `.env` is not listed before pushing. Keep credentials out of commits; use `.env.example` for safe placeholders.
