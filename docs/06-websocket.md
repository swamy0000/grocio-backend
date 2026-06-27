# Grocio WebSocket Standards
Version: 1.0

---

# Purpose

This document defines all real-time communication standards used in Grocio.

Every live event between backend and Flutter must follow these rules.

Goals

- Real-time Order Tracking
- Live Driver Location
- Instant Notifications
- Store Dashboard Updates
- Delivery Partner Updates
- Future Horizontal Scaling

---

# Why WebSocket

HTTP

Client

↓

Request

↓

Response

↓

Connection Closed

WebSocket

Client

↓

Connection Open

↓

Server Push

↓

Real-time Updates

---

# Current Use Cases

✓ Order Status

✓ Store Dashboard

Future

Driver Location

Wallet Updates

Notifications

Offers

Admin Dashboard

Inventory

Chat

---

# WebSocket Lifecycle

Flutter App

↓

Connect

↓

Authenticate

↓

Subscribe

↓

Receive Events

↓

Reconnect (If Lost)

↓

Disconnect

---

# Connection Endpoint

/ws

Future

wss://api.grocio.com/ws

Always use Secure WebSocket in production.

---

# Authentication

Current

Anonymous

Future

JWT Authentication

Authorization Header

Bearer Token

Validate before subscription.

---

# Topic Naming

Use

/topic

Examples

/topic/store/orders

/topic/order/{orderId}

/topic/user/{userId}/orders

/topic/user/{userId}/wallet

/topic/user/{userId}/notifications

/topic/driver/{driverId}

/topic/admin/dashboard

---

# Event Naming

Events should describe business actions.

Correct

ORDER_PLACED

ORDER_ACCEPTED

ORDER_PACKING

ORDER_READY

OUT_FOR_DELIVERY

DELIVERED

PAYMENT_COMPLETED

WALLET_UPDATED

COUPON_APPLIED

NOTIFICATION_RECEIVED

Incorrect

UPDATE

CHANGE

DATA

MESSAGE

---

# Event Payload

Always send JSON.

Never send plain String.

Correct

{
    "event":"ORDER_STATUS_CHANGED",
    "orderId":35,
    "status":"PACKING",
    "timestamp":"..."
}

Incorrect

"PACKING"

---

# Event Structure

Every event

event

payload

timestamp

requestId

Example

{
    "event":"DRIVER_LOCATION_UPDATED",

    "payload":{

        "driverId":12,

        "latitude":17.456,

        "longitude":78.345,

        "bearing":125

    },

    "timestamp":"...",

    "requestId":"REQ123"
}

---

# Driver Location

Future

Update every

2-5 seconds

Avoid

Every second

to reduce battery usage.

---

# Location Fields

Latitude

Longitude

Bearing

Speed

Accuracy

UpdatedAt

---

# Order Events

ORDER_CREATED

ORDER_ACCEPTED

ORDER_PACKING

ORDER_READY

OUT_FOR_DELIVERY

ARRIVING

DELIVERED

CANCELLED

REFUNDED

---

# Wallet Events

BALANCE_UPDATED

MONEY_ADDED

PAYMENT_DEBITED

REFUND_CREDITED

---

# Coupon Events

COUPON_RESERVED

COUPON_APPLIED

COUPON_REJECTED

COUPON_BURNED

---

# Notification Events

NEW_NOTIFICATION

PROMOTION

ORDER_UPDATE

PAYMENT_UPDATE

SYSTEM_ALERT

---

# Store Events

NEW_ORDER

ORDER_CANCELLED

ORDER_ASSIGNED

ORDER_PICKED

---

# Delivery Events

NEW_DELIVERY

DELIVERY_ACCEPTED

LOCATION_UPDATED

OTP_VERIFIED

DELIVERY_COMPLETED

---

# Heartbeat

Future

Ping every

30 seconds

Pong response

Disconnect after timeout.

---

# Reconnection

Flutter should automatically reconnect.

Retry

1 sec

2 sec

5 sec

10 sec

30 sec

Maximum

60 sec

Use exponential backoff.

---

# Duplicate Events

Every event should contain

eventId

Ignore duplicate eventIds.

---

# Ordering

Events should include

sequenceNumber

Prevent out-of-order updates.

---

# Offline Handling

If connection lost

Reconnect

↓

Call REST API

↓

Sync Latest State

Never depend only on WebSocket.

---

# Security

Authenticate connection.

Authorize subscription.

Never expose another user's events.

Validate order ownership.

---

# Logging

Log

Connection

Disconnect

Subscription

Errors

Reconnections

Never log

JWT

Sensitive Payload

---

# Performance

Small payloads.

No unnecessary fields.

Compress if needed.

Avoid sending full Order object repeatedly.

Send only changed fields.

---

# Future Scaling

Current

Spring WebSocket

Future

Redis Pub/Sub

↓

Multiple Backend Servers

↓

RabbitMQ

↓

Kafka

↓

Event Streaming

---

# Event Bus

Future

Every business event

↓

Event Bus

↓

Notification

↓

Analytics

↓

Tracking

↓

Email

↓

SMS

---

# Event Versioning

Future

eventVersion

Allows backward compatibility.

---

# Monitoring

Track

Active Connections

Messages/sec

Disconnect Rate

Reconnect Rate

Latency

---

# Testing

Test

Connection

Reconnect

Duplicate Events

Large Payloads

Network Failure

Authorization

---

# Review Checklist

✓ Topic Naming

✓ JSON Payload

✓ Authentication

✓ Authorization

✓ Small Payload

✓ Logging

✓ Retry Strategy

✓ Heartbeat

✓ Monitoring

✓ Future Ready

---

# Copilot Instructions

Whenever generating WebSocket code

Always use JSON payloads.

Never send plain strings.

Always authenticate users.

Always validate ownership.

Use business event names.

Keep payloads small.

Support reconnection.

Design events for future scaling.

Prefer event-driven architecture over tightly coupled updates.