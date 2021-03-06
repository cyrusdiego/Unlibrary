/*
 * ExchangeFragment
 *
 * October 22, 2020
 *
 * Copyright (c) Team 24, Fall2020, CMPUT301, University of Alberta
 */
package com.example.unlibrary.exchange;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.unlibrary.MainActivity;
import com.example.unlibrary.book_list.BooksFragment;
import com.example.unlibrary.databinding.FragmentExchangeBinding;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Host fragment for Exchange feature
 */
@AndroidEntryPoint
public class ExchangeFragment extends Fragment {
    private ExchangeViewModel mViewModel;
    private FragmentExchangeBinding mBinding;

    /**
     * Initialize ViewModel of the fragment that will be retained when the fragment is
     * paused or stopped, then resumed.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(ExchangeViewModel.class);
    }

    /**
     * Draws the fragment UI. Sets the {@link com.example.unlibrary.book_list.BooksSource} to the
     * child {@link BooksFragment}.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentExchangeBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());
        mBinding.setViewModel(mViewModel);

        // Add listener to enter key in search bar
        mBinding.searchExchangeBook.setOnEditorActionListener((textView, i, keyEvent) -> {
            mViewModel.search(textView.getText().toString());
            return true;
        });

        // Child fragments are can only be accessed on view creation, so this is the earliest
        // point where we can specify the data source
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f instanceof BooksFragment) {
                BooksFragment bookFragment = (BooksFragment) f;

                bookFragment.setBooksSource(mViewModel);
                bookFragment.setOnItemClickListener(mViewModel::selectCurrentBook);
            }
        }

        // Setup observer
        mViewModel.getNavigationEvent().observe(this, navDirections -> Navigation.findNavController(mBinding.getRoot()).navigate(navDirections));
        mViewModel.getSuccessRequestMsgEvent().observe(this, s -> ((MainActivity) requireActivity()).showToast(s));

        return mBinding.getRoot();
    }

}
