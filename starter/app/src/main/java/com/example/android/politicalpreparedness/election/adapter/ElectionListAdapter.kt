package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.ListItemElectionBinding
import com.example.android.politicalpreparedness.network.models.Election

class ElectionListAdapter(private val clickListener: ElectionListener) :
    ListAdapter<Election, ElectionListAdapter.ElectionViewHolder>(ElectionDiffCallback) {

    /**
     * List of elections that adapter will show
     */
    var elections: List<Election> = emptyList()
        set(value) {
            field = value
            // Notify any registered observers that the data set has changed.
            notifyDataSetChanged()
        }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ElectionViewHolder {
        val withDataBinding: ListItemElectionBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            ElectionViewHolder.LAYOUT,
            parent,
            false
        )

        return ElectionViewHolder(withDataBinding)
    }

    override fun getItemCount() = elections.size

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ElectionViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            clickListener.onClick(elections[position])
        }
        holder.binding.also {
            it.election = elections[position]
        }
    }

    /**
     * Custom listener that handles clicks on [RecyclerView] items.  Passes the [Election]
     * associated with the current item to the [onClick] function.
     * @param clickListener lambda that will be called with the current [Election]
     */
    class ElectionListener(val clickListener: (election: Election) -> Unit) {
        fun onClick(election: Election) = clickListener(election)
    }

    /**
     * The ElectionViewHolder constructor takes the binding variable from the associated
     * ListViewItem, which nicely gives it access to the full [Election] information.
     */
    class ElectionViewHolder(val binding: ListItemElectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.list_item_election
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [Election]
     * has been updated.
     */
    companion object ElectionDiffCallback : DiffUtil.ItemCallback<Election>() {
        override fun areItemsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Election, newItem: Election): Boolean {
            return oldItem.name == newItem.name
        }
    }
}





