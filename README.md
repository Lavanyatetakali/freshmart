# 🛒 FreshMart – Full-Stack Online Grocery Website

A complete, production-ready online grocery store built with **HTML/CSS/JS/Bootstrap** on the frontend and **Java Spring Boot + MySQL** on the backend.

---

## 📁 Project Structure

```
grocery-store/
├── frontend/
│   ├── index.html               ← Home page
│   ├── css/
│   │   └── style.css            ← Full design system
│   ├── js/
│   │   ├── data.js              ← Product data & coupons
│   │   ├── cart.js              ← Cart/wishlist logic
│   │   └── app.js               ← Home page JS
│   └── pages/
│       ├── products.html        ← Shop / product listing
│       ├── cart.html            ← Shopping cart
│       ├── checkout.html        ← Multi-step checkout
│       ├── wishlist.html        ← Wishlist
│       ├── login.html           ← User login
│       ├── register.html        ← User registration
│       ├── dashboard.html       ← User dashboard
│       ├── admin.html           ← Admin dashboard
│       ├── about.html           ← About us
│       └── contact.html         ← Contact & FAQ
│
└── backend/
    ├── pom.xml
    └── src/main/
        ├── resources/
        │   ├── application.properties
        │   └── schema.sql           ← Full DB schema + seed data
        └── java/com/grocerystore/
            ├── FreshMartApplication.java
            ├── entity/              ← JPA Entities (User, Product, Order…)
            ├── repository/          ← Spring Data JPA repositories
            ├── service/             ← Business logic layer
            ├── controller/          ← REST API controllers
            ├── dto/                 ← Request/Response DTOs
            ├── security/            ← JWT filter + UserDetailsService
            ├── config/              ← SecurityConfig, AppConfig
            └── exception/           ← Global exception handler
```

---

## 🚀 How to Run

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Node.js (optional, for live-server)
- Git

---

### Step 1 – Clone the Repository
```bash
git clone https://github.com/yourname/freshmart.git
cd freshmart
```

---

### Step 2 – Set Up the Database

Open MySQL Workbench or the CLI and run:

```sql
-- Run the full schema + seed script
SOURCE grocery-store/backend/src/main/resources/schema.sql;
```

Or manually:
```sql
CREATE DATABASE freshmart_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE freshmart_db;
-- then paste the contents of schema.sql
```

---

### Step 3 – Configure the Backend

Edit `backend/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/freshmart_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
jwt.secret=freshMartSuperSecretKey2025ChangeThisInProduction!
```

---

### Step 4 – Run the Spring Boot Backend

```bash
cd grocery-store/backend
mvn clean install
mvn spring-boot:run
```

Backend will start on **http://localhost:8080**

> **Default Admin:**  
> Email: `admin@freshmart.in`  
> Password: `Admin@1234`

---

### Step 5 – Run the Frontend

#### Option A – Live Server (VS Code)
1. Open the `frontend/` folder in VS Code
2. Right-click `index.html` → **Open with Live Server**
3. Browse at **http://127.0.0.1:5500**

#### Option B – Python HTTP Server
```bash
cd grocery-store/frontend
python -m http.server 5500
# Open http://localhost:5500
```

#### Option C – Node.js serve
```bash
npx serve grocery-store/frontend
```

---

## 🌐 API Endpoints

### Auth
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login, returns JWT |

### Products
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/products` | All products (paginated) |
| GET | `/api/products/search?q=tom` | Search products |
| GET | `/api/products/featured` | Featured 8 products |
| GET | `/api/products/{id}` | Product detail |
| GET | `/api/products/category/{catId}` | By category |
| POST | `/api/products` | Create (Admin) |
| PUT | `/api/products/{id}` | Update (Admin) |
| DELETE | `/api/products/{id}` | Soft delete (Admin) |
| GET | `/api/products/low-stock?threshold=10` | Low stock alert (Admin) |

### Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | All active categories |
| POST | `/api/categories` | Create (Admin) |
| PUT | `/api/categories/{id}` | Update (Admin) |
| DELETE | `/api/categories/{id}` | Delete (Admin) |

### Cart (requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/cart` | Get user's cart |
| POST | `/api/cart` | Add item to cart |
| PUT | `/api/cart` | Update quantity |
| DELETE | `/api/cart/{productId}` | Remove item |
| DELETE | `/api/cart` | Clear entire cart |

### Orders (requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orders` | Place order |
| GET | `/api/orders/my` | My orders |
| GET | `/api/orders/my/{id}` | Order detail |
| GET | `/api/orders` | All orders (Admin) |
| PATCH | `/api/orders/{id}/status?status=SHIPPED` | Update status (Admin) |

### Coupons
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/coupons/validate` | Validate coupon code |
| GET | `/api/coupons` | All coupons (Admin) |
| POST | `/api/coupons` | Create coupon (Admin) |
| DELETE | `/api/coupons/{id}` | Delete coupon (Admin) |

### Users (requires JWT)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/me` | Current user profile |
| PUT | `/api/users/me` | Update profile |
| GET | `/api/users` | All users (Admin) |
| PATCH | `/api/users/{id}/toggle-status` | Enable/disable user (Admin) |

---

## 🔐 Authentication

The API uses **JWT Bearer tokens**. After login:

```http
Authorization: Bearer <your_jwt_token>
```

### Example Login Request
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@freshmart.in","password":"Admin@1234"}'
```

### Example Authenticated Request
```bash
curl http://localhost:8080/api/cart \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

---

## 📦 Sample API Responses

### Login Response
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGci...",
    "email": "admin@freshmart.in",
    "firstName": "Admin",
    "lastName": "FreshMart",
    "role": "ADMIN"
  }
}
```

### Product Response
```json
{
  "success": true,
  "message": "Products fetched",
  "data": {
    "content": [
      {
        "id": 1,
        "name": "Organic Tomatoes",
        "price": 49.00,
        "oldPrice": 69.00,
        "unit": "500g",
        "emoji": "🍅",
        "stockQuantity": 120,
        "rating": 4.5,
        "categoryName": "Vegetables",
        "discountPercent": 29
      }
    ],
    "page": 0,
    "size": 12,
    "totalElements": 38,
    "totalPages": 4
  }
}
```

---

## 🗄️ Database Tables

| Table | Purpose |
|-------|---------|
| `users` | Customer and admin accounts |
| `categories` | Product categories |
| `products` | All grocery products |
| `cart_items` | User shopping carts |
| `orders` | Placed orders |
| `order_items` | Line items per order |
| `coupons` | Discount coupon codes |
| `payments` | Payment transaction records |

---

## ✨ Features Implemented

### Frontend
- ✅ Responsive design (mobile + desktop)
- ✅ Hero carousel with 3 slides
- ✅ Category grid with emoji icons
- ✅ Product cards with ratings, stock badges, quantity selector
- ✅ Search with live suggestions
- ✅ Filter by category, price, rating, availability
- ✅ Sort by price / rating / name
- ✅ Add to cart with quantity
- ✅ Wishlist (heart toggle)
- ✅ Cart page with coupon input
- ✅ Multi-step checkout (address → payment → review)
- ✅ Order tracking timeline
- ✅ User login & registration with password strength meter
- ✅ User dashboard (orders, wishlist, profile, addresses)
- ✅ Admin dashboard (KPI cards, charts, products CRUD, orders, users, inventory, coupons)
- ✅ About Us & Contact Us with FAQ accordion
- ✅ Newsletter subscription
- ✅ Toast notifications
- ✅ Offer banners & coupon copy
- ✅ Scroll to top button

### Backend
- ✅ JWT Authentication & Authorization
- ✅ Role-based access (USER / ADMIN)
- ✅ Product CRUD with soft delete
- ✅ Category management
- ✅ Cart management per user
- ✅ Order placement with stock deduction
- ✅ Coupon validation and application
- ✅ Full-text product search
- ✅ Pagination & sorting
- ✅ Global exception handling
- ✅ Input validation
- ✅ CORS configuration

---

## 🛠️ Tech Stack

| Layer | Technology |
|-------|-----------|
| Frontend | HTML5, CSS3, Bootstrap 5, JavaScript |
| Backend | Java 17, Spring Boot 3.2 |
| ORM | Spring Data JPA / Hibernate |
| Database | MySQL 8.0 |
| Security | Spring Security + JWT |
| Build | Maven |
| Fonts | Google Fonts (Playfair Display, DM Sans) |
| Icons | Font Awesome 6 |

---

## 📞 Support

- Email: hello@freshmart.in  
- Phone: +91 98765 43210

---

*Built with ❤️ using Spring Boot + Bootstrap*
