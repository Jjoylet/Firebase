package com.joylet.firebaseapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val inputName : EditText = findViewById(R.id.inputName)
        val inputEmail : EditText = findViewById(R.id.inputEmail)
        val inputAge : EditText = findViewById(R.id.inputAge)
        val buttonSave : Button = findViewById(R.id.buttonSave)
        val buttonFetch : Button = findViewById(R.id.buttonFetch)
        val txtRealTime : TextView = findViewById(R.id.txtRealTime)

       val database = Firebase.database

       val refUsers = database.getReference("users")

       // refUsers.setValue("Felistus")

        buttonSave.setOnClickListener{
            val name = inputName.text.toString().trim()
            val age = inputAge.text.toString().trim().toIntOrNull()
            val email = inputEmail.text.toString().trim()

            if (name.isNotEmpty() && age != null && email.isNotEmpty()){
                //save
                val user = User(name, age, email)
                refUsers.push().setValue(user )
                inputAge.text.clear()
                inputAge.text.clear()
                inputName.text.clear()

                val bottomDialog = BottomSheetDialog(this)
                bottomDialog.setContentView(R.layout.bottom_sheet)
                bottomDialog.show()

            }

        }

        //Onee time fetch
        buttonFetch.setOnClickListener{
            refUsers.get().addOnSuccessListener {
                val children = it.children
                for (child in children){
                    Log.d("USER_CHILD", "onCreate: $child" )
                }
            }
        }
           refUsers.get().addOnSuccessListener {
               val intent = Intent(this,UserActivity::class.java)
               startActivity(intent)
           }



        val realtime = database.getReference("realtime")
        realtime.setValue("Joylette Shem")

        realtime.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                txtRealTime.text = snapshot.getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }
}

data class User(val name:String ="", val age:Int = 0 ,val email:String = "")