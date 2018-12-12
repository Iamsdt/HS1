package com.iamsdt.hs1.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.iamsdt.hs1.R

import kotlin.reflect.KClass
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.net.ConnectivityManager


fun AppCompatActivity.runThread(timer: Long, clazz: KClass<out AppCompatActivity>) =
        Thread {
            try {
                Thread.sleep(timer)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                toNextActivity(clazz)
                finish()
            }
        }.start()

fun AppCompatActivity.toNextActivity(
        clazz: KClass<out AppCompatActivity>,
        extraKey: String = "",
        extra: String = "",
        finish: Boolean = false) {
    val intent = Intent(this, clazz.java)

    if (extraKey.isNotEmpty()) {
        intent.putExtra(extraKey, extra)
    }

    startActivity(intent)

    if (finish) {
        finish()
    }
}

fun Fragment.toNextActivity(
        clazz: KClass<out AppCompatActivity>,
        extraKey: String = "",
        extra: String = "") {

    val intent = Intent(context, clazz.java)

    if (extraKey.isNotEmpty()) {
        intent.putExtra(extraKey, extra)
    }

    startActivity(intent)
}


fun AppCompatActivity.customTab(link: String) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(R.attr.colorPrimary)
    builder.setShowTitle(true)
    builder.addDefaultShareMenuItem()
    //builder.setCloseButtonIcon(BitmapFactory.decodeResource(
    //resources, R.drawable.dialog_back))
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(link))
}

fun AppCompatActivity.sendEmail(
        email: String,
        subject: String) {

    val intent = Intent(Intent.ACTION_SENDTO)
    intent.type = "text/plain"
    intent.data = Uri.parse("mailto:$email")
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    startActivity(Intent.createChooser(intent, "Send Email"))
}

fun AppCompatActivity.isNetWorkAvailable(): Boolean {
    val manager = getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val info: NetworkInfo? = manager.activeNetworkInfo


    return info != null && info.isConnected
}


