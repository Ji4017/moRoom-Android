package com.moroom.android.ui.navui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.firebase.auth.FirebaseAuth

import com.moroom.android.ui.login.MoveToLogin
import com.moroom.android.R
import com.moroom.android.ui.result.ResultActivity
import com.moroom.android.ui.search.SearchActivity
import com.moroom.android.ui.write.WriteActivity
import com.moroom.android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListener()
    }

    private fun setupViews() {
        setupRecyclerView()
        setupHomeImg()
    }

    private fun setupRecyclerView() {
        binding.homeRecyclerView.setHasFixedSize(true)
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setupHomeImg() {
        binding.imgHome.clipToOutline = true
    }

    private fun setupListener() {
        binding.etSearch.isFocusable = false
        binding.etSearch.setOnClickListener {
            val intent = Intent(activity, SearchActivity::class.java)
            getSearchResult.launch(intent)
        }

        binding.fab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            val intent = if (user != null) Intent(activity, WriteActivity::class.java)
            else Intent(activity, MoveToLogin::class.java)
            startActivity(intent)
        }

        binding.linearUam.setOnClickListener { navigateToDormitory("충북 청주시 청원구 안덕벌로19번길 116 (내덕동) 우암마을") }
        binding.linearYeJi.setOnClickListener { navigateToDormitory("충북 청주시 청원구 안덕벌로19번길 116 (내덕동) 예지관") }
        binding.linearGukJe.setOnClickListener { navigateToDormitory("충북 청주시 청원구 안덕벌로19번길 116 (내덕동) 국제학사") }
        binding.linearJinWon.setOnClickListener { navigateToDormitory("충북 청주시 청원구 수암로66번길 48-2 (우암동, 한진 신세대 아파트)") }
    }

    private fun navigateToDormitory(address: String) {
        val intent = Intent(activity, ResultActivity::class.java)
        intent.putExtra("searchedAddress", address)
        startActivity(intent)
    }

    private val getSearchResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        // setResult에 의해 SearchActivity 로부터의 결과 값이 이곳으로 전달됨.
        if (result.resultCode == Activity.RESULT_OK) {
            if (result.data != null) {
                val data = result.data!!.getStringExtra("data")
                val intent = Intent(activity, ResultActivity::class.java)
                intent.putExtra("searchedAddress", data)
                startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}