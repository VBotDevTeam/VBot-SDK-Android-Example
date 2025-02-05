package com.vpmedia.vbotsdksample.view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vpmedia.vbotsdksample.R
import com.vpmedia.vbotsdksample.databinding.ItemHotlineBinding
import com.vpmedia.vbotsdksample.model.SDKMember

class AdapterAccount(private var mutableList: MutableList<SDKMember>) :
    RecyclerView.Adapter<AdapterAccount.ViewHolder>() {
    private lateinit var listener: AdapterContactListener

    interface AdapterContactListener {
        fun onclickLogin(model: SDKMember)
    }

    fun setListener(listener: AdapterContactListener) {
        this.listener = listener
    }

    fun setData(list: List<SDKMember>) {
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
                listener.onclickLogin(mutableList[position])
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
        fun bind(model: SDKMember) = with(itemView) {
            try {
                viewBinding.tvName.text = model.name
                when (model.type) {
                    "ios" -> {
                        viewBinding.ivType.setImageResource(R.drawable.ic_va_ios)
                    }

                    "android" -> {
                        viewBinding.ivType.setImageResource(R.drawable.ic_va_android)
                    }
                }

                when (model.typeTest) {
                    "vbot" -> {
                        viewBinding.llLayout.setBackgroundResource(R.drawable.bg_vbot_10)
                    }
                    "xanhsm"->{
                        viewBinding.llLayout.setBackgroundResource(R.drawable.bg_main_10)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}