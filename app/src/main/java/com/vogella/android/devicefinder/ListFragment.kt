package com.vogella.android.devicefinder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import com.google.android.play.core.internal.s

/**
 * A simple [Fragment] subclass.
 * Use the [ListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ListFragment : Fragment(R.layout.fragment_list) {

    private val args: ListFragmentArgs by navArgs()

    private lateinit var vtTittle: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val a = 1
        vtTittle=view.findViewById(R.id.tv_lf)
        vtTittle.text =  getString(R.string.tittle_list_fragment, args.firstname, args.lastname)
    }

}