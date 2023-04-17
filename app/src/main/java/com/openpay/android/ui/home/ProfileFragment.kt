package com.openpay.android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.openpay.android.databinding.FragmentHomeBinding
import com.openpay.android.model.State
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()

    private val mAdapter = ReviewListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.run {
            reviewRecyclerView.adapter = mAdapter
        }

        observeProfile()

        profileViewModel.getProfile()

        return binding.root
    }

    private fun observeProfile() {
        profileViewModel.profileState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Error -> {
                    showToast(it.message)
                    showLoading(false)
                }
                is State.Loading -> showLoading(true)
                is State.Success -> {
                    it.data.let { profile ->
                        binding.collapsingToolbar.title = profile.name
                        Glide.with(binding.root.context)
                            .load("https://image.tmdb.org/t/p/w500/" + profile.profile_path)
                            .into(binding.imageView)

                        binding.imageView
                        mAdapter.submitList(profile.known_for?.toMutableList())
                        showLoading(false)
                    }
                }

            }
        }
    }

    private fun showLoading(isLoading: Boolean) {

    }

    private fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}