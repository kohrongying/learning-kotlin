package com.thoughtworks.miniweibo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.thoughtworks.miniweibo.R
import com.thoughtworks.miniweibo.databinding.HomeTimelineFragmentBinding

class HomeTimelineFragment : Fragment() {

    private val viewModel: HomeTimelineViewModel by viewModels()

    private lateinit var binding: HomeTimelineFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,  R.layout.home_timeline_fragment, container, false)

        binding.viewModel = viewModel

        binding.setLifecycleOwner(this)

        binding.postsGrid.adapter = PostGridAdapter(PostListener {
            id -> Toast.makeText(context, "${id}", Toast.LENGTH_LONG).show()
        })

        // Bind button to nav
        val action = HomeTimelineFragmentDirections.actionHomeTimelineFragmentToPostDetailFragment()
        binding.button.setOnClickListener {
                view -> view.findNavController().navigate(action)
        }

        return binding.root
    }
}