package com.example.myfirstapp.fragments
import HoursFragment
import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.myfirstapp.R
import com.example.myfirstapp.adapters.VpAdapter
import com.example.myfirstapp.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainFragment : Fragment() {
    private val fList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance(),
        ExpenseListFragment.newInstance(),
    )
    private val tList = listOf(
        "Hours",
        "Days",
        "Expenses"
    )
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private lateinit var carAnimation: Annotation


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        init()

        val button = view.findViewById<ImageButton>(R.id.ibWashesMap)
        button.setOnClickListener {
            val mapsFragment = MapsFragment()

            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, mapsFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }




    private fun init() = with(binding) {
        val adapter = VpAdapter(activity as FragmentActivity, fList)
        vp.adapter = adapter
        TabLayoutMediator(tabLayout, vp){
            tab, pos -> tab.text = tList[pos]
        }.attach()
    }

    private fun permissionListener(){
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission() {
        if (!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)


        }
    }




    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()


    }
}