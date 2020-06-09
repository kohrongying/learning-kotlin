package com.thoughtworks.miniweibo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.thoughtworks.miniweibo.R
import kotlinx.android.synthetic.main.home_timeline_fragment.*

class HomeTimelineFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_timeline_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val action = HomeTimelineFragmentDirections.actionHomeTimelineFragmentToPostDetailFragment()
        button.setOnClickListener {
            view -> view.findNavController().navigate(action)
        }
    }
}