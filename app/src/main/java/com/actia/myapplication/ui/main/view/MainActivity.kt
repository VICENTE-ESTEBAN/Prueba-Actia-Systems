package com.actia.myapplication.ui.main.view

import android.os.Bundle
import androidx.databinding.DataBindingUtil.setContentView
import com.actia.myapplication.R
import com.actia.myapplication.databinding.ActivityMainBinding
import com.actia.myapplication.ui.base.ui.BaseActivity


class MainActivity : BaseActivity(){

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding  = setContentView(this, R.layout.activity_main)
    }

}