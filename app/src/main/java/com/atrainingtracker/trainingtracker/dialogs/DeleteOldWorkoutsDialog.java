package com.atrainingtracker.trainingtracker.dialogs;

import static androidx.appcompat.app.AlertDialog.Builder;

import android.app.Dialog;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.atrainingtracker.R;
import com.atrainingtracker.trainingtracker.TrainingApplication;
import com.atrainingtracker.trainingtracker.database.WorkoutSummariesDatabaseManager;
import com.atrainingtracker.trainingtracker.helpers.DeleteWorkoutTask;

import java.util.List;

public class DeleteOldWorkoutsDialog extends DialogFragment {
    public static final String TAG = DeleteOldWorkoutsDialog.class.getName();
    private static final boolean DEBUG = TrainingApplication.DEBUG && false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Builder alertDialogBuilder = new Builder(requireContext());
        alertDialogBuilder.setTitle(R.string.deleteOldWorkouts);
        alertDialogBuilder.setMessage(R.string.deleteWorkoutsThatAreOlderThanDays);
        final EditText input = new EditText(getContext());
        input.setText(R.string.defaultDaysToKeep);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);  // TODO: recheck!
        alertDialogBuilder.setView(input);
        alertDialogBuilder.setPositiveButton(R.string.OK, (dialog, whichButton) -> {
            try {
                int daysToKeep = Integer.parseInt(input.getText().toString());
                List<Long> oldWorkoutIds = WorkoutSummariesDatabaseManager.getOldWorkouts(daysToKeep);

                (new DeleteWorkoutTask(getContext())).execute(oldWorkoutIds.toArray(new Long[0]));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.Cancel, null);
        return alertDialogBuilder.create();
    }
}
