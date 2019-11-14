package de.hsrm.lback.myapplication.domains.location.views.edit;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.domains.location.models.Location;
import de.hsrm.lback.myapplication.domains.location.views.edit.components.search.SearchLocationFragment;
import de.hsrm.lback.myapplication.helpers.BackgroundManager;
import de.hsrm.lback.myapplication.helpers.ResourcesHelper;
import de.hsrm.lback.myapplication.helpers.adapters.LocationLogoAdapter;
import de.hsrm.lback.myapplication.services.LocationService;

import static de.hsrm.lback.myapplication.domains.location.views.edit.EditLocationViewModel.ANONYMOUS_UID;

public class EditLocationActivity extends AppCompatActivity {

    public static final int ANONYMOUS_SRC = 0;
    public static final int ANONYMOUS_TARGET = 1;

    private ImageView locationLogo;
    private EditText displayName;
    private RecyclerView logoChooser;
    private RelativeLayout logoContainer;
    private SearchLocationFragment searchLocationFragment;

    private Transition scaleTransition;

    private EditLocationViewModel viewModel;
    private LocationService locationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        this.locationLogo = findViewById(R.id.location_logo);
        this.displayName = findViewById(R.id.edit_location_display_name);
        this.logoChooser = findViewById(R.id.logo_chooser);
        this.logoContainer = findViewById(R.id.logo_container);
        this.searchLocationFragment = (SearchLocationFragment) this.getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_search_location);


        this.locationService = new LocationService(this);



        // retrieve location and set into viewModel
        int locationUid = getIntent().getIntExtra(Location.LOCATION_UID, ANONYMOUS_UID);
        this.viewModel = ViewModelProviders.of(
                this,
                new EditLocationViewModel.Factory(getApplication(), locationUid)
        ).get(EditLocationViewModel.class);

        // set changeListener
        this.viewModel.getLocationData().observe(this, this::onLocationChange);
        this.searchLocationFragment.setViewModel(this.viewModel);

        this.locationLogo.setOnClickListener(this::onLogoClick);
        this.logoChooser.setAdapter(new LocationLogoAdapter(ResourcesHelper.getLogoList(), this::onLogoChosen));

        scaleTransition =
                TransitionInflater.from(this).inflateTransition(R.transition.scale_transition);

    }

    private Bitmap createBackground() {
        return BackgroundManager.getBlurryBackground(this.findViewById(R.id.edit_location));
    }

    private void onLogoChosen(String s) {
        // update logo
        viewModel.setLogo(s);
        hideLogoChooser();
    }

    private void showLogoChooser() {
        TransitionManager.beginDelayedTransition(logoContainer, scaleTransition);
        ViewGroup.LayoutParams params = logoChooser.getLayoutParams();
        params.height = 380;
        logoChooser.setLayoutParams(params);
        logoChooser.setVisibility(View.VISIBLE);


    }

    private void hideLogoChooser() {
        TransitionManager.beginDelayedTransition(logoContainer, scaleTransition);
        ViewGroup.LayoutParams params = logoChooser.getLayoutParams();
        params.height = 0;
        logoChooser.setLayoutParams(params);
        logoChooser.setVisibility(View.GONE);
    }

    private void onLocationChange(@Nullable Location location) {
        if (location != null) {
            // set display name
            this.displayName.setText(location.getDisplayName());

            // set Logo
            int resId = ResourcesHelper.getResId(location.getLogo(), R.drawable.class);
            if (resId > 0)
                this.locationLogo.setImageResource(resId);

        }
    }

    private void onSubmit() {
        this.viewModel.setDisplayName(this.displayName.getText().toString());
        // save location if not anonymous
        if (!this.viewModel.update()) {
            Intent result = new Intent();

            result.putExtra(Location.SERIALIZED_LOCATION,
                    LocationService.serializeLocation(viewModel.getLocation())
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
        } else if (id == R.id.delete) {
            this.onDelete();
            return true;
        }

        return false;

    }

    private void onDelete() {
        viewModel.delete();
        finish();
    }

    /**
     * Process click on logo
     */
    public void onLogoClick(View v) {
        showLogoChooser();
    }


}
