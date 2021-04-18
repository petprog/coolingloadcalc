package com.android.petprog.coolingloadcalc.view.fragment

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.petprog.coolingloadcalc.R
import com.android.petprog.coolingloadcalc.databinding.FragmentCoolingLoadCalculatorBinding
import com.android.petprog.coolingloadcalc.model.CalculatedData
import com.android.petprog.coolingloadcalc.model.StateTempDetail
import com.android.petprog.coolingloadcalc.util.convertCelsiusToFahrenheit
import com.android.petprog.coolingloadcalc.util.convertFahrenheitToCelsius
import com.android.petprog.coolingloadcalc.util.toKilo
import com.android.petprog.coolingloadcalc.util.toOneDecimal
import com.android.petprog.coolingloadcalc.viewmodel.CalculatedDataViewModel
import com.android.petprog.coolingloadcalc.viewmodel.CoolingLoadCalculatorFragmentViewModel
import java.util.*
import kotlin.math.roundToInt

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CoolingLoadCalculatorFragment : Fragment(), AdapterView.OnItemSelectedListener,
    SensorEventListener {

    private lateinit var binding: FragmentCoolingLoadCalculatorBinding

    private var mSensorManager: SensorManager? = null
    private var currentDegree = 0

    private lateinit var stateTempDetail: StateTempDetail

    private lateinit var mCalculatedDataViewModel: CalculatedDataViewModel

    private lateinit var mCalculatorFragmentViewModel: CoolingLoadCalculatorFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        initData()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cooling_load_calculator,
            container,
            false
        )

        mCalculatedDataViewModel = ViewModelProvider(this).get(CalculatedDataViewModel::class.java)
        mCalculatorFragmentViewModel =
            ViewModelProvider(this).get(CoolingLoadCalculatorFragmentViewModel::class.java)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.orientations,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Apply the adapter to the spinner
            binding.spinnerOrientationWallA.adapter = adapter
            binding.spinnerOrientationWallB.adapter = adapter
            binding.spinnerOrientationWallC.adapter = adapter
            binding.spinnerOrientationWallD.adapter = adapter
        }

        binding.spinnerOrientationWallA.onItemSelectedListener = this
        binding.spinnerOrientationWallB.onItemSelectedListener = this
        binding.spinnerOrientationWallC.onItemSelectedListener = this
        binding.spinnerOrientationWallD.onItemSelectedListener = this

        if (!binding.spaceRequiredTemperature.text.isNullOrEmpty()) {
            mCalculatorFragmentViewModel.requiredTemperature =
                binding.spaceRequiredTemperature.text.toString().toInt()
        }


        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.states,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Apply the adapter to the spinner
            binding.spinnerState.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.space_purposes,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Apply the adapter to the spinner
            binding.spinnerSpacePurpose.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.roof_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Apply the adapter to the spinner
            binding.spinnerRoofType.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.wall_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Apply the adapter to the spinner
            binding.spinnerWallType.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.window_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Apply the adapter to the spinner
            binding.spinnerWindowType.adapter = adapter
        }

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.ceiling_types,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Apply the adapter to the spinner
            binding.spinnerCeilingType.adapter = adapter
        }

        binding.spinnerState.onItemSelectedListener = this
        binding.spinnerSpacePurpose.onItemSelectedListener = this
        binding.spinnerRoofType.onItemSelectedListener = this
        binding.spinnerWallType.onItemSelectedListener = this
        binding.spinnerWindowType.onItemSelectedListener = this
        binding.spinnerCeilingType.onItemSelectedListener = this

        binding.buttonAddEquipment.setOnClickListener {

            val builder = AlertDialog.Builder(requireActivity())
            // String array for alert dialog multi choice items
            var totalLoad = 0.0
            val equipments = resources.getStringArray(R.array.equipments)
            val selectedEquipments = mutableListOf<String>()
            //setTitle
            builder.setTitle(R.string.add_equipment_text)
            //set multi-choice
            builder.setMultiChoiceItems(equipments, null) { _, which, isChecked ->
                if (isChecked) {
                    selectedEquipments.add(equipments[which])
                } else if (selectedEquipments.contains(equipments[which])) {
                    selectedEquipments.remove(equipments[which])
                }
            }
            builder.setPositiveButton("OK") { _, _ ->
                for (equipment in selectedEquipments) {
                    totalLoad += mCalculatorFragmentViewModel.getEquipment(equipment)
                }
                mCalculatorFragmentViewModel.mTotalEquipmentLoad = totalLoad / 1000
                mCalculatorFragmentViewModel.mListOfEquipments =
                    selectedEquipments.toList().toString()
            }
            builder.setNeutralButton("Cancel") { _, _ ->
            }
            val dialog = builder.create()
            dialog.show()
        }


        binding.buttonAddFloorType.setOnClickListener {
            val builder = AlertDialog.Builder(requireActivity())
            var totalU = 0.0
            // String array for alert dialog multi choice items
            val floorTypes = resources.getStringArray(R.array.floor_types)
            val floorTypesSelected = mutableListOf<String>()
            //setTitle
            builder.setTitle(R.string.add_floor_types_text)
            //set multi-choice
            builder.setMultiChoiceItems(floorTypes, null) { _, which, isChecked ->
                if (isChecked) {
                    floorTypesSelected.add(floorTypes[which])
                } else if (floorTypesSelected.contains(floorTypes[which])) {
                    floorTypesSelected.remove(floorTypes[which])
                }
            }
            // Set the positive/yes button click listener
            builder.setPositiveButton("OK") { _, _ ->
                for (floorType in floorTypesSelected) {
                    totalU += mCalculatorFragmentViewModel.convertToThermalTransmittance(floorType)

                }
                mCalculatorFragmentViewModel.mTotalFloorTypeU = totalU
                mCalculatorFragmentViewModel.mFloorType = floorTypesSelected.toList().toString()
            }
            // Set the neutral/cancel button click listener
            builder.setNeutralButton("Cancel") { _, _ ->
                // Do something when click the neutral button
            }
            val dialog = builder.create()
            // Display the alert dialog on interface
            dialog.show()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getOrientationAButton.setOnClickListener {
            binding.textViewOrientationWallA.setText(
                getOrientation(currentDegree),
                TextView.BufferType.EDITABLE
            )
        }

        binding.getOrientationBButton.setOnClickListener {
            binding.textViewOrientationWallB.setText(
                getOrientation(currentDegree),
                TextView.BufferType.EDITABLE
            )
        }

        binding.getOrientationCButton.setOnClickListener {
            binding.textViewOrientationWallC.setText(
                getOrientation(currentDegree),
                TextView.BufferType.EDITABLE
            )
        }

        binding.getOrientationDButton.setOnClickListener {
            binding.textViewOrientationWallD.setText(
                getOrientation(currentDegree),
                TextView.BufferType.EDITABLE
            )
        }

        binding.getDoorOrientationButton.setOnClickListener {
            binding.textViewOrientationDoor.setText(
                getOrientation(currentDegree),
                TextView.BufferType.EDITABLE
            )
        }

        binding.calculateButton.setOnClickListener {
            var spaceArea = 0.0
            var spaceName = ""
            mCalculatorFragmentViewModel.mRoofLoad = roofCoolingLoad().toKilo().toOneDecimal()
            mCalculatorFragmentViewModel.mWallLoad = wallCoolingLoad().toKilo().toOneDecimal()
            mCalculatorFragmentViewModel.mFloorLoad = floorCoolingLoad().toKilo().toOneDecimal()
            mCalculatorFragmentViewModel.mWindowLoad = windowLoad().toKilo().toOneDecimal()
            mCalculatorFragmentViewModel.mDoorLoad = doorLoad().toKilo().toOneDecimal()
            mCalculatorFragmentViewModel.mCeilingLoad = ceilingLoad().toKilo().toOneDecimal()
            mCalculatorFragmentViewModel.mPeopleLoad = peopleLoad().toKilo().toOneDecimal()

            if (binding.spaceWidth.text!!.isNotEmpty() && binding.spaceLength.text!!.isNotEmpty()) {
                spaceArea = binding.spaceWidth.text.toString()
                    .toDouble() * binding.spaceLength.text.toString().toDouble()
            }

            if (binding.spaceName.text!!.isNotEmpty()) {
                spaceName = binding.spaceName.text.toString()
            }

            val totalLoad: Double =
                (mCalculatorFragmentViewModel.mRoofLoad + mCalculatorFragmentViewModel.mWallLoad + mCalculatorFragmentViewModel.mFloorLoad + mCalculatorFragmentViewModel.mWindowLoad + mCalculatorFragmentViewModel.mDoorLoad + mCalculatorFragmentViewModel.mCeilingLoad + mCalculatorFragmentViewModel.mPeopleLoad + mCalculatorFragmentViewModel.mTotalEquipmentLoad).toOneDecimal()

            val data = CalculatedData(
                0,
                Date(),
                spaceName,
                mCalculatorFragmentViewModel.stateName,
                spaceArea,
                mCalculatorFragmentViewModel.mSpacePurpose,
                mCalculatorFragmentViewModel.mRoofType,
                mCalculatorFragmentViewModel.mRoofArea,
                mCalculatorFragmentViewModel.mRoofLoad,
                mCalculatorFragmentViewModel.mWallType,
                mCalculatorFragmentViewModel.mWallAreaA,
                mCalculatorFragmentViewModel.mWallAreaB,
                mCalculatorFragmentViewModel.mWallAreaC,
                mCalculatorFragmentViewModel.mWallAreaD,
                mCalculatorFragmentViewModel.mWallAOrientationA,
                mCalculatorFragmentViewModel.mWallAOrientationB,
                mCalculatorFragmentViewModel.mWallAOrientationC,
                mCalculatorFragmentViewModel.mWallAOrientationD,
                mCalculatorFragmentViewModel.mWallLoad,
                mCalculatorFragmentViewModel.mFloorType,
                mCalculatorFragmentViewModel.mFloorArea,
                mCalculatorFragmentViewModel.mFloorLoad,
                mCalculatorFragmentViewModel.mWindowType,
                mCalculatorFragmentViewModel.mWindowArea,
                mCalculatorFragmentViewModel.mWindowLoad,
                mCalculatorFragmentViewModel.mDoorArea,
                mCalculatorFragmentViewModel.mDoorOrientation,
                mCalculatorFragmentViewModel.mDoorLoad,
                mCalculatorFragmentViewModel.mCeilingType,
                mCalculatorFragmentViewModel.mCeilingArea,
                mCalculatorFragmentViewModel.mCeilingLoad,
                mCalculatorFragmentViewModel.mListOfEquipments,
                mCalculatorFragmentViewModel.mTotalEquipmentLoad,
                mCalculatorFragmentViewModel.mNumberOfPeople,
                mCalculatorFragmentViewModel.mPeopleLoad,
                totalLoad
            )
            val direction =
                CoolingLoadCalculatorFragmentDirections.actionCoolingLoadCalculatorFragmentToDetailFragment(
                    data
                )
            findNavController().navigate(direction)
        }
    }


    private fun lowerDegree(degree: Int): Int {
        return degree - 45 / 2
    }

    private fun upperDegree(degree: Int): Int {
        return degree + 45 / 2
    }

    private fun getOrientation(degree: Int): String {
        return when (degree) {
            in 0..upperDegree(0) -> return "N"
            in lowerDegree(360)..360 -> return "N"
            in lowerDegree(45)..upperDegree(45) -> return "NE"
            in lowerDegree(90)..upperDegree(90) -> return "E"
            in lowerDegree(135)..upperDegree(135) -> return "SE"
            in lowerDegree(180)..upperDegree(180) -> return "S"
            in lowerDegree(225)..upperDegree(225) -> return "SW"
            in lowerDegree(270)..upperDegree(270) -> return "W"
            in lowerDegree(315)..upperDegree(315) -> return "NW"
            else -> "NAN"
        }
    }

    private fun initData() {
        mSensorManager =
            requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager?
    }

    override fun onResume() {
        super.onResume()
        @Suppress("DEPRECATION")
        mSensorManager?.registerListener(
            this,
            mSensorManager?.getDefaultSensor(Sensor.TYPE_ORIENTATION),
            SensorManager.SENSOR_DELAY_GAME
        )
    }


    override fun onNothingSelected(p0: AdapterView<*>?) {
//        TODO("Not yet implemented")
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, p3: Long) {

        val purpose = resources.getStringArray(R.array.space_purposes)
        val states = resources.getStringArray(R.array.states)

        val wallTypes = resources.getStringArray(R.array.wall_types)
        val roofTypes = resources.getStringArray(R.array.roof_types)
//        val floorTypes = resources.getStringArray(R.array.floor_types)
        val windowTypes = resources.getStringArray(R.array.window_types)
        val ceilingTypes = resources.getStringArray(R.array.ceiling_types)

        when (parent?.id) {

            R.id.spinnerWallType -> mCalculatorFragmentViewModel.mWallType = wallTypes[position]
//            R.id.spinnerFloorType -> floorType = floorTypes[position]
            R.id.spinnerRoofType -> {
                mCalculatorFragmentViewModel.mRoofType = roofTypes[position]
            }
            R.id.spinnerWindowType -> mCalculatorFragmentViewModel.mWindowType =
                windowTypes[position]
            R.id.spinnerCeilingType -> mCalculatorFragmentViewModel.mCeilingType =
                ceilingTypes[position]

            R.id.spinnerState -> {
                mCalculatorFragmentViewModel.stateName = states[position]
                stateTempDetail = mCalculatorFragmentViewModel.selectStateDetails(states[position])
            }

            R.id.spinnerSpacePurpose -> {
                mCalculatorFragmentViewModel.mSpacePurpose = purpose[position]
            }
        }
//
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val degree = (event?.values?.get(0)!!).roundToInt()
        currentDegree = degree
    }

    private fun selectCltdWall(orientation: String): Int {
        var cltd = 0
        if (mCalculatorFragmentViewModel.mapCltdWall.containsKey(orientation))
            cltd = mCalculatorFragmentViewModel.mapCltdWall[orientation] ?: error("")
        return cltd
    }

    private fun selectCltdDoor(orientation: String): Int {
        var cltd = 0
        if (mCalculatorFragmentViewModel.mapCltdDoor.containsKey(orientation))
            cltd = mCalculatorFragmentViewModel.mapCltdDoor[orientation] ?: error("")
        return cltd
    }

    private fun coolingLoadRWC(
        thermalTransmittance: Double,
        cltdCorrected: Double,
        area: Double
    ): Double {

        return thermalTransmittance * cltdCorrected * area
    }

    private fun calculateCltdCorrected(
        cltd: Int,
        meanTemp: Double,
        requiredTemp: Int = mCalculatorFragmentViewModel.requiredTemperature.toDouble()
            .convertCelsiusToFahrenheit()
    ): Double {
        return (cltd + (78 - requiredTemp) + (meanTemp - 85)).convertFahrenheitToCelsius()
    }

//    private fun solarLoadThroughGlass(area: Double, sc: Double, scl: Double): Double {
//        return area * sc * scl
//    }

    private fun coolingLoadPCF(
        thermalTransmittance: Double,
        area: Double,
        tempAdjacentSpace: Double,
        tempInsideDesign: Double
    ): Double {
        return thermalTransmittance * area * (mCalculatorFragmentViewModel.requiredTemperature + 21 - mCalculatorFragmentViewModel.requiredTemperature)
    }


    private fun peopleLoad(): Double {
        var load = 0.0
        if (!binding.numberOfPeople.text.isNullOrEmpty()) {
            mCalculatorFragmentViewModel.mNumberOfPeople =
                binding.numberOfPeople.text.toString().toInt()
            val totalLoad =
                mCalculatorFragmentViewModel.getHeatGainByPeople(mCalculatorFragmentViewModel.mSpacePurpose)
            load += peopleLoadFormula(mCalculatorFragmentViewModel.mNumberOfPeople, totalLoad, 1)
        }
        return load
    }

    private fun peopleLoadFormula(n: Int, shg: Int, ccf: Int): Double {
        return (n * shg * ccf).toDouble()
    }

//    private fun lightingLoad(
//        lightCapacity: Double,
//        lightingUseFactor: Double,
//        specialAllowanceFactor: Double,
//        clf: Double
//    ): Double {
//        return lightCapacity * lightingUseFactor * specialAllowanceFactor * clf
//    }

//    private fun powerLoad(
//        horsePowerRating: Double,
//        efficiencyFactor: Double,
//        coolingLoadFactor: Double
//    ): Double {
//        return horsePowerRating * efficiencyFactor * coolingLoadFactor
//    }

//    private fun miscellaneousSensibleLoad(
//        loadInput: Double,
//        usageFactor: Double,
//        radiationFactor: Double,
//        clf: Double
//    ): Double {
//        return loadInput * usageFactor * radiationFactor * clf
//    }

//    private fun miscellaneousLatentLoad(
//        loadInput: Double,
//        loadFactor: Double,
//        clf: Double
//    ): Double {
//        return loadInput * loadFactor * clf
//    }

    private fun roofCoolingLoad(): Double {
        var load = 0.0
        if (!binding.roofWidth.text.isNullOrEmpty() && !binding.roofLength.text.isNullOrEmpty()) {
            val u = mCalculatorFragmentViewModel.convertToThermalTransmittance(
                mCalculatorFragmentViewModel.mRoofType
            )
            mCalculatorFragmentViewModel.mRoofArea =
                binding.roofWidth.text.toString().toDouble() * binding.roofLength.text.toString()
                    .toDouble()
            val cltd =
                calculateCltdCorrected(5, stateTempDetail.meanTemp)
            load = coolingLoadRWC(u, cltd, mCalculatorFragmentViewModel.mRoofArea)
        }
        return load
    }

    private fun wallCoolingLoad(): Double {
        var load = 0.0
        if (!binding.wallWidthA.text.isNullOrEmpty() && !binding.wallHeightA.text.isNullOrEmpty()) {
            val orientation = binding.textViewOrientationWallA.text.toString()
            mCalculatorFragmentViewModel.mWallAOrientationA = orientation
            val u = mCalculatorFragmentViewModel.convertToThermalTransmittance(
                mCalculatorFragmentViewModel.mWallType
            )
            mCalculatorFragmentViewModel.mWallAreaA =
                binding.wallWidthA.text.toString().toDouble() * binding.wallHeightA.text.toString()
                    .toDouble()
            val cltdCorrected =
                calculateCltdCorrected(
                    selectCltdWall(orientation),
                    stateTempDetail.meanTemp
                )
            load = coolingLoadRWC(u, cltdCorrected, mCalculatorFragmentViewModel.mWallAreaA)
        }

        if (!binding.wallWidthB.text.isNullOrEmpty() && !binding.wallHeightB.text.isNullOrEmpty()) {
            val orientation = binding.textViewOrientationWallB.text.toString()
            mCalculatorFragmentViewModel.mWallAOrientationB = orientation
            val u = mCalculatorFragmentViewModel.convertToThermalTransmittance(
                mCalculatorFragmentViewModel.mWallType
            )
            mCalculatorFragmentViewModel.mWallAreaB =
                binding.wallWidthB.text.toString().toDouble() * binding.wallHeightB.text.toString()
                    .toDouble()
            val cltd =
                calculateCltdCorrected(
                    selectCltdWall(orientation),
                    stateTempDetail.meanTemp
                )
            load += coolingLoadRWC(u, cltd, mCalculatorFragmentViewModel.mWallAreaB)
        }

        if (!binding.wallWidthC.text.isNullOrEmpty() && !binding.wallHeightC.text.isNullOrEmpty()) {
            val orientation = binding.textViewOrientationWallC.text.toString()
            mCalculatorFragmentViewModel.mWallAOrientationC = orientation
            val u = mCalculatorFragmentViewModel.convertToThermalTransmittance(
                mCalculatorFragmentViewModel.mWallType
            )
            mCalculatorFragmentViewModel.mWallAreaC =
                binding.wallWidthC.text.toString().toDouble() * binding.wallHeightC.text.toString()
                    .toDouble()
            val cltd =
                calculateCltdCorrected(
                    selectCltdWall(orientation),
                    stateTempDetail.meanTemp,
                    stateTempDetail.dailyRangeTemp
                )
            load += coolingLoadRWC(u, cltd, mCalculatorFragmentViewModel.mWallAreaC)
        }

        if (!binding.wallWidthD.text.isNullOrEmpty() && !binding.wallHeightD.text.isNullOrEmpty()) {
            val orientation = binding.textViewOrientationWallD.text.toString()
            mCalculatorFragmentViewModel.mWallAOrientationD = orientation
            val u = mCalculatorFragmentViewModel.convertToThermalTransmittance(
                mCalculatorFragmentViewModel.mWallType
            )
            mCalculatorFragmentViewModel.mWallAreaD =
                binding.wallWidthD.text.toString().toDouble() * binding.wallHeightD.text.toString()
                    .toDouble()
            val cltd =
                calculateCltdCorrected(
                    selectCltdWall(orientation),
                    stateTempDetail.meanTemp
                )
            load += coolingLoadRWC(u, cltd, mCalculatorFragmentViewModel.mWallAreaD)
        }
        return load
    }

    private fun floorCoolingLoad(): Double {
        var load = 0.0
        if (!binding.floorWidth.text.isNullOrEmpty() && !binding.floorLength.text.isNullOrEmpty()) {
            mCalculatorFragmentViewModel.mFloorArea =
                binding.floorWidth.text.toString().toDouble() * binding.floorLength.text.toString()
                    .toDouble()
            load = coolingLoadPCF(
                mCalculatorFragmentViewModel.mTotalFloorTypeU,
                mCalculatorFragmentViewModel.mFloorArea,
                1.0,
                1.0
            )
        }
        return load
    }

    private fun doorLoad(): Double {
        var load = 0.0
        if (!binding.doorWidth.text.isNullOrEmpty() && !binding.doorHeight.text.isNullOrEmpty()) {
            val orientation = binding.textDoorOrientation.text.toString()
            mCalculatorFragmentViewModel.mDoorOrientation = orientation
            val u = 1.0 // constant
            mCalculatorFragmentViewModel.mDoorArea =
                binding.doorWidth.text.toString().toDouble() * binding.doorHeight.text.toString()
                    .toDouble()
            val cltd =
                calculateCltdCorrected(
                    selectCltdDoor(orientation),
                    stateTempDetail.meanTemp
                )
            load += coolingLoadRWC(u, cltd, mCalculatorFragmentViewModel.mDoorArea)
        }
        return load
    }

    private fun windowLoad(): Double {
        var load = 0.0
        if (!binding.windowWidth.text.isNullOrEmpty() && !binding.windowHeight.text.isNullOrEmpty() && !binding.numberOfWindow.text.isNullOrEmpty()) {
//            val orientation = binding.textDoorOrientation.text.toString()
//            mDoorOrientation = orientation
            val u = 1.0 // constant
            mCalculatorFragmentViewModel.mWindowArea = binding.windowWidth.text.toString()
                .toDouble() * binding.windowHeight.text.toString().toDouble()
            val cltd =
                calculateCltdCorrected(
                    43,
                    stateTempDetail.meanTemp
                )
            load += (coolingLoadRWC(
                u,
                cltd,
                mCalculatorFragmentViewModel.mWindowArea
            ) * binding.numberOfWindow.text.toString()
                .toInt())
        }
        return load
    }

    private fun ceilingLoad(): Double {
        var load = 0.0
        if (!binding.ceilingWidth.text.isNullOrEmpty() && !binding.ceilingLength.text.isNullOrEmpty()) {
            val u = mCalculatorFragmentViewModel.convertToThermalTransmittance(
                mCalculatorFragmentViewModel.mCeilingType
            )
            mCalculatorFragmentViewModel.mCeilingArea = binding.ceilingWidth.text.toString()
                .toDouble() * binding.ceilingLength.text.toString().toDouble()
            load = coolingLoadPCF(u, mCalculatorFragmentViewModel.mCeilingArea, 1.0, 1.0)
        }
        return load
    }
}