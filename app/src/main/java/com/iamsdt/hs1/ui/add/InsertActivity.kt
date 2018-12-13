/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 6:25 PM.
 */

package com.iamsdt.hs1.ui.add

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.esafirm.imagepicker.features.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.*
import com.iamsdt.hs1.ui.SigninActivity
import com.iamsdt.hs1.utils.PostType
import kotlinx.android.synthetic.main.activity_insert.*
import kotlinx.android.synthetic.main.content_insert.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream

class InsertActivity : AppCompatActivity() {

    private val vm: InsertVm by viewModel()

    private var imgLink = ""

    private var subId = 0

    private var type: PostType = PostType.LINK

    private var isPictureShowing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)
        setSupportActionBar(toolbar)

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null)
            toNextActivity(SigninActivity::class)

        subId = intent.getIntExtra(Intent.EXTRA_TEXT, 1)

        vm.status.observe(this, Observer {
            it?.let { m ->
                if (m.status == 1) {
                    showToast(ToastType.SUCCESSFUL, m.title)

                    //clear every thing
                    //complete add empty image
                    val img = getDrawable(R.drawable.ic_add_img2)
                    img_picker.setImageDrawable(img)

                    titleEt.editText?.text?.clear()
                    desEt.editText?.text?.clear()
                    linkEt.editText?.text?.clear()
                    progress_bar.gone()
                }
            }
        })

        insertData()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    private fun insertData() {

        val img = img_picker

        img?.setOnClickListener {
            ImagePicker.create(this)
                    .single()
                    .includeVideo(false)
                    .showCamera(true)
                    .start()

        }

        addBtn.setOnClickListener {
            if (isPictureShowing) {
                addImgBtn.gone()
            } else addBtn.show()
        }

        addImgBtn?.setOnClickListener {
            img_picker.show()
            img_picker_tv.show()
        }

        postBtn.setOnClickListener {
            val title = titleEt.editText?.text?.toString() ?: ""
            val des = desEt.editText?.text?.toString() ?: ""
            val link = linkEt.editText?.text?.toString() ?: ""

            if (title.isEmpty() || title.length <= 3) {
                titleEt.error = "Tittle is not valid"
                return@setOnClickListener
            }

            vm.add(title, des, type, link, imgLink, subId)

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {

            val selectImage = ImagePicker.getFirstImageOrNull(data) ?: return

            val bit = BitmapFactory.decodeFile(selectImage.path)

            img_picker?.setImageBitmap(bit)

            val baos = ByteArrayOutputStream()
            bit.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val byte = baos.toByteArray()

            //complete show dialog

            progress_bar.show()

            val ref = FirebaseStorage.getInstance().getReference("pic")
            ref.putBytes(byte).addOnCompleteListener {
                if (it.isSuccessful) {
                    imgLink = it.result?.uploadSessionUri?.toString() ?: ""
                }
            }.addOnProgressListener {
                val progress = 100.0 * it.bytesTransferred / it.totalByteCount

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progress_bar.setProgress(progress.toInt(), true)
                } else {
                    progress_bar.progress = progress.toInt()
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
