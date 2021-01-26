package com.example.getyourdrink.ingredients

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
import com.example.getyourdrink.api.Ingredient
import com.example.getyourdrink.databinding.FragmentIngredientsItemListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class IngredientDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class IngredientAdapter(val clickListener: IngredientListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(IngredientDiffCallback()) {


    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val ingredientItem = getItem(position) as DataItem.IngredientItem
                holder.bind(ingredientItem.ingredient, clickListener)
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

    fun addHeaderAndSubmitList(list: List<Ingredient>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.IngredientItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.IngredientItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view =
                    layoutInflater.inflate(R.layout.fragment_ingredients_header, parent, false)
                return TextViewHolder(view)
            }
        }
    }


    class ViewHolder private constructor(val binding: FragmentIngredientsItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val ingredientName: TextView = binding.ingredientName
        val ingredientImage: ImageView = binding.ingredientImage

        fun bind(item: Ingredient, clickListener: IngredientListener) {
            binding.ingredient = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding =
                    FragmentIngredientsItemListBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

}


class IngredientListener(val clickListener: (ingredient: Ingredient) -> Unit) {
    fun onClick(ingredient: Ingredient) = clickListener(ingredient)
}

sealed class DataItem {
    abstract val id: Long

    data class IngredientItem(val ingredient: Ingredient) : DataItem() {
        override val id = ingredient.id
    }

    object Header : DataItem() {
        override val id = Long.MIN_VALUE
    }
}