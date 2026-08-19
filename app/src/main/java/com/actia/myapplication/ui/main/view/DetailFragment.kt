package com.actia.myapplication.ui.main.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.actia.myapplication.R
import com.actia.myapplication.data.domain.model.Item
import com.actia.myapplication.databinding.FragmentDetailBinding
import com.actia.myapplication.ui.main.viewmodel.MainViewModel
import com.actia.myapplication.util.Constants
import org.koin.androidx.viewmodel.ext.android.activityViewModel


class DetailFragment : Fragment() {

    private lateinit var binding:FragmentDetailBinding

    private val mViewModel: MainViewModel by activityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments==null)
        {
            showToastAndExit(resources.getString(R.string.no_data_movie))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val args = arguments

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_detail, container, false)

        configToolBar()

        mViewModel.getDetailItemLiveData.observe(viewLifecycleOwner) {
            binding.obj = it
            isShowVeloVisible(false)
        }

        mViewModel.hasErrorOnRequestiveData.observe(viewLifecycleOwner){
            if(it && isResumed) {
                showToastAndExit(
                    resources.getString(R.string.error_on_request)
                )
            }
        }
        @Suppress("DEPRECATION")
        val data = args!!.getParcelable<Item>(Constants.KEY_BUNDLE_ITEM)
        if(!mViewModel.canGetDetail(data)) {
            showToastAndExit(resources.getString(R.string.no_data_movie))
        }else
        {
            isShowVeloVisible(true)
        }

        binding.lifecycleOwner = this


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showToastAndExit(textToShow:String){
        Toast.makeText(context, textToShow, Toast.LENGTH_LONG).show()
        @Suppress("DEPRECATION")
        activity?.onBackPressed()
    }

    private fun configToolBar() {
        val mToolbar: Toolbar = binding.toolbar
        mToolbar.setNavigationOnClickListener {
            @Suppress("DEPRECATION")
            activity?.onBackPressed()
        }
    }

    private fun isShowVeloVisible(isVisible:Boolean){
        binding.includeVelo.flVelo.visibility = if (isVisible) View.VISIBLE else View.GONE
    }


}