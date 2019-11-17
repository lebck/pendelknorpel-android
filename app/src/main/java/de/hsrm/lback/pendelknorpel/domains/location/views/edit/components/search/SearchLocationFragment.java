package de.hsrm.lback.pendelknorpel.domains.location.views.edit.components.search;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.domains.location.models.Location;
import de.hsrm.lback.pendelknorpel.helpers.Callback;
import de.hsrm.lback.pendelknorpel.helpers.adapters.LocationSearchAdapter;

public class SearchLocationFragment extends Fragment {
    private LiveData<List<Location>> locationResults;
    private EditText locationText;
    private LocationSearchAdapter searchResultsAdapter;
    private ListView searchResults;
    private boolean viewCreated = false;
    @Nullable private SearchLocationViewModel viewModel;
    @Nullable private Callback<Location> onClickCallback;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        this.searchResultsAdapter = new LocationSearchAdapter(
                inflater,
                this::onSearchResultClick
        );
        return inflater.inflate(R.layout.fragment_search_location, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated = true;
        this.locationText = view.findViewById(R.id.edit_location_name);
        this.searchResults = view.findViewById(R.id.search_results);
        this.searchResults.setAdapter(searchResultsAdapter);
        this.locationText.requestFocus();
        this.showKeyboard();

        if (this.viewModel != null) {
            this.locationText.addTextChangedListener(viewModel);
            this.locationResults = this.viewModel.getLocationResults();
            this.locationResults.observe(this, this::onSearchResultsChange);
            this.viewModel.getLocationData().observe(this, this::onLocationChange);
        }

    }

    private void onSearchResultsChange(List<Location> locations) {
        if (this.locationText.hasFocus()) {
            searchResultsAdapter.setLocations(locations);
            searchResultsAdapter.notifyDataSetChanged();
        }
    }

    private void onSearchResultClick(View view) {
        if (viewModel != null) {
            int index = (int) view.getTag(R.id.location_index);
            viewModel.setLocationByIndex(index);
        }

        if (onClickCallback != null) onClickCallback.handle(viewModel.getLocationData().getValue());

        hideKeyboard();
    }

    public void onLocationChange (@Nullable Location location) {
        if (location != null) {
            locationText.setText(location.getName());
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        // Find the currently focused view, so we can grab the correct window token from it.
        View view = getActivity().getCurrentFocus();
        // If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(getActivity());
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (!locationText.hasFocus()) {
            return;
        }
        imm.showSoftInput(locationText, 0);
    }


    public void setViewModel(SearchLocationViewModel viewModel) {
        this.viewModel = viewModel;
        if (this.viewCreated) {
            this.locationText.addTextChangedListener(viewModel);
            this.locationResults = this.viewModel.getLocationResults();
            this.locationResults.observe(this, this::onSearchResultsChange);
            this.viewModel.getLocationData().observe(this, this::onLocationChange);
        }
    }

    public void setOnClickCallback(Callback<Location> onClickCallback) {
        this.onClickCallback = onClickCallback;
    }
}
