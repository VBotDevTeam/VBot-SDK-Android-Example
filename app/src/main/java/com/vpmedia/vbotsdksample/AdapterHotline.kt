package com.vpmedia.vbotsdksample

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vpmedia.sdkvbot.domain.pojo.mo.Hotline
import com.vpmedia.vbotsdksample.databinding.ItemHotlineBinding


class AdapterHotline(private var mutableList: MutableList<Hotline>) :
    RecyclerView.Adapter<AdapterHotline.ViewHolder>() {
    private lateinit var listener: AdapterContactListener

    interface AdapterContactListener {
        fun onclickHotline(hotline: Hotline)
    }

    fun setListener(listener: AdapterContactListener) {
        this.listener = listener
    }

    fun setData(list: List<Hotline>) {
        this.mutableList.clear()
        this.mutableList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemHotlineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.bind(mutableList[position])
            holder.itemView.setOnClickListener {
                listener.onclickHotline(mutableList[position])
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }


    inner class ViewHolder(private var viewBinding: ItemHotlineBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: Hotline) = with(itemView) {
            try {
                viewBinding.tvNumberHotline.text = model.name
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}