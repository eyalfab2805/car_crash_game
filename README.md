# 🏎️ Obstacle Game - Eyal Fabian

<br/>
<br/>
<br/>

## 📌 Overview
<br/>
<br/>

Obstacle Game is an Android game developed in Kotlin, where the player controls a car driving on a road and must avoid obstacles while collecting coins.
The game includes two control modes, a scoring system, and a Top 10 leaderboard with location display.

<br/>
<br/>
<br/>

## 📸 Screenshots

<table width="100%">
  <tr>
    <th align="left">Main Menu</th>
    <th></th>
    <th align="center">Gameplay</th>
    <th></th>
    <th align="right">Top Ten & Map View</th>
  </tr>
  <tr>
    <td align="left">
      <img width="150" height="897" alt="Screenshot 2025-12-27 230727" src="https://github.com/user-attachments/assets/480ade25-5b6e-4f4a-9564-552ccecac29a" />
    </td>
    <td width="10%"></td>
    <td align="center">
      <img width="150" height="891" alt="Screenshot 2025-12-27 230751" src="https://github.com/user-attachments/assets/84953d13-e1cd-4caa-9857-e28a2ba66575" />
    </td>
    <td width="10%"></td>
    <td align="right">
      <img width="150" height="906" alt="Screenshot 2025-12-27 230835" src="https://github.com/user-attachments/assets/41de72dd-d53b-47b4-adbd-4aac77939cce" />
    </td>
  </tr>
</table>

<br/>
<br/>
<br/>



## 🎮 Gameplay

The player controls a car located at the bottom of the road.

Obstacles fall from the top of the screen.

Coins can be collected.

The game ends when the player loses all lives.

<br/>
<br/>
<br/>


## 🕹️ Control Modes

Button Mode – Move the car using on-screen left/right buttons.

Sensor Mode – Tilt the device left or right to move the car.

The control mode is selected from the main menu.


<br/>
<br/>
<br/>

## ⚙️ Game Features

8×5 road grid with lane separators

Lives system with heart indicators

Coin collection

Distance counter

Game speed toggle (normal / fast)

Sound effects and vibration on crash

Top 10 leaderboard

Google Maps integration for score locations


<br/>
<br/>
<br/>

## 🏆 Scoring System

Score = Distance

Coins are displayed separately

Top 10 scores are sorted by:

Distance

Time (tie-breaker)

<br/>
<br/>
<br/>


## 🗺️ Location Tracking

Player location is saved when the game ends

Uses FusedLocationProviderClient

Emulator may show default USA location

Real devices save correct GPS location automatically


<br/>
<br/>
<br/>

## 💾 Data Persistence

Scores are stored locally using SharedPreferences

Stored as JSON

Data persists between app launches

Data is cleared when the app is uninstalled or storage is cleared

<br/>
<br/>
<br/>


## 🧩 Project Structure
com.example.obstacle_game
<br/>
│
<br/>
├── MainActivity
<br/>
├── MenuActivity
<br/>
├── HighScoresActivity
<br/>
│
<br/>
├── data
<br/>
│   ├── ScoreEntry
<br/>
│   └── ScoreRepository
<br/>
│
<br/>
├── ui
<br/>
│   ├── TopTenListFragment
<br/>
│   ├── TopTenAdapter
<br/>
│   └── LocationMapFragment
<br/>
│
<br/>
└── utilities
<br/>
    ├── GameManager
    <br/>
    ├── TiltDetector
    <br/>
    ├── SoundPlayer
    <br/>
    └── SignalManager

<br/>
<br/>
<br/>


## 🛠️ Technologies Used

Kotlin

Android SDK

Timer & TimerTask

SharedPreferences

Google Maps API

Sensors (Accelerometer)

RecyclerView

Fragments


<br/>
<br/>
<br/>

## ▶️ How to Run

Open the project in Android Studio

Sync Gradle

Run on:

Emulator (default mock location)

Physical device (recommended)
