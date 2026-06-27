# Grocio State Management Guide
Version: 1.0

---

# Purpose

This document defines how application state is managed in all Grocio Flutter applications.

Current State Management

GetX

Future

Riverpod (Optional)

---

# Why GetX

Simple

Reactive

Dependency Injection

Routing

Fast

Less Boilerplate

Easy Testing

Production Ready

---

# GetX Responsibilities

State Management

Dependency Injection

Routing

Lifecycle

Reactive Updates

---

# Folder Structure

feature/

bindings/

controllers/

repositories/

services/

models/

screens/

widgets/

---

# Controller Responsibilities

Controllers coordinate business logic.

Controllers should

Call repositories

Manage loading state

Manage error state

Manage success state

Expose reactive variables

Controllers should never

Perform HTTP calls directly

Contain UI widgets

Navigate without routes

Contain database logic

---

# Repository Responsibilities

Repositories communicate with APIs.

Repository

↓

ApiService

↓

Backend

Repositories return models.

---

# Service Responsibilities

Services handle

HTTP

Local Storage

Secure Storage

Maps

Notifications

WebSocket

Analytics

Services should not manage UI state.

---

# Bindings

Every feature should have one binding.

Example

OrdersBinding

Registers

OrdersController

OrdersRepository

OrdersService

Use

Get.lazyPut()

Never create dependencies inside widgets.

---

# Dependency Injection

Preferred

Get.lazyPut()

Singleton

Get.put()

Temporary

Get.create()

Avoid unnecessary singletons.

---

# Reactive State

Use

RxBool

RxInt

RxString

RxList

RxMap

Rxn<T>

Example

isLoading

orders

searchText

selectedAddress

---

# UI Updates

Use

Obx

GetBuilder

Recommended

Obx for reactive variables.

GetBuilder for manual updates.

---

# Loading State

Each controller should expose

isLoading

isRefreshing

isSubmitting

Example

Loading

↓

Content

↓

Error

↓

Retry

---

# Error State

Expose

errorMessage

hasError

Never throw UI exceptions.

---

# Success State

Expose

isSuccess

message

navigationAction

---

# Controller Lifecycle

onInit()

Load initial data.

onReady()

Execute UI-dependent logic.

onClose()

Dispose

Controllers

Streams

Timers

WebSocket

Animation Controllers

---

# Search Example

searchText

↓

Debounce

↓

Repository

↓

Update Results

Use

300 ms debounce.

---

# Pagination

currentPage

pageSize

hasMore

isLoadingMore

Never fetch all data.

---

# Refresh

Support

Pull To Refresh

Soft Refresh

Background Refresh

---

# WebSocket

One shared

WebSocketManager

Controllers subscribe.

Never create socket inside controllers.

---

# Authentication

AuthController

Stores

JWT

User

Login State

Refresh Token (Future)

---

# Navigation

Use

Get.toNamed()

Get.offAllNamed()

Never push widget classes directly.

---

# Local Storage

Use

GetStorage

SharedPreferences

Secure Storage

Sensitive data

↓

Flutter Secure Storage

---

# API Response Pattern

Loading

↓

Success

↓

Empty

↓

Error

Every API should support all four.

---

# Controller Example

OrdersController

↓

OrdersRepository

↓

OrdersService

↓

ApiClient

↓

Backend

---

# Shared Controllers

AuthController

ThemeController

NotificationController

WebSocketController

Keep global state minimal.

---

# Performance

Dispose controllers.

Avoid nested Obx.

Avoid rebuilding large widgets.

Use workers

debounce

interval

ever

once

---

# Workers

debounce

Search

interval

Live Location

ever

Cart Count

once

Initialization

---

# Testing

Test Controllers

Mock Repositories

Mock Services

Never mock UI.

---

# Anti Patterns

Do NOT

Call API inside Widget

Business Logic in UI

Create Controller inside build()

Nested Obx everywhere

Static global variables

Multiple controllers for same feature

---

# Folder Example

orders/

bindings/

orders_binding.dart

controllers/

orders_controller.dart

repositories/

orders_repository.dart

services/

orders_service.dart

models/

order.dart

screens/

orders_screen.dart

widgets/

order_card.dart

search_bar.dart

---

# Naming Convention

Controller

OrdersController

Repository

OrdersRepository

Service

OrdersService

Binding

OrdersBinding

---

# Review Checklist

✓ Binding Created

✓ Repository Used

✓ Service Used

✓ Controller Reactive

✓ Loading State

✓ Error State

✓ Pagination

✓ Refresh

✓ Disposal

✓ Production Ready

---

# Copilot Instructions

Whenever generating Flutter code

Use GetX architecture.

Use Bindings.

Keep widgets dumb.

Repositories call services.

Controllers manage state.

Expose Rx variables.

Support loading, error, and empty states.

Dispose resources properly.

Never call APIs directly from widgets.

Design for scalability.