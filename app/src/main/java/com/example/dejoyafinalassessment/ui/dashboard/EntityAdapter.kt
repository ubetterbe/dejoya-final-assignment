package com.example.dejoyafinalassessment.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dejoyafinalassessment.R
import com.example.dejoyafinalassessment.data.model.Entity
import com.example.dejoyafinalassessment.databinding.ItemEntityBinding

class EntityAdapter(
    private val onItemClick: (Entity) -> Unit
) : RecyclerView.Adapter<EntityAdapter.EntityViewHolder>() {

    private var entities: List<Entity> = emptyList()

    fun submitList(newEntities: List<Entity>) {
        entities = newEntities
        // list only ever comes from one dashboard load, not worth wiring up
        // DiffUtil for a handful of items that just get set once
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntityViewHolder {
        val binding = ItemEntityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntityViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EntityViewHolder, position: Int) {
        holder.bind(entities[position])
    }

    override fun getItemCount(): Int = entities.size

    inner class EntityViewHolder(private val binding: ItemEntityBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(entity: Entity) {
            val context = binding.root.context
            binding.textSportName.text = entity.sportName
            binding.textFieldType.text = entity.fieldType
            binding.textPlayerCount.text = context.getString(
                R.string.dashboard_players_format,
                entity.playerCount
            )

            // brief only wants the summary card to say whether it's an Olympic
            // sport, not description - description is what's saved for Details
            if (entity.olympicSport) {
                binding.textOlympicStatus.text = context.getString(R.string.dashboard_olympic_sport)
                binding.textOlympicStatus.setTextColor(ContextCompat.getColor(context, R.color.olympic_yes))
            } else {
                binding.textOlympicStatus.text = context.getString(R.string.dashboard_not_olympic_sport)
                binding.textOlympicStatus.setTextColor(
                    ContextCompat.getColor(context, android.R.color.darker_gray)
                )
            }

            binding.root.setOnClickListener { onItemClick(entity) }
        }
    }
}
