/*
 * Copyright (c) Shudipto Trafder
 * Created on 12/7/18 6:25 PM.
 */

package com.iamsdt.hs1.ui.add

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.esafirm.imagepicker.features.ImagePicker
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.iamsdt.hs1.R
import com.iamsdt.hs1.ext.*
import com.iamsdt.hs1.ui.SigninActivity
import com.iamsdt.hs1.utils.PostType
import kotlinx.android.synthetic.main.activity_insert.*
import kotlinx.android.synthetic.main.content_insert.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.io.ByteArrayOutputStream

class InsertActivity : AppCompatActivity() {

    private val vm: InsertVm by viewModel()

    private var imgLink = ""

    private var subId = 0

    private var isPictureShowing = false

    private var selected: PostType = PostType.LINK

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


        val list = listOf(
                PostType.LINK, PostType.IMAGE, PostType.VIDEO
        )

        val spAdapter = ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                list)

        spinner2.adapter = spAdapter
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selected = list[position]
            }

        }

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
            isPictureShowing = if (isPictureShowing) {
                addImgBtn.gone()
                false
            } else {
                addImgBtn.show()
                true

            }
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

            vm.add(title, des, selected, link, imgLink, subId)

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

            ref.putBytes(byte).addOnProgressListener {
                val progress = 100.0 * it.bytesTransferred / it.totalByteCount

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    progress_bar.setProgress(progress.toInt(), true)
                } else {
                    progress_bar.progress = progress.toInt()
                }
            }.continueWithTask(Continuation<UploadTask.TaskSnapshot,
                    Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        Timber.e(it)
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result
                    imgLink = downloadUri?.toString() ?: ""
                } else {
                    Timber.i("Error")
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
