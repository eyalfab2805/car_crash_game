🏎️ Obstacle Game – Android (Kotlin)
📌 Overview

Obstacle Game is an Android game developed in Kotlin, where the player controls a car driving on a road and must avoid obstacles while collecting coins.
The game includes two control modes, a scoring system, and a Top 10 leaderboard with location display.

📸 Screenshots
Main Menu
<img width="420" height="897" alt="Screenshot 2025-12-27 230727" src="https://github.com/user-attachments/assets/480ade25-5b6e-4f4a-9564-552ccecac29a" />
Gameplay
<img width="415" height="891" alt="Screenshot 2025-12-27 230751" src="https://github.com/user-attachments/assets/84953d13-e1cd-4caa-9857-e28a2ba66575" />
Top Ten & Map View
<img width="427" height="906" alt="Screenshot 2025-12-27 230835" src="https://github.com/user-attachments/assets/41de72dd-d53b-47b4-adbd-4aac77939cce" />
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
