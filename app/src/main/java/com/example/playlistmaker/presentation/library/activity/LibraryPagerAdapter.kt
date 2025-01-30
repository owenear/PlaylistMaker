package com.example.playlistmaker.presentation.library.activity

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.presentation.favorites.activity.FavoritesFragment
import com.example.playlistmaker.presentation.playlists.activity.PlaylistsFragment

class LibraryPagerAdapter(hostFragment: Fragment)
    : FragmentStateAdapter(hostFragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) FavoritesFragment.newInstance()
               else PlaylistsFragment.newInstance()
    }

}