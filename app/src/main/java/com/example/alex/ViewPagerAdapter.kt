package com.example.alex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(private val items: List<OnboardingItem>) :
    RecyclerView.Adapter<ViewPagerAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivSlideImage)
        val tvTitle: TextView = itemView.findViewById(R.id.tvSlideTitle)
        val tvDesc: TextView = itemView.findViewById(R.id.tvSlideDesc)

        fun bind(item: OnboardingItem) {
            ivImage.setImageResource(item.imageRes)
            tvTitle.text = item.title
            tvDesc.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_slide, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}