package com.example.loversdiary.ui.momentslist

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.loversdiary.R
import com.example.loversdiary.data.Event
import com.example.loversdiary.data.Moment
import com.example.loversdiary.databinding.MomentsListFragmentBinding
import com.example.loversdiary.ui.momentslist.MomentsListAdapter.OnItemClickListener
import com.example.loversdiary.util.NpaLinerLayoutManager
import com.example.loversdiary.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MomentsListFragment : Fragment(R.layout.moments_list_fragment), OnItemClickListener {

    private val momentsListViewModel: MomentsListViewModel by viewModels()

    private val momentsListAdapter: MomentsListAdapter = MomentsListAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = MomentsListFragmentBinding.bind(view)

        binding.apply {
            momentsListFrgRecyclerList.apply {
                adapter = momentsListAdapter
                layoutManager = NpaLinerLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            fabAdd.setOnClickListener {
                momentsListViewModel.onAddMomentClick()
            }
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            momentsListViewModel.onAddEditResult(result)
        }

        momentsListViewModel.dataList.observe(viewLifecycleOwner) {
            momentsListAdapter.setFilms(it.first)
            momentsListAdapter.setEvents(it.second)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            momentsListViewModel.momentsListEvent.collect { event ->
                when (event) {
                    is MomentsListViewModel.MomentsListEvent.NavigateToAddMoment -> {
                        val action = MomentsListFragmentDirections.actionMomentsListFragmentToAddEditMomentFragment(null, null, "Новый момент")
                        findNavController().navigate(action)
                    }
                    is MomentsListViewModel.MomentsListEvent.NavigateToEditMoment -> {
                        val action = MomentsListFragmentDirections.actionMomentsListFragmentToAddEditMomentFragment(event.moment, event.event, "Момент")
                        findNavController().navigate(action)
                    }
                    is MomentsListViewModel.MomentsListEvent.ShowMomentSavedConfirmationMessage -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)
    }

    override fun onItemClick(moment: Moment, event: Event) {
        momentsListViewModel.onMomentItemSelected(moment, event)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.moments_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_redirect_to_statistic -> {
                findNavController().navigate(R.id.action_momentsListFragment_to_statisticFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}