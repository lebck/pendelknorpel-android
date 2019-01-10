package de.hsrm.lback.myapplication.models.repositories.tasks;

import de.hsrm.lback.myapplication.models.Location;
import de.hsrm.lback.myapplication.persistence.LocationDao;

public class InsertAsyncTask extends LocationAsyncTask {
    public InsertAsyncTask(LocationDao locationDao) {
        super(locationDao);
    }

    @Override
    protected Void doInBackground(final Location... params) {
        locationDao.insert(params[0]);
        return null;
    }
}
