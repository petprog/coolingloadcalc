package com.android.petprog.coolingloadcalc

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.petprog.coolingloadcalc.databinding.FragmentWallBinding
import kotlin.math.pow

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

data class StateTempDetails(
    val stateName: String,
    val hTemp: Double,
    val lmN: Int,
    val lmNEOrNW: Int,
    val lmEOrW: Int,
    val lmSEOrSW: Int,
    val lmS: Int,
    val lmNor: Int
)

val statesDetails = listOf(

    StateTempDetails("Abuja", 83.3, 44, 184, 225, 134, 39, 289),
    StateTempDetails("Abia", 81.7, 38, 163, 242, 177, 43, 302),
    StateTempDetails("Adamawa", 103.0, 34, 114, 239, 219, 110, 294),
    StateTempDetails("Akwa Ibom", 83.0, 38, 163, 242, 177, 43, 302),
    StateTempDetails("Anambra", 84.0, 38, 163, 242, 177, 43, 302),
    StateTempDetails("Bauchi", 98.0, 44, 184, 225, 134, 39, 289),
    StateTempDetails("Bayelsa", 82.4, 38, 163, 242, 177, 43, 302),
    StateTempDetails("Benue", 94.0, 37, 156, 241, 184, 55, 300),
    StateTempDetails("Borno", 78.4, 40, 178, 227, 142, 40, 290),
    StateTempDetails("Cross River", 81.1, 38, 163, 242, 177, 43, 302),
    StateTempDetails("Delta", 80.9, 38, 163, 242, 177, 43, 302),
    StateTempDetails("Ekiti", 90.0, 34, 114, 239, 219, 110, 294),
    StateTempDetails("Ebonyi", 84.0, 82, 71, 224, 242, 162, 275),
    StateTempDetails("Edo", 82.0, 44, 184, 225, 134, 39, 289),
    StateTempDetails("Enugu", 88.0, 38, 163, 242, 177, 43, 302),
    StateTempDetails("Gombe", 100.0, 37, 156, 241, 184, 55, 300),
    StateTempDetails("Imo", 84.0, 82, 71, 224, 242, 162, 275),
    StateTempDetails("Jigawa", 103.0, 40, 178, 227, 142, 40, 290),
    StateTempDetails("Kaduna", 77.3, 44, 184, 225, 134, 39, 289),
    StateTempDetails("Kano", 101.0, 40, 178, 227, 142, 40, 290),
    StateTempDetails("Katsina", 77.3, 44, 184, 225, 134, 39, 289),
    StateTempDetails("Kebbi", 104.0, 40, 178, 227, 142, 40, 290),
    StateTempDetails("Kogi", 93.0, 32, 71, 224, 242, 162, 275),
    StateTempDetails("Kwara", 95.0, 34, 114, 239, 219, 110, 294),
    StateTempDetails("Lagos", 80.6, 34, 114, 239, 219, 110, 294),
    StateTempDetails("Nasarawa", 95.0, 37, 156, 241, 184, 55, 300),
    StateTempDetails("Niger", 91.0, 37, 156, 241, 184, 55, 300),
    StateTempDetails("Ogun", 93.0, 34, 114, 239, 219, 110, 294),
    StateTempDetails("Ondo", 91.0, 34, 114, 239, 219, 110, 294),
    StateTempDetails("Osun", 82.9, 37, 156, 241, 184, 55, 300),
    StateTempDetails("Oyo", 83.5, 37, 156, 241, 184, 55, 300),
    StateTempDetails("Plateau", 72.4, 37, 156, 241, 184, 55, 300),
    StateTempDetails("Rivers", 91.4, 34, 114, 239, 219, 110, 294),
    StateTempDetails("Sokoto", 77.3, 44, 184, 225, 134, 39, 289),
    StateTempDetails("Taraba", 99.0, 44, 184, 225, 134, 39, 289),
    StateTempDetails("Yobe", 85.5, 60, 194, 212, 106, 40, 280),
    StateTempDetails("Zamfara", 100.0, 40, 178, 227, 142, 40, 290)
)

val mapCltdO = mapOf(
    "N" to 13,
    "NE" to 24,
    "E" to 33,
    "SE" to 32,
    "S" to 24,
    "SW" to 21,
    "W" to 18,
    "NW" to 14
)


class WallFragment : Fragment(), AdapterView.OnItemSelectedListener {

    lateinit var binding: FragmentWallBinding

//    var lengthWallA: Double = 0.0
//    var widthWallA: Double = 0.0
//    var lengthWallB: Double = 0.0
//    var widthWallB: Double = 0.0
//    var lengthWallC: Double = 0.0
//    var widthWallC: Double = 0.0
//    var lengthWallD: Double = 0.0
//    var widthWallD: Double = 0.0

    var isInternalAtWallA = false
    var isInternalAtWallB = false
    var isInternalAtWallC = false
    var isInternalAtWallD = false

    var orientationA = ""
    var orientationB = ""
    var orientationC = ""
    var orientationD = ""


    lateinit var stateTempDetails: StateTempDetails


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
                R.id.radioButtonExternalWallA -> isInternalAtWallA = false
                R.id.radioButtonInternalWallA -> isInternalAtWallA = true
            }
        }

        val checkedIdB = binding.radioGroupWallB.checkedRadioButtonId

        if (-1 != checkedIdB) {
            when (checkedIdB) {
                R.id.radioButtonExternalWallB -> isInternalAtWallB = false
                R.id.radioButtonInternalWallB -> isInternalAtWallB = true
            }
        }

        val checkedIdC = binding.radioGroupWallC.checkedRadioButtonId

        if (-1 != checkedIdC) {
            when (checkedIdC) {
                R.id.radioButtonExternalWallC -> isInternalAtWallC = false
                R.id.radioButtonInternalWallC -> isInternalAtWallC = true
            }
        }

        val checkedIdD = binding.radioGroupWallD.checkedRadioButtonId

        if (-1 != checkedIdD) {
            when (checkedIdD) {
                R.id.radioButtonExternalWallD -> isInternalAtWallD = false
                R.id.radioButtonInternalWallD -> isInternalAtWallD = true
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

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.states,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Apply the adapter to the spinner
            binding.spinnerState.adapter = adapter
        }

        binding.spinnerState.onItemSelectedListener = this

        binding.submitButton.setOnClickListener {
            Toast.makeText(requireContext(), totalWallHeat().toString(), Toast.LENGTH_LONG).show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_first).setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    fun internalWallCalc(
        aW: Double,
        cltdO: Int,
        lm: Int,
        uW: Double = 0.26,
        tR: Double = 71.6
    ): Double {
        return uW * aW * ((cltdO + lm) + (78 - tR) + (tR - 80))
    }

    fun externalWallCalc(
        aW: Double,
        cltdO: Int,
        lm: Int,
        tO: Double,
        uW: Double = 0.26,
        tR: Double = 71.6
    ): Double {
        return uW * aW * ((cltdO + lm) + (78 - tR) + (tO - 80))
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
//        TODO("Not yet implemented")
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {

        val languages = resources.getStringArray(R.array.orientations)
        val states = resources.getStringArray(R.array.states)

        when (parent?.id) {
            R.id.spinnerWallA -> {
                orientationA = languages[position]
            }
            R.id.spinnerWallB -> {
                orientationB = languages[position]
            }
            R.id.spinnerWallC -> {
                orientationC = languages[position]
            }
            R.id.spinnerWallD -> {
                orientationD = languages[position]

            }

            R.id.spinnerState -> {
                stateTempDetails = selectStateDetails(states[position])
            }
        }
//
    }

    fun selectCLTDO(orientation: String): Int = mapCltdO.getOrElse(orientation) { 4 }

    fun selectStateDetails(state: String): StateTempDetails {
        var stateTempDetails: StateTempDetails = statesDetails[0]
        for (sd in statesDetails) {
            if (sd.stateName == state) {
                stateTempDetails = sd
            }
        }
        return stateTempDetails
    }

    fun areaOfWallBAndDInFeet(length: Double, width: Double) = length * width * 3.28.pow(2.0)
    fun areaOfWallAInFeet(length: Double, width: Double) =
        ((length * width) - 1.0 - 1.575) * 3.28.pow(2.0)

    fun areaOfWallCInFeet(length: Double, width: Double) =
        ((length * width) - (1.0 * 1.2)) * 3.28.pow(2.0)

    fun wallAHeat(): Double {
        var area = 0.0
        if (!binding.textInputEditTextWallLengthA.text.isNullOrEmpty() && !binding.textInputEditTextWallWidthA.text.isNullOrEmpty()) {
            area = areaOfWallAInFeet(
                binding.textInputEditTextWallLengthA.text.toString()
                    .toDouble(), binding.textInputEditTextWallWidthA.text.toString().toDouble()
            )
        }
        if (isInternalAtWallA) {
            return internalWallCalc(area, selectCLTDO(orientationA), selectLM(orientationA))
        } else {
            return externalWallCalc(area, selectCLTDO(orientationA), selectLM(orientationA), stateTempDetails.hTemp)
        }
    }

    fun selectLM(orientation: String): Int {
        return when(orientation) {
            "N" -> stateTempDetails.lmN
            "NE"-> stateTempDetails.lmNEOrNW
            "NW"-> stateTempDetails.lmNEOrNW
            "S"-> stateTempDetails.lmS
            "SW"-> stateTempDetails.lmSEOrSW
            "SE"-> stateTempDetails.lmSEOrSW
            "E"-> stateTempDetails.lmEOrW
            "W"-> stateTempDetails.lmEOrW
            else -> 0
        }
    }


    fun wallBHeat(): Double {
        var area = 0.0
        if (!binding.textInputEditTextWallLengthB.text.isNullOrEmpty() && !binding.textInputEditTextWallWidthB.text.isNullOrEmpty()) {
            area = areaOfWallBAndDInFeet(
                binding.textInputEditTextWallLengthB.text.toString()
                    .toDouble(), binding.textInputEditTextWallWidthB.text.toString().toDouble()
            )
        }
        return if (isInternalAtWallB) {
            internalWallCalc(area, selectCLTDO(orientationB), selectLM(orientationB))
        } else {
            externalWallCalc(area, selectCLTDO(orientationB), selectLM(orientationB), stateTempDetails.hTemp)
        }
    }

    fun wallCHeat(): Double {
        var area = 0.0
        if (!binding.textInputEditTextWallLengthC.text.isNullOrEmpty() && !binding.textInputEditTextWallWidthC.text.isNullOrEmpty()) {
            area = areaOfWallCInFeet(
                binding.textInputEditTextWallLengthC.text.toString()
                    .toDouble(), binding.textInputEditTextWallWidthC.text.toString().toDouble()
            )
        }
        return if (isInternalAtWallC) {
            internalWallCalc(area, selectCLTDO(orientationC), selectLM(orientationC))
        } else {
            externalWallCalc(area, selectCLTDO(orientationC), selectLM(orientationC), stateTempDetails.hTemp)
        }
    }

    fun wallDHeat(): Double {
        var area = 0.0
        if (!binding.textInputEditTextWallLengthD.text.isNullOrEmpty() && !binding.textInputEditTextWallWidthD.text.isNullOrEmpty()) {
            area = areaOfWallBAndDInFeet(
                binding.textInputEditTextWallLengthD.text.toString()
                    .toDouble(), binding.textInputEditTextWallWidthD.text.toString().toDouble()
            )
        }
        return if (isInternalAtWallB) {
            internalWallCalc(area, selectCLTDO(orientationD), selectLM(orientationD))
        } else {
            externalWallCalc(area, selectCLTDO(orientationD), selectLM(orientationD), stateTempDetails.hTemp)
        }
    }

    fun totalWallHeat(): Double {
        return wallAHeat() + wallBHeat() + wallCHeat() + wallDHeat()
    }
}