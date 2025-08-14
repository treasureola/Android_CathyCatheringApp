# Android Cathy Catering App

## Overview
Cathy Catering is a comprehensive Android-based online store platform designed for dessert ordering.  
The app leverages **Firebase Realtime Database** for user authentication, cart management, and order history, while also integrating a **third-party API** to fetch product details.  

---

## Features

### 1. User Authentication
- Register using email and password.
- Login for existing users.
- Firebase Authentication for secure authorization.

### 2. Product Listing
- Dessert product data fetched from an external API.
- Displays product name, description, image, and price.

### 3. Cart Management
- Add products to cart.
- Persistent cart using Firebase Realtime Database (syncs across devices).
- Modify item quantities and remove products.
- Display of total cart price.

### 4. Checkout Process
- Proceed to checkout with cart items.
- Collects test payment information.
- Stores orders in Firebase Realtime Database.

### 5. Order History
- View past orders with details and items.
- Retrieve order history from Firebase.

### 6. User Account
- View account info (name, email, phone number).
- Data stored and retrieved from Firebase.

---

## Technical Implementation
- **Firebase**: Authentication, Realtime Database for orders, cart, and user profiles.
- **API Integration**: Fetch dessert product data.
- **Shared Preferences**: Local storage for user preferences and cart.
- **LiveData & ViewModel**: Manage cart state and update UI reactively.

---

## Potential Improvements
- Integrate secure real payment gateway.
- Improve UI/UX (layout, navigation, and performance).
- Robust error handling with user-friendly messages.
- Push notifications for orders, promotions, and updates.
- Enhanced security for sensitive data.
- Performance optimization to reduce network calls.

---

## Video Demonstration
[![Watch the video](https://img.youtube.com/vi/VIDEO_ID/0.jpg)]((https://github.com/treasureola/Android_CathyCatheringApp/blob/main/cathy_catering_demo.mp4))
