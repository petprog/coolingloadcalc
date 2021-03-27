package com.android.petprog.coolingloadcalc

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
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.petprog.coolingloadcalc.databinding.FragmentCoolingLoadCalculatorBinding
import com.android.petprog.coolingloadcalc.model.CalculatedData
import com.android.petprog.coolingloadcalc.model.StateTempDetail
import com.android.petprog.coolingloadcalc.util.convertFahrenheitToKelvin

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CoolingLoadCalculatorFragment : Fragment(), AdapterView.OnItemSelectedListener,
    SensorEventListener {

    private val allStatesTempDetails = listOf(
        StateTempDetail("Abuja", 89, 24),
        StateTempDetail("Abia", 82, 20),
        StateTempDetail("Adamawa", 103, 40),
        StateTempDetail("Akwa Ibom", 90, 18),
        StateTempDetail("Anambra", 90, 18),
        StateTempDetail("Bauchi", 100, 43),
        StateTempDetail("Bayelsa", 90, 16),
        StateTempDetail("Borno", 106, 48),
        StateTempDetail("Cross River", 96, 28),
        StateTempDetail("Delta", 88, 19),
        StateTempDetail("Ekiti", 90, 26),
        StateTempDetail("Ebonyi", 89, 24),
        StateTempDetail("Edo", 88, 21),
        StateTempDetail("Enugu", 88, 23),
        StateTempDetail("Gombe", 100, 43),
        StateTempDetail("Imo", 87, 20),
        StateTempDetail("Jigawa", 103, 49),
        StateTempDetail("Kaduna", 95, 45),
        StateTempDetail("Kano", 101, 49),
        StateTempDetail("Katsina", 101, 43),
        StateTempDetail("Kebbi", 104, 39),
        StateTempDetail("Kogi", 96, 30),
        StateTempDetail("Kwara", 95, 30),
        StateTempDetail("Lagos", 91, 12),
        StateTempDetail("Nasarawa", 95, 32),
        StateTempDetail("Niger", 94, 34),
        StateTempDetail("Ogun", 93, 22),
        StateTempDetail("Ondo", 82, 12),
        StateTempDetail("Osun", 93, 28),
        StateTempDetail("Oyo", 93, 25),
        StateTempDetail("Plateau", 92, 39),
        StateTempDetail("Rivers", 92, 16),
        StateTempDetail("Sokoto", 106, 48),
        StateTempDetail("Taraba", 99, 38),
        StateTempDetail("Yobe", 106, 48),
        StateTempDetail("Zamfara", 100, 42)

    )

    private val mapCltdDoor = mapOf(
        "N" to 24,
        "NE" to 25,
        "E" to 29,
        "SE" to 30,
        "S" to 37,
        "SW" to 63,
        "W" to 67,
        "NW" to 47
    )

    private val mapCltdWall = mapOf(
        "N" to 13,
        "NE" to 24,
        "E" to 33,
        "SE" to 32,
        "S" to 24,
        "SW" to 21,
        "W" to 18,
        "NW" to 14
    )

    private lateinit var binding: FragmentCoolingLoadCalculatorBinding

    private var mSensorManager: SensorManager? = null
    private var currentDegree = 0

    private var spaceArea = 0.0
    private var mSpacePurpose = ""
    private var stateName = ""

    private var mRoofType = ""
    private var mRoofArea = 0.0
    private var mRoofLoad = 0.0

    private var mWallType = ""
    private var mWallLoad = 0.0
    private var mWallAreaA = 0.0
    private var mWallAreaB = 0.0
    private var mWallAreaC = 0.0
    private var mWallAreaD = 0.0
    private var mWallAOrientationA = ""
    private var mWallAOrientationB = ""
    private var mWallAOrientationC = ""
    private var mWallAOrientationD = ""

    private var mFloorType = ""
    private var mFloorArea = 0.0
    private var mFloorLoad = 0.0
    private var mTotalFloorTypeU = 0.0

    private var mWindowType = ""
    private var mWindowArea = 0.0
    private var mWindowLoad = 0.0

    private var mDoorType = ""
    private var mDoorArea = 0.0
    private var mDoorLoad = 0.0
    private var mDoorOrientation = ""

    private var mCeilingType = ""
    private var mCeilingArea = 0.0
    private var mCeilingLoad = 0.0

    private var mListOfEquipments = ""
    var mTotalEquipmentLoad = 0.0

    lateinit var stateTempDetail: StateTempDetail


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        initData()
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_cooling_load_calculator,
            container,
            false
        )

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

//        ArrayAdapter.createFromResource(
//            requireContext(),
//            R.array.floor_types,
//            android.R.layout.simple_spinner_item
//        ).also { adapter ->
//            // Apply the adapter to the spinner
//            binding.spinnerFloorType.adapter = adapter
//        }

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
        binding.spinnerCeilingType.onItemSelectedListener = this

//        binding.submitButton.setOnClickListener {
//            Toast.makeText(requireContext(), totalWallHeat().toString(), Toast.LENGTH_LONG).show()
//        }

        binding.buttonAddEquipment.setOnClickListener {

            val builder = AlertDialog.Builder(requireActivity())
            // String array for alert dialog multi choice items
            var totalU = 0.0
            val equipments = resources.getStringArray(R.array.equipments)
            val selectedEquipments = mutableListOf<String>()
            //setTitle
            builder.setTitle(R.string.add_equipment_text)
            //set multi-choice
            builder.setMultiChoiceItems(equipments, null) { dialog, which, isChecked ->
                if (isChecked) {
                    selectedEquipments.add(equipments[which])
                } else if (selectedEquipments.contains(equipments[which])) {
                    selectedEquipments.remove(equipments[which])
                }
            }
            builder.setPositiveButton("OK") { dialog, which ->
                for (equipment in selectedEquipments) {
                    totalU += getEquipment(equipment)
                    mTotalEquipmentLoad = totalU
                }
//                Toast.makeText(
//                    requireContext(),
//                    "totalEquipmentLoad $mTotalEquipmentLoad",
//                    Toast.LENGTH_SHORT
//                ).show()
                mListOfEquipments = selectedEquipments.toList().toString()
            }
            builder.setNeutralButton("Cancel") { dialog, which ->
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
            builder.setMultiChoiceItems(floorTypes, null) { dialog, which, isChecked ->
                if (isChecked) {
                    floorTypesSelected.add(floorTypes[which])
                } else if (floorTypesSelected.contains(floorTypes[which])) {
                    floorTypesSelected.remove(floorTypes[which])
                }
            }
            // Set the positive/yes button click listener
            builder.setPositiveButton("OK") { dialog, which ->
                for (floorType in floorTypesSelected) {
                    totalU += convertToThermalTransmittance(floorType)
                    mTotalFloorTypeU = totalU
                }
//                Toast.makeText(
//                    requireContext(),
//                    "floorTypesU $mTotalFloorTypeU",
//                    Toast.LENGTH_SHORT
//                ).show()
                mFloorType = floorTypesSelected.toList().toString()
            }
            // Set the neutral/cancel button click listener
            builder.setNeutralButton("Cancel") { dialog, which ->
                // Do something when click the neutral button
            }
            val dialog = builder.create()
            // Display the alert dialog on interface
            dialog.show()
        }

        return binding.root
    }

    private fun getEquipment(equipment: String): Double {
        return 0.0
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.getOrientationAButton.setOnClickListener {
            binding.textViewOrientationWallA.text = getOrientation(currentDegree)
        }

        binding.getOrientationBButton.setOnClickListener {
            binding.textViewOrientationWallB.text = getOrientation(currentDegree)
        }

        binding.getOrientationCButton.setOnClickListener {
            binding.textViewOrientationWallC.text = getOrientation(currentDegree)
        }

        binding.getOrientationDButton.setOnClickListener {
            binding.textViewOrientationWallD.text = getOrientation(currentDegree)
        }

        binding.getDoorOrientationButton.setOnClickListener {
            binding.textViewOrientationDoor.text = getOrientation(currentDegree)
        }


        binding.calculateButton.setOnClickListener {
            var spaceArea = 0.0
            mRoofLoad = roofCoolingLoad()
            mWallLoad = wallCoolingLoad()
            mFloorLoad = floorCoolingLoad()
            mWindowArea = windowLoad()
            mDoorLoad = doorLoad()
            mCeilingLoad = ceilingLoad()

            if (binding.spaceArea.text!!.isNotEmpty()) {
                spaceArea = binding.spaceArea.text.toString().toDouble()
            }

            val data = CalculatedData(
                stateName,
                spaceArea,
                mSpacePurpose,
                mRoofType,
                mRoofArea,
                mRoofLoad,
                mWallType,
                mWallAreaA,
                mWallAreaB,
                mWallAreaC,
                mWallAreaD,
                mWallAOrientationA,
                mWallAOrientationB,
                mWallAOrientationC,
                mWallAOrientationD,
                mWallLoad,
                mFloorType,
                mFloorArea,
                mFloorLoad,
                mWindowType,
                mWindowArea,
                mWindowLoad,
                mDoorArea,
                mDoorOrientation,
                mDoorLoad,
                mCeilingType,
                mCeilingArea,
                mCeilingLoad,
                mListOfEquipments
            )

//            Toast.makeText(requireContext(), data.description, Toast.LENGTH_LONG).show()
            val direction =
                CoolingLoadCalculatorFragmentDirections.actionCoolingLoadCalculatorFragmentToDetailFragment(
                    data
                )
            findNavController().navigate(direction)
        }


    }

    override fun onStart() {
        super.onStart()

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

        val orientations = resources.getStringArray(R.array.orientations)
        val purpose = resources.getStringArray(R.array.space_purposes)
        val states = resources.getStringArray(R.array.states)

        val wallTypes = resources.getStringArray(R.array.wall_types)
        val roofTypes = resources.getStringArray(R.array.roof_types)
        val floorTypes = resources.getStringArray(R.array.floor_types)
        val windowTypes = resources.getStringArray(R.array.window_types)
        val ceilingTypes = resources.getStringArray(R.array.ceiling_types)

        when (parent?.id) {
//            R.id.spinnerOrientationWallA -> mWallAOrientationA = orientations[position]
//            R.id.spinnerOrientationWallB -> mWallAOrientationA = orientations[position]
//            R.id.spinnerOrientationWallC -> wallAOrientationC = orientations[position]
//            R.id.spinnerOrientationWallD -> wallAOrientationD = orientations[position]

            R.id.spinnerWallType -> mWallType = wallTypes[position]
//            R.id.spinnerFloorType -> floorType = floorTypes[position]
            R.id.spinnerRoofType -> {
                mRoofType = roofTypes[position]
            }
            R.id.spinnerWindowType -> mWindowType = windowTypes[position]
            R.id.spinnerCeilingType -> mCeilingType = ceilingTypes[position]

            R.id.spinnerState -> {
                stateName = states[position]
                stateTempDetail = selectStateDetails(states[position])
            }

            R.id.spinnerSpacePurpose -> {
                mSpacePurpose = purpose[position]
            }
        }
//
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        val degree = Math.round(event?.values?.get(0)!!)
        currentDegree = degree
    }

    private fun selectCltdWall(orientation: String): Int {
        var cltd = 0
        if (mapCltdWall.containsKey(orientation))
            cltd = mapCltdWall[orientation]!!
        return cltd
    }

    private fun selectCltdDoor(orientation: String): Int {
        var cltd = 0
        if (mapCltdDoor.containsKey(orientation))
            cltd = mapCltdDoor[orientation]!!
        return cltd
    }

    // U
    private fun convertToThermalTransmittance(material: String): Double {
        return when (material) {
            // Floor
            "Rug" -> 4.6
            "Tile" -> 113.6
            "Terazzo" -> 71.0
            "Wood" -> 8.35
            "Concrete" -> 0.8
            "Decking" -> 1.7

            // Roofs
            "Asbestos" -> 27.0
            "Aluminium Roofing Sheet" -> 17.0
            "Slate roofing" -> 114.0
            "Pitched roof with felt" -> 0.6

            // Wall
            "Hollow Block walls" -> 1.7
            "Clay file walls" -> 3.07
            "Bricks walls" -> 2.2

            // Window
            "Wooding frame" -> 1.7
            "Metal frame" -> 5.8

            else -> 0.0
        }
    }


    private fun selectStateDetails(state: String): StateTempDetail {
        var stateTempDetails: StateTempDetail = allStatesTempDetails[0]
        for (sd in allStatesTempDetails) {
            if (sd.stateName == state) {
                stateTempDetails = sd
            }
        }
        return stateTempDetails
    }

    private fun coolingLoadRWC(
        thermalTransmittance: Double,
        cltdCorrected: Double,
        area: Double
    ): Double {

        return thermalTransmittance * cltdCorrected * area
    }

    private fun calculateCltdCorrected(cltd: Int, meanTemp: Double, dailyRange: Int): Double {
        return (cltd + (78 - dailyRange) + (meanTemp - 85)).convertFahrenheitToKelvin()
    }

    private fun solarLoadThroughGlass(area: Double, sc: Double, scl: Double): Double {
        return area * sc * scl
    }

    private fun coolingLoadPCF(
        thermalTransmittance: Double,
        area: Double,
        tempAdjacentSpace: Double,
        tempInsideDesign: Double
    ): Double {
        return thermalTransmittance * area * (tempAdjacentSpace - tempInsideDesign)
    }

    private fun peopleLoad(n: Double, shg: Double, ccf: Double): Double {
        return n * shg * ccf
    }

    private fun lightingLoad(
        lightCapacity: Double,
        lightingUseFactor: Double,
        specialAllowanceFactor: Double,
        clf: Double
    ): Double {
        return lightCapacity * lightingUseFactor * specialAllowanceFactor * clf
    }

    private fun powerLoad(
        horsePowerRating: Double,
        efficiencyFactor: Double,
        coolingLoadFactor: Double
    ): Double {
        return horsePowerRating * efficiencyFactor * coolingLoadFactor
    }

    private fun miscellaneousSensibleLoad(
        loadInput: Double,
        usageFactor: Double,
        radiationFactor: Double,
        clf: Double
    ): Double {
        return loadInput * usageFactor * radiationFactor * clf
    }

    private fun miscellaneousLatentLoad(
        loadInput: Double,
        loadFactor: Double,
        clf: Double
    ): Double {
        return loadInput * loadFactor * clf
    }

    private fun roofCoolingLoad(): Double {
        var load = 0.0
        if (!binding.roofArea.text.isNullOrEmpty()) {
            val u = convertToThermalTransmittance(mRoofType)
            mRoofArea = binding.roofArea.text.toString().toDouble()
            val cltd =
                calculateCltdCorrected(0, stateTempDetail.meanTemp, stateTempDetail.dailyRangeTemp)
            load = coolingLoadRWC(u, cltd, mRoofArea)
        }
        return load
    }

    private fun wallCoolingLoad(): Double {
        var load = 0.0
        if (!binding.wallAreaA.text.isNullOrEmpty()) {
            val orientation = binding.textViewOrientationWallA.text.toString()
            mWallAOrientationA = orientation
            val u = convertToThermalTransmittance(mWallType)
            mWallAreaA = binding.wallAreaA.text.toString().toDouble()
            val cltdCorrected =
                calculateCltdCorrected(
                    selectCltdWall(orientation),
                    stateTempDetail.meanTemp,
                    stateTempDetail.dailyRangeTemp
                )
            load = coolingLoadRWC(u, cltdCorrected, mWallAreaA)
        }

        if (!binding.wallAreaB.text.isNullOrEmpty()) {
            val orientation = binding.textViewOrientationWallB.text.toString()
            mWallAOrientationB = orientation
            val u = convertToThermalTransmittance(mWallType)
            mWallAreaB = binding.wallAreaB.text.toString().toDouble()
            val cltd =
                calculateCltdCorrected(
                    selectCltdWall(orientation),
                    stateTempDetail.meanTemp,
                    stateTempDetail.dailyRangeTemp
                )
            load += coolingLoadRWC(u, cltd, mWallAreaB)
        }

        if (!binding.wallAreaC.text.isNullOrEmpty()) {
            val orientation = binding.textViewOrientationWallC.text.toString()
            mWallAOrientationC = orientation
            val u = convertToThermalTransmittance(mWallType)
            mWallAreaC = binding.wallAreaC.text.toString().toDouble()
            val cltd =
                calculateCltdCorrected(
                    selectCltdWall(orientation),
                    stateTempDetail.meanTemp,
                    stateTempDetail.dailyRangeTemp
                )
            load += coolingLoadRWC(u, cltd, mWallAreaC)
        }

        if (!binding.wallAreaD.text.isNullOrEmpty()) {
            val orientation = binding.textViewOrientationWallD.text.toString()
            mWallAOrientationD = orientation
            val u = convertToThermalTransmittance(mWallType)
            mWallAreaD = binding.wallAreaD.text.toString().toDouble()
            val cltd =
                calculateCltdCorrected(
                    selectCltdWall(orientation),
                    stateTempDetail.meanTemp,
                    stateTempDetail.dailyRangeTemp
                )
            load += coolingLoadRWC(u, cltd, mWallAreaD)
        }
        return load
    }

    private fun floorCoolingLoad(): Double {
        var load = 0.0
        if (!binding.floorArea.text.isNullOrEmpty()) {
            mFloorArea = binding.floorArea.text.toString().toDouble()
            load = coolingLoadPCF(mTotalFloorTypeU, mFloorArea, 1.0, 1.0)
        }
        return load
    }

    private fun doorLoad(): Double {
        var load = 0.0
        if (!binding.doorArea.text.isNullOrEmpty()) {
            val orientation = binding.textDoorOrientation.text.toString()
            mDoorOrientation = orientation
            val u = 1.0 // constant
            mDoorArea = binding.doorArea.text.toString().toDouble()
            val cltd =
                calculateCltdCorrected(
                    selectCltdDoor(orientation),
                    stateTempDetail.meanTemp,
                    stateTempDetail.dailyRangeTemp
                )
            load += coolingLoadRWC(u, cltd, mDoorArea)
        }
        return load
    }

    private fun windowLoad(): Double {
        var load = 0.0
        if (!binding.windowArea.text.isNullOrEmpty()) {
//            val orientation = binding.textDoorOrientation.text.toString()
//            mDoorOrientation = orientation
            val u = 1.0 // constant
            mWindowArea = binding.windowArea.text.toString().toDouble()
            val cltd =
                calculateCltdCorrected(
                    43,
                    stateTempDetail.meanTemp,
                    stateTempDetail.dailyRangeTemp
                )
            load += coolingLoadRWC(u, cltd, mWindowArea)
        }
        return load
    }

    private fun ceilingLoad(): Double {
        var load = 0.0
        if (!binding.ceilingArea.text.isNullOrEmpty()) {
            val u = convertToThermalTransmittance(mCeilingType)
            mCeilingArea = binding.ceilingArea.text.toString().toDouble()
            load = coolingLoadPCF(u, mCeilingArea, 1.0, 1.0)
        }
        return load
    }

    /**
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

    lengthWallA = binding.textInputEditTextWallLengthA.text.toString().toDouble()

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
    ) */
}