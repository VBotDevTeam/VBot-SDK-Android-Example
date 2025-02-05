package com.vpmedia.vbotsdksample.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.vpmedia.vbotsdksample.databinding.FragmentSelectAccountBinding
import com.vpmedia.vbotsdksample.model.SDKMember

class SelectAccount : SuperBottomSheetFragment(), AdapterAccount.AdapterContactListener {

    private lateinit var binding: FragmentSelectAccountBinding
    private lateinit var adapterAccount: AdapterAccount
    private lateinit var mListener: ListenerBottomSheet
    private var list = arrayListOf<SDKMember>()
    private var isCall = false
    private var nameLogin = ""

    override fun isSheetAlwaysExpanded(): Boolean {
        return true //all screen
    }

    override fun animateCornerRadius(): Boolean {
        return false
    }

    interface ListenerBottomSheet {
        fun onClickAccount(model: SDKMember, isCall: Boolean)
    }

    fun setListener(listener: ListenerBottomSheet, isCall: Boolean, nameLogin: String? = null) {
        this.mListener = listener
        this.isCall = isCall
        this.nameLogin = nameLogin ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        binding = FragmentSelectAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        if (isCall) {
            list.addAll(SDKMember.getCall())
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                list.removeIf { it.name == nameLogin }
            } else {
                for (i in 0 until list.size) {
                    if (list[i].name == nameLogin) {
                        list.removeAt(i)
                        break
                    }
                }
            }
        } else {
            list.addAll(SDKMember.getLoginXanhSM())
        }

        adapterAccount.setData(list)
    }

    private fun initAdapter() {
        adapterAccount = AdapterAccount(mutableListOf())
        adapterAccount.setListener(this)
        binding.rvSelect.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(context, 2)
            adapter = adapterAccount
        }
    }

    override fun onclickLogin(model: SDKMember) {
        mListener.onClickAccount(model, isCall)
        this.dismiss()
    }

}