package com.moroom.android.ui.result

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.moroom.android.R
import com.moroom.android.databinding.ActivityResultBinding
import com.moroom.android.ui.adapter.result.SearchResultsAdapter
import com.moroom.android.ui.login.MoveToLogin
import com.moroom.android.ui.nav.MainActivity
import com.moroom.android.ui.search.SearchActivity
import com.moroom.android.ui.write.presentation.view.WriteActivity

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpViews()
        setUpListeners()
    }

    private fun setUpViews() {
        setUpReviews()
        setUpRecyclerView()
        setUpReviewObserver()
    }

    private fun setUpReviews() {
        val searchedAddress = intent.getStringExtra("searchedAddress") ?: ""
        Log.d("HashCode", searchedAddress)
        viewModel.loadReviews(searchedAddress)
        binding.tvAddressTitle.text = searchedAddress
    }

    private fun setUpRecyclerView() {
        binding.searchedRecyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.searchedRecyclerView.layoutManager = layoutManager
    }

    private fun setUpReviewObserver() {
        viewModel.review.observe(this) { review ->
            binding.searchedRecyclerView.adapter = SearchResultsAdapter(review)
        }

        viewModel.reviewExists.observe(this) { reviewExists ->
            if (!reviewExists) suggestReviewWriting()
        }
    }

    private fun suggestReviewWriting() {
        binding.apply {
            pleaseMessage3.visibility = View.VISIBLE
            stripBannerImage.visibility = View.GONE
            linearFooterMessage.visibility = View.GONE
        }
    }

    private fun setUpListeners() {
        binding.apply {
            imgBack.setOnClickListener { onBackPressed() }
            imgHome.setOnClickListener { navigateToMainActivity(R.id.navigation_home) }
            imgMap.setOnClickListener { navigateToMainActivity(R.id.navigation_map) }
            imgProfile.setOnClickListener { navigateToMainActivity(R.id.navigation_profile) }
            etSearch.setOnClickListener { startSearchActivity() }
            fab.setOnClickListener { startWriteActivity() }
            stripBannerImage.setOnClickListener { startWriteActivity() }
            moveToWrite.setOnClickListener { startWriteActivity() }
        }
    }

    private fun navigateToMainActivity(destination: Int) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("nav_destination", destination)
        startActivity(intent)
    }

    private fun startWriteActivity() {
        val user = FirebaseAuth.getInstance().currentUser
        val intent = if (user != null) {
            Intent(this, WriteActivity::class.java)
        } else {
            Intent(this, MoveToLogin::class.java)
        }
        startActivity(intent)
    }

    private fun startSearchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        getSearchResult.launch(intent)
    }

    private val getSearchResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            if (result.data != null) {
                val data = result.data!!.getStringExtra("data")
                val intent = Intent(this, ResultActivity::class.java)
                intent.putExtra("searchedAddress", data)
                startActivity(intent)
            }
        }
    }
}