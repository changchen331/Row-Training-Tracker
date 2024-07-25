package com.atrainingtracker.banalservice.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.atrainingtracker.R;
import com.atrainingtracker.banalservice.BANALService;
import com.atrainingtracker.trainingtracker.MyHelper;

import java.util.Objects;

public class SetCalibrationFactorDialogFragment extends DialogFragment {
    public static final String TAG = "SetCalibrationFactorDialogFragment";
    private static final boolean DEBUG = BANALService.DEBUG & false;
    private static final String CALIBRATION_FACTOR = "CALIBRATION_FACTOR";
    private static final String TITLE_NAME = "TITLE_NAME";
    private static final String FIELD_NAME = "FIELD_NAME";
    private String mTitleName;
    private String mFieldName;
    private String mCalibrationFactor = 1.0 + "";
    private double mOldCalibrationFactor = 1;
    private EditText etCalibrationFactor;
    private EditText etMeasured;
    private EditText etCorrect;
    private NewCalibrationFactorListener mNewCalibrationFactorListener = null;
    private TextWatcher calibrationDistancesChangedWatcher = new TextWatcher() {

        @SuppressLint({"LongLogTag", "SetTextI18n"})
        @Override
        public void afterTextChanged(Editable s) {
            if (DEBUG) Log.d(TAG, "afterTextChanged");
            double measuredDistance = MyHelper.string2Double(etMeasured.getText().toString());
            double correctDistance = MyHelper.string2Double(etCorrect.getText().toString());

            etCalibrationFactor.setText(mOldCalibrationFactor / measuredDistance * correctDistance + "");
        }

        @SuppressLint("LongLogTag")
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (DEBUG) Log.d(TAG, "beforeTextChanged");
        }

        @SuppressLint("LongLogTag")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (DEBUG) Log.d(TAG, "onTextChanged");
        }
    };

    public static SetCalibrationFactorDialogFragment newInstance(String calibrationFactor, String titleName, String fieldName) {
        SetCalibrationFactorDialogFragment fragment = new SetCalibrationFactorDialogFragment();

        Bundle args = new Bundle();
        args.putString(CALIBRATION_FACTOR, calibrationFactor);
        args.putString(TITLE_NAME, titleName);
        args.putString(FIELD_NAME, fieldName);
        fragment.setArguments(args);

        return fragment;
    }

    public void setNewCalibrationFactorListener(NewCalibrationFactorListener listener) {
        mNewCalibrationFactorListener = listener;
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.i(TAG, "onCreate()");

        mCalibrationFactor = requireArguments().getString(CALIBRATION_FACTOR);
        mTitleName = requireArguments().getString(TITLE_NAME);
        mFieldName = requireArguments().getString(FIELD_NAME);
        mOldCalibrationFactor = MyHelper.string2Double(mCalibrationFactor);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        builder.setTitle(getString(R.string.Set_foo, mTitleName));

        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout

        final View mainDialog = inflater.inflate(R.layout.dialog_set_calibration_factor, null);

        etMeasured = mainDialog.findViewById(R.id.etMeasured);
        etMeasured.addTextChangedListener(calibrationDistancesChangedWatcher);

        etCorrect = mainDialog.findViewById(R.id.etCorrect);
        etCorrect.addTextChangedListener(calibrationDistancesChangedWatcher);

        etCalibrationFactor = mainDialog.findViewById(R.id.etCalibrationFactor);
        etCalibrationFactor.setText(mCalibrationFactor);

        ((TextView) mainDialog.findViewById(R.id.tvCalibrationFactor)).setText(mFieldName + ":");

        builder.setView(mainDialog);

        // Add action buttons
        builder.setPositiveButton(getString(R.string.OK), (dialog, id) -> mNewCalibrationFactorListener.newCalibrationFactor(etCalibrationFactor.getText().toString()));
        builder.setNegativeButton(R.string.Cancel, (dialog, id) -> Objects.requireNonNull(SetCalibrationFactorDialogFragment.this.getDialog()).cancel());
        return builder.create();
    }

    public interface NewCalibrationFactorListener {
        void newCalibrationFactor(String calibrationFactor);
    }

}
