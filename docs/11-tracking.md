# Grocio Order Tracking System
Version: 1.0

---

# Purpose

The Tracking module provides real-time order visibility to customers, delivery partners, stores, and administrators.

Tracking must be

Reliable

Scalable

Provider Independent

Real-time

Production Ready

---

# Vision

Grocio tracking should provide an experience similar to

Blinkit

Zepto

Swiggy Instamart

Uber Eats

Amazon

while remaining independent of any map provider.

---

# Core Philosophy

Business Logic

↓

Map Engine

↓

Map Provider

↓

Rendering Engine

Never couple business logic with Google Maps or OpenStreetMap.

---

# Architecture

Flutter UI

↓

TrackingMap Widget

↓

GrocioMapController

↓

MapEngine

↓

OpenStreetMapEngine

GoogleMapEngine (Future)

MapboxEngine (Future)

---

# Provider Independence

Supported Providers

Current

OpenStreetMap

Future

Google Maps

Mapbox

HERE Maps

Changing providers should not affect UI code.

---

# Map Components

Map

Markers

Polylines

Camera

Route

ETA

Driver

Store

Customer

---

# Marker Types

STORE

CUSTOMER

DRIVER

WAREHOUSE

CURRENT_LOCATION

PICKUP

DROP

---

# Driver Tracking Flow

Store Accepts Order

↓

Assign Driver

↓

Driver Shares Location

↓

Backend Receives Location

↓

WebSocket Broadcast

↓

Flutter Updates Marker

↓

Smooth Animation

---

# Tracking Status

STORE_ACCEPTED

PACKING

READY_FOR_PICKUP

OUT_FOR_DELIVERY

ARRIVING

DELIVERED

Tracking starts only after

OUT_FOR_DELIVERY.

---

# Driver Location

Update every

2–5 seconds

Fields

driverId

latitude

longitude

bearing

speed

accuracy

updatedAt

---

# ETA

Current

Routing Service

Future

Traffic

Road Closures

Machine Learning ETA

---

# Route Rendering

Current

OSRM Routing

Future

Google Directions

Mapbox Directions

Alternative Routes

Traffic Layer

---

# Camera Behaviour

Initial

Fit Store + Customer

When Delivery Starts

Follow Driver

Customer Interaction

Disable Auto Follow

Follow Button

Re-enable Auto Follow

---

# Marker Animation

Never jump markers.

Always interpolate movement.

Smooth duration

300–600 ms

Use bearing for vehicle rotation.

---

# Route Refresh

Current

Every location update

Future

Refresh only if deviation > 50 meters

Reduce API calls.

---

# Driver Card

Show

Driver Name

Rating

Vehicle Number

Phone Button

Chat Button

ETA

Delivery Status

---

# Customer View

Map

↓

Driver

↓

ETA

↓

OTP

↓

Order Status

↓

Delivery Details

---

# Store View

Assigned Driver

↓

Pickup Status

↓

Delivery Progress

↓

Completion

---

# Delivery Partner View

Pickup Route

↓

Customer Route

↓

OTP Verification

↓

Delivery Complete

---

# Offline Behaviour

Internet Lost

↓

Freeze Marker

↓

Show Last Known Location

↓

Reconnect

↓

Sync Latest Location

---

# WebSocket Events

DRIVER_ASSIGNED

LOCATION_UPDATED

ETA_UPDATED

ORDER_STATUS_CHANGED

DELIVERY_COMPLETED

---

# Tracking APIs

GET

/orders/{id}/tracking

GET

/drivers/{id}/location

POST

/drivers/location

GET

/orders/{id}/route

---

# Tracking Database

driver_locations

Current Location

Future

driver_location_history

delivery_routes

route_cache

tracking_events

---

# Performance

Throttle updates.

Avoid unnecessary redraws.

Cache markers.

Update only changed objects.

Use lightweight payloads.

---

# Security

Customer

↓

Can view only own order.

Driver

↓

Can update only assigned order.

Store

↓

Can view only store orders.

Admin

↓

Can view all.

---

# Future Features

Live Traffic

Multiple Stops

Warehouse Tracking

Heat Maps

Delivery Zones

Geo Fencing

Driver Arrival Alerts

Dynamic ETA

Route Optimization

Delivery Replay

Navigation Integration

---

# Flutter Architecture

core/maps

↓

MapEngine

↓

GrocioMapController

↓

TrackingMap Widget

↓

OrderTrackingScreen

UI never talks directly to flutter_map.

---

# Map Folder Structure

core/maps

engine/

providers/

controllers/

models/

services/

factory/

widgets/

---

# Review Checklist

✓ Driver Tracking

✓ ETA

✓ Route

✓ WebSocket

✓ Smooth Animation

✓ Provider Independent

✓ Offline Support

✓ Security

✓ Performance

✓ Production Ready

---

# Copilot Instructions

Whenever generating tracking code

Never couple UI with a map provider.

Always use MapEngine abstraction.

Animate driver movement.

Update only changed markers.

Use WebSocket for real-time updates.

Support future provider switching.

Keep business logic outside widgets.

Design for enterprise scalability.