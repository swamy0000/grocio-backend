# Grocio Coupon Engine
Version: 1.0

---

# Purpose

The Coupon Engine manages all discounts, promotional campaigns, referral rewards, loyalty benefits, and marketing offers within Grocio.

The engine must be scalable, fraud-resistant, and flexible enough to support future promotional strategies without major code changes.

---

# Vision

The Coupon Engine should support

Public Coupons

Private Coupons

Referral Coupons

Campaign Coupons

First Order Offers

Loyalty Rewards

Festival Offers

Auto Apply Coupons

Membership Discounts

Future

Bank Offers

Brand Sponsored Coupons

Cashback Coupons

Subscription Coupons

Location Based Offers

Category Coupons

Product Coupons

---

# Responsibilities

Coupon Validation

Eligibility Check

Discount Calculation

Reservation

Burn

Usage Tracking

Fraud Prevention

Campaign Support

Analytics

Admin Management

---

# Coupon Lifecycle

Draft

↓

Active

↓

Reserved

↓

Applied

↓

Payment Success

↓

Burned

↓

Expired

---

# Coupon Status

DRAFT

ACTIVE

PAUSED

EXPIRED

DELETED

---

# Coupon Types

PUBLIC

PRIVATE

REFERRAL

FIRST_ORDER

LOYALTY

CAMPAIGN

Future

BANK

BRAND

MEMBERSHIP

CATEGORY

PRODUCT

LOCATION

---

# Coupon Flow

Customer

↓

Apply Coupon

↓

Validate

↓

Reserve

↓

Checkout

↓

Payment Success

↓

Burn Coupon

↓

Update Usage

↓

Record History

---

# Reservation vs Burn

Reservation

Coupon temporarily locked during checkout.

Burn

Coupon permanently consumed after successful payment.

Never burn before successful payment.

---

# Validation Rules

Coupon Exists

Coupon Active

Within Valid Dates

Minimum Cart Value

Maximum Discount

Usage Limit

Per User Limit

Private Eligibility

First Order Eligibility

Campaign Rules

Category Rules (Future)

Product Rules (Future)

---

# Usage Limits

Global Limit

Example

5000 Uses

Per User Limit

Example

1 Use

Daily Limit (Future)

Weekly Limit

Monthly Limit

---

# Discount Types

Flat Discount

Example

₹100 OFF

Percentage Discount

Example

20% OFF

Maximum Discount

Example

20%

Maximum ₹200

---

# Auto Apply

Coupons can be marked

autoApply = true

System automatically selects

Highest Savings

Highest Priority

Only one coupon unless stackable.

---

# Coupon Priority

Higher priority

↓

Applied first

Example

FIRST_ORDER

Priority 100

Festival

Priority 80

Referral

Priority 50

---

# Coupon Ownership

Public

Available to everyone.

Private

Mapped using

coupon_users

Only assigned users can apply.

---

# Referral Coupons

User Invites Friend

↓

Friend Registers

↓

Friend Places First Order

↓

Coupon Generated

↓

Both Users Rewarded

---

# Loyalty Coupons

Generated based on

Orders

Spend

Membership

Customer Level

Future

Gold

Silver

Platinum

---

# Campaign Coupons

Festival

Weekend

Flash Sale

Brand Campaign

Seasonal Offer

---

# Coupon Burn Rules

Burn only after

Payment Success

Inventory Success

Order Created

Never burn on failed payment.

Never burn on cancelled checkout.

---

# Coupon Restore

Future

Payment Failed

↓

Reservation Released

Order Cancelled Before Burn

↓

Reservation Released

Burned Coupon

↓

Never Restore

Unless Admin Override

---

# Fraud Prevention

Prevent

Duplicate Usage

Parallel Checkout Abuse

Expired Coupon Usage

Multiple Device Exploits

Coupon Guessing

Tampered Requests

---

# Coupon Analytics

Track

Total Usage

Unique Users

Discount Given

Campaign Performance

Conversion Rate

Revenue Impact

Abandonment

Top Performing Coupons

---

# Coupon Database

coupons

Master Table

coupon_users

Private Mapping

coupon_usage

Immutable History

Future

coupon_campaigns

coupon_rules

coupon_categories

coupon_products

coupon_locations

---

# Coupon APIs

Validate Coupon

Apply Coupon

Remove Coupon

List Available Coupons

Reserve Coupon

Burn Coupon

Coupon History

Admin Coupon Management

---

# Notifications

Coupon Applied

Coupon Expired

Coupon Reserved

Coupon Burned

Campaign Started

Campaign Ended

---

# Performance

Index

coupon_code

status

valid_from

expiry_date

coupon_type

Use caching for frequently used coupons.

---

# Security

Never trust coupon amount from Flutter.

Backend calculates discount.

Validate ownership.

Validate usage.

Validate payment.

Never expose internal campaign rules.

---

# Future Features

Coupon Stacking

Wallet + Coupon

AI Personalized Coupons

Location Coupons

Weather Based Offers

Dynamic Coupons

Birthday Coupons

Anniversary Coupons

Spin Wheel Rewards

Scratch Cards

Gamification

Partner Offers

Membership Discounts

---

# Review Checklist

✓ Coupon Exists

✓ Coupon Active

✓ Valid Dates

✓ Usage Limit

✓ User Eligibility

✓ Discount Calculated

✓ Reservation Created

✓ Burn After Payment

✓ Usage Recorded

✓ Analytics Updated

✓ Fraud Checks

✓ Production Ready

---

# Copilot Instructions

Whenever generating Coupon module code

Never calculate discount in Controller.

Always validate coupon server-side.

Always reserve before payment.

Always burn after successful payment.

Always record coupon usage.

Never trust Flutter discount values.

Support future campaign extensions.

Design for scalability and fraud prevention.