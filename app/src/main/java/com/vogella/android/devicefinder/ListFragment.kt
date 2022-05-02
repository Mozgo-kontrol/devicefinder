package com.vogella.android.devicefinder


import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.vogella.android.dev.DeviceAdapter
import com.vogella.android.devicefinder.Tools.makeToHighFirstLetter
import com.vogella.android.devicefinder.data.*


/**
 * A simple [Fragment] subclass.
 * Use the [context.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment(R.layout.fragment_list) {

    private val PERMISSION_REQUEST_CODE = 100
    private val TAG: String = ListFragment::class.java.simpleName
    private lateinit var vtTittle: TextView

    private var firstname :String = ""
    private var lastname :String = ""

    //C8EA3968-E8E0-4A30-BFF5-87D21AD1A040

    private val deviceListViewModel by viewModels<DeviceListViewModel> {
            DevicesListViewModelFactory(this.context!!)
        }
    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        vtTittle = view.findViewById(R.id.tv_title)
        firstname = makeToHighFirstLetter("Firstname")
        lastname = makeToHighFirstLetter("Lastname")

        vtTittle.text =  getString(R.string.tittle_list_fragment, firstname, lastname)

        val employee = Employee(4, firstname, lastname)

        val recyclerView : RecyclerView = view.findViewById(R.id.recyclerView)
        val deviceAdapter = DeviceAdapter(employee, deviceListViewModel)

        recyclerView.adapter = deviceAdapter
        //data is observable
        deviceListViewModel.getDeviceList().observe(viewLifecycleOwner, {
            it?.let {
                with(deviceAdapter) {
                    submitList(it as MutableList<Device>)
                    notifyDataSetChanged()
                }
                Log.wtf(TAG, "Update lifeData!")
            }
            })

        val mLayoutManager = LinearLayoutManager(this.context)
        recyclerView.layoutManager = mLayoutManager

        val fab: View = view.findViewById(R.id.fab_add)
        fab.setOnClickListener {
            Log.wtf(TAG, "Fab button")
            bottomSheetDialog(context!!)
        }
        askForPermissions(context!!)
        // f4521d2e6dbf8a85
        Log.wtf(TAG, "last name:$firstname $lastname!")
        Log.wtf(TAG, "getSystemInfo : ${Tools.getSystemDetail(context!!)}")
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

    private fun bottomSheetDialog(context: Context){
        val bottomSheetDialog = BottomSheetDialog(context!!)
        val dialogView = layoutInflater.inflate(R.layout.add_dialog,null)
        dialogView.findViewById<TextView>(R.id.tv_model_afd)
        val editTextDialog = dialogView.findViewById<EditText>(R.id.et_model_afd)
        val buttonDialogOk = dialogView.findViewById<Button>(R.id.bn_model_afd)
        val buttonDialogCancel = dialogView.findViewById<Button>(R.id.bn_model_cancel_afd)
        bottomSheetDialog.setContentView(dialogView)
        bottomSheetDialog.show()
        buttonDialogOk.setOnClickListener {
            val modelMame = editTextDialog.text.toString().trim()

            if (modelMame.isNotEmpty()){
                deviceListViewModel.addNewDevice(modelMame)
                bottomSheetDialog.dismiss()
            }
        }
        buttonDialogCancel.setOnClickListener{bottomSheetDialog.dismiss()}
    }



    private fun goToLoginFragment(){
        findNavController().navigateUp()
    }



    private fun askForPermissions(context: Context){
        if (ActivityCompat.checkSelfPermission(
                context,
                READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(READ_PHONE_STATE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            Log.wtf(TAG, "Permissions is Granted")
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        @NonNull permissions: Array<String>,
        @NonNull grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (ActivityCompat.checkSelfPermission(
                    context!!,
                    READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else {
                Log.wtf(TAG, "Permissions is Granted")
            }
        }
    }
}



