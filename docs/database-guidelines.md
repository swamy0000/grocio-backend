# Grocio Database Guidelines
Version: 1.0

---

# Purpose

This document defines the database standards for the Grocio backend.

Every new table, column, relationship, and migration must follow these guidelines.

Goals

- Scalability
- Performance
- Data Integrity
- Easy Maintenance
- Enterprise Design
- Future Microservices Compatibility

---

# Database

Current

PostgreSQL

Future

PostgreSQL Cluster

Read Replicas

Partitioning

Redis Cache

Warehouse Databases

Analytics Database

---

# Naming Standards

Tables

Plural

Correct

users

orders

products

wallet_transactions

coupon_usage

Incorrect

user

UserTable

tbl_users

Columns

snake_case

Correct

user_id

created_at

updated_at

payment_status

Incorrect

userId

CreatedDate

PaymentStatus

---

# Primary Keys

Always

BIGSERIAL

or

@GeneratedValue(strategy = IDENTITY)

Example

user_id

order_id

wallet_id

Future

UUID support for distributed services.

---

# Foreign Keys

Always use foreign keys.

Example

orders.user_id

↓

users.user_id

order_items.order_id

↓

orders.order_id

Never store duplicate information.

---

# Index Strategy

Always create indexes for

Foreign Keys

Search Columns

Filter Columns

Status Columns

Created Dates

Examples

phone_number

order_time

status

payment_status

coupon_code

product_name

---

# Unique Constraints

Use UNIQUE where required.

Examples

phone_number

coupon_code

email

Never rely only on backend validation.

---

# Audit Columns

Every business table should contain

created_at

updated_at

Future

created_by

updated_by

deleted_at

deleted_by

---

# Soft Delete

Never hard delete

Orders

Payments

Wallet Transactions

Coupon Usage

Notification History

Future

Use

deleted_at

deleted_by

---

# Hard Delete

Allowed

Temporary Tables

Cache Tables

OTP Tables

Search Cache

Logs (Old)

---

# Enum Strategy

Avoid raw String values.

Use Java Enum.

Examples

OrderStatus

PaymentStatus

CouponStatus

WalletTransactionType

DeliveryStatus

---

# Money

Never use float.

Never use double.

Always use

BigDecimal

Examples

wallet_balance

product_price

discount

refund

payment_amount

---

# Date & Time

Always use

TIMESTAMP

Java

LocalDateTime

Future

Instant

UTC Storage

---

# Boolean Naming

Correct

is_active

is_verified

is_deleted

Incorrect

active

verify

flag

---

# Status Columns

Prefer status instead of multiple booleans.

Correct

status

ACTIVE

PAUSED

EXPIRED

DELETED

Wrong

isActive

isPaused

isDeleted

---

# Relationships

One To One

User

↓

Wallet

One To Many

Order

↓

Order Items

Many To Many

Coupons

↓

Users

(using junction table)

---

# Junction Tables

Always use

coupon_users

product_categories

user_roles

Never store comma separated IDs.

---

# History Tables

Immutable

order_status_history

wallet_transactions

coupon_usage

notification_history

Never update history.

Only insert.

---

# Inventory

Never delete inventory records.

Maintain stock history.

Future

inventory_history

inventory_adjustments

warehouse_stock

---

# Payments

Payments are immutable.

Never modify payment history.

Refund should create a new record.

---

# Wallet

Wallet Balance

Mutable

Wallet Transactions

Immutable

Ledger Based

---

# Coupon

Coupon Master

Mutable

Coupon Usage

Immutable

Coupon Burn

Only after successful payment.

---

# Search Optimization

Indexes

GIN

Future

Full Text Search

ElasticSearch

---

# Partitioning

Future

Orders

Wallet Transactions

Notification History

Audit Logs

Partition by

Month

Year

---

# Archival

Future

Move

Orders > 2 Years

Logs > 6 Months

Notifications > 90 Days

Analytics

Separate Database

---

# Transactions

Use Transaction for

Place Order

Wallet Debit

Coupon Burn

Inventory Update

Refund

Never leave partial updates.

---

# Constraints

NOT NULL

CHECK

UNIQUE

FOREIGN KEY

DEFAULT

Use database constraints.

Do not rely only on Java validation.

---

# Performance

Avoid

SELECT *

Fetch only required columns.

Use projections.

Use pagination.

Avoid N+1 queries.

---

# Migration

Future

Flyway

Never manually modify production schema.

All schema changes through migration scripts.

---

# Naming Examples

users

orders

order_items

products

categories

cart

cart_items

wallet

wallet_transactions

payments

payment_modes

delivery_addresses

delivery_partners

coupons

coupon_usage

coupon_users

notifications

stores

store_inventory

drivers

driver_locations

---

# Future Tables

wishlist

reviews

ratings

refunds

returns

subscriptions

memberships

warehouse

warehouse_inventory

inventory_history

delivery_slots

delivery_routes

offers

loyalty_points

referrals

analytics_events

search_history

popular_searches

user_preferences

feature_flags

audit_logs

---

# Database Folder Structure

database/

ERD

Tables

Indexes

Constraints

Views

Triggers

Functions

Migration Scripts

---

# Review Checklist

✓ Table Name

✓ Primary Key

✓ Foreign Key

✓ Index

✓ Audit Columns

✓ Constraints

✓ Transaction Safe

✓ BigDecimal Used

✓ Enum Used

✓ History Table

✓ Performance Verified

✓ Future Compatible

---

# Copilot Instructions

Whenever creating database entities

Always use snake_case.

Always include audit columns.

Always use BigDecimal for money.

Always use enums for status.

Always create indexes for foreign keys.

Never duplicate data.

Never store comma separated IDs.

Always think about future scalability.

Design tables for millions of rows.

Use immutable history tables.

Favor normalization while allowing deliberate denormalization only when backed by measurable performance requirements and documented rationale.