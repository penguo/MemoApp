package com.penguodev.memoapp.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.penguodev.memoapp.R
import com.penguodev.memoapp.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var viewModel: HomeViewModel

    private var adapter: HomeMemoRcvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)

        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        adapter = HomeMemoRcvAdapter()

        viewModel.run {
            itemList.observe(this@HomeActivity, Observer {
                adapter?.submitList(it)
            })
        }
    }
}