package com.android.petprog.coolingloadcalc.view.fragment

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.petprog.coolingloadcalc.R
import com.android.petprog.coolingloadcalc.databinding.FragmentDetailBinding
import com.android.petprog.coolingloadcalc.util.convertFromBtuPerHrToHp
import com.android.petprog.coolingloadcalc.util.convertFromBtuPerHrToTon
import com.android.petprog.coolingloadcalc.util.convertFromKwToBtuPerHr
import com.android.petprog.coolingloadcalc.util.toOneDecimal
import com.android.petprog.coolingloadcalc.viewmodel.CalculatedDataViewModel

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    private lateinit var mCalculatedDataViewModel: CalculatedDataViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        )

        mCalculatedDataViewModel = ViewModelProvider(this).get(CalculatedDataViewModel::class.java)

        val args = DetailFragmentArgs.fromBundle(requireArguments())
        binding.detailLocationTextView.text = args.data.stateName
        binding.detailSpaceDimensionTextView.text = args.data.spaceArea.toString()
        binding.detailUsageTypeTextView.text = args.data.purposeOfSpace

        binding.detailRoofTypeTextView.text = args.data.roofType
        binding.detailRoofAreaTextView.text = args.data.roofArea.toString()
        binding.detailRoofLoadTextView.text = args.data.roofLoad.toString()

        binding.detailWallTypeTextView.text = args.data.wallType
        binding.detailWallATextView.text =
            wallDescription(args.data.wallAArea.toString(), args.data.wallAOrientation)
        binding.detailWallBTextView.text =
            wallDescription(args.data.wallBArea.toString(), args.data.wallBOrientation)
        binding.detailWallCTextView.text =
            wallDescription(args.data.wallCArea.toString(), args.data.wallCOrientation)
        binding.detailWallDTextView.text =
            wallDescription(args.data.wallDArea.toString(), args.data.wallDOrientation)
        binding.detailWallLoadTextView.text = args.data.wallLoad.toString()

        binding.detailFloorTypeTextView.text = args.data.floorType
        binding.detailFloorAreaTextView.text = args.data.floorArea.toString()
        binding.detailFloorLoadTextView.text = args.data.floorLoad.toString()

        binding.detailWindowTypeTextView.text = args.data.windowType
        binding.detailWindowAreaTextView.text = args.data.windowArea.toString()
        binding.detailWindowLoadTextView.text = args.data.windowLoad.toString()

        binding.detailDoorOrientationTextView.text = args.data.doorOrientation
        binding.detailDoorAreaTextView.text = args.data.doorArea.toString()
        binding.detailDoorLoadTextView.text = args.data.doorLoad.toString()

        binding.detailCeilingTypeTextView.text = args.data.ceilingType
        binding.detailCeilingAreaTextView.text = args.data.ceilingArea.toString()
        binding.detailCeilingLoadTextView.text = args.data.ceilingLoad.toString()
        binding.detailListOfEquipmentsTextView.text = args.data.listOfEquipments
        binding.detailPeopleLoadTextView.text = args.data.peopleLoad.toString()
        binding.detailEquipmentsLoadTextView.text = args.data.equipmentLoad.toString()
        binding.detailNumberOfPeopleTextView.text = args.data.numberOfPeople.toString()

        binding.detailLoadInBtuPerHr.text =
            args.data.totalLoad.convertFromKwToBtuPerHr().toOneDecimal().toString()
        binding.detailLoadInKw.text = args.data.totalLoad.toString()
        binding.detailLoadInHp.text =
            args.data.totalLoad.convertFromKwToBtuPerHr().convertFromBtuPerHrToHp().toOneDecimal()
                .toString()
        binding.detailLoadInTon.text =
            args.data.totalLoad.convertFromKwToBtuPerHr().convertFromBtuPerHrToTon().toOneDecimal()
                .toString()

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun wallDescription(area: String, orientation: String): String {
        return "Area: $area ${getString(R.string.m_square_text)}  |  Orientation: $orientation"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        view.findViewById<Button>(R.id.button_second).setOnClickListener {
//            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_save, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                val args = DetailFragmentArgs.fromBundle(requireArguments())
                mCalculatedDataViewModel.addCalculatedData(args.data)
                val direction =
                    DetailFragmentDirections.actionDetailFragmentToCalculatedDataListFragment()
                findNavController().navigate(direction)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }
}