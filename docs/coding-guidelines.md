# Grocio Backend Coding Guidelines
Version: 1.0

---

# Purpose

This document defines the coding standards for every backend module in Grocio.

The primary goals are:

- Maintainability
- Readability
- Scalability
- Testability
- Production Readiness
- AI-assisted Development Compatibility

Every new feature, bug fix, and refactoring must follow these guidelines.

---

# Engineering Philosophy

Grocio is designed for long-term growth.

Every line of code should be written with future extensibility in mind.

Never optimize for today's requirement only.

Always think:

Can this feature support 10x growth?

Can another developer understand this after 2 years?

Can this module become a microservice later?

---

# Core Engineering Principles

Grocio follows

• SOLID

• DRY

• KISS

• YAGNI

• Clean Architecture

• Feature Based Architecture

• Transaction Safe Design

• DTO Driven API Design

---

# Java Version

Use Java 21 LTS.

Always use modern Java features where appropriate.

Prefer

Optional

Streams

Records (Future)

Switch Expressions

Text Blocks

Avoid deprecated APIs.

---

# Package Naming

Packages must always use lowercase.

Correct

com.grocio.backend.order.service

Incorrect

OrderService

Order_Service

Orderservice

---

# Class Naming

Use PascalCase.

Correct

OrderService

CouponValidator

WalletController

OrderMapper

Incorrect

orderservice

orderService

ORDER_SERVICE

---

# Method Naming

Methods must describe business intent.

Correct

placeOrder()

validateCoupon()

burnCoupon()

refundWallet()

reserveInventory()

Incorrect

save()

run()

execute()

process()

doWork()

---

# Variable Naming

Variables must explain meaning.

Correct

walletBalance

couponDiscount

deliveryPartner

orderHistory

Incorrect

x

temp

value

obj

data

---

# Constants

Never hardcode values.

Wrong

if(status.equals("PLACED"))

Correct

OrderStatus.PLACED

Store reusable constants inside

common/constants

---

# Magic Numbers

Never write

5

100

999

inside business logic.

Use constants.

Example

MAX_CART_ITEMS

DEFAULT_DELIVERY_FEE

OTP_LENGTH

---

# Controller Rules

Controllers are thin.

Responsibilities

Receive Request

Validate Request

Call Service

Return Response

Controllers must NEVER

Contain business logic

Contain calculations

Access repositories

Contain transaction logic

Generate IDs

Call EntityManager

---

# Service Rules

Business rules belong only here.

Examples

Coupon Logic

Wallet Debit

Inventory Deduction

Payment Flow

Order Flow

Tracking

Notification Trigger

Services should remain focused.

Large services should split logic into private helper methods.

---

# Repository Rules

Repositories only interact with database.

Repositories must never

Calculate prices

Apply coupons

Deduct wallet

Change inventory

Repositories should expose only persistence methods.

---

# DTO Rules

Flutter communicates only using DTOs.

Never expose Entity.

Correct

LoginRequest

LoginResponse

OrderResponse

CouponResponse

Incorrect

User Entity

Order Entity

Product Entity

---

# Entity Rules

Entities represent database only.

Entities should never

Contain API response logic

Contain UI logic

Contain validation

Contain HTTP knowledge

---

# Mapper Rules

Every Entity ↔ DTO conversion belongs inside Mapper.

Never map objects inside Controller.

Never map objects inside Repository.

Example

OrderMapper

CouponMapper

WalletMapper

---

# Validator Rules

Every business validation belongs inside Validator classes.

Examples

CouponValidator

OrderValidator

WalletValidator

AddressValidator

Never validate inside Controller.

---

# Exception Rules

Never catch generic Exception unless absolutely required.

Create custom exceptions.

BusinessException

ValidationException

WalletException

CouponException

OrderException

InventoryException

AuthenticationException

---

# Global Exception Handler

Every exception should be handled by

GlobalExceptionHandler

Controllers should remain clean.

---

# Logging

Always use SLF4J.

Never use

System.out.println()

Log important events.

User Login

Order Created

Wallet Debit

Coupon Burn

Refund

Inventory Update

Payment Success

Never log

JWT

PIN

OTP

Passwords

Sensitive Data

---

# Transactions

Every critical workflow must be transactional.

Examples

Place Order

Wallet Payment

Refund

Coupon Burn

Inventory Deduction

If one operation fails

Rollback entire transaction.

---

# Constructor Injection

Always use

@RequiredArgsConstructor

Avoid

Field Injection

Example

Good

private final OrderService orderService;

Bad

@Autowired

private OrderService orderService;

---

# Lombok

Allowed

@Getter

@Setter

@RequiredArgsConstructor

@Builder

@NoArgsConstructor

@AllArgsConstructor

Avoid excessive annotations.

---

# Optional Usage

Good

userRepository.findById(id)
.orElseThrow(...)

Bad

if(user==null)

---

# Streams

Prefer Streams when readability improves.

Do not overuse Streams.

Readable code is more important.

---

# Enums

Replace Strings with Enums.

Bad

"PLACED"

"DELIVERED"

"COD"

Good

OrderStatus.PLACED

PaymentMode.COD

CouponStatus.ACTIVE

---

# Date & Time

Use

LocalDateTime

Instant

OffsetDateTime

Never use java.util.Date in new code.

---

# API Response

Every endpoint should return

ApiResponse<T>

Format

success

message

data

timestamp

requestId

Never return

Map<String,Object>

HashMap

Entity

---

# Folder Structure

Every module follows

controller

service

repository

dto

mapper

validator

exception

model

---

# Documentation

Every public method should contain JavaDoc if business logic is non-trivial.

---

# Code Duplication

Never duplicate business logic.

Extract common code into

common

utils

shared services

---

# Performance

Avoid N+1 queries.

Use projections when necessary.

Use pagination.

Never fetch unnecessary data.

---

# Security

Never trust frontend validation.

Always validate on backend.

Always verify JWT.

Always check ownership.

---

# Testing

Every Service should be unit testable.

Business logic should not depend directly on Spring MVC.

---

# Review Checklist

Before committing code verify

✓ DTO used

✓ Validation done

✓ Logging added

✓ Exception handled

✓ Transaction checked

✓ Mapper used

✓ Repository clean

✓ No duplicate logic

✓ No magic numbers

✓ No System.out.println()

✓ No hardcoded strings

✓ Enum used

✓ JavaDoc added

✓ Production Ready

---

# Copilot Instructions

Whenever generating code for Grocio:

Follow Feature-Based Architecture.

Never generate business logic inside Controllers.

Never use Map<String,Object>.

Never expose JPA Entities.

Always create DTOs.

Always create Mapper classes.

Always use constructor injection.

Always use SLF4J logging.

Always follow SOLID principles.

Always generate production-ready code.

If architecture conflicts with existing code, suggest refactoring instead of introducing inconsistent patterns.