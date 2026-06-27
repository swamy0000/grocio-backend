# Financial Engine

## Purpose

The Financial Engine is responsible for **all money movement** inside Grocio.

It handles payment processing, wallet management, and refund operations to ensure secure, traceable, and auditable financial transactions.

---

## Responsibilities

### Payment
- Process customer payments
- Manage payment lifecycle (initiation, validation, confirmation)
- Handle payment retries and timeouts
- Integrate with payment gateways

### Wallet
- Manage customer wallet balance
- Handle wallet transactions (credit, debit)
- Maintain wallet transaction history
- Enforce transaction immutability

### Refund
- Process refunds for canceled/failed orders
- Manage refund lifecycle
- Handle refund retries
- Track refund status

### Gateway Integration
- Integrate with Razorpay (primary)
- Handle webhook callbacks from payment gateways
- Validate webhook authenticity
- Process asynchronous payment confirmations

---

## Does NOT Own

The Financial Engine explicitly does **not** own:

- **Orders** — Order module handles order lifecycle, fulfillment, and tracking
- **Inventory** — Product module manages inventory, stock levels, and warehouse operations
- **Notifications** — Notification module sends emails, SMS, and push notifications
- **Customer** — Profile module manages customer account and personal information

---

## Business Principles

1. **Every rupee is traceable** — All financial transactions are logged with full audit trail
2. **Every financial operation is auditable** — All changes are recorded with timestamps and actors
3. **Every failure is recoverable** — Failed transactions can be retried or rolled back
4. **Backend is source of truth** — Business logic resides in backend; clients trust backend decisions
5. **Webhook is authoritative** — Asynchronous payment confirmations via webhook supersede synchronous responses
6. **One active payment per order** — Only one payment can be processing for an order at any time
7. **Multiple payment attempts allowed** — Customers can retry failed payments (up to max retries)
8. **Wallet transactions are immutable** — Once recorded, wallet transactions cannot be modified or deleted

---

## Architecture

```
Financial Engine
├── Payment Module
│   ├── Payment Service
│   ├── Payment Gateway Integration
│   └── Webhook Handler
├── Wallet Module
│   ├── Wallet Service
│   ├── Transaction Service
│   └── Balance Manager
├── Refund Module
│   ├── Refund Service
│   ├── Refund Processor
│   └── Refund Retry Handler
└── Shared
    ├── Constants
    ├── Exceptions
    └── Enums
```

---

## Future Scope

### Payment Gateways
- Stripe integration
- PhonePe integration
- PayPal integration

### Wallet Features
- Gift Cards
- Loyalty Points
- Referral Rewards

### Advanced Payment Features
- Split Payments
- Partial Refunds
- Installment Plans
- Buy-Now-Pay-Later (BNPL)

---

## Key Constants

See `financial.shared.constants.FinancialConstants` for default values:
- Default currency: INR
- Payment timeout: 10 minutes
- Max payment retries: 3
- Max refund retries: 3
- Default gateway: RAZORPAY

---

## Exception Hierarchy

All financial exceptions extend `FinancialException`:
- `PaymentException` — Payment-related errors
- `WalletException` — Wallet-related errors
- `RefundException` — Refund-related errors

---

## Development Guidelines

- **No hardcoded values** — Use constants from `FinancialConstants`
- **Validate always** — All inputs must be validated before processing
- **Log extensively** — Log all financial operations for audit trail
- **Handle failures gracefully** — Implement retry logic and fallbacks
- **Test thoroughly** — Financial code requires comprehensive test coverage

---

## Related Modules

- **Order Module** — Initiates payment requests
- **Notification Module** — Sends payment/refund confirmations
- **Profile Module** — Provides customer information
- **Product Module** — Provides pricing information
