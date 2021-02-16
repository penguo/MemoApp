package com.penguodev.memoapp.ui.home

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.penguodev.memoapp.R
import com.penguodev.memoapp.common.InAppReviewHelper
import com.penguodev.memoapp.databinding.ActivityHomeBinding
import com.penguodev.memoapp.ui.editor.EditorActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    private var adapter: HomeMemoRcvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.lifecycleOwner = this
        setSupportActionBar(binding.toolbar)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java).also {
            binding.viewModel = it
        }
        adapter = HomeMemoRcvAdapter(viewModel).also {
            binding.recyclerviewHome.adapter = it
        }

        viewModel.apply {
            itemList.observe(this@HomeActivity, Observer {
                adapter?.submitList(it)
            })
            actionOpenEditor.observe(this@HomeActivity, Observer {
                startActivity(EditorActivity.createActivityIntent(this@HomeActivity, null))
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_setting -> {
                onClickSetting()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClickSetting() {
        AlertDialog.Builder(this)
            .setItems(arrayOf("리뷰 작성")) { dialog, which ->
                when (which) {
                    0 -> {
                        InAppReviewHelper.show(this, onSuccess = {
                            Toast.makeText(this, "success!", Toast.LENGTH_SHORT).show()
                        }, onError = {
                            Toast.makeText(this, "error! $it", Toast.LENGTH_SHORT).show()
                        })
                    }
                }
                dialog.dismiss()
            }.show()
    }
}