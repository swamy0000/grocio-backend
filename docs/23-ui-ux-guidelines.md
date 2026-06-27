# Grocio UI/UX Guidelines
Version: 1.0

---

# Purpose

This document defines the design principles, UI standards, UX patterns, and interaction rules used across all Grocio applications.

Consistency is more important than creativity.

Every screen should feel like it belongs to the same product.

---

# Design Philosophy

Simple

Fast

Modern

Minimal

Accessible

Consistent

Responsive

Delightful

---

# Inspiration

Blinkit

Zepto

Uber Eats

Swiggy Instamart

Google Material 3

Apple Human Interface Guidelines

---

# Core Principles

Speed First

One Hand Usage

Minimal Clicks

Readable Content

Clear Hierarchy

Predictable Navigation

---

# Layout Rules

Use

8dp Grid System

Spacing

4

8

12

16

20

24

32

48

Never use random spacing values.

---

# Screen Structure

AppBar

↓

Content

↓

Bottom Sheet / FAB (Optional)

↓

Bottom Navigation

Maintain the same structure wherever possible.

---

# Typography

Heading 1

32

Heading 2

24

Heading 3

20

Title

18

Body

16

Caption

14

Small

12

Use a single font family across the app.

---

# Color System

Primary

Secondary

Success

Warning

Error

Background

Surface

Outline

Text Primary

Text Secondary

Never hardcode colors.

Use AppTheme.

---

# Border Radius

Small

8

Medium

12

Large

16

Extra Large

24

Buttons and cards should follow the same radius.

---

# Elevation

0

Flat

2

Card

4

Dialog

8

Bottom Sheet

Avoid excessive shadows.

---

# Icons

Material Icons

Consistent Size

20

24

28

32

Avoid mixing icon packs.

---

# Buttons

Primary

Filled

Secondary

Outlined

Ghost

Text

Danger

Loading Button

Disabled Button

Maintain consistent heights.

---

# Button Height

Small

40

Medium

48

Large

56

---

# Cards

Use for

Products

Orders

Coupons

Wallet

Addresses

Notifications

Analytics

Cards should have

Padding

Rounded Corners

Consistent Shadow

---

# Images

Product Images

1:1

Banner

16:9

Profile

Circle

Category

Square

Use placeholders while loading.

---

# Search

Always visible where appropriate.

Debounce

300ms

Support

Suggestions

Recent Searches

Popular Searches

Clear Button

Voice Search (Future)

---

# Lists

Use

Lazy Loading

Pagination

Pull To Refresh

Skeleton Loader

Avoid loading everything.

---

# Forms

Inline Validation

Helpful Errors

Keyboard Types

Auto Focus (when appropriate)

Loading State

Disable submit while processing.

---

# Empty States

Always include

Illustration

Message

Action Button

Examples

Empty Cart

No Orders

No Search Results

No Notifications

---

# Error States

Network Error

↓

Retry Button

Server Error

↓

Support Option

Validation Error

↓

Inline Message

---

# Loading States

Skeleton

Shimmer

Progress Indicator

Never show blank screens.

---

# Bottom Sheets

Preferred for

Order Details

Filters

Address Selection

Payment Options

Coupons

Share

Avoid full-screen dialogs unless necessary.

---

# Dialogs

Confirmation

Delete

Logout

Permission

Error

Success

Keep dialogs concise.

---

# Navigation

Bottom Navigation

Main Modules

Drawer (Future)

Use named routes.

---

# Animations

Use

Fade

Slide

Scale

Hero

Lottie

Keep animations under

300ms

Avoid distracting animations.

---

# Feedback

SnackBar

Success

Error

Info

Warning

Haptic Feedback (Future)

---

# Accessibility

Minimum Touch Target

48x48

Readable Font Sizes

Contrast

Screen Reader Friendly

Keyboard Navigation (Web)

---

# Responsive Design

Support

Phone

Tablet

Web

Desktop (Future)

Never assume screen size.

---

# Dark Mode

Future Support

Use theme colors only.

Avoid hardcoded whites/blacks.

---

# Maps

Use

TrackingMap Widget

Never use provider-specific widgets directly.

---

# Performance

Optimize images.

Avoid unnecessary rebuilds.

Use const widgets.

Lazy load large lists.

Cache network images.

---

# Micro Interactions

Button Press

Favorite Animation

Add to Cart Animation

Order Status Animation

Pull To Refresh

Page Transitions

Keep subtle.

---

# Notifications

Badge

Unread Count

Grouped Notifications

Smooth Updates

---

# Review Checklist

✓ Consistent Spacing

✓ Theme Colors

✓ Typography

✓ Responsive

✓ Loading States

✓ Error States

✓ Empty States

✓ Accessibility

✓ Performance

✓ Production Ready

---

# Copilot Instructions

Whenever generating Flutter UI

Use AppTheme.

Use reusable widgets.

Follow 8dp spacing.

Keep consistent typography.

Support loading, error, and empty states.

Avoid hardcoded values.

Design mobile-first.

Use responsive layouts.

Maintain consistent interaction patterns.