# Grocio Delivery Partner System
Version: 1.0

---

# Purpose

The Delivery Partner module manages rider onboarding, availability, order assignment, live tracking, earnings, navigation, OTP verification, and performance.

This module powers the Delivery Partner application.

---

# Vision

The Delivery Partner System should support

Online / Offline

Live Location

Order Assignment

Navigation

OTP Delivery

Earnings

Ratings

Attendance

Daily Reports

Future

Batch Deliveries

Route Optimization

Shift Management

Heat Maps

AI Assignment

---

# Responsibilities

Partner Registration

Authentication

Availability

Location Updates

Order Acceptance

Pickup

Delivery

OTP Verification

Earnings

Ratings

History

Analytics

---

# Delivery Partner Lifecycle

Register

↓

Verification

↓

Approved

↓

Online

↓

Receive Orders

↓

Pickup

↓

Deliver

↓

Complete

↓

Offline

---

# Partner Status

PENDING_VERIFICATION

APPROVED

REJECTED

ACTIVE

SUSPENDED

BLOCKED

---

# Online Status

ONLINE

OFFLINE

BUSY

ON_BREAK

---

# Order Assignment Flow

New Order

↓

Nearest Available Partner

↓

Assignment

↓

Accept

↓

Pickup

↓

Navigate

↓

Deliver

↓

OTP Verification

↓

Complete

---

# Assignment Rules

Partner must be

Approved

Online

Within Delivery Radius

Not Busy

Vehicle Verified

Sufficient Rating (Future)

---

# Delivery Lifecycle

Assigned

↓

Accepted

↓

Reached Store

↓

Picked Up

↓

Out For Delivery

↓

Arriving

↓

Delivered

---

# Live Location

Update every

2–5 seconds

Fields

partnerId

latitude

longitude

bearing

speed

accuracy

updatedAt

---

# Navigation

Current

OpenStreetMap

OSRM

Future

Google Navigation

Mapbox Navigation

HERE Maps

---

# OTP Verification

Customer receives OTP

↓

Partner enters OTP

↓

Backend validates

↓

Order Delivered

↓

Payment Completed

---

# Earnings

Base Pay

Distance Pay

Incentive

Peak Bonus

Referral Bonus

Tips

Future

Surge Pricing

Weekly Bonus

Rain Bonus

Festival Bonus

---

# Earnings Statement

Date

Orders

Distance

Incentives

Tips

Total Earnings

Settlement Status

---

# Ratings

Customer Rating

Store Rating

Average Rating

Cancellation Rate

Acceptance Rate

Completion Rate

---

# Daily Dashboard

Orders Completed

Distance Travelled

Working Hours

Total Earnings

Average Rating

Acceptance %

Completion %

---

# Performance Metrics

Acceptance Rate

Completion Rate

Cancellation Rate

Late Deliveries

Average Delivery Time

Customer Rating

Store Rating

---

# Notifications

New Order

Pickup Reminder

Customer Arriving

OTP Required

Delivery Completed

Low Battery Reminder (Future)

---

# APIs

POST

/partners/login

GET

/partners/profile

PUT

/partners/status

POST

/partners/location

GET

/partners/orders

POST

/orders/{id}/accept

POST

/orders/{id}/pickup

POST

/orders/{id}/deliver

---

# WebSocket Events

NEW_ORDER_ASSIGNED

ORDER_CANCELLED

LOCATION_UPDATED

DELIVERY_COMPLETED

SYSTEM_NOTIFICATION

---

# Database

delivery_partners

driver_locations

driver_location_history

driver_earnings

driver_attendance

driver_performance

driver_ratings

---

# Fraud Prevention

Prevent

Fake GPS

Multiple Logins

Location Spoofing

OTP Replay

Fake Deliveries

Duplicate Earnings

---

# Security

JWT Authentication

Verify Assigned Orders

Validate OTP

Secure WebSocket

Device Verification (Future)

---

# Future Features

Batch Deliveries

Multiple Orders

AI Order Assignment

Shift Scheduling

Heat Maps

Fuel Tracking

EV Support

Partner Wallet

Partner Loans

Insurance

Emergency SOS

Voice Navigation

Route Optimization

---

# Folder Structure

delivery-partner/

controller/

service/

repository/

dto/

entity/

mapper/

validator/

tracking/

earnings/

ratings/

---

# Review Checklist

✓ Partner Verified

✓ Assignment Logic

✓ Live Location

✓ OTP Validation

✓ Earnings

✓ Ratings

✓ Notifications

✓ Performance Metrics

✓ Security

✓ Production Ready

---

# Copilot Instructions

Whenever generating Delivery Partner code

Always validate assignment.

Always verify order ownership.

Use WebSocket for live updates.

Record location history.

Calculate earnings through services.

Never expose sensitive data.

Design for batch delivery and future scaling.

Keep tracking independent from map provider.