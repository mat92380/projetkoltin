package fr.epfmm.projetmaterielmobile

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val StartButton = findViewById<Button>(R.id.start_button)

        StartButton.setOnClickListener{
            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            startActivity(intent)
        }

    }
}