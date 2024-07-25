package com.atrainingtracker.banalservice.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.atrainingtracker.R;
import com.atrainingtracker.banalservice.BANALService;
import com.atrainingtracker.banalservice.BSportType;
import com.atrainingtracker.banalservice.database.SportTypeDatabaseManager;

import java.util.List;

public class SelectSportTypeDialog extends DialogFragment {
    public static final String TAG = SelectSportTypeDialog.class.getName();
    private static final boolean DEBUG = BANALService.DEBUG && false;

    private static final String B_SPORT_TYPE = "B_SPORT_TYPE";

    private BSportType mBSportType;
    private BANALService.GetBanalServiceInterface mGetBanalServiceInterface;

    public static SelectSportTypeDialog newInstance(BSportType bSportType) {
        if (DEBUG) Log.i(TAG, "newInstance");

        SelectSportTypeDialog fragment = new SelectSportTypeDialog();

        Bundle args = new Bundle();
        args.putString(B_SPORT_TYPE, bSportType.name());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onAttach(@NonNull final Context context) {
        super.onAttach(context);
        if (DEBUG) Log.i(TAG, "onAttach");

        try {
            mGetBanalServiceInterface = (BANALService.GetBanalServiceInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement GetBanalServiceInterface");
        }

    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.d(TAG, "onCreate");

        mBSportType = BSportType.valueOf(requireArguments().getString(B_SPORT_TYPE));
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        List<String> sportTypeNames = SportTypeDatabaseManager.getSportTypesUiNameList(mBSportType);
        final List<Long> sportTypeIds = SportTypeDatabaseManager.getSportTypesIdList(mBSportType);

        String bSportTypeName;
        switch (mBSportType) {
            case RUN:
                bSportTypeName = getString(R.string.sport_type_run);
                break;

            case ROWING:
                bSportTypeName = getString(R.string.sport_type_row);
                break;

            default:
                bSportTypeName = getString(R.string.sport_type_other);
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle(getString(R.string.select_sport_type_format, bSportTypeName));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1);
        arrayAdapter.addAll(sportTypeNames);

        builder.setAdapter(arrayAdapter, (dialog, which) -> {
            mGetBanalServiceInterface.getBanalServiceComm().setUserSelectedSportTypeId(sportTypeIds.get(which));
            dialog.cancel();
        });

        return builder.create();
    }
}
