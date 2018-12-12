/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 6:25 PM.
 */

package com.iamsdt.hs1.ui.add

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.esafirm.imagepicker.features.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.ToastType
import com.iamsdt.hs1.ext.showToast
import kotlinx.android.synthetic.main.activity_insert.*
import kotlinx.android.synthetic.main.content_insert.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

class InsertActivity : AppCompatActivity() {

    private val vm: InsertVm by viewModel()

    var imgLink = ""

    var subId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        setSupportActionBar(toolbar)

        subId = intent.getIntExtra(Intent.EXTRA_TEXT, 0)

        vm.status.observe(this, Observer {
            it?.let { m ->
                if (m.status == 1) {
                    showToast(ToastType.SUCCESSFUL, m.title)
                }
            }
        })

        insertData()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun insertData() {

        val img = pickImg

        img.setOnClickListener {
            ImagePicker.create(this)
                    .single()
                    .includeVideo(true)
                    .showCamera(true)
                    .start()

        }

        postBtn.setOnClickListener {
            val title = titleEt.editText?.text?.toString() ?: ""
            val type = typeEt.editText?.text?.toString() ?: ""
            val link = linkEt.editText?.text?.toString() ?: ""

            if (title.isEmpty() || title.length <= 3) {
                titleEt.error = "Tittle is not valid"
                return@setOnClickListener
            }

            if (type.isEmpty() || type.length <= 3) {
                typeEt.error = "Type is not valid"
                return@setOnClickListener
            }

            vm.add(title, type, link, imgLink, subId)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            val selectImage = ImagePicker.getFirstImageOrNull(data) ?: return

            val bit = BitmapFactory.decodeFile(selectImage.path)

            pickImg.setImageBitmap(bit)

            val baos = ByteArrayOutputStream()
            bit.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val byte = baos.toByteArray()

            //todo show dialog

            val ref = FirebaseStorage.getInstance().getReference("pic")
            ref.putBytes(byte).addOnCompleteListener {
                if (it.isSuccessful) {
                    val uri = it.result?.uploadSessionUri
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

}
