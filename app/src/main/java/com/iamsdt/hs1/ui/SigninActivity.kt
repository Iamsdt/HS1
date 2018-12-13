/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 12:57 PM.
 */

package com.iamsdt.hs1.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.auth.FirebaseAuth
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.*
import com.iamsdt.hs1.ui.main.MainActivity
import com.iamsdt.hs1.ui.work.MainTable
import com.iamsdt.hs1.ui.work.SyncCat
import com.iamsdt.hs1.ui.work.SyncSub
import kotlinx.android.synthetic.main.content_signin.*

class SigninActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        if (user != null) {
            showToast(ToastType.SUCCESSFUL, "SignIn automatically")
            toNextActivity(MainActivity::class)
            fakeTest()

        } else {
            signIN(auth)
        }

    }

    private fun fakeTest() {
        val main = OneTimeWorkRequest.Builder(
                MainTable::class.java).build()

        WorkManager.getInstance().enqueue(main)
    }

    private fun signIN(auth: FirebaseAuth) {
        sign_button.setOnClickListener {
            val email = email_lay?.editText?.text?.toString() ?: ""
            val pass = pass_lay?.editText?.text?.toString() ?: ""

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                //login
                if (isNetWorkAvailable()) {
                    signinLay.show()
                    loginProgress.show()
                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener { t ->
                                if (t.isSuccessful) {
                                    //next activity
                                    showToast(ToastType.SUCCESSFUL, "Signin Successfully")
                                    loginProgress.gone()
                                    signinLay.gone()
                                    toNextActivity(MainActivity::class)

                                    // complete: 12/13/18 sync with server
                                    startWorker()
                                } else {
                                    loginProgress.gone()
                                    signinLay.gone()
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

    private fun startWorker() {
        val cat = OneTimeWorkRequest.Builder(
                SyncCat::class.java).build()

        val sub = OneTimeWorkRequest.Builder(
                SyncSub::class.java).build()


        val main = OneTimeWorkRequest.Builder(
                MainTable::class.java).build()

        WorkManager.getInstance().enqueue(listOf(cat, sub, main))
    }
}
