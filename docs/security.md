# Grocio Backend Security Guidelines
Version: 1.0

---

# Purpose

This document defines the security standards for the Grocio platform.

Security is a responsibility shared by every module.

Every API, Service, Database operation, and WebSocket event must follow these standards.

Goals

- Protect Customer Data
- Protect Payments
- Prevent Unauthorized Access
- Prevent Fraud
- Enterprise Security
- OWASP Compliance

---

# Security Philosophy

Never trust the client.

Flutter is only a UI.

Every request must be validated by backend.

Backend is the single source of truth.

---

# Authentication

Current

JWT Authentication

Future

Access Token

Refresh Token

Device Tokens

Session Management

---

# Authorization

Authentication

↓

Authorization

↓

Business Validation

Never skip ownership verification.

---

# User Roles

Current

CUSTOMER

Future

STORE_OWNER

DELIVERY_PARTNER

ADMIN

SUPER_ADMIN

SUPPORT_AGENT

---

# JWT

JWT contains

userId

phoneNumber

role

issuedAt

expiry

Never store

PIN

Password

Wallet Balance

Sensitive Information

inside JWT.

---

# JWT Expiration

Access Token

24 Hours

Future

Refresh Token

30 Days

---

# Protected APIs

Require JWT

Orders

Wallet

Coupons

Addresses

User Profile

Tracking

Payments

Notifications

---

# Public APIs

Login

Register

Check Phone

Home

Products

Categories

Banners

Search Suggestions

---

# PIN Security

Never log PIN.

Never expose PIN.

Never return PIN in API.

Future

PIN hashing using BCrypt.

---

# Password Policy

Future

Minimum 8 characters

Uppercase

Lowercase

Number

Special Character

---

# OTP Security

Generate Random OTP.

4 digits (Current)

6 digits (Future)

Expire after 5 minutes.

Never store plaintext OTP long term.

---

# Device Validation

Future

Device ID

Platform

Last Login

Trusted Devices

Logout All Devices

---

# Ownership Validation

Always verify

Order belongs to user.

Wallet belongs to user.

Address belongs to user.

Coupon belongs to user.

Never trust userId from request body.

Extract from JWT where possible.

---

# Input Validation

Always validate

@NotNull

@NotBlank

@Email

@Pattern

@Positive

Backend validation is mandatory.

---

# SQL Injection

Always use JPA.

Never build SQL strings manually.

Wrong

SELECT * FROM users WHERE phone='"

Correct

Repository Methods

Prepared Statements

---

# XSS

Escape user generated content.

Never trust frontend.

---

# CSRF

Current

Disabled

Reason

JWT Stateless Authentication

---

# CORS

Allow only trusted origins.

Development

Flutter localhost

Production

Official Domains Only

Never use

*

in production.

---

# Headers

Always include

Authorization

Content-Type

Accept

Future

X-Request-ID

Idempotency-Key

Device-ID

---

# Logging

Log

Login

Logout

Order

Wallet

Coupon

Payment

Admin Actions

Never log

PIN

JWT

OTP

Password

Card Data

Sensitive Personal Data

---

# Secrets

Never commit

JWT Secret

Database Password

API Keys

Firebase Keys

Google Maps Keys

Use

Environment Variables

Secret Manager (Future)

---

# Database Security

Least Privilege

Parameterized Queries

Encrypted Backups

Audit Logging

Future

Read Replica Accounts

Separate Analytics User

---

# API Security

Validate Request

Validate JWT

Validate Ownership

Validate Business Rules

Return DTO

Never expose Entity

---

# WebSocket Security

Authenticate before connection.

Authorize subscriptions.

Do not broadcast sensitive data.

Validate order ownership before sending events.

---

# Payment Security

Never trust payment status from Flutter.

Backend verifies payment gateway response.

Wallet deduction only inside transaction.

Coupon burn only after payment success.

---

# Wallet Security

Prevent duplicate debit.

Ledger based transactions.

Every wallet operation must be auditable.

---

# Coupon Security

Prevent multiple use.

Prevent concurrent redemption.

Validate expiration.

Validate ownership.

Burn only after successful payment.

---

# Rate Limiting

Future

Login

OTP

Search

Coupon Apply

Wallet

Payment

Profile Update

---

# Brute Force Protection

Future

Lock account after repeated failures.

Temporary cooldown.

Captcha after threshold.

---

# Session Management

Future

Logout

Logout All Devices

Device Revocation

Refresh Token Rotation

---

# File Upload Security

Validate MIME Type.

Validate Size.

Virus Scan (Future).

Never execute uploaded files.

Store outside application directory.

---

# Error Messages

Good

Invalid credentials.

Bad

Phone exists but PIN incorrect.

Avoid revealing sensitive information.

---

# Data Privacy

Collect only required data.

Mask sensitive fields.

Support future data export and account deletion.

---

# Audit Logs

Track

Login

Wallet

Payment

Order Status

Admin Changes

Coupon Creation

Inventory Updates

Audit logs are immutable.

---

# Security Headers

Future

HSTS

X-Frame-Options

Content-Security-Policy

X-Content-Type-Options

Referrer-Policy

---

# OWASP

Follow OWASP Top 10.

Prevent

Injection

Broken Authentication

Sensitive Data Exposure

Broken Access Control

Security Misconfiguration

---

# Incident Response

Future

Alerting

Audit Investigation

Token Revocation

Emergency Maintenance

---

# Review Checklist

✓ JWT Verified

✓ Authorization Checked

✓ Ownership Verified

✓ Validation Done

✓ No Sensitive Logs

✓ DTO Returned

✓ Transactions Safe

✓ Secrets Externalized

✓ Security Headers

✓ Production Ready

---

# Copilot Instructions

When generating security related code

Always use JWT.

Never expose sensitive fields.

Always validate ownership.

Always validate requests.

Never trust Flutter.

Never log secrets.

Always think OWASP.

Prefer secure defaults over convenience.

If unsure, choose the safer implementation.