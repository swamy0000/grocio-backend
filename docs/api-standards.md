# Grocio Backend API Standards
Version: 1.0

---

# Purpose

This document defines the API standards used across the entire Grocio backend.

Every REST endpoint must follow these standards.

Goals

- Consistency
- Predictability
- Easy Flutter Integration
- Easy Testing
- Enterprise Ready
- Future API Versioning

---

# API Design Principles

Grocio follows RESTful API design.

Rules

• Resources are nouns

• HTTP Methods define actions

• JSON only

• Stateless APIs

• JWT Authentication

• DTO Driven

• Version Ready

---

# Base URL

Development

http://localhost:8089/api

Future Production

https://api.grocio.com/api

---

# Versioning

Current

/api

Future

/api/v1

/api/v2

Never break existing APIs.

---

# Resource Naming

Correct

/users

/orders

/cart

/products

/search

/coupons

/wallet

/notifications

Incorrect

/getUsers

/addOrder

/createCoupon

/doSearch

---

# HTTP Methods

GET

Retrieve Data

POST

Create Resource

PUT

Complete Update

PATCH

Partial Update

DELETE

Delete Resource

---

# URL Standards

Correct

GET /orders

GET /orders/25

POST /orders

PUT /orders/25

DELETE /orders/25

Nested Resources

/orders/{id}/cancel

/orders/{id}/track

/orders/{id}/history

/orders/{id}/invoice

---

# Request Body

Always DTO

Example

LoginRequest

OrderRequest

CouponRequest

WalletTopupRequest

Never send Entity objects.

---

# Response Format

Every API returns

ApiResponse<T>

Structure

{
    "success": true,
    "message": "Order created successfully",
    "data": {},
    "timestamp": "2026-06-18T11:45:22",
    "requestId": "REQ_123456"
}

Never return

Map<String,Object>

HashMap

Entity

Raw String

---

# Error Response

Format

{
    "success": false,
    "message": "Coupon expired",
    "errorCode": "COUPON_EXPIRED",
    "timestamp": "...",
    "requestId": "..."
}

---

# Success Status Codes

200 OK

Successful GET

201 Created

Resource Created

202 Accepted

Async Processing

204 No Content

Delete Success

---

# Client Errors

400 Bad Request

Validation Failed

401 Unauthorized

JWT Missing

403 Forbidden

No Permission

404 Not Found

Resource Missing

409 Conflict

Duplicate Data

422 Unprocessable Entity

Business Rule Failed

429 Too Many Requests

Rate Limit

---

# Server Errors

500 Internal Server Error

Unexpected Exception

503 Service Unavailable

Maintenance

---

# Pagination

Always support

page

size

sort

Example

GET

/products?page=0&size=20&sort=name

Future

cursor pagination

---

# Filtering

Correct

/products?category=Fruits

/orders?status=PLACED

/products?brand=Amul

/products?active=true

---

# Searching

Correct

/search?q=apple

/search?q=milk

Never

/search/apple

---

# Sorting

Example

/products?sort=price

/products?sort=rating

/products?sort=name

Future

sort=price,desc

---

# Authentication

Protected APIs

Authorization

Bearer JWT_TOKEN

Public APIs

Login

Register

Check Phone

Home

Categories

Products

---

# Validation

Use Bean Validation

@NotNull

@NotBlank

@Size

@Email

@Positive

Never validate only in Flutter.

---

# DTO Naming

Request

LoginRequest

RegisterRequest

OrderRequest

CouponRequest

Response

LoginResponse

OrderResponse

CouponResponse

WalletResponse

---

# API Naming

Controller

OrderController

Service

OrderService

Repository

OrderRepository

Mapper

OrderMapper

Validator

OrderValidator

---

# File Upload

Future

multipart/form-data

Return uploaded URL

Never store file bytes inside database.

---

# Idempotency

Safe APIs

GET

PUT

DELETE

POST Payment

Future

Idempotency-Key Header

---

# Rate Limiting

Future

Login

OTP

Search

Coupon Apply

Wallet

Payment

---

# API Documentation

Swagger/OpenAPI

Every endpoint must include

Purpose

Request

Response

Error Codes

Business Rules

---

# Logging

Log

Request

Execution Time

Error

RequestId

Never log

JWT

PIN

OTP

Password

---

# Performance

Compress JSON

Pagination

Lazy Loading

Caching

Avoid N+1 Queries

---

# Security

Always verify JWT

Always verify ownership

Never trust userId from client

Never expose internal IDs unnecessarily

Always validate input

---

# API Evolution

Never remove fields.

Only add optional fields.

Deprecate before removal.

Maintain backward compatibility.

---

# Flutter Guidelines

Flutter should never parse dynamic JSON manually.

Every response should map into DTO/Model classes.

Never depend on field ordering.

Handle unknown fields gracefully.

---

# Example Login Response

{
    "success": true,
    "message": "Login successful",
    "data": {
        "userId": 1,
        "name": "John",
        "token": "JWT_TOKEN"
    },
    "timestamp": "...",
    "requestId": "..."
}

---

# Example Order Response

{
    "success": true,
    "message": "Order placed successfully",
    "data": {
        "orderId": 125,
        "status": "PLACED",
        "paymentStatus": "PAID"
    },
    "timestamp": "...",
    "requestId": "..."
}

---

# Review Checklist

✓ REST Naming

✓ DTO Used

✓ Validation

✓ Authentication

✓ Correct Status Code

✓ ApiResponse Used

✓ Logging

✓ Documentation

✓ Security Verified

✓ Performance Checked

---

# Copilot Instructions

When generating APIs for Grocio

Always use REST principles.

Always create Request DTOs.

Always create Response DTOs.

Always return ApiResponse<T>.

Never expose Entities.

Never return Map<String,Object>.

Always validate requests.

Always use proper HTTP status codes.

Always keep APIs backward compatible.

If existing code differs, recommend incremental refactoring instead of introducing inconsistent APIs.