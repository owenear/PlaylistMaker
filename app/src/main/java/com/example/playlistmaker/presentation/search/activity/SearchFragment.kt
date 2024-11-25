package com.example.playlistmaker.presentation.search.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.search.models.Track
import com.example.playlistmaker.presentation.player.activity.PlayerActivity
import com.example.playlistmaker.presentation.search.models.SearchScreenState
import com.example.playlistmaker.presentation.search.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment()  {

    private lateinit var binding: FragmentSearchBinding

    private val searchViewModel by viewModel<SearchViewModel>()

    private val searchAdapter by lazy {
        SearchAdapter() { trackItem -> trackListClickListener(trackItem) }
    }
    private val historyAdapter by lazy {
        SearchAdapter() { trackItem -> trackListClickListener(trackItem) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchRecyclerView.adapter = searchAdapter
        binding.searchHistoryRecyclerView.adapter = historyAdapter

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
        searchViewModel.addToHistory(trackItem)
        val playerIntent = Intent(requireContext(), PlayerActivity::class.java)
        playerIntent.putExtra("trackItem",trackItem)
        this.startActivity(playerIntent)
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

    private fun showInitial(){
        binding.placeholderTextView.visibility = View.GONE
        binding.placeholderImageView.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.GONE
        binding.searchHistoryGroup.visibility = View.GONE
    }

    private fun showLoading() {
        binding.searchHistoryGroup.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.GONE
        binding.placeholderTextView.visibility = View.GONE
        binding.placeholderImageView.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun showError() {
        binding.searchHistoryGroup.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.placeholderTextView.visibility = View.VISIBLE
        binding.placeholderImageView.visibility = View.VISIBLE
        binding.updateButton.visibility = View.VISIBLE
        binding.placeholderTextView.text = getString(R.string.search_net_error)
        binding.placeholderImageView.setImageResource(R.drawable.ic_net_error)
    }

    private fun showEmpty() {
        binding.searchHistoryGroup.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.placeholderTextView.visibility = View.VISIBLE
        binding.placeholderImageView.visibility = View.VISIBLE
        binding.updateButton.visibility = View.GONE
        binding.placeholderTextView.text = getString(R.string.search_nothing_found)
        binding.placeholderImageView.setImageResource(R.drawable.ic_nothing_found)
    }

    private fun showSearchContent(trackList: List<Track>){
        searchAdapter.items = trackList
        binding.placeholderTextView.visibility = View.GONE
        binding.placeholderImageView.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.searchHistoryGroup.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.VISIBLE
    }

    private fun showHistoryContent(trackList: List<Track>){
        historyAdapter.items = trackList
        binding.placeholderTextView.visibility = View.GONE
        binding.placeholderImageView.visibility = View.GONE
        binding.updateButton.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.searchRecyclerView.visibility = View.GONE
        binding.searchHistoryGroup.visibility = if (trackList.isEmpty()) View.GONE else View.VISIBLE
    }

    companion object {
        const val SEARCH_STRING_DEF = ""
    }

}