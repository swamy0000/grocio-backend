# Grocio Flutter Architecture
Version: 1.0

---

# Purpose

This document defines the Flutter architecture for the Grocio applications.

All Flutter applications

Customer

Store

Delivery Partner

Admin

must follow the same architecture.

The architecture should be

Scalable

Maintainable

Reusable

Testable

Enterprise Ready

---

# Architecture

Presentation

↓

Application

↓

Domain

↓

Data

↓

Core

This is inspired by Clean Architecture while remaining practical.

---

# Folder Structure

lib/

core/

shared/

features/

routes/

main.dart

---

# Core Module

Contains reusable infrastructure.

core/

api/

auth/

constants/

exceptions/

extensions/

maps/

network/

storage/

theme/

utils/

widgets/

services/

---

# Shared Module

Contains reusable business-independent code.

shared/

models/

widgets/

helpers/

animations/

dialogs/

bottom_sheets/

extensions/

mixins/

validators/

---

# Features

Every feature is isolated.

features/

authentication/

home/

search/

cart/

checkout/

orders/

tracking/

wallet/

coupons/

profile/

address/

notifications/

settings/

---

# Feature Structure

Each feature follows

feature/

bindings/

controllers/

models/

repositories/

services/

widgets/

screens/

state/

---

Example

orders/

bindings/

controllers/

models/

repositories/

services/

screens/

widgets/

---

# Responsibilities

Presentation

Only UI

Controller

Business coordination

Repository

Data source

Service

API calls

Models

Serialization

Widgets

Reusable UI

---

# State Management

Current

GetX

Reasons

Simple

Fast

Dependency Injection

Routing

Reactive

Future

Riverpod (optional)

---

# Routing

Centralized routing.

Never navigate using widget imports.

Use

AppRoutes

Get.toNamed()

---

# Dependency Injection

Use

Get.put()

Get.lazyPut()

Bindings

Never instantiate services directly inside widgets.

---

# API Layer

Every feature has

Repository

↓

ApiService

↓

Backend

Widgets never call HTTP directly.

---

# Model Flow

Backend DTO

↓

Flutter Model

↓

Controller

↓

UI

---

# UI Rules

Widgets should never

Call APIs

Contain business logic

Store application state

Perform calculations

Widgets display only.

---

# Business Logic

Business logic belongs inside

Controllers

Services

Repositories

Never inside UI.

---

# Reusable Widgets

Buttons

Cards

Dialogs

Bottom Sheets

Search Bars

Order Cards

Product Cards

Shimmers

Empty States

Loading Indicators

All reusable.

---

# Theme

Single Theme

Light

Dark (Future)

Never hardcode colors.

Use

AppTheme

---

# Assets

assets/

images/

icons/

animations/

fonts/

svg/

lottie/

---

# Responsive Design

Support

Android

iOS

Tablet

Web

Desktop (Future)

Never use fixed widths.

---

# Animations

Use

Implicit Animations

Lottie

Hero

AnimatedContainer

AnimatedSwitcher

Avoid excessive animations.

---

# Forms

Validation

Formatter

Error Messages

Loading State

Submit State

Success State

---

# Error Handling

Network Error

Server Error

Validation Error

Timeout

Unauthorized

Offline

Show friendly UI.

---

# Loading States

Skeleton

Shimmer

Progress

Placeholder

Avoid blank screens.

---

# Empty States

No Orders

No Search Results

Empty Cart

No Notifications

No Address

Always provide call-to-action.

---

# Offline Support

Future

Cache

Retry

Offline Queue

Sync Later

---

# Performance

Lazy Lists

Pagination

Caching

Image Optimization

Debouncing

Dispose Controllers

Avoid rebuilds

---

# Search

Debounce

300 ms

Pagination

Suggestions

History

Popular

---

# Maps

Use

MapEngine

GrocioMapController

Never directly use flutter_map or GoogleMap inside screens.

---

# WebSocket

Single WebSocket Manager

Shared

Reconnect Automatically

Expose Streams

---

# Localization

Future

English

Telugu

Hindi

Tamil

Kannada

---

# Security

Secure Storage

JWT

No sensitive logs

Certificate Pinning (Future)

---

# Testing

Widget Tests

Controller Tests

Golden Tests

Integration Tests

---

# Folder Example

features/

orders/

controllers/

order_controller.dart

repositories/

order_repository.dart

services/

order_service.dart

models/

order_response.dart

screens/

orders_screen.dart

widgets/

order_card.dart

order_search_bar.dart

---

# Naming

Screen

OrdersScreen

Controller

OrdersController

Repository

OrdersRepository

Service

OrdersService

Widget

OrderCard

---

# Review Checklist

✓ Feature Isolated

✓ API Separated

✓ Widgets Reusable

✓ Theme Used

✓ Responsive

✓ Loading States

✓ Error States

✓ Testable

✓ Performance Optimized

✓ Production Ready

---

# Copilot Instructions

Whenever generating Flutter code

Follow feature-based architecture.

Keep widgets dumb.

Place business logic inside controllers.

Use GetX for state management.

Never call APIs directly from widgets.

Use reusable widgets.

Support responsive layouts.

Use AppTheme.

Design for future scalability.

Keep code clean and modular.