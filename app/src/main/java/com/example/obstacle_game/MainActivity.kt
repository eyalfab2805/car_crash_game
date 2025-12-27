package com.example.obstacle_game

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.obstacle_game.data.ScoreEntry
import com.example.obstacle_game.data.ScoreRepository
import com.example.obstacle_game.utilities.GameManager
import com.example.obstacle_game.utilities.SoundPlayer
import com.example.obstacle_game.utilities.TiltDetector
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {

    private lateinit var cells: Array<Array<ImageView>>
    private lateinit var heartViews: List<ImageView>
    private lateinit var coinsText: TextView
    private lateinit var odometerText: TextView

    private var gameManager: GameManager? = null
    private var timer: Timer? = null

    private var tiltDetector: TiltDetector? = null
    private var soundPlayer: SoundPlayer? = null

    private var ticks = 0
    private var currentCoins = 0

    private var controlMode: String = "BUTTONS"
    private var isFast: Boolean = false

    private var loopStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        controlMode = intent.getStringExtra("CONTROL_MODE") ?: "BUTTONS"
        isFast = intent.getBooleanExtra("IS_FAST", false)

        if (!bindViewsWithMessage()) {
            return
        }

        requestLocationIfNeeded()

        soundPlayer = SoundPlayer(this)

        gameManager = GameManager(
            context = this,
            cells = cells,
            heartViews = heartViews,
            onCrash = { soundPlayer?.playCrash() },
            onCoinsChanged = { coins ->
                currentCoins = coins
                coinsText.text = "Coins: $coins"
            }
        )

        tiltDetector = TiltDetector(
            context = this,
            onTiltLeft = { gameManager?.movePlayer("LEFT") },
            onTiltRight = { gameManager?.movePlayer("RIGHT") }
        )

        setupButtonsSafe()

        val controls = findViewById<View?>(R.id.controlsLayout)
        controls?.visibility = if (controlMode == "SENSORS") View.GONE else View.VISIBLE

        window.decorView.post { forceRedraw() }
    }

    override fun onResume() {
        super.onResume()

        if (controlMode == "SENSORS") tiltDetector?.start()

        window.decorView.post {
            if (!loopStarted) {
                loopStarted = true
                startGameLoop()
            }
            forceRedraw()
        }
    }

    override fun onPause() {
        super.onPause()
        tiltDetector?.stop()
        stopGameLoop()
        loopStarted = false
    }

    override fun onDestroy() {
        super.onDestroy()
        stopGameLoop()
        soundPlayer?.release()
    }

    private fun startGameLoop() {
        stopGameLoop()

        val interval = if (isFast) 250L else 450L

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    val gm = gameManager ?: return@runOnUiThread

                    val running = gm.tick()
                    if (running) {
                        ticks++
                        odometerText.text = "Distance: $ticks"
                        forceRedraw()
                    } else {
                        stopGameLoop()
                        askNameAndSave()
                    }
                }
            }
        }, interval, interval)
    }

    private fun stopGameLoop() {
        timer?.cancel()
        timer = null
    }

    private fun forceRedraw() {
        val root = window.decorView
        root.post {
            root.invalidate()
            root.requestLayout()
        }
    }

    private fun askNameAndSave() {
        val input = EditText(this)
        input.hint = "Your name"

        AlertDialog.Builder(this)
            .setTitle("Game Over")
            .setMessage("Enter your name")
            .setView(input)
            .setCancelable(false)
            .setPositiveButton("Save") { _, _ ->
                val name = input.text.toString().trim().ifEmpty { "Player" }
                saveScore(name)
                finish()
            }
            .show()
    }

    @SuppressLint("SetTextI18n")
    private fun bindViewsWithMessage(): Boolean {
        val coins = findViewById<TextView?>(R.id.coinsText)
        val odo = findViewById<TextView?>(R.id.odometerText)
        val controls = findViewById<View?>(R.id.controlsLayout)

        if (coins == null || odo == null || controls == null) {
            Toast.makeText(
                this,
                "Missing view(s): coins=${coins != null}, odo=${odo != null}, controls=${controls != null}",
                Toast.LENGTH_LONG
            ).show()
            return false
        }

        coinsText = coins
        odometerText = odo

        coinsText.text = "Coins: 0"
        odometerText.text = "Distance: 0"

        val h1 = findViewById<ImageView?>(R.id.heart1)
        val h2 = findViewById<ImageView?>(R.id.heart2)
        val h3 = findViewById<ImageView?>(R.id.heart3)
        if (h1 == null || h2 == null || h3 == null) {
            Toast.makeText(this, "Missing heart view(s)", Toast.LENGTH_LONG).show()
            return false
        }
        heartViews = listOf(h1, h2, h3)

        fun cell(id: Int): ImageView? = findViewById(id)

        val ids = listOf(
            listOf(R.id.cell_0_0, R.id.cell_0_1, R.id.cell_0_2, R.id.cell_0_3, R.id.cell_0_4),
            listOf(R.id.cell_1_0, R.id.cell_1_1, R.id.cell_1_2, R.id.cell_1_3, R.id.cell_1_4),
            listOf(R.id.cell_2_0, R.id.cell_2_1, R.id.cell_2_2, R.id.cell_2_3, R.id.cell_2_4),
            listOf(R.id.cell_3_0, R.id.cell_3_1, R.id.cell_3_2, R.id.cell_3_3, R.id.cell_3_4),
            listOf(R.id.cell_4_0, R.id.cell_4_1, R.id.cell_4_2, R.id.cell_4_3, R.id.cell_4_4),
            listOf(R.id.cell_5_0, R.id.cell_5_1, R.id.cell_5_2, R.id.cell_5_3, R.id.cell_5_4),
            listOf(R.id.cell_6_0, R.id.cell_6_1, R.id.cell_6_2, R.id.cell_6_3, R.id.cell_6_4),
            listOf(R.id.cell_7_0, R.id.cell_7_1, R.id.cell_7_2, R.id.cell_7_3, R.id.cell_7_4)
        )

        val tmp = Array(ids.size) { r ->
            Array(ids[r].size) { c ->
                cell(ids[r][c]) ?: run {
                    Toast.makeText(
                        this,
                        "Missing cell view: ${resources.getResourceEntryName(ids[r][c])}",
                        Toast.LENGTH_LONG
                    ).show()
                    return false
                }
            }
        }

        cells = tmp
        return true
    }

    private fun setupButtonsSafe() {
        val left = findViewById<ImageView?>(R.id.btnLeft)
        val right = findViewById<ImageView?>(R.id.btnRight)

        left?.setOnClickListener {
            gameManager?.movePlayer("LEFT")
            forceRedraw()
        }

        right?.setOnClickListener {
            gameManager?.movePlayer("RIGHT")
            forceRedraw()
        }
    }

    private fun requestLocationIfNeeded() {
        val fine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (fine != PackageManager.PERMISSION_GRANTED && coarse != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                101
            )
        }
    }

    private fun saveScore(name: String) {
        val score = ticks
        val fused = LocationServices.getFusedLocationProviderClient(this)

        val fine = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fine != PackageManager.PERMISSION_GRANTED && coarse != PackageManager.PERMISSION_GRANTED) {
            ScoreRepository.addScore(
                this,
                ScoreEntry(
                    name = name,
                    score = score,
                    coins = currentCoins,
                    distance = ticks,
                    timeMillis = System.currentTimeMillis(),
                    lat = 0.0,
                    lng = 0.0
                )
            )
            return
        }

        val cts = CancellationTokenSource()
        fused.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, cts.token)
            .addOnSuccessListener { loc ->
                val lat = loc?.latitude ?: 0.0
                val lng = loc?.longitude ?: 0.0
                ScoreRepository.addScore(
                    this,
                    ScoreEntry(
                        name = name,
                        score = score,
                        coins = currentCoins,
                        distance = ticks,
                        timeMillis = System.currentTimeMillis(),
                        lat = lat,
                        lng = lng
                    )
                )
            }
            .addOnFailureListener {
                ScoreRepository.addScore(
                    this,
                    ScoreEntry(
                        name = name,
                        score = score,
                        coins = currentCoins,
                        distance = ticks,
                        timeMillis = System.currentTimeMillis(),
                        lat = 0.0,
                        lng = 0.0
                    )
                )
            }
    }
}
