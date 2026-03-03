# 📈 Real-Time Price Tracker

A real-time stock price tracking Android app built with **Jetpack Compose** and **WebSockets**.

---

## ✨ Features

- 📡 Live price updates via WebSocket
- 📊 25 stock symbols tracked simultaneously
- 🟢🔴 Connection status indicator
- ↑↓ Price direction indicators (green = up, red = down)
- ⚡ Price flash animation on change
- 🔃 Auto-sorted list by highest price
- 📄 Symbol details screen with description
- 🌙 Light & Dark theme support

---

## 🏗️ Architecture

**MVVM (Model-View-ViewModel)**

```
app/
├── data/
│   ├── model/          # StockItem data class
│   └── websocket/      # WebSocket connection handler
├── ui/
│   ├── feed/           # Feed screen + ViewModel
│   └── details/        # Details screen + ViewModel
├── navigation/         # NavHost & routes
└── MainActivity.kt
```

---

## 🚀 Getting Started

1. Clone the repository
2. Open in Android Studio
3. Click **Run ▶️**
4. Tap **Start** to begin live price tracking

---

## 📱 Screens

**Feed Screen** — scrollable list of 25 stocks with live prices and Start/Stop button

**Details Screen** — selected stock with live price updates and company description

---

## 👨‍💻 Built With

- 100% Kotlin
- 100% Jetpack Compose
- MVVM Architecture
