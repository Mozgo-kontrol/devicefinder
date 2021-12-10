package com.vogella.android.devicefinder

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.MainThread
import androidx.compose.foundation.layout.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vogella.android.dev.DeviceAdapter
import com.vogella.android.devicefinder.data.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


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
         val firstname = args.firstname[0].uppercase()+ args.firstname.substring(1, args.firstname.length)
        val lastname = args.lastname[0].uppercase()+ args.lastname.substring(1, args.lastname.length)
        Log.wtf(TAG, "last name:$firstname $lastname!")
        vtTittle.text =  getString(R.string.tittle_list_fragment, firstname, lastname)

        val employee = Employee(4, firstname, lastname)

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

        findNavController().navigateUp()
    }


    @JvmName("getAnimalsObserver1")
    private fun getAnimalsObserver(): Observer<String> {
        return object : Observer<String> {

            override fun onSubscribe(d: Disposable) {
                Log.d(TAG, "onSubscribe")
            }

            override fun onNext(s: String) {
                Log.d(TAG, "Name: $s")
            }

            override fun onError(e: Throwable) {
                Log.e(TAG, "onError: " + e.message)
            }

            override fun onComplete() {
                Log.d(TAG, "All items are emitted!")
            }


        }
    }


}



