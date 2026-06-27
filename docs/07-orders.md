# Grocio Order Management System
Version: 1.0

---

# Purpose

The Order module is the core business engine of Grocio.

Every purchase made by a customer eventually becomes an Order.

This document defines the complete lifecycle, business rules, validations, integrations, payment flow, inventory flow, coupon flow, tracking flow and future scalability.

---

# Vision

The Order module should support

• Customer App

• Store App

• Delivery Partner App

• Admin Panel

without architectural changes.

The order engine should remain independent from payment providers and map providers.

---

# Responsibilities

The Order module is responsible for

Order Creation

Order Validation

Inventory Reservation

Payment Processing

Coupon Processing

Wallet Processing

Status Updates

OTP Verification

Order History

Reorder

Cancellation

Refund

Notifications

Tracking

Audit History

---

# Order Lifecycle

Cart

↓

Checkout

↓

Coupon Validation

↓

Inventory Validation

↓

Payment Validation

↓

Order Created

↓

Payment Success

↓

Inventory Deduction

↓

Coupon Burn

↓

Cart Cleanup

↓

Tracking Started

↓

Delivered

---

# Order Status Flow

PENDING_PAYMENT

↓

PLACED

↓

STORE_ACCEPTED

↓

PACKING

↓

PACKED

↓

READY_FOR_PICKUP

↓

OUT_FOR_DELIVERY

↓

ARRIVING

↓

DELIVERED

Alternative

↓

CANCELLED

↓

REFUNDED

---

# Payment Status

PENDING

PAID

UNPAID

FAILED

REFUNDED

PARTIALLY_REFUNDED

---

# Delivery Status

NOT_ASSIGNED

ASSIGNED

ARRIVED_STORE

PICKED_UP

ON_THE_WAY

ARRIVED_DESTINATION

DELIVERED

FAILED

---

# Business Rules

An order cannot exist without

Customer

Delivery Address

Order Items

Payment Method

Inventory Validation

---

# Order Creation Rules

Validate Customer

↓

Validate Address

↓

Validate Cart

↓

Validate Coupon

↓

Validate Inventory

↓

Create Order

↓

Process Payment

↓

Deduct Inventory

↓

Burn Coupon

↓

Clear Cart

↓

Publish Events

---

# Inventory Rules

Inventory should never become negative.

Stock must be validated before payment.

Inventory deduction happens only inside transaction.

Rollback inventory if transaction fails.

---

# Coupon Rules

Coupon must be

ACTIVE

Not Expired

Within Valid Time

Minimum Cart Value

Maximum Usage

Per User Limit

Private Coupon Validation

Coupon burns only after successful payment.

---

# Wallet Rules

Wallet balance must be checked.

Wallet debit only after validation.

Wallet ledger entry mandatory.

Rollback on failure.

---

# COD Rules

Order

PLACED

Payment

UNPAID

Payment completes after delivery.

---

# Online Payment Rules

Current

Coming Soon

Future

UPI

Cards

Net Banking

Google Pay

Apple Pay

Razorpay

Stripe

Flow

PENDING_PAYMENT

↓

Gateway

↓

Success

↓

PLACED

↓

Inventory

↓

Coupon Burn

---

# Order Cancellation

Allowed

PLACED

STORE_ACCEPTED

Not Allowed

OUT_FOR_DELIVERY

DELIVERED

REFUNDED

Future

Admin Override

---

# Refund Rules

Wallet Payment

↓

Wallet Credit

Online Payment

↓

Gateway Refund

COD

↓

No Refund

unless prepaid amount exists.

---

# OTP Delivery

OTP generated

During order creation.

OTP verified

During delivery.

OTP invalid

↓

Reject Delivery

OTP correct

↓

Mark Delivered

↓

Complete Payment

---

# Reorder

Customer

↓

Previous Order

↓

Validate Products

↓

Validate Inventory

↓

Create New Cart

↓

Navigate Checkout

Unavailable products

↓

Ignore

Out of stock

↓

Notify Customer

Price Changes

↓

Use Latest Price

Never old price.

---

# Notifications

Order Placed

Store Accepted

Packing

Out For Delivery

Delivered

Cancelled

Refund Completed

---

# WebSocket Events

ORDER_CREATED

ORDER_ACCEPTED

ORDER_PACKING

ORDER_PICKED

ORDER_OUT_FOR_DELIVERY

ORDER_DELIVERED

ORDER_CANCELLED

---

# Order History

Every status change

↓

Insert History

History is immutable.

Never update history.

---

# Order Timeline

Order Created

↓

Store Accepted

↓

Packing

↓

Pickup

↓

Out For Delivery

↓

Delivered

Timeline visible in Flutter.

---

# Tracking Integration

Tracking starts

Only after

OUT_FOR_DELIVERY

Tracking receives

Driver

ETA

Live Location

Route

---

# Delivery Partner

Assign

↓

Accept

↓

Pickup

↓

Deliver

↓

OTP Verification

↓

Complete

---

# Store Module

New Order

↓

Accept

↓

Reject

↓

Pack

↓

Ready

↓

Assign Rider

---

# Payment Integration

Future

Payment Strategy Pattern

WalletStrategy

CODStrategy

UPIStrategy

CardStrategy

StripeStrategy

RazorpayStrategy

Controller never knows payment implementation.

---

# Fraud Prevention

Duplicate Orders

Double Payment

Duplicate Coupons

Inventory Race Conditions

Wallet Double Debit

OTP Replay

must be prevented.

---

# Transactions

Single Transaction

Create Order

Payment

Wallet

Inventory

Coupon

Cart Cleanup

History

If one fails

Rollback everything.

---

# Audit

Track

Created

Updated

Cancelled

Refunded

Delivered

OTP Verified

Store Accepted

Partner Assigned

---

# Performance

Pagination

Projection

Indexes

Lazy Loading

Batch Inserts

Avoid N+1

---

# Future Features

Scheduled Delivery

Express Delivery

Multi Store

Warehouse Routing

Partial Delivery

Split Orders

Returns

Exchange

Subscription Orders

Gift Orders

Corporate Orders

Invoice Download

Digital Receipt

Order Notes

Delivery Instructions

Priority Delivery

---

# Review Checklist

✓ Customer Validated

✓ Address Validated

✓ Cart Validated

✓ Coupon Validated

✓ Inventory Checked

✓ Payment Processed

✓ Wallet Updated

✓ Coupon Burned

✓ Inventory Updated

✓ Cart Cleared

✓ History Recorded

✓ Notification Sent

✓ WebSocket Sent

✓ Transaction Safe

✓ Production Ready

---

# Copilot Instructions

Whenever generating Order module code

Never place business logic in Controller.

Always use DTOs.

Always use Transaction.

Always record Order History.

Always validate Inventory.

Always validate Coupon.

Always validate Wallet.

Always use Strategy Pattern for Payments.

Always publish WebSocket events.

Always design for future payment gateways.

Never expose Order Entity directly.

Always think about rollback scenarios.

Design the Order module to support millions of orders.