package de.hsrm.lback.myapplication.views.journeyoverview.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    public interface DateReceiver {

        void onDateReceived(int year, int month, int dayOfMonth);
    }
    private DateReceiver receiver;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this::onDateSet, year, month, dayOfMonth);
    }

    private void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        receiver.onDateReceived(year, month+1, dayOfMonth);
    }


    public void setReceiver(DateReceiver receiver) {
        this.receiver = receiver;
    }
}
