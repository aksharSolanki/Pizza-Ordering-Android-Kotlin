package org.stn01418888.midtermprojectbyakshar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import org.stn01418888.midtermprojectbyakshar.databinding.ActivityPizzaOrderBinding
import java.io.*

private const val TAG = "PizzaOrderActivity"
private const val KEY_RESULT = "result"
private var res = "Total Price"

class PizzaOrderActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityPizzaOrderBinding
    private var pizza_size: String = ""
    private var delivery_address: String = ""

    //Booleans
    private var is_meat: Boolean = false
    private var is_cheese: Boolean = false
    private var is_veggies: Boolean = false
    private var is_delivery: Boolean = false

    //Pizza object
    private var newPizza: Pizza = Pizza()

    //TextViews
    private lateinit var total: TextView
    private var totalPrice: Double = 0.0
    private lateinit var editAddress: EditText

    //Radiobuttons
    private lateinit var rbSmall: RadioButton
    private lateinit var rbGroup: RadioGroup

    //Checkboxes
    private lateinit var cbMeat: CheckBox
    private lateinit var cbCheese: CheckBox
    private lateinit var cbVeggies: CheckBox

    //SwitchButton
    private lateinit var switchDelivery: Switch

    //ImageViews
    private lateinit var imgViewToppings: ImageView

    //button
    private lateinit var btnOrder: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pizza_order)

        //TextViews
        total = findViewById(R.id.textViewPrice)
        editAddress = findViewById(R.id.editAddress)
        editAddress.isEnabled = false

        //RadioButtons
        rbGroup = findViewById(R.id.radioGroup)

        //CheckBoxes
        cbMeat = findViewById(R.id.cbMeat)
        cbCheese = findViewById(R.id.cbCheese)
        cbVeggies = findViewById(R.id.cbVeggies)

        //SwitchButton
        switchDelivery = findViewById(R.id.switchDelivery)

        //ImageView
        imgViewToppings = findViewById(R.id.imgViewToppings)
        imgViewToppings.setImageResource(R.drawable.toppings)

        //Button
        btnOrder = findViewById<Button>(R.id.btnOrder);

        rbGroup.setOnCheckedChangeListener { rbGroup, checkedID ->
            if (checkedID == R.id.rbSmall) {
                newPizza.pizza_size_price = 9.0
                pizza_size = "SMALL"
            } else if (checkedID == R.id.rbMedium) {
                newPizza.pizza_size_price = 10.0
                pizza_size = "MEDIUM"
            } else {
                newPizza.pizza_size_price = 11.0
                pizza_size = "LARGE"
            }
            total.text = "Total Price: $" + String.format("%.2f", calculate_total())
            res = "Total Price: $" + String.format("%.2f", calculate_total())
        }

        //Checked changed listener for Meat checkbox
        cbMeat.setOnCheckedChangeListener { compoundButton, b ->
            var checked: Boolean = cbMeat.isChecked
            if (checked) {
                newPizza.meat_price = 2.0
                is_meat = true
                imgViewToppings.setImageResource(R.drawable.meat)
            } else {
                newPizza.meat_price = 0.0
                is_meat = false
            }
            total.text = "Total Price: $" + String.format("%.2f", calculate_total())
            res = "Total Price: $" + String.format("%.2f", calculate_total())
        }

        //Checked changed listener for Cheese checkbox
        cbCheese.setOnCheckedChangeListener { compoundButton, b ->
            var checked: Boolean = cbCheese.isChecked
            if (checked) {
                imgViewToppings.setImageResource(R.drawable.cheese)
                newPizza.cheese_price = 2.0
                is_cheese = true
            } else {
                newPizza.cheese_price = 0.0
                is_cheese = false
            }
            total.text = "Total Price: $" + String.format("%.2f", calculate_total())
            res = "Total Price: $" + String.format("%.2f", calculate_total())
        }

        //Checked changed listener for Veggies checkbox
        cbVeggies.setOnCheckedChangeListener { compoundButton, b ->
            var checked: Boolean = cbVeggies.isChecked
            if (checked) {
                newPizza.veggies_price = 2.0
                is_veggies = true
                imgViewToppings.setImageResource(R.drawable.veggies)
            } else {
                newPizza.veggies_price = 0.0
                is_veggies = false
            }
            total.text = "Total Price: $" + String.format("%.2f", calculate_total())
            res = "Total Price: $" + String.format("%.2f", calculate_total())
        }

        //Checked changed listener for Switch button
        switchDelivery.setOnCheckedChangeListener { compoundButton, onSwitch ->
            if (onSwitch) {
                editAddress.isEnabled = true
                is_delivery = true
            } else {
                editAddress.isEnabled = false
                editAddress.text.clear()
                is_delivery = false
            }
        }

        btnOrder.setOnClickListener {
            newPizza.address = editAddress.text.toString()
            delivery_address = editAddress.text.toString()
            //Information about the order to be saved : SIZE | TOPPINGS | PRICE | DELIVERY REQUIRED | ADDRESS
            var toppings: String = ""
            var address: String = ""
            var delivery: String
            if (is_meat) {
                toppings += "MEAT "
            }
            if (is_cheese) {
                toppings += "CHEESE "
            }
            if (is_veggies) {
                toppings += "VEGGIES "
            }
            if (is_delivery) {
                address = delivery_address.uppercase()
                delivery = "REQUIRED"
            } else {
                address = "N/A"
                delivery = "NOT REQUIRED"
            }
            var str1: String = toppings.substring(0, toppings.lastIndexOf(" "))
            var final_toppings: String = str1.replace(" ", ", ")
            if (final_toppings.isEmpty()) {
                final_toppings = "NO TOPPINGS"
            }

            //SAVE ORDER TRANSACTION TO FILE
            //FILENAME
            val filename = "pizza_order_transactions.txt"

            //DATA
            val filedata =
                    "\nSIZE : " + pizza_size +
                    "\nTOPPINGS : " + final_toppings +
                    "\nPRICE : $" + totalPrice +
                    "\nDELIVERY : " + delivery +
                    "\nADDRESS : " + address + "\n\n"

            //FILEOUTPUTSTREAM
            val fileoutputStream: FileOutputStream

            try {
                fileoutputStream = openFileOutput(filename, Context.MODE_APPEND)
                fileoutputStream.write(filedata.toByteArray())
            } catch (ex: FileNotFoundException) {
                ex.printStackTrace()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            clear()
            Toast.makeText(this, "Order confirmed", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, OrderHistoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun calculate_total(): Double {
        totalPrice = (newPizza.pizza_size_price + newPizza.meat_price + newPizza.cheese_price
                + newPizza.veggies_price)
        return totalPrice
    }

    fun clear() {
        rbGroup.clearCheck()
        cbMeat.isChecked = false
        cbCheese.isChecked = false
        cbVeggies.isChecked = false
        switchDelivery.isChecked = false
        editAddress.text.clear()
    }

    fun onLogoutClick(view: View) {
        clear()
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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