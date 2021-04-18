/**
 * Created by Taiwo Farinu on 18-Apr-21
 */

package com.android.petprog.coolingloadcalc.viewmodel

import androidx.lifecycle.ViewModel
import com.android.petprog.coolingloadcalc.model.StateTempDetail

class CoolingLoadCalculatorFragmentViewModel : ViewModel() {
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

    var mSpacePurpose = ""
    var stateName = ""

    var mRoofType = ""
    var mRoofArea = 0.0
    var mRoofLoad = 0.0

    var mWallType = ""
    var mWallLoad = 0.0
    var mWallAreaA = 0.0
    var mWallAreaB = 0.0
    var mWallAreaC = 0.0
    var mWallAreaD = 0.0
    var mWallAOrientationA = ""
    var mWallAOrientationB = ""
    var mWallAOrientationC = ""
    var mWallAOrientationD = ""

    var mFloorType = ""
    var mFloorArea = 0.0
    var mFloorLoad = 0.0
    var mTotalFloorTypeU = 0.0

    var mWindowType = ""
    var mWindowArea = 0.0
    var mWindowLoad = 0.0

    var mDoorArea = 0.0
    var mDoorLoad = 0.0
    var mDoorOrientation = ""

    var mCeilingType = ""
    var mCeilingArea = 0.0
    var mCeilingLoad = 0.0

    var mListOfEquipments = ""
    var mTotalEquipmentLoad = 0.0

    var mNumberOfPeople = 0
    var mPeopleLoad = 0.0

    var requiredTemperature = 0


    val mapCltdDoor = mapOf(
        "N" to 24,
        "NE" to 25,
        "E" to 29,
        "SE" to 30,
        "S" to 37,
        "SW" to 63,
        "W" to 67,
        "NW" to 47
    )

     val mapCltdWall = mapOf(
        "N" to 13,
        "NE" to 24,
        "E" to 33,
        "SE" to 32,
        "S" to 24,
        "SW" to 21,
        "W" to 18,
        "NW" to 14
    )

    // U
    fun convertToThermalTransmittance(material: String): Double {
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
            "Aluminium" -> 17.0
            "Stone coated" -> 12.9
            "Slate" -> 114.0
            "Fibre" -> 10.0
            "Pitched roof with felt" -> 0.6

            // Wall
            "Hollow Block" -> 1.7
            "Clay file" -> 3.07
            "Bricks" -> 2.2

            // Window
            "Wooding frame" -> 1.7
            "Metal frame" -> 5.8

            else -> 1.0
        }
    }


    fun getHeatGainByPeople(spacePurpose: String): Int {
        return when (spacePurpose) {
            "Auditorium", "Corridor" -> 130
            "Gallery", "Photo studio" -> 160
            "Bar", "Cafeteria", "Coffee station", "Restaurant" -> 145
            "Bedroom", "Break room" -> 130
            "Barber Shop", "Classroom" -> 140
            "Computer lab", "Library" -> 160
            "Courtroom" -> 140
            "Dining room" -> 145
            "Dance floor" -> 265
            "Equipment room" -> 295
            "Gym" -> 585
            "Museum" -> 160
            "Office space" -> 200
            "Pharmacy" -> 160
            "Pet shop" -> 140
            "Reception area" -> 130
            "Supermarket" -> 160
            "Science lab" -> 140
            "Stage" -> 115
            "Swimming pool" -> 140
            "Warehouse" -> 440
            else -> 140
        }
    }

    fun getEquipment(equipment: String): Int {

        return when (equipment) {
            "Blender" -> 470
            "Coffee Maker" -> 1660
            "Computer" -> 120
            "Electric cooker without oven" -> 1320
            "Food Warmer" -> 250
            "Fryer" -> 250
            "Gas Cooker with oven" -> 800
            "Hair Dryer" -> 450
            "Hot Plate (Double Burner)" -> 1830
            "Hot Plate (Single Burner)" -> 1040
            "Lamp" -> 30
            "Microwave" -> 2630
            "Refrigerator (Small)" -> 690
            "Refrigerator (Large)" -> 310
            "Steam Cooker" -> 8120
            "Steam Kettle" -> 35
            "Toaster" -> 1510
            "TV" -> 250
            else -> 40
        }
    }

    fun selectStateDetails(state: String): StateTempDetail {
        var stateTempDetails: StateTempDetail = allStatesTempDetails[0]
        for (sd in allStatesTempDetails) {
            if (sd.stateName == state) {
                stateTempDetails = sd
            }
        }
        return stateTempDetails
    }
}