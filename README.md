🏎️ Obstacle Game – Android (Kotlin)
📌 Overview

Obstacle Game is an Android game developed in Kotlin, where the player controls a car driving on a road and must avoid obstacles while collecting coins.
The game includes two control modes, a scoring system, and a Top 10 leaderboard with location display.

📸 Screenshots
Main Menu
<img src="d6d30a5d-6c1d-4eae-a632-af7fdd6a62fa.png" width="300"/>
Gameplay
<img src="79ac77c1-681e-4d3a-9ed6-9f582dd96195.png" width="300"/>
Top Ten & Map View
<img src="4dc9a8a3-7333-43e4-bb8a-edd93bae83d6.png" width="300"/>
🎮 Gameplay

The player controls a car located at the bottom of the road.

Obstacles fall from the top of the screen.

Coins can be collected.

The game ends when the player loses all lives.

🕹️ Control Modes

Button Mode – Move the car using on-screen left/right buttons.

Sensor Mode – Tilt the device left or right to move the car.

The control mode is selected from the main menu.

⚙️ Game Features

8×5 road grid with lane separators

Lives system with heart indicators

Coin collection

Distance counter

Game speed toggle (normal / fast)

Sound effects and vibration on crash

Top 10 leaderboard

Google Maps integration for score locations

🏆 Scoring System

Score = Distance

Coins are displayed separately

Top 10 scores are sorted by:

Distance

Time (tie-breaker)

🗺️ Location Tracking

Player location is saved when the game ends

Uses FusedLocationProviderClient

Emulator may show default USA location

Real devices save correct GPS location automatically

💾 Data Persistence

Scores are stored locally using SharedPreferences

Stored as JSON

Data persists between app launches

Data is cleared when the app is uninstalled or storage is cleared

🧩 Project Structure
com.example.obstacle_game
│
├── MainActivity
├── MenuActivity
├── HighScoresActivity
│
├── data
│   ├── ScoreEntry
│   └── ScoreRepository
│
├── ui
│   ├── TopTenListFragment
│   ├── TopTenAdapter
│   └── LocationMapFragment
│
└── utilities
    ├── GameManager
    ├── TiltDetector
    ├── SoundPlayer
    └── SignalManager

🛠️ Technologies Used

Kotlin

Android SDK

Timer & TimerTask

SharedPreferences

Google Maps API

Sensors (Accelerometer)

RecyclerView

Fragments

▶️ How to Run

Open the project in Android Studio

Sync Gradle

Run on:

Emulator (default mock location)

Physical device (recommended)
