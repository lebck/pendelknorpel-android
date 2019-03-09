package de.hsrm.lback.myapplication.models.repositories.tasks;

import android.os.AsyncTask;

import de.hsrm.lback.myapplication.models.Location;

public class LocationAsyncTask extends AsyncTask<Location, Void, Void> {

    private Runner runner;

    public LocationAsyncTask(Runner runner) {
        this.runner = runner;
    }

    @Override
    protected Void doInBackground(final Location... locations) {
        runner.run(locations);
        return null;
    }

    public interface Runner {
        void run(final Location... locations);
    }
}
