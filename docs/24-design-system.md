# Grocio Design System (GDS)
Version: 1.0

---

# Purpose

The Grocio Design System defines the visual language, reusable components, design tokens, and interaction standards across all Grocio applications.

Every Flutter application

Customer

Store

Delivery Partner

Admin

must use the same design system.

---

# Design Principles

Consistency

Clarity

Accessibility

Scalability

Performance

Reusability

---

# Design Tokens

Design Tokens are the single source of truth.

Never hardcode values.

Everything comes from tokens.

---

# Colors

Primary

Secondary

Success

Warning

Error

Info

Background

Surface

Outline

Divider

Disabled

Shadow

Text Primary

Text Secondary

Text Disabled

---

# Semantic Colors

Order Placed

Blue

Packing

Orange

Out For Delivery

Purple

Delivered

Green

Cancelled

Red

Pending Payment

Amber

Wallet Credit

Green

Wallet Debit

Red

---

# Typography

Display Large

32

Display Medium

28

Heading

24

Title

20

Subtitle

18

Body

16

Caption

14

Small

12

Button

16

Use one font family.

---

# Font Weight

Regular

Medium

SemiBold

Bold

ExtraBold

---

# Spacing Scale

4

8

12

16

20

24

32

40

48

64

Never invent spacing.

---

# Radius Scale

4

8

12

16

20

24

32

---

# Elevation

0

1

2

4

8

12

16

---

# Opacity Tokens

10%

20%

40%

60%

80%

100%

---

# Icon Sizes

16

20

24

28

32

40

48

---

# Avatar Sizes

32

40

48

56

64

80

96

---

# Button System

Primary Button

Secondary Button

Outlined Button

Ghost Button

Danger Button

Loading Button

Icon Button

FAB

Floating Pill Button

---

# Input Components

TextField

SearchField

OTPField

PhoneField

PINField

AmountField

Dropdown

Date Picker

Time Picker

---

# Card Components

Product Card

Order Card

Coupon Card

Wallet Card

Category Card

Notification Card

Address Card

Payment Card

Analytics Card

---

# List Components

Product List

Category List

Order List

Wallet History

Notifications

Search Results

Coupons

Addresses

---

# App Bars

Default

Search

Transparent

Collapsible

Sliver

---

# Navigation

Bottom Navigation

Tab Bar

Navigation Rail

Drawer (Future)

---

# Bottom Sheets

Order Details

Address Picker

Coupon List

Payment Options

Filter Sheet

Sort Sheet

Share Sheet

Confirmation Sheet

---

# Dialogs

Confirmation

Delete

Success

Error

Permission

Logout

Rating

---

# Chips

Filter Chip

Status Chip

Offer Chip

Category Chip

Tag Chip

Selection Chip

---

# Badges

Notification

Discount

New

HOT

BESTSELLER

Out of Stock

---

# Loaders

Circular

Linear

Shimmer

Skeleton

Dot Loader

---

# Empty States

Orders

Cart

Search

Wallet

Coupons

Notifications

Addresses

Tracking

---

# Illustrations

Success

Error

Offline

Empty

Maintenance

Update Required

---

# Motion

Fade

Slide

Scale

Hero

Ripple

Lottie

Keep animations

<300ms

---

# Haptic Feedback

Button

Success

Error

Warning

Future

Navigation

---

# Shadows

Light

Medium

Heavy

Use consistently.

---

# Borders

Outline

Divider

Dashed

Selected

Focused

---

# Image Rules

Product

1:1

Banner

16:9

Category

1:1

Profile

Circle

---

# Status Indicators

Online

Green

Offline

Gray

Busy

Orange

Error

Red

---

# Theme

Light

Dark (Future)

High Contrast (Future)

---

# Accessibility

Touch Target

48dp

Color Contrast

WCAG AA

Readable Fonts

Keyboard Support

---

# Responsive Breakpoints

Phone

0-600

Tablet

600-1024

Desktop

1024+

---

# Naming

AppButton

AppCard

AppDialog

AppTextField

AppSearchBar

AppLoader

AppEmptyState

AppChip

AppBadge

AppAvatar

---

# Folder Structure

core/

theme/

colors.dart

typography.dart

spacing.dart

radius.dart

shadows.dart

icons.dart

components/

buttons/

cards/

dialogs/

inputs/

chips/

badges/

loaders/

---

# Review Checklist

✓ Tokens Used

✓ Components Reusable

✓ Theme Applied

✓ Accessibility

✓ Responsive

✓ Consistent

✓ Performance

✓ Production Ready

---

# Copilot Instructions

Whenever generating Flutter UI

Always use Design Tokens.

Never hardcode colors.

Never hardcode spacing.

Never hardcode font sizes.

Use reusable components.

Follow GDS naming conventions.

Support light and future dark theme.

Keep components generic.

Design once, reuse everywhere.