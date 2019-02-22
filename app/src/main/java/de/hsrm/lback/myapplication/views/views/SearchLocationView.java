package de.hsrm.lback.myapplication.views.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import de.hsrm.lback.myapplication.R;
import de.hsrm.lback.myapplication.helpers.adapters.LocationSearchAdapter;

public class SearchLocationView extends LinearLayout{
    private EditText locationText;
    private ListView searchResults;
    private LocationSearchAdapter searchResultsAdapter;


    public SearchLocationView(Context context) {
        super(context);
    }

    public SearchLocationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SearchLocationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SearchLocationView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

}
