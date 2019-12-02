package de.hsrm.lback.pendelknorpel.domains.location.views.edit.components.logo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

import de.hsrm.lback.pendelknorpel.R;
import de.hsrm.lback.pendelknorpel.util.ResourcesHelper;
import de.hsrm.lback.pendelknorpel.util.adapters.IconAdapter;


/**
 * provides a list of logos for the user to choose from
 */
public class ChooseLogoFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    private OnFragmentBackgroundCallbackListener backgroundCallBack;

    public ChooseLogoFragment() {
        // Required empty public constructor
    }

    public static ChooseLogoFragment getInstance(
            OnFragmentInteractionListener listener,
            OnFragmentBackgroundCallbackListener backgroundCallBack
    ) {
        ChooseLogoFragment fragment = new ChooseLogoFragment();

        fragment.mListener = listener;
        fragment.backgroundCallBack = backgroundCallBack;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_logo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GridView logoGrid = view.findViewById(R.id.logo_grid);

        List<String> logos = ResourcesHelper.getLogoList();

        logoGrid.setAdapter(new IconAdapter(logos, this::onLogoClick));

        // Bitmap b = backgroundCallBack.onSetBackground();

        //view.findViewById(R.id.choose_icon).setBackground(new BitmapDrawable(getResources(), b));

    }

    private void onLogoClick(View view) {
        String logo = (String) view.findViewById(R.id.location_logo).getTag();
        mListener.onFragmentInteraction(logo);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String logoString);
    }

    public interface OnFragmentBackgroundCallbackListener {
        Bitmap onSetBackground();
    }
}
