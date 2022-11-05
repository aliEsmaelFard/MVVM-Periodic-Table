package com.alief.periodictable.ui.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.alief.periodictable.R
import com.alief.periodictable.model.Element
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailBottomSheet(val element: Element): BottomSheetDialogFragment()
{
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.detail_bottom_sheet, container, false)

        val symbol: TextView = view.findViewById(R.id.item_symbol)
        val name: TextView = view.findViewById(R.id.item_name)
        val atomicNumber: TextView = view.findViewById(R.id.item_atomic_num)
        val state: TextView = view.findViewById(R.id.item_state)
        val cardView: View = view.findViewById(R.id.item_layout)
        val electorConfigTV: TextView = view.findViewById(R.id.sheet_config)
        val groupTV: TextView = view.findViewById(R.id.sheet_group)
        val periodTV: TextView = view.findViewById(R.id.sheet_period)
        val ionRadiosTV: TextView = view.findViewById(R.id.sheet_ion_radios)


        view.apply {
            atomicNumber.text = element.atomicNumber.toString()
            state.text = element.standardState
            name.text = element.name
            symbol.text = element.symbol

            if (element.cpkHexColor == "unknown" || element.cpkHexColor == "0" || element.cpkHexColor.length < 6)
            {
                cardView.setBackgroundColor(Color.parseColor("#A9D5D4"))
            }
            else {
                cardView.setBackgroundColor(Color.parseColor("#${element.cpkHexColor}"))
            }
        }

        electorConfigTV.text = "Electronic Configuration: ${element.electronicConfiguration}"
        groupTV.text = "Group: ${element.group}"
        periodTV.text = "Period: ${element.period}"
        ionRadiosTV.text = "Ion Radios: ${element.ionRadius}"

        return view
    }
}