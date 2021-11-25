package org.stn01418888.midtermprojectbyakshar

import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import org.stn01418888.midtermprojectbyakshar.databinding.ActivityOrderHistoryBinding
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader
private const val TAG = "OrderHistoryActivity"
class OrderHistoryActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    //private lateinit var binding: ActivityOrderHistoryBinding
    private lateinit var textHistory: TextView
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_history)

        textHistory = findViewById(R.id.textHistory)
        textHistory.movementMethod = ScrollingMovementMethod()
        btnBack = findViewById(R.id.btnBack)

        val filename = "pizza_order_transactions.txt"
        if(filename.isNotEmpty() && filename.trim().isNotEmpty()) {
            var fileInputStream: FileInputStream? = null
            fileInputStream = openFileInput(filename)

            var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)

            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null

            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text+"\n")
            }
            textHistory.setText(stringBuilder.toString()).toString()
        } else {
            Toast.makeText(this, "There are no transactions for this account", Toast.LENGTH_SHORT)
            }

        btnBack.setOnClickListener{
            val intent = Intent(this, PizzaOrderActivity::class.java)
            startActivity(intent)
        }
    }
    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause() was called")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop() was called")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume() was called")
    }

    // override onSaveInstanceState(Bundle) method to save additional data
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState() was called")
    }
}