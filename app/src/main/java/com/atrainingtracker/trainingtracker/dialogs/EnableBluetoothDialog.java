package com.atrainingtracker.trainingtracker.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.atrainingtracker.R;
import com.atrainingtracker.banalservice.Protocol;
import com.atrainingtracker.trainingtracker.TrainingApplication;
import com.atrainingtracker.trainingtracker.interfaces.RemoteDevicesSettingsInterface;

public class EnableBluetoothDialog extends DialogFragment {
    public static final String TAG = EnableBluetoothDialog.class.getName();
    private static final boolean DEBUG = TrainingApplication.DEBUG && false;

    RemoteDevicesSettingsInterface mRemoteDevicesSettingsInterface = null;

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mRemoteDevicesSettingsInterface = (RemoteDevicesSettingsInterface) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity + " must implement RemoteDevicesSettingsInterface");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(requireContext());
        alertDialogBuilder.setMessage(R.string.bluetooth_disabled_on_device).setCancelable(false).setPositiveButton(R.string.enable_bluetooth, new DialogInterface.OnClickListener() {
            @SuppressLint("MissingPermission")
            public void onClick(DialogInterface dialog, int id) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter.enable();
                mRemoteDevicesSettingsInterface.startPairing(Protocol.BLUETOOTH_LE);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.Cancel, (dialog, id) -> dialog.cancel());

        return alertDialogBuilder.create();
    }
}
