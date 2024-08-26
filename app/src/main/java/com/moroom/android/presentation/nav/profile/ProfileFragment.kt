package com.moroom.android.presentation.nav.profile

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseUser
import com.moroom.android.R
import com.moroom.android.databinding.FragmentProfileBinding
import com.moroom.android.presentation.login.LoginActivity
import com.moroom.android.presentation.nav.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view: View = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        setUpObserver()
        setUpListener()
    }

    private fun setUpView() {
        val currentUser = viewModel.currentUser
        if (currentUser != null) showUserProfile(currentUser)
        else showGoToLogin()
    }

    private fun showUserProfile(currentUser: FirebaseUser) {
        binding.tvUserEmail.text = currentUser.email
    }

    private fun setUpObserver() {
        viewModel.isUserDeleted.observe(viewLifecycleOwner) {
            if (it) {
                Toast.makeText(activity, getString(R.string.delete_complete), Toast.LENGTH_SHORT)
                    .show()
                navigateToMain()
            } else {
                Toast.makeText(activity, getString(R.string.user_delete_error), Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setUpListener() {
        binding.tvLogout.setOnClickListener { showLogoutDialog() }
        binding.tvDeleteAccount.setOnClickListener { showDeleteDialog() }
        binding.moveToLogin.setOnClickListener { navigateToLogin() }
    }

    private fun showLogoutDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialog = builder.create()
        builder.setTitle(getString(R.string.log_out))
        builder.setMessage(getString(R.string.log_out_box))
        builder.setPositiveButton(getString(R.string.yes)) { _: DialogInterface?, _: Int -> viewModel.logout(); navigateToMain() }
        builder.setNegativeButton(getString(R.string.no), null)
        dialog.show()
    }

    private fun showDeleteDialog() {
        val builder = AlertDialog.Builder(requireContext())
        val dialog = builder.create()
        builder.setTitle(getString(R.string.delete_account))
        builder.setMessage(getString(R.string.delete_account_box))
        builder.setPositiveButton(getString(R.string.yes)) { _: DialogInterface?, _: Int -> viewModel.deleteUserData() }
        builder.setNegativeButton(getString(R.string.no), null)
        dialog.show()
    }

    private fun showGoToLogin() {
        binding.apply {
            tvUserEmail.visibility = View.GONE
            tvLogout.visibility = View.GONE
            tvDeleteAccount.visibility = View.GONE
            tvMoRoomVersion.visibility = View.GONE
            linearTop.visibility = View.GONE
            linearSeparation3.visibility = View.GONE
            linearSeparation4.visibility = View.GONE
            linearSeparation1.visibility = View.GONE
            linearSeparation2.visibility = View.GONE

            moveToLogin.visibility = View.VISIBLE
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMain() {
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
