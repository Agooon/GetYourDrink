package com.example.getyourdrink.drinks

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.getyourdrink.R
import com.example.getyourdrink.api.Drink
import com.example.getyourdrink.databinding.FragmentDrinksItemListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class DrinkDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class DrinksAdapter(val clickListener: DrinkListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(DrinkDiffCallback()) {


    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val ingredientItem = getItem(position) as DataItem.DrinkItem
                holder.bind(ingredientItem.drink, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    fun addHeaderAndSubmitList(list: List<Drink>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.DrinkItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.DrinkItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.fragment_drinks_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }


    class ViewHolder private constructor(val binding: FragmentDrinksItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ingredientName: TextView = binding.drinkName
        val ingredientImage: ImageView = binding.drinkImage

        fun bind(item: Drink, clickListener: DrinkListener) {
            binding.drink = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding = FragmentDrinksItemListBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}


class DrinkListener(val clickListener: (drink: Drink) -> Unit) {
    fun onClick(drink: Drink) = clickListener(drink)
}

sealed class DataItem {
    abstract val id: Long

    data class DrinkItem(val drink: Drink) : DataItem() {
        override val id = drink.id
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }
}