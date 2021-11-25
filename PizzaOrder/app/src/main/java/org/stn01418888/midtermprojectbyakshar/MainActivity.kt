package org.stn01418888.midtermprojectbyakshar

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
private const val TAG = "MainActivity" //classname

class MainActivity : AppCompatActivity() {

    private lateinit var editId: EditText
    private lateinit var editPhone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        editId = findViewById(R.id.editId)
        editPhone = findViewById(R.id.editPhone)

        val pref = getPreferences(Context.MODE_PRIVATE)
        val id = pref.getString("ID", "")
        val phone = pref.getLong("PHONE", 0)

        editId.setText(id)
        editPhone.setText(phone.toString())
    }

    //Using the onLoginClick button a SharedPreferences file will be created
    //The ID and Phone of customer will be stored as key/value pairs
    fun onLoginClick(view: View) {
        val pref = getPreferences(Context.MODE_PRIVATE)
        val editor = pref.edit()

        //save id
        editor.putString("ID", editId.text.toString())
        //save phone
        editor.putLong("PHONE", editPhone.text.toString().toLong())
        //commit changes
        editor.commit()
        //Confirmation toast
        Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, PizzaOrderActivity::class.java)
        startActivity(intent)
    }

    //fun onClearClick will clear a user from the database
    fun onClearClick(view: View) {
        val pref = getPreferences(Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.commit()
        editId.setText("")
        editPhone.setText("")
        Toast.makeText(this, "User Cleared from Database", Toast.LENGTH_SHORT).show()
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