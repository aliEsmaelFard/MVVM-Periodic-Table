package com.alief.periodictable.adaptor

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.alief.periodictable.R
import com.alief.periodictable.model.Element

class MyAdaptor(val listener: OnElementClickListener) :
    RecyclerView.Adapter<MyAdaptor.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Element>() {
        override fun areItemsTheSame(oldItem: Element, newItem: Element): Boolean {
            return oldItem.atomicNumber == newItem.atomicNumber
        }

        override fun areContentsTheSame(oldItem: Element, newItem: Element): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.elements_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val element = differ.currentList[position]

        holder.apply {
            atomicNumber.text = element.atomicNumber.toString()
            state.text = element.standardState
            name.text = element.name
            symbol.text = element.symbol

            if (element.cpkHexColor == "unknown" || element.cpkHexColor == "0" || element.cpkHexColor.length < 6)
            {
            cardView.setCardBackgroundColor(Color.parseColor("#A9D5D4"))
            }
            else {
                cardView.setCardBackgroundColor(Color.parseColor("#${element.cpkHexColor}"))
            }
        }


    }

    override fun getItemCount() = differ.currentList.size


    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val symbol: TextView = itemView.findViewById(R.id.item_symbol)
        val name: TextView = itemView.findViewById(R.id.item_name)
        val atomicNumber: TextView = itemView.findViewById(R.id.item_atomic_num)
        val state: TextView = itemView.findViewById(R.id.item_state)
        var cardView: CardView = itemView.findViewById(R.id.cardview)
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = differ.currentList[bindingAdapterPosition]
            listener.onClicked(position)
        }


    }
}

interface OnElementClickListener {
    fun onClicked(element: Element)
}
