package com.moroom.android.navui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.moroom.android.Review
import com.moroom.android.ReviewAdapter
import com.moroom.android.MoveToLogin
import com.moroom.android.ResultActivity
import com.moroom.android.SearchActivity
import com.moroom.android.WriteActivity
import com.moroom.android.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupListener()
    }

    private fun setupViews() {
        setupHomeImg()
        setupRecyclerView()
        setupBestReviews()
    }

    private fun setupHomeImg() {
        binding.imgHome.clipToOutline = true
    }

    private fun setupRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.homeRecyclerView.setHasFixedSize(true)
        binding.homeRecyclerView.layoutManager = layoutManager
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

    private fun setupBestReviews() {
        val bestReviewList = ArrayList<Review>()
        val database: DatabaseReference = Firebase.database.reference
        database.child("Address").child("bestReviews").get()
            .addOnSuccessListener { snapshot ->
                for (data in snapshot.children) {
                    Log.d("dataSnapshot", data.toString())
                    val content = data.getValue(Review::class.java)
                    content?.let { bestReviewList.add(it) }
                }
                setupAdapter(bestReviewList)
            }
            .addOnFailureListener {
                Log.e(javaClass.simpleName, it.toString())
            }
    }

    private fun setupAdapter(bestReviewList: ArrayList<Review>) {
        val adapter: RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> =
            ReviewAdapter(bestReviewList)
        binding.homeRecyclerView.adapter = adapter
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