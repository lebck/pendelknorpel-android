package de.hsrm.lback.myapplication.models.repositories.tasks;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.persistence.LocationDao;

public class UpdateAsyncTask extends LocationAsyncTask {

    public UpdateAsyncTask(LocationDao locationDao) {
        super(locationDao);
    }

    @Override
    protected Void doInBackground(final Location... params) {
        locationDao.update(params[0]);
        return null;
    }
}

