/*
 * LibraryFragment
 *
 * October 22, 2020
 *
 * Copyright (c) Team 24, Fall2020, CMPUT301, University of Alberta
 */
package com.example.unlibrary.library;

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
import com.example.unlibrary.databinding.FragmentLibraryBinding;
import com.example.unlibrary.util.FilterMap;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Host fragment for Library feature
 */
@AndroidEntryPoint
public class LibraryFragment extends Fragment {
    private LibraryViewModel mViewModel;
    private FragmentLibraryBinding mBinding;
    private FilterMap mUpdateFilter;

    /**
     * Initialize ViewModel of the fragment that will be retained when the fragment is
     * paused or stopped, then resumed.
     *
     * @param savedInstanceState bundle used to restore its previous state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);
    }

    /**
     * Sets up listeners to binding variables and sends {@link com.example.unlibrary.book_list.BooksSource}
     * to child {@link BooksFragment} to display.
     *
     * @param inflater           {@link LayoutInflater} that can be used to inflate any view in the fragment
     * @param container          {@link ViewGroup} possibly null parent view that this fragment will attach to
     * @param savedInstanceState bundle used to restore its previous state
     * @return {@link View} for this fragment
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Get the activity viewModel
        mViewModel = new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);

        // Setup data binding
        mBinding = FragmentLibraryBinding.inflate(inflater, container, false);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        // Child fragments are can only be accessed on view creation, so this is the earliest
        // point where we can specify the data source
        for (Fragment f : getChildFragmentManager().getFragments()) {
            if (f instanceof BooksFragment) {
                BooksFragment bookFragment = (BooksFragment) f;
                bookFragment.setBooksSource(mViewModel);
                bookFragment.setOnItemClickListener(mViewModel::selectCurrentBook);
            }
        }
        mBinding.setViewModel(mViewModel);
        mBinding.setLifecycleOwner(getViewLifecycleOwner());

        // Setup observers
        mViewModel.getNavigationEvent().observe(this, navDirections -> Navigation.findNavController(mBinding.fabAdd).navigate(navDirections));
        mViewModel.getFailureMsgEvent().observe(this, s -> ((MainActivity) requireActivity()).showToast(s));

        // Setup OnClickListener for filter button. Done in fragment because a dialog needs to be shown.
        // TODO theme filter button to be active/highlighted when it is filtering
        mUpdateFilter = new FilterMap(true);
        mUpdateFilter.setMap(mViewModel.getFilter().getMap());
        mBinding.fabFilter.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Select status to filter by.")
                    .setNeutralButton("Cancel", null)
                    .setPositiveButton("Filter", (dialog, which) -> mViewModel.setFilter(mUpdateFilter))
                    .setOnDismissListener(dialog -> mUpdateFilter.setMap(mViewModel.getFilter().getMap()))
                    .setMultiChoiceItems(mUpdateFilter.itemStrings(), mUpdateFilter.itemBooleans(), (dialog, which, isChecked) -> mUpdateFilter.set(mUpdateFilter.itemStrings()[which], isChecked))
                    .show();
        });

        return mBinding.getRoot();
    }
}
