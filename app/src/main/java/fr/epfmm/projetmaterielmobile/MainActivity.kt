package fr.epfmm.projetmaterielmobile

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

class MainActivity : AppCompatActivity() {
    private fun startQRScan() {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Scan a QR code")
        integrator.setOrientationLocked(false)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setBeepEnabled(true)
        integrator.initiateScan()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val SearchButton = findViewById<Button>(R.id.Search_Button)
        val Fav_Button = findViewById<Button>(R.id.Fav_Button)
        val Qr_Button = findViewById<Button>(R.id.Qr_Button)


        SearchButton.setOnClickListener{
            val intent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(intent)
        }
        Fav_Button.setOnClickListener{
            val intent = Intent(this@MainActivity, FavoriActivity::class.java)
            startActivity(intent)
        }
        Qr_Button.setOnClickListener{
            startQRScan()


        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Log.d(TAG, "Cancelled scan")
            } else {
                val scanResult = result.contents
                val tvScanResult = findViewById<TextView>(R.id.tv_scan_result)
                tvScanResult.text = scanResult

                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(tvScanResult.text.toString())
                startActivity(intent)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}