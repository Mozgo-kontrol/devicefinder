package com.vogella.android.devicefinder

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vogella.android.dev.DeviceAdapter
import com.vogella.android.devicefinder.data.*

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment(R.layout.fragment_list) {

    private val TAG: String = ListFragment::class.java.simpleName
    private val args: ListFragmentArgs by navArgs()

    private lateinit var vtTittle: TextView


    private val deviceListViewModel by viewModels<DeviceListViewModel> {
            DevicesListViewModelFactory(this.context!!)
        }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        vtTittle = view.findViewById(R.id.tv_title)

        vtTittle.text =  getString(R.string.tittle_list_fragment, args.firstname, args.lastname)

        val employee = Employee(4, args.firstname, args.lastname)

        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerView)

        val deviceAdapter = DeviceAdapter(employee, deviceListViewModel)

        recyclerView.adapter = deviceAdapter
        //data is observable
        deviceListViewModel.deviceLiveData.observe(viewLifecycleOwner, {
            it?.let { it ->

                with(deviceAdapter) {
                    submitList(it as MutableList<Device>)
                    notifyDataSetChanged()
                }
                Log.wtf(TAG, "Update lifeData!")
            }
            })
        val mLayoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = mLayoutManager
    }
     override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_sign_out -> {
                Log.i("item id ", item.itemId.toString() + "")
                goToLoginFragment()
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun goToLoginFragment(){

        val action = ListFragmentDirections.actionListFragmentToLoginFragment()
        findNavController().navigate(action)

    }
}



