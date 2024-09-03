package dev.alexisvillarruel.mediaplayer

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var btnAdelante: Button
    private lateinit var btnPausar: Button
    private lateinit var btnReproducir: Button
    private lateinit var btnAtras: Button
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var seekBar: SeekBar
    private lateinit var tvTiempoRestante: TextView
    private lateinit var tvDuracion : TextView
    private lateinit var tvNombreMusica : TextView
    private var horaInicio = 0.0
    private var horaFinal = 0.0
    private val myHandler = Handler()
    private val TiempoAdelanto = 5000
    private val TiempoAtraso = 5000
    private var oneTimeOnly = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnAdelante = findViewById(R.id.btnAdelante)
        btnPausar = findViewById(R.id.btnPausar)
        btnReproducir = findViewById(R.id.btnReproducir)
        btnAtras = findViewById(R.id.btnAtras)

        tvTiempoRestante = findViewById(R.id.tvTiempoRestante)
        tvDuracion = findViewById(R.id.tvDuracion)
        tvNombreMusica = findViewById(R.id.tvNombreMusica)
        tvNombreMusica.text = "song.mp3"

        mediaPlayer = MediaPlayer.create(this, R.raw.song)
        seekBar = findViewById(R.id.seekBar)
        seekBar.isClickable = false
        btnPausar.isEnabled = false

        btnReproducir.setOnClickListener{
            Toast.makeText(applicationContext, "Reproduciendo sonido", Toast.LENGTH_SHORT).show()
            mediaPlayer.start()
            horaFinal = mediaPlayer.duration.toDouble()
            horaInicio= mediaPlayer.currentPosition.toDouble()

            if (oneTimeOnly == 0){
                seekBar.max = horaFinal.toInt()
                oneTimeOnly = 1
            }

            tvDuracion.text = String.format(
                "%d min, %d seg",
                TimeUnit.MILLISECONDS.toMinutes(horaFinal.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(horaFinal.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(horaFinal.toLong()))
            )

            tvTiempoRestante.text = String.format(
                "%d min, %d seg",
                TimeUnit.MILLISECONDS.toMinutes(horaInicio.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(horaInicio.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(horaInicio.toLong()))
            )

            seekBar.progress = horaInicio.toInt()
            myHandler.postDelayed(ActualizaTiempoSonido, 100)
            btnPausar.isEnabled = true
            btnReproducir.isEnabled = false
        }

            btnPausar.setOnClickListener{
                Toast.makeText(applicationContext, "Pausando Sonido", Toast.LENGTH_SHORT).show()
                mediaPlayer.pause()
                btnPausar.isEnabled =false
                btnReproducir.isEnabled = true

            }
            btnAdelante.setOnClickListener {
                val  temp = horaInicio.toInt()

                if (temp + TiempoAdelanto <= horaFinal){
                    horaInicio += TiempoAdelanto
                    mediaPlayer.seekTo(horaInicio.toInt())
                    Toast.makeText(applicationContext, "HAs saltado 5 seg hacia adelante", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, "no se puede saltar 5 seg hacia adelante", Toast.LENGTH_SHORT).show()
                }
            }
            btnAtras.setOnClickListener {
                val temp = horaInicio.toInt()

                if (temp - TiempoAtraso > 0){
                    horaInicio -= TiempoAtraso
                    mediaPlayer.seekTo(horaInicio.toInt())
                    Toast.makeText(applicationContext,"Has saltado 5 seg atras", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(applicationContext, "no se puede saltar hacai atras 5 seg", Toast.LENGTH_SHORT).show()
                }
            }

        }


    private val ActualizaTiempoSonido: Runnable = object : Runnable {
        override fun run() {
            horaInicio = mediaPlayer.currentPosition.toDouble()
            tvTiempoRestante.text = String.format(
                "%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(horaInicio.toLong()),
                TimeUnit.MILLISECONDS.toSeconds(horaInicio.toLong()) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(horaInicio.toLong()))
            )
            seekBar.progress = horaInicio.toInt()
            myHandler.postDelayed(this, 100)
        }
    }
}

