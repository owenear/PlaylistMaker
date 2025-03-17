package com.example.playlistmaker.presentation.search.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.activity.PlayerFragment
import com.example.playlistmaker.presentation.search.models.SearchScreenState
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import com.example.playlistmaker.services.NetworkBroadcastReceiver
import com.example.playlistmaker.util.debounce
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment()  {

    private val networkBroadcastReceiver: BroadcastReceiver by inject()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel by viewModel<SearchViewModel>()

    private lateinit var clickListenerDebounce: (Track) -> Unit

    private val searchAdapter by lazy {
        SearchAdapter() { trackItem -> clickListenerDebounce(trackItem) }
    }

    private val historyAdapter by lazy {
        SearchAdapter() { trackItem -> clickListenerDebounce(trackItem) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (binding.searchInputEditText.hasFocus() && binding.searchInputEditText.text.isNullOrEmpty())
            searchViewModel.processQuery(SEARCH_STRING_DEF)
        if (!binding.searchInputEditText.hasFocus() &&
            binding.searchInputEditText.text != null &&
            binding.searchInputEditText.text!!.isNotEmpty() )
            binding.searchInputEditText.requestFocus()

        ContextCompat.registerReceiver(
            requireContext(),
            networkBroadcastReceiver,
            IntentFilter(NetworkBroadcastReceiver.ACTION),
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
    }

    override fun onPause() {
        super.onPause()
        requireContext().unregisterReceiver(networkBroadcastReceiver)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchRecyclerView.adapter = searchAdapter
        binding.searchHistoryRecyclerView.adapter = historyAdapter

        clickListenerDebounce = debounce<Track>(CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope, false) {
                trackItem -> trackListClickListener(trackItem)
        }

        binding.searchClearIcon.setOnClickListener {
            binding.searchInputEditText.setText(SEARCH_STRING_DEF)
            searchViewModel.processQuery(SEARCH_STRING_DEF)
            val inputMethodManager = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(binding.searchInputEditText.windowToken, 0)
        }

        binding.searchInputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.searchInputEditText.text.isNullOrEmpty())
                searchViewModel.processQuery(binding.searchInputEditText.text.toString())
        }

        binding.searchInputEditText.addTextChangedListener(
            onTextChanged = { charSequence, _, _, _ ->
                binding.searchClearIcon.visibility = if (charSequence.isNullOrEmpty()) View.GONE else View.VISIBLE
                if (binding.searchInputEditText.hasFocus())
                    searchViewModel.processQuery(charSequence.toString())
            }
        )

        binding.searchInputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && !binding.searchInputEditText.text.isNullOrEmpty()) {
                searchViewModel.search(binding.searchInputEditText.text.toString())
            }
            false
        }

        binding.updateButton.setOnClickListener {
            searchViewModel.search(binding.searchInputEditText.text.toString())
        }

        binding.clearHistoryButton.setOnClickListener {
            searchViewModel.clearHistory()
        }

        searchViewModel.stateLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }

    }

    private fun trackListClickListener(trackItem: Track) {
        trackItem.previewUrl = trackItem.previewUrl.ifEmpty { getString(R.string.player_default_preview_url) }
        searchViewModel.addToHistory(trackItem)
        findNavController().navigate(R.id.action_searchFragment_to_playerFragment,
            PlayerFragment.createArgs(trackItem))
    }

    private fun render(state: SearchScreenState) {
        when (state) {
            is SearchScreenState.Initial -> showInitial()
            is SearchScreenState.Loading -> showLoading()
            is SearchScreenState.SearchContent -> showSearchContent(state.trackList)
            is SearchScreenState.HistoryContent -> showHistoryContent(state.trackList)
            is SearchScreenState.Error -> showError()
            is SearchScreenState.Empty -> showEmpty()
        }
    }

    private fun showInitial() = with(binding) {
        placeholderTextView.visibility = View.GONE
        placeholderImageView.visibility = View.GONE
        updateButton.visibility = View.GONE
        progressBar.visibility = View.GONE
        searchRecyclerView.visibility = View.GONE
        searchHistoryGroup.visibility = View.GONE
    }

    private fun showLoading() = with(binding) {
        searchHistoryGroup.visibility = View.GONE
        searchRecyclerView.visibility = View.GONE
        placeholderTextView.visibility = View.GONE
        placeholderImageView.visibility = View.GONE
        updateButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun showError() = with(binding) {
        searchHistoryGroup.visibility = View.GONE
        searchRecyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        placeholderTextView.visibility = View.VISIBLE
        placeholderImageView.visibility = View.VISIBLE
        updateButton.visibility = View.VISIBLE
        placeholderTextView.text = getString(R.string.search_net_error)
        placeholderImageView.setImageResource(R.drawable.ic_net_error)
    }

    private fun showEmpty() = with(binding) {
        searchHistoryGroup.visibility = View.GONE
        searchRecyclerView.visibility = View.GONE
        progressBar.visibility = View.GONE
        placeholderTextView.visibility = View.VISIBLE
        placeholderImageView.visibility = View.VISIBLE
        updateButton.visibility = View.GONE
        placeholderTextView.text = getString(R.string.search_nothing_found)
        placeholderImageView.setImageResource(R.drawable.ic_nothing_found)
    }

    private fun showSearchContent(trackList: List<Track>){
        searchAdapter.items = trackList
        with(binding) {
            placeholderTextView.visibility = View.GONE
            placeholderImageView.visibility = View.GONE
            updateButton.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchHistoryGroup.visibility = View.GONE
            searchRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun showHistoryContent(trackList: List<Track>){
        historyAdapter.items = trackList
        with(binding) {
            placeholderTextView.visibility = View.GONE
            placeholderImageView.visibility = View.GONE
            updateButton.visibility = View.GONE
            progressBar.visibility = View.GONE
            searchRecyclerView.visibility = View.GONE
            searchHistoryGroup.visibility =
                if (trackList.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (binding.searchInputEditText.text.isNullOrEmpty())
            searchViewModel.processInitial()
        _binding = null
    }

    companion object {
        const val SEARCH_STRING_DEF = ""
        private const val CLICK_DEBOUNCE_DELAY = 500L
    }

}