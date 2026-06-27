# Grocio Backend Engineering Standards
Version: 1.0

---

# Project Overview

Grocio is a production-grade grocery delivery platform inspired by Blinkit, Zepto, Swiggy Instamart and Amazon Fresh.

This project is designed for long-term scalability and maintainability.

The project currently uses a Modular Monolith architecture. Every feature is isolated into its own package while sharing a single Spring Boot application.

Future migration to Microservices should require minimal code changes.

---

# Technology Stack

Spring Boot

Spring Security

Spring Data JPA

PostgreSQL

JWT Authentication

WebSocket (STOMP)

Flutter Client

OpenStreetMap (Current)

Google Maps (Future)

---

# Architecture

Every feature follows Feature Based Architecture.

Example

auth/
order/
wallet/
coupon/
cart/
home/
tracking/

Each module owns its own

Controller

DTO

Service

Mapper

Validator

Exceptions

Business Logic

Repositories remain in the repository package until future migration.

---

# Layer Flow

Controller

↓

Validator

↓

Service

↓

Mapper

↓

Repository

↓

Database

Business logic must never exist inside Controllers.

Repositories must never contain business logic.

---

# Controllers

Controllers are responsible only for

Receiving Request

Returning Response

Calling Service

Controllers must NOT

Contain calculations

Contain validation

Contain business rules

Access Repository directly

Use Entity as Request

Return Entity as Response

Use Map<String,Object>

Use HashMap

---

# Services

Service layer contains

Business Logic

Transaction Management

Workflow

Decision Making

Calling multiple repositories

Coupon Logic

Wallet Logic

Order Logic

Inventory Logic

Services must be small and focused.

Large methods should be split into private helper methods.

---

# Repository Rules

Repositories are responsible only for database access.

Never place business logic inside Repository.

Never calculate values inside Repository.

Repositories should extend JpaRepository.

---

# DTO Rules

Never expose JPA Entity outside backend.

Flutter must communicate only through DTOs.

Entity

↓

Mapper

↓

DTO

DTO

↓

Mapper

↓

Entity

---

# Validation Rules

Validation belongs inside Validator classes.

Never validate inside Controller.

Use Bean Validation whenever possible.

Examples

@NotBlank

@NotNull

@Pattern

@Email

@Size

---

# Mapper Rules

All Entity ↔ DTO conversion belongs inside Mapper classes.

Never map objects inside Controller.

Never map objects inside Repository.

---

# API Response Standard

Every API must return

ApiResponse<T>

Format

{

success

message

data

timestamp

requestId

}

Never return

Entity

List<Entity>

Map<String,Object>

HashMap

---

# Exception Handling

Use custom exceptions

BusinessException

ValidationException

UnauthorizedException

ResourceNotFoundException

Use GlobalExceptionHandler.

Avoid try-catch inside Controllers.

---

# Logging

Always use SLF4J.

Never use System.out.println().

Log

Login

Order Creation

Payment

Wallet Debit

Coupon Burn

WebSocket Events

Do NOT log

PIN

JWT

OTP

Passwords

Sensitive Customer Data

---

# Security

JWT Authentication

Stateless Session

Never expose PIN.

Never expose JWT.

Never expose internal database details.

---

# Database Rules

Soft Delete where required.

Orders should never be hard deleted.

Wallet transactions are immutable.

Order history is immutable.

Coupon burn only after successful payment.

Inventory deduction must happen inside Transaction.

---

# Transaction Rules

Wallet Debit

Coupon Burn

Inventory Update

Order Creation

Payment Record

must execute inside one Transaction.

If one operation fails

Rollback everything.

---

# WebSocket Rules

WebSocket sends only events.

Business logic never exists inside WebSocket controller.

Use Service Layer.

---

# Naming Standards

Controllers

AuthController

OrderController

WalletController

Services

AuthService

OrderService

WalletService

DTO

LoginRequest

LoginResponse

Mapper

OrderMapper

CouponMapper

Validator

OrderValidator

CouponValidator

---

# Constructor Injection

Always use constructor injection.

Prefer

@RequiredArgsConstructor

Avoid field injection.

Do not use @Autowired on fields.

---

# SOLID Principles

Follow

Single Responsibility

Open Closed

Liskov

Interface Segregation

Dependency Inversion

---

# Clean Code

Small Methods

Readable Variables

Meaningful Names

No duplicate logic.

Reuse utilities.

---

# Performance

Avoid N+1 Queries.

Fetch only required fields.

Support pagination.

Support filtering.

Support searching.

---

# Future Compatibility

Design modules for

Admin Panel

Store App

Delivery Partner App

Loyalty Program

Referral System

UPI

Cards

Google Pay

Apple Pay

Razorpay

Stripe

Firebase Notifications

Analytics

Multi Store

Multi Warehouse

---

# AI Instructions

Whenever generating code

Read this document first.

Follow project architecture.

Never modify unrelated files.

Generate only requested classes.

Use existing project standards.

If architecture conflict exists

Ask before changing.

Never generate placeholder code.

Never generate TODO comments.

Generate production-ready code.
