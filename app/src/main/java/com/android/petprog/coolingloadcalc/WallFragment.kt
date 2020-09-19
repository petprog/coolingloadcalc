package com.android.petprog.coolingloadcalc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.petprog.coolingloadcalc.databinding.FragmentWallBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

data class stateTempDetails(
    val stateName: String,
    val hTemp: Double,
    val lmN: Int,
    val lmNEOrNW: Int,
    val EOrW: Int,
    val SEOrSW: Int,
    val S: Int,
    val hor: Int
)

val statesDetails = listOf(

    stateTempDetails("Abuja", 83.3, 44, 184, 225, 134, 39, 289),
    stateTempDetails("Abia", 81.7, 38, 163, 242, 177, 43, 302),
    stateTempDetails("Adamawa", 103.0, 34, 114, 239, 219, 110, 294),
    stateTempDetails("Akwa Ibom", 83.0, 38, 163, 242, 177, 43, 302),
    stateTempDetails("Anambra", 84.0, 38, 163, 242, 177, 43, 302),
    stateTempDetails("Bauchi", 98.0, 44, 184, 225, 134, 39, 289),
    stateTempDetails("Bayelsa", 82.4, 38, 163, 242, 177, 43, 302),
    stateTempDetails("Benue", 94.0, 37, 156, 241, 184, 55, 300),
    stateTempDetails("Borno", 78.4, 40, 178, 227, 142, 40, 290),
    stateTempDetails("Cross River", 81.1, 38, 163, 242, 177, 43, 302),
    stateTempDetails("Delta", 80.9, 38, 163, 242, 177, 43, 302),
    stateTempDetails("Ekiti", 90.0, 34, 114, 239, 219, 110, 294),
    stateTempDetails("Ebonyi", 84.0, 82, 71, 224, 242, 162, 275),
    stateTempDetails("Edo", 82.0, 44, 184, 225, 134, 39, 289),
    stateTempDetails("Enugu", 88.0, 38, 163, 242, 177, 43, 302),
    stateTempDetails("Gombe", 100.0, 37, 156, 241, 184, 55, 300),
    stateTempDetails("Imo", 84.0, 82, 71, 224, 242, 162, 275),
    stateTempDetails("Jigawa", 103.0, 40, 178, 227, 142, 40, 290),
    stateTempDetails("Kaduna", 77.3, 44, 184, 225, 134, 39, 289),
    stateTempDetails("Kano", 101.0, 40, 178, 227, 142, 40, 290),
    stateTempDetails("Katsina", 77.3, 44, 184, 225, 134, 39, 289),
    stateTempDetails("Kebbi", 104.0, 40, 178, 227, 142, 40, 290),
    stateTempDetails("Kogi", 93.0, 32, 71, 224, 242, 162, 275),
    stateTempDetails("Kwara", 95.0, 34, 114, 239, 219, 110, 294),
    stateTempDetails("Lagos", 80.6, 34, 114, 239, 219, 110, 294),
    stateTempDetails("Nasarawa", 95.0, 37, 156, 241, 184, 55, 300),
    stateTempDetails("Niger", 91.0, 37, 156, 241, 184, 55, 300),
    stateTempDetails("Ogun", 93.0, 34, 114, 239, 219, 110, 294),
    stateTempDetails("Ondo", 91.0, 34, 114, 239, 219, 110, 294),
    stateTempDetails("Osun", 82.9, 37, 156, 241, 184, 55, 300),
    stateTempDetails("Oyo", 83.5, 37, 156, 241, 184, 55, 300),
    stateTempDetails("Plateau", 72.4, 37, 156, 241, 184, 55, 300),
    stateTempDetails("Rivers", 91.4, 34, 114, 239, 219, 110, 294),
    stateTempDetails("Sokoto", 77.3, 44, 184, 225, 134, 39, 289),
    stateTempDetails("Taraba", 99.0, 44, 184, 225, 134, 39, 289),
    stateTempDetails("Yobe", 85.5, 60, 194, 212, 106, 40, 280),
    stateTempDetails("Zamfara", 100.0, 40, 178, 227, 142, 40, 290)
)


class WallFragment : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var binding: FragmentWallBinding

    var lengthWallA: Double = 0.0
    var widthWallA: Double = 0.0
    var lengthWallB: Double = 0.0
    var widthWallB: Double = 0.0
    var lengthWallC: Double = 0.0
    var widthWallC: Double = 0.0
    var lengthWallD: Double = 0.0
    var widthWallD: Double = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wall, container, false)

//        lengthWallA = binding.textInputEditTextWallLengthA.text.toString().toDouble()

        val checkedIdA = binding.radioGroupWallA.checkedRadioButtonId

        if (-1 != checkedIdA) {
            when (checkedIdA) {
                R.id.radioButtonExternalWallA -> externalWallCalc()
                R.id.radioButtonInternalWallA -> internalWallCalc()
            }
        }

        val checkedIdB = binding.radioGroupWallB.checkedRadioButtonId

        if (-1 != checkedIdB) {
            when (checkedIdB) {
                R.id.radioButtonExternalWallB -> externalWallCalc()
                R.id.radioButtonInternalWallB -> internalWallCalc()
            }
        }

        val checkedIdC = binding.radioGroupWallC.checkedRadioButtonId

        if (-1 != checkedIdC) {
            when (checkedIdC) {
                R.id.radioButtonExternalWallC -> externalWallCalc()
                R.id.radioButtonInternalWallC -> internalWallCalc()
            }
        }

        val checkedIdD = binding.radioGroupWallD.checkedRadioButtonId

        if (-1 != checkedIdD) {
            when (checkedIdD) {
                R.id.radioButtonExternalWallD -> externalWallCalc()
                R.id.radioButtonInternalWallD -> internalWallCalc()
            }
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.orientations,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Apply the adapter to the spinner
            binding.spinnerWallA.adapter = adapter
            binding.spinnerWallB.adapter = adapter
            binding.spinnerWallC.adapter = adapter
            binding.spinnerWallD.adapter = adapter
        }

        binding.spinnerWallA.onItemSelectedListener = this
        binding.spinnerWallB.onItemSelectedListener = this
        binding.spinnerWallC.onItemSelectedListener = this
        binding.spinnerWallD.onItemSelectedListener = this


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    fun internalWallCalc() {

    }

    fun externalWallCalc() {

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
//        TODO("Not yet implemented")
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        TODO("Not yet implemented")
    }
}