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
import androidx.fragment.app.viewModels
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
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
    }

    private fun setupRecyclerView() {
        binding.homeRecyclerView.setHasFixedSize(true)
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun setupListener() {
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

        binding.linearUam.setOnClickListener { navigateToDormitory(getString(R.string.UAM)) }
        binding.linearYeJi.setOnClickListener { navigateToDormitory(getString(R.string.YEJI)) }
        binding.linearGukJe.setOnClickListener { navigateToDormitory(getString(R.string.GUKJE)) }
        binding.linearJinWon.setOnClickListener { navigateToDormitory(getString(R.string.JINWON)) }
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