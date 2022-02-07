package com.example.hairbook.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hairbook.R
import com.example.hairbook.adapters.ServiceAdapter
import com.example.hairbook.data.ManDataSource
import com.example.hairbook.data.WomanDataSource
import com.example.hairbook.databinding.FragmentChooseServiceBinding
import com.example.hairbook.models.ServiceItem
import com.google.android.material.transition.MaterialElevationScale

class ChooseServiceFragment : Fragment(), ServiceAdapter.OnItemClickListener {

    companion object{
        var gender: String = "woman"
    }
    private lateinit var binding: FragmentChooseServiceBinding
    private var serviceAdapter = ServiceAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChooseServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycleView()
        addDataSet(view)

        binding.chooseWoman.setOnClickListener {
            gender = "woman"
            val data = WomanDataSource.createDataSet()
            binding.chooseMan.setImageResource(R.drawable.ic_man)
            binding.chooseWoman.setImageResource(R.drawable.ic_woman_choosen)
            serviceAdapter.changeLists(data)
        }

        binding.chooseMan.setOnClickListener {
            gender = "man"
            binding.chooseWoman.setImageResource(R.drawable.ic_woman)
            binding.chooseMan.setImageResource(R.drawable.ic_man_choosen)
            val data = ManDataSource.createDataSet()
            serviceAdapter.changeLists(data)
        }

        binding.myAppointments.setOnClickListener {
            val directions = ChooseServiceFragmentDirections.actionChooseServiceFragmentToMyAppointmentsFragment()
            findNavController().navigate(directions)
        }

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

    private fun addDataSet(view: View) {
        lateinit var data : ArrayList<ServiceItem>
        if(gender== "woman"){
            data = WomanDataSource.createDataSet()
        }else if(gender== "man"){
            binding.chooseWoman.setImageResource(R.drawable.ic_woman)
            binding.chooseMan.setImageResource(R.drawable.ic_man_choosen)
            data = ManDataSource.createDataSet()
        }
        serviceAdapter.initList(data)
    }


    private fun initRecycleView() {
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = serviceAdapter
        }
    }

    override fun onItemClick(view: View, item: ServiceItem) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.reply_motion_duration_large).toLong()
        }
        val serviceItemDetailTransitionName = getString(R.string.service_item_detail_transition_name)
        val extras = FragmentNavigatorExtras(view to serviceItemDetailTransitionName)
        val directions = ChooseServiceFragmentDirections.actionChooseServiceFragmentToPickDateFragment(item.id, item.gender)
        findNavController().navigate(directions, extras)
    }

}