package com.penguodev.memoapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.penguodev.memoapp.R
import com.penguodev.memoapp.databinding.ActivityHomeBinding
import com.penguodev.memoapp.ui.editor.EditorActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    private var adapter: HomeMemoRcvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java).also {
            binding.viewModel = it
        }
        adapter = HomeMemoRcvAdapter().also {
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
}