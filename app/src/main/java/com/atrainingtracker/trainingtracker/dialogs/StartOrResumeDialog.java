package com.atrainingtracker.trainingtracker.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.atrainingtracker.R;
import com.atrainingtracker.trainingtracker.TrainingApplication;
import com.atrainingtracker.trainingtracker.interfaces.StartOrResumeInterface;

import java.util.Objects;

public class StartOrResumeDialog extends DialogFragment {
    public static final String TAG = StartOrResumeDialog.class.getName();
    private static final boolean DEBUG = TrainingApplication.DEBUG && false;

    private StartOrResumeInterface mStartOrResumeInterface;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mStartOrResumeInterface = (StartOrResumeInterface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity + " must implement ChooseStartOrResumeInterface");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("UseRequireInsteadOfGet") AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialogBuilder.setMessage(R.string.start_or_resume_dialog_message);
        // alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(R.string.start_new_workout, (dialog, id) -> {
            mStartOrResumeInterface.chooseStart();
            dialog.cancel();
        });

        alertDialogBuilder.setNegativeButton(R.string.resume_workout, (dialog, id) -> {
            mStartOrResumeInterface.chooseResume();
            dialog.cancel();
        });

        return alertDialogBuilder.create();
    }
}
