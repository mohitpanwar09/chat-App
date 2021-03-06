package com.example.letstalk.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.letstalk.MainActivity
import com.example.letstalk.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //login button
        Bt_login.setOnClickListener {
            progress_login.visibility= View.VISIBLE
            Bt_login.isEnabled=false
            back_register_editText.isEnabled=false
            performLogin()
        }
        //back to registration page
        back_register_editText.setOnClickListener{

            startActivity(Intent(this, Registration::class.java))
            finish()
        }
    }



    private fun performLogin(){
        val emailLogin=Ed_userEmail_login.text.toString()
        val passwordLogin=Ed_userPassword_login.text.toString()

        if(emailLogin.isEmpty() || passwordLogin.isEmpty()){
            Toast.makeText(this, "Email or Password can't be empty", Toast.LENGTH_SHORT).show()
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(emailLogin,passwordLogin)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    progress_login.visibility=View.GONE
                    Bt_login.isEnabled=true
                    back_register_editText.isEnabled=true
                    val user = FirebaseAuth.getInstance().currentUser
                    val intent=Intent(this, MainActivity::class.java)
                    intent.putExtra("user",user)
                    intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                else
                {
                    progress_login.visibility=View.GONE
                    Bt_login.isEnabled=true
                    back_register_editText.isEnabled=true
                    Toast.makeText(this, "Fail to login", Toast.LENGTH_LONG).show()
                    return@addOnCompleteListener
                }

            }
    }


}