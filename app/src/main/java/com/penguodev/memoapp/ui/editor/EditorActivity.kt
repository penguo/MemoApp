package com.penguodev.memoapp.ui.editor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.penguodev.memoapp.R
import com.penguodev.memoapp.databinding.ActivityEditorBinding

class EditorActivity : AppCompatActivity() {
    companion object {
        private const val KEY_ID = "KEY_ID"
        private const val INVALID_ID = -1L
        fun createActivityIntent(context: Context, id: Long?): Intent {
            return Intent(context, EditorActivity::class.java).apply {
                putExtra(KEY_ID, id)
            }
        }
    }

    private lateinit var binding: ActivityEditorBinding
    private lateinit var viewModel: EditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_editor)
        binding.lifecycleOwner = this

        val id = intent.getLongExtra(
            KEY_ID,
            INVALID_ID
        ) // KEY_ID에 값이 있다면, 해당 값을 가지고 없다면 INVALID_ID 값을 가진다.
            .takeIf { it != INVALID_ID } // 안의 조건문이 true일 때만 값을 유지한다. 조건문이 false일 경우 null을 반환.

        viewModel = ViewModelProvider(this, EditorViewModel.Factory(this.application, id)).get(
            EditorViewModel::class.java
        ).also { binding.viewModel = it }
            .apply {
                actionFinish.observe(this@EditorActivity, Observer {
                    finish()
                })
                actionBack.observe(this@EditorActivity, Observer {
                    onBackPressed()
                })
            }
    }

    override fun onBackPressed() {
        if ((viewModel.prevItem == null && viewModel.memo.value == "") || (viewModel.prevItem?.memo == viewModel.memo.value)) { // 변화 없음
            viewModel.actionFinish.call()
        } else {
            AlertDialog.Builder(this)
                .setTitle("에디터 화면을 나갑니다.")
                .setMessage("수정된 내용이 모두 사라집니다.")
                .setPositiveButton("확인") { dialog, which ->
                    dialog.dismiss()
                    viewModel.actionFinish.call()
                }
                .setNegativeButton("취소") { dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }
    }
}
