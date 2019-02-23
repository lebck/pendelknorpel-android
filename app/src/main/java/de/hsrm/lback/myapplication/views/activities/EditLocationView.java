package de.hsrm.lback.myapplication.views.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.BackgroundManager;
import de.hsrm.lback.myapplication.helpers.ResourcesHelper;
import de.hsrm.lback.myapplication.helpers.adapters.LocationSearchAdapter;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.viewmodels.LocationViewModel;
import de.hsrm.lback.myapplication.views.fragments.ChooseLogoFragment;

public class EditLocationView extends AppCompatActivity implements TextWatcher {

    public static final int ANONYMOUS_SRC = 0;
    public static final int ANONYMOUS_TARGET = 1;
    private ImageView locationLogo;
    private EditText locationText;
    private EditText displayName;
    private ListView searchResults;

    private LocationViewModel viewModel;
    private LiveData<Location> locationLiveData;
    private MutableLiveData<List<Location>> locationResults;
    private LocationRepository locationRepository;
    private LocationSearchAdapter searchResultsAdapter;

    private int locationUid;

    private ChooseLogoFragment chooseLogoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location_view);
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        this.locationText = findViewById(R.id.edit_location_name);
        this.locationLogo = findViewById(R.id.location_logo);
        this.displayName = findViewById(R.id.edit_location_display_name);
        this.searchResults = findViewById(R.id.search_results);

        this.locationResults = new MutableLiveData<>();
        this.locationResults.setValue(Collections.emptyList());
        this.locationRepository = new LocationRepository(this);
        this.viewModel = new LocationViewModel(getApplication());
        this.chooseLogoFragment = ChooseLogoFragment.getInstance(this::onLogoChosen, this::createBackground);

        this.searchResultsAdapter = new LocationSearchAdapter(
                this,
                locationResults.getValue(),
                this::onSearchResultClick
        );

        this.searchResults.setAdapter(searchResultsAdapter);


        // retrieve location
        this.locationUid = getIntent().getIntExtra(Location.LOCATION_UID, 0);

        if (locationUid != 0)
            this.locationLiveData = locationRepository.get(locationUid);
        else {
            this.locationLiveData = new MutableLiveData<>();
            ((MutableLiveData<Location>)this.locationLiveData)
                    .setValue(new Location("", 0));
            viewModel.init(locationLiveData.getValue());
        }

        this.locationLiveData.observe(this, this::onLocationChange);
        this.locationLogo.setOnClickListener(this::onLogoClick);
        this.locationText.addTextChangedListener(this);
        this.locationResults.observe(this, this::onSearchResultsChange);

        this.onLocationChange();

    }

    private Bitmap createBackground() {
        return BackgroundManager.getBlurryBackground(this.findViewById(R.id.edit_location));
    }

    private void onSearchResultClick(View view) {

        String name = ((TextView) view).getText().toString();
        String apiId = (String) view.getTag();

        this.viewModel.getLocation().setName(name);
        this.viewModel.getLocation().setApiId(apiId);

        this.locationText.setText(name);
        this.displayName.setText(viewModel.getLocation().getDisplayName());
        this.locationResults.setValue(Collections.emptyList());

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);

        this.locationText.clearFocus();
    }

    private void onSearchResultsChange(List<Location> locations) {
        if (this.locationText.hasFocus()) {
            searchResultsAdapter.setLocations(locations);
            searchResultsAdapter.notifyDataSetChanged();
        }
    }

    private void onLogoChange(String s) {
        int resId = ResourcesHelper.getResId(s, R.drawable.class);
        if (resId > 0)
            this.locationLogo.setImageResource(resId);
    }


    private void onLogoChosen(String s) {
        // remove fragment
        getSupportFragmentManager()
                .beginTransaction()
                .remove(chooseLogoFragment)
                .commit();

        // update logo
        viewModel.getLocation().setLogo(s);
        onLocationChange();
    }

    private void onLocationChange() {
        this.onLocationChange(this.viewModel.getLocation());
    }

    private void onLocationChange(Location location) {
        if (location != null) {
            this.locationText.setText(location.getName());
            this.displayName.setText(location.getDisplayName());
            this.onLogoChange(location.getLogo());
            if (this.viewModel.getLocation() == null) {
                this.viewModel.init(location);
                this.onLogoChange(this.viewModel.getLocation().getLogo());
            }
        }
    }

    private void onSubmit() {
        // save data to location object
        this.viewModel.getLocation().setName(this.locationText.getText().toString());
        this.viewModel.getLocation().setDisplayName(this.displayName.getText().toString());
        // save location if not anonymous
        if (locationUid != 0) {
            this.viewModel.update();
        } else {
            Intent result = new Intent();

            result.putExtra(Location.SERIALIZED_LOCATION,
                    LocationRepository.serializeLocation(viewModel.getLocation())
            );

            setResult(RESULT_OK, result);
        }

        // return to previous activity
        super.finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_location_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.submit) {
            this.onSubmit();
            return true;
        }

        return false;

    }

    /** Process click on logo */
    public void onLogoClick(View v) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .add(R.id.edit_location, chooseLogoFragment)
                .addToBackStack(null);

        transaction.commit();
    }


    /** display locations based on search term */
    private void previewLocations(String searchTerm) {
        this.locationRepository.search(searchTerm, this.locationResults);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String searchTerm = s.toString();

        this.previewLocations(searchTerm);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().equals("")) this.locationResults.setValue(Collections.emptyList());
    }
}
