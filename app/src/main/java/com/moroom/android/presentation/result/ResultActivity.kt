package com.moroom.android.presentation.result

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.moroom.android.R
import com.moroom.android.databinding.ActivityResultBinding
import com.moroom.android.presentation.adapter.result.SearchResultsAdapter
import com.moroom.android.presentation.login.MoveToLogin
import com.moroom.android.presentation.nav.MainActivity
import com.moroom.android.presentation.search.SearchActivity
import com.moroom.android.presentation.write.WriteActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
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
        setUpRecyclerView()
        initializeSearchResults()
        observeReview()
    }

    private fun initializeSearchResults() {
        val searchedAddress = intent.getStringExtra("searchedAddress") ?: ""
        viewModel.fetchReviews(searchedAddress)
        binding.tvAddressTitle.text = searchedAddress
    }

    private fun setUpRecyclerView() {
        binding.searchedRecyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        binding.searchedRecyclerView.layoutManager = layoutManager
    }

    private fun observeReview() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.reviewsState.collect { reviewState ->
                    binding.progressBar.visibility = View.GONE
                    when (reviewState) {
                        is ReviewState.Success -> binding.searchedRecyclerView.adapter = SearchResultsAdapter(reviewState.reviews)
                        is ReviewState.Empty -> suggestReviewWriting()
                        is ReviewState.Error -> Toast.makeText(this@ResultActivity, R.string.result_error_inquiry, Toast.LENGTH_SHORT).show()
                        is ReviewState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun suggestReviewWriting() {
        binding.apply {
            binding.progressBar.visibility = View.GONE
            pleaseMessage3.visibility = View.VISIBLE
            stripBannerImage.visibility = View.GONE
            linearFooterMessage.visibility = View.GONE
        }
    }

    private fun setUpListeners() {
        binding.apply {
            imgBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
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