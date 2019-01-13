package de.hsrm.lback.myapplication.views.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.ResourcesHelper;
import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.models.repositories.LocationRepository;
import de.hsrm.lback.myapplication.viewmodels.LocationViewModel;

public class EditLocationView extends AppCompatActivity {

    private ImageView locationLogo;
    private EditText locationText;
    private LocationViewModel viewModel;
    private LiveData<Location> locationLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location_view);
        Toolbar toolbar = findViewById(R.id.edit_toolbar);
        setSupportActionBar(toolbar);

        this.locationText = findViewById(R.id.location_name);
        this.locationLogo = findViewById(R.id.location_logo);


        // retrieve location
        int locationUid = getIntent().getIntExtra(Location.LOCATION_UID, -1);

        LocationRepository locationRepository = new LocationRepository(this);

        this.viewModel = new LocationViewModel(getApplication());

        if (locationUid != 0)
            this.locationLiveData = locationRepository.get(locationUid);
        else {
            this.locationLiveData = new MutableLiveData<>();
            ((MutableLiveData<Location>)this.locationLiveData)
                    .setValue(new Location("", 0));
        }

        this.locationLiveData.observe(this, this::onLocationChange);

        this.locationLogo.setOnClickListener(this::onLogoClick);

    }

    private void onLogoChange(String s) {
        int resId = ResourcesHelper.getResId(s, R.drawable.class);
        if (resId > 0)
            this.locationLogo.setImageResource(resId);
    }

    private void onLocationChange(Location location) {
        if (location != null) {
            this.locationText.setText(location.getName().getValue());

            if (this.viewModel.getLocation() == null) {
                this.viewModel.init(location);
                this.viewModel.getLocation().getLogo().observe(this, this::onLogoChange);
            }
        }
    }

    private void onSubmit() {
        // save data to location object
        this.viewModel.getLocation().setName(this.locationText.getText().toString());

        // save location
        this.viewModel.update();

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
        // TODO make real imagechooser to choose from list of icons
        this.viewModel.getLocation().getLogo().setValue("ic_done_black_24dp");
    }
}
