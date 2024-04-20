package com.ifs21019.lostfound.presentation.lostfound

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ifs21019.lostfound.data.model.LostFound
import com.ifs21019.lostfound.data.remote.MyResult
import com.ifs21019.lostfound.databinding.ActivityObjectManageBinding
import com.ifs21019.lostfound.helper.Utils.Companion.observeOnce
import com.ifs21019.lostfound.presentation.ViewModelFactory

class LostFoundManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityObjectManageBinding
    private val viewModel by viewModels<LostFoundViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
    }

    private fun setupView() {
        showLoading(false)
    }

    private fun setupAction() {
        val isAddObject = intent.getBooleanExtra(KEY_IS_ADD, true)
        if (isAddObject) {
            manageAddObject()
        } else {
            val lostFound = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                    intent.getParcelableExtra(KEY_OBJECT, LostFound::class.java)
                }
                else -> {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra<LostFound>(KEY_OBJECT)
                }
            }
            if (lostFound == null) {
                finishAfterTransition()
                return
            }
            manageEditObject(lostFound)
        }
        binding.appbarObjectManage.setNavigationOnClickListener {
            finishAfterTransition()
        }
    }

    private fun manageAddObject() {
        binding.apply {
            appbarObjectManage.title = "Tambah Object"
            btnObjectManageSave.setOnClickListener {
                val title = etObjectManageTitle.text.toString()
                val description = etObjectManageDesc.text.toString()
                val status = if (radioButtonLost.isChecked) "lost" else "found"
                if (title.isEmpty() || description.isEmpty()) {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage("Tidak boleh ada data yang kosong!")
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    return@setOnClickListener
                }
                observePostObject(title, description, status)
            }
        }
    }

    private fun observePostObject(title: String, description: String, status: String) {
        viewModel.postObject(title, description, status).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }
                is MyResult.Success -> {
                    showLoading(false)
                    val resultIntent = Intent()
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }
                is MyResult.Error -> {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage(result.error)
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    showLoading(false)
                }
            }
        }
    }

    private fun manageEditObject(lostfound: LostFound) {
        binding.apply {
            appbarObjectManage.title = "Ubah Object"
            etObjectManageTitle.setText(lostfound.title)
            etObjectManageDesc.setText(lostfound.description)
            // Tambahkan logika untuk menentukan status berdasarkan isCompleted
            val status = if (lostfound.isCompleted) "found" else "lost"
            // Lanjutkan dengan mengatur status pada radioButton
            if (status == "found") {
                radioButtonFound.isChecked = true
            } else {
                radioButtonLost.isChecked = true
            }
            btnObjectManageSave.setOnClickListener {
                val title = etObjectManageTitle.text.toString()
                val description = etObjectManageDesc.text.toString()
                if (title.isEmpty() || description.isEmpty()) {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage("Tidak boleh ada data yang kosong!")
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    return@setOnClickListener
                }
                observePutObject(lostfound.id, title, description, status, lostfound.isCompleted)
            }
        }
    }

    private fun observePutObject(
        lostfoundId: Int,
        title: String,
        description: String,
        status: String,
        isCompleted: Boolean,
    ) {
        // Tentukan status berdasarkan isCompleted
        val status = if (isCompleted) "found" else "lost"

        viewModel.putObject(
            lostfoundId,
            title,
            description,
            status,
            isCompleted
        ).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }
                is MyResult.Success -> {
                    showLoading(false)
                    val resultIntent = Intent()
                    setResult(RESULT_CODE, resultIntent)
                    finishAfterTransition()
                }
                is MyResult.Error -> {
                    AlertDialog.Builder(this@LostFoundManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage(result.error)
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                    showLoading(false)
                }
            }
        }
    }



    private fun showLoading(isLoading: Boolean) {
        binding.pbObjectManage.visibility =
            if (isLoading) View.VISIBLE else View.GONE
        binding.btnObjectManageSave.isActivated = !isLoading
        binding.btnObjectManageSave.text =
            if (isLoading) "" else "Simpan"
    }

    companion object {
        const val KEY_IS_ADD = "is_add"
        const val KEY_OBJECT = "object"
        const val RESULT_CODE = 1002
    }
}
