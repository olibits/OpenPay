package com.openpay.android.ui.dashboard

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.openpay.android.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.openpay.android.model.State
import com.openpay.android.model.movie.TopRatedMoviesResponse
import com.openpay.android.model.movie.MovieResponse
import kotlin.math.abs

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val dashboardViewModel: DashboardViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initViewPagerPopularMovies(dashboardViewModel)
        initViewTopRelatedMovies(dashboardViewModel)
        return root
    }

    private fun initViewPagerPopularMovies(dashboardViewModel: DashboardViewModel) {
        val viewPagerPopularMovies: ViewPager2 = binding.viewPagerPopularMovies
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(
            MarginPageTransformer(
                (40 * Resources.getSystem()
                    .displayMetrics.density).toInt()
            )
        )
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        viewPagerPopularMovies.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            setPageTransformer(compositePageTransformer)
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }

        dashboardViewModel.popularMoviesState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Error -> showError(it.message)
                is State.Loading -> {}
                is State.Success -> showPopularMoviesViewPager(viewPagerPopularMovies, it.data)
            }
        }
        dashboardViewModel.getPopularMovies()
    }

    private fun showPopularMoviesViewPager(
        viewPagerPopularMovies: ViewPager2,
        list: MovieResponse?
    ) {
        list?.let {
            viewPagerPopularMovies.adapter = CarouselAdapter(it.results)
        }
    }

    private fun showError(message: String) {
        showToast(message)
    }

    private fun showToast(message: String, duration: Int = Toast.LENGTH_LONG) {
        Toast.makeText(context, message, duration).show()
    }

    private fun initViewTopRelatedMovies(dashboardViewModel: DashboardViewModel) {
        val viewPagerBestMovies: ViewPager2 = binding.viewPagerBestMovies
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(
            MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt())
        )
        compositePageTransformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = (0.80f + r * 0.20f)
        }
        viewPagerBestMovies.apply {
            clipChildren = false
            clipToPadding = false
            offscreenPageLimit = 3
            setPageTransformer(compositePageTransformer)
            (getChildAt(0) as RecyclerView).overScrollMode =
                RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
        }

        dashboardViewModel.topRatedMoviesState.observe(viewLifecycleOwner) {
            when (it) {
                is State.Error -> showError(it.message)
                is State.Loading -> {}
                is State.Success -> showTopRatedMoviesViewPager(viewPagerBestMovies, it.data)
            }
        }
        dashboardViewModel.getTopRatedMovies()
    }

    private fun showTopRatedMoviesViewPager(
        viewPagerBestMovies: ViewPager2,
        list: TopRatedMoviesResponse?
    ) {
        list?.let {
            viewPagerBestMovies.adapter = CarouselAdapter(it.results)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
