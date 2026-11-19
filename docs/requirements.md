# ShopSphere Requirements

## 1. User stories

### Customer
1. As a Customer, I can register with my email and password so that I have an account.
2. As a Customer, I can log in so that I can use protected features.
3. As a Customer, I can view and update my profile so that my details stay correct.
4. As a Customer (or visitor), I can browse a paginated list of products so that I can see what is for sale.
5. As a Customer (or visitor), I can search products by name so that I can find what I want.
6. As a Customer (or visitor), I can filter products by category and price range, and sort them, so that I can narrow results.
7. As a Customer (or visitor), I can view one product's full details so that I can decide to buy.
8. As a Customer, I can add a product to my cart so that I can buy it later.
9. As a Customer, I can view my cart with item totals so that I can review before ordering.
10. As a Customer, I can change an item's quantity in my cart so that I can buy the right amount.
11. As a Customer, I can remove an item or clear my cart so that I can change my mind.
12. As a Customer, I can place an order from my cart so that I can buy the items.
13. As a Customer, I can pay for my order through the payment gateway so that the order is confirmed.
14. As a Customer, I can retry payment on a failed order so that I don't have to start over.
15. As a Customer, I can view my list of orders so that I can track my purchases.
16. As a Customer, I can view one order, including its status and payment status, so that I know where it stands.
17. As a Customer, I can cancel my order before it ships so that I can change my mind.

### Seller
18. As a Seller, I can create a product so that I can sell it.
19. As a Seller, I can edit my own products so that details and prices stay correct.
20. As a Seller, I can activate or deactivate my own products so that I control what is visible.
21. As a Seller, I can update the stock of my own products so that availability is accurate.
22. As a Seller, I can view a list of my own products, including inactive ones, so that I can manage them.
23. As a Seller, I can view orders that contain my products so that I know what to fulfil.

### Admin
24. As an Admin, I can list and view any user so that I can manage the platform.
25. As an Admin, I can change a user's role (for example Customer to Seller) so that sellers can be approved.
26. As an Admin, I can deactivate or reactivate a user so that I can block abuse.
27. As an Admin, I can create, edit and delete categories so that the catalogue stays organised.
28. As an Admin, I can edit or deactivate any product so that I can moderate listings.
29. As an Admin, I can view all orders so that I can oversee sales.
30. As an Admin, I can mark orders as shipped and delivered so that customers see progress.

### Later (not in the first version)
- Google sign-in, refresh tokens, wishlists, reviews, coupons.

## 2. Business rules

1. New registrations are always Customers. A client can never choose its own role.
2. Passwords are stored only as hashes.
3. Only active products can be added to a cart or ordered.
4. Prices and totals are always calculated by the server from the database. The client's values are ignored.
5. An order item stores a snapshot of the product's name and price at purchase time.
6. Placing an order is all-or-nothing. If any item is unavailable or out of stock, nothing is saved and the customer sees which item failed.
7. Stock is reduced when the order is placed (reserved) and restored if the order is cancelled or payment finally fails.
8. Two customers buying the last unit at once must not both succeed.
9. A Seller can only edit or deactivate products they own. A Customer can only see their own orders.
10. Payment success sets the order to PAID. Payment failure sets it to PAYMENT_FAILED, and the customer may retry.
11. Payment confirmation from the gateway must be verified (signature check) and must be safe to receive twice (idempotent).
12. Products are deactivated, never hard-deleted. A category can be deleted only if it has no products.
13. Cancelling is allowed only while an order is PENDING_PAYMENT, PAYMENT_FAILED or PAID.

## 3. Order statuses and transitions

| From | To | Triggered by |

| (new) | PENDING_PAYMENT | Customer places the order |
| PENDING_PAYMENT | PAID | System, on verified payment success |
| PENDING_PAYMENT | PAYMENT_FAILED | System, on payment failure |
| PAYMENT_FAILED | PENDING_PAYMENT | Customer retries payment |
| PENDING_PAYMENT / PAYMENT_FAILED / PAID | CANCELLED | Customer |
| PAID | SHIPPED | Admin |
| SHIPPED | DELIVERED | Admin |

No other transitions are allowed.

## 4. Endpoints

### User module
| # | Method and path | What it does | Who |

| 1 | POST /api/auth/register | Create a Customer account | Anonymous |
| 2 | POST /api/auth/login | Log in, return a token | Anonymous |
| 3 | GET /api/users/me | View my profile | Any logged-in user |
| 4 | PUT /api/users/me | Update my profile | Any logged-in user |
| 5 | GET /api/users | List users (paged) | Admin |
| 6 | GET /api/users/{id} | View one user | Admin |
| 7 | PATCH /api/users/{id}/role | Change a user's role | Admin |
| 8 | PATCH /api/users/{id}/status | Activate or deactivate a user | Admin |

### Product module
| # | Method and path | What it does | Who |

| 9 | GET /api/products | List active products (page, search, filter, sort) | Anonymous |
| 10 | GET /api/products/{id} | View one product | Anonymous |
| 11 | POST /api/products | Create a product (owner = caller) | Seller, Admin |
| 12 | PUT /api/products/{id} | Edit a product | Owning Seller, Admin |
| 13 | PATCH /api/products/{id}/status | Activate or deactivate | Owning Seller, Admin |
| 14 | PATCH /api/products/{id}/stock | Update stock | Owning Seller, Admin |
| 15 | GET /api/sellers/me/products | List my products, including inactive | Seller |
| 16 | GET /api/categories | List categories | Anonymous |
| 17 | POST /api/categories | Create a category | Admin |
| 18 | PUT /api/categories/{id} | Edit a category | Admin |
| 19 | DELETE /api/categories/{id} | Delete an empty category | Admin |

### Order module
| # | Method and path | What it does | Who |

| 20 | GET /api/cart | View my cart | Customer |
| 21 | POST /api/cart/items | Add an item to my cart | Customer |
| 22 | PUT /api/cart/items/{itemId} | Change an item's quantity | Customer |
| 23 | DELETE /api/cart/items/{itemId} | Remove an item | Customer |
| 24 | DELETE /api/cart | Clear my cart | Customer |
| 25 | POST /api/orders | Place an order from my cart | Customer |
| 26 | GET /api/orders | List my orders (paged) | Customer |
| 27 | GET /api/orders/{id} | View one order | Owning Customer, Admin |
| 28 | POST /api/orders/{id}/cancel | Cancel an order | Owning Customer |
| 29 | GET /api/seller/orders | Orders containing my products | Seller |
| 30 | GET /api/admin/orders | List all orders (paged, filter by status) | Admin |
| 31 | PATCH /api/orders/{id}/status | Mark SHIPPED or DELIVERED | Admin |

### Payment module
| # | Method and path | What it does | Who |

| 32 | POST /api/orders/{id}/payments | Start a payment attempt | Owning Customer |
| 33 | GET /api/orders/{id}/payments | View payment attempts for an order | Owning Customer, Admin |
| 34 | POST /api/payments/verify | Confirm payment result after the gateway redirect | Owning Customer |
| 35 | POST /api/payments/webhook | Receive gateway notification (signature verified) | Gateway only |

### Platform
| # | Method and path | What it does | Who |

| 36 | GET /actuator/health | Health check | Anonymous |

## 5. Conventions
- Money is stored as an exact decimal (or whole smallest-unit amount), never floating-point.
- Lists are paged: page number and size, with a maximum size.
- Every error returns one consistent shape: status, message, timestamp, path (validation errors also list field problems).
- Status codes: 201 on create, 400 invalid input, 401 not logged in, 403 not allowed, 404 not found, 409 conflict (duplicate email, out of stock).
- Timestamps in UTC.
- Never return password hashes or internal entities. Use response DTOs.