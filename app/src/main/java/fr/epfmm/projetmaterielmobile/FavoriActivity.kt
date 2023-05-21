package fr.epfmm.projetmaterielmobile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FavoriActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favori)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}