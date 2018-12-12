/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 12:57 PM.
 */

package com.iamsdt.hs1.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.ToastType
import com.iamsdt.hs1.ext.isNetWorkAvailable
import com.iamsdt.hs1.ext.showToast
import com.iamsdt.hs1.ext.toNextActivity
import com.iamsdt.hs1.ui.main.CatActivity
import kotlinx.android.synthetic.main.content_signin.*

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val auth = FirebaseAuth.getInstance()

        sign_button.setOnClickListener {
            val email = email_lay?.editText?.text?.toString() ?: ""
            val pass = email_lay?.editText?.text?.toString() ?: ""


            if (email.isNotEmpty() && pass.isNotEmpty()) {
                //login
                if (isNetWorkAvailable()) {
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener { t ->
                                if (t.isSuccessful) {
                                    //next activity
                                    toNextActivity(CatActivity::class)
                                } else {
                                    showToast(ToastType.ERROR, "Wrong Email or Password")
                                }
                            }

                } else {
                    showToast(ToastType.ERROR, "Internet is not connected")
                }

            } else if (email.isEmpty()) {
                email_lay.error = "Email is empty"
            } else if (pass.isEmpty()) {
                email_lay.error = "Password is empty"
            }
        }


    }
}
