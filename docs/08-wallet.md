# Grocio Wallet System
Version: 1.0

---

# Purpose

The Wallet module is Grocio's internal digital wallet.

The wallet must provide secure, auditable and transaction-safe money management.

Every movement of money must be traceable.

The wallet is not just a balance.

It is a financial ledger.

---

# Vision

The Wallet module should support

Customer Wallet

Cashback

Refunds

Rewards

Referral Bonus

Gift Credits

Promotional Credits

Admin Adjustments

Future

UPI Top-up

Card Top-up

Auto Recharge

Wallet Transfer

Subscriptions

---

# Responsibilities

Maintain Balance

Ledger

Credits

Debits

Refunds

Cashback

Rewards

Top-up

Payment Settlement

Audit

History

---

# Wallet Components

Wallet

↓

Balance

↓

Transactions

↓

Ledger

↓

History

↓

Reports

---

# Wallet Balance

Wallet balance is only a summary.

Actual source of truth

↓

Wallet Transactions

Never trust balance alone.

Balance should always be recalculated from ledger if needed.

---

# Wallet Transaction Types

CREDIT

DEBIT

REFUND

CASHBACK

BONUS

TOPUP

EXPIRY

ADJUSTMENT

REVERSAL

---

# Wallet Transaction Status

PENDING

SUCCESS

FAILED

REVERSED

CANCELLED

---

# Wallet Sources

Order Refund

↓

Wallet Credit

Cashback

↓

Wallet Credit

Top-up

↓

Wallet Credit

Order Payment

↓

Wallet Debit

Admin Bonus

↓

Wallet Credit

---

# Wallet Flow

Current Balance

↓

Validation

↓

Ledger Entry

↓

Balance Update

↓

Notification

↓

History

---

# Ledger Rules

Every money movement creates

ONE

immutable ledger record.

Never edit ledger.

Never delete ledger.

Corrections

↓

New Adjustment Entry

---

# Payment Flow

Wallet Selected

↓

Balance Check

↓

Sufficient?

↓

YES

↓

Debit

↓

Order Paid

↓

Record Ledger

↓

Notification

↓

Complete

NO

↓

Reject Payment

↓

Show Error

---

# Refund Flow

Order Cancelled

↓

Refund Eligible?

↓

YES

↓

Wallet Credit

↓

Ledger Entry

↓

Notification

↓

Balance Updated

---

# Cashback Flow

Eligible Order

↓

Cashback Rule

↓

Wallet Credit

↓

Ledger

↓

Notification

---

# Referral Bonus

Invite Friend

↓

Friend Completes First Order

↓

Reward

↓

Wallet Credit

↓

Ledger

---

# Wallet Top-up

Future

UPI

Cards

Net Banking

Google Pay

Apple Pay

Bank Transfer

---

# Wallet Validation

Balance >= Order Amount

No Duplicate Debit

Valid User

Transaction Not Processed

---

# Wallet Security

Never allow

Negative Balance

Duplicate Debit

Double Refund

Manual Balance Update

Every update

↓

Ledger Entry

---

# Wallet APIs

GET

Wallet Balance

GET

Wallet Transactions

POST

Wallet Top-up

POST

Wallet Refund

POST

Wallet Cashback

GET

Wallet Statement

---

# Wallet Statement

Date

Type

Amount

Balance Before

Balance After

Description

Reference ID

Created At

---

# Wallet Database

wallet

Current Balance

wallet_transactions

Complete Ledger

Future

wallet_limits

wallet_rewards

wallet_cashback

wallet_settlement

---

# Wallet Notifications

Money Added

Money Debited

Refund Received

Cashback Received

Top-up Success

Top-up Failed

---

# Audit

Track

Credits

Debits

Refunds

Cashback

Manual Adjustments

Failed Transactions

Audit records are immutable.

---

# Performance

Index

user_id

created_at

transaction_type

status

Paginate statements.

Never load complete history.

---

# Fraud Prevention

Prevent

Duplicate Debit

Concurrent Debit

Balance Tampering

Replay Requests

Negative Balance

Unauthorized Access

---

# Future Features

Wallet Expiry

Loyalty Wallet

Reward Wallet

Gift Wallet

Corporate Wallet

Split Payments

Wallet Transfer

Auto Recharge

Scheduled Top-up

Family Wallet

Subscription Wallet

---

# Review Checklist

✓ Balance Verified

✓ Ledger Entry Created

✓ Transaction Safe

✓ Audit Recorded

✓ Notification Sent

✓ No Negative Balance

✓ Fraud Checks

✓ History Available

✓ Performance Verified

✓ Production Ready

---

# Copilot Instructions

Whenever generating Wallet module code

Never update balance without creating a ledger entry.

Always validate available balance.

Always use database transactions.

Never delete wallet transactions.

Never modify historical records.

Refunds must create new credit entries.

Cashback must create separate transactions.

Use BigDecimal for all monetary values.

Design the wallet like a banking ledger.

Future-proof for payment gateway integration.