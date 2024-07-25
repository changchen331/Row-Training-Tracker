package com.atrainingtracker.banalservice.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.atrainingtracker.R;
import com.atrainingtracker.banalservice.BANALService;
import com.atrainingtracker.banalservice.Protocol;
import com.atrainingtracker.banalservice.database.DevicesDatabaseManager.DevicesDbHelper;
import com.atrainingtracker.banalservice.devices.DeviceType;
import com.atrainingtracker.banalservice.helpers.UIHelper;
import com.atrainingtracker.trainingtracker.database.EquipmentDbHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DeviceListCursorAdapter extends SimpleCursorAdapter {
    public static final String[] COLUMNS = new String[]{DevicesDbHelper.C_ID, DevicesDbHelper.PROTOCOL, DevicesDbHelper.DEVICE_TYPE, DevicesDbHelper.NAME, DevicesDbHelper.PAIRED};
    protected static final String[] FROM = {DevicesDbHelper.DEVICE_TYPE, DevicesDbHelper.NAME, DevicesDbHelper.C_ID, DevicesDbHelper.PAIRED, DevicesDbHelper.C_ID};
    protected static final int[] TO = {R.id.ivIcon, R.id.tvDeviceName, R.id.tvMainValue, R.id.cbPaired, R.id.tvEquipment};
    private static final String TAG = "DeviceListCursorAdapter";
    private static final boolean DEBUG = BANALService.DEBUG & false;
    protected PairingChangedInterface mPairingChangedInterface;
    protected Context mContext;
    protected Map<Long, TextView> mDeviceId2tvMainValue;

    public DeviceListCursorAdapter(Context context, Cursor cursor, PairingChangedInterface pairingChangedInterface) {
        super(context, R.layout.device_list_row, cursor, FROM, TO);
        // TODO Auto-generated constructor stub

        mContext = context;
        mPairingChangedInterface = pairingChangedInterface;
        mDeviceId2tvMainValue = new HashMap<>();
    }

    public void deleteLookupTable() {
        mDeviceId2tvMainValue = new HashMap<>();
    }

    @Override
    public void bindView(View row, Context context, Cursor cursor) {
        if (DEBUG) Log.d(TAG, "bindView");
        super.bindView(row, context, cursor);

        // get the views
        ImageView ivIcon = row.findViewById(R.id.ivIcon);
        TextView tvDeviceName = row.findViewById(R.id.tvDeviceName);
        TextView tvMainValue = row.findViewById(R.id.tvMainValue);
        CheckBox cbPaired = row.findViewById(R.id.cbPaired);
        TextView tvEquipment = row.findViewById(R.id.tvEquipment);

        // get the data
        @SuppressLint("Range") DeviceType deviceType = DeviceType.valueOf(cursor.getString(cursor.getColumnIndex(DevicesDbHelper.DEVICE_TYPE)));
        @SuppressLint("Range") Protocol protocol = Protocol.valueOf(cursor.getString(cursor.getColumnIndex(DevicesDbHelper.PROTOCOL)));
        @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(DevicesDbHelper.NAME));
        @SuppressLint("Range") int paired = cursor.getInt(cursor.getColumnIndex(DevicesDbHelper.PAIRED));
        @SuppressLint("Range") final long deviceId = cursor.getLong(cursor.getColumnIndex(DevicesDbHelper.C_ID));

        // bind the data to the views
        ivIcon.setImageResource(UIHelper.getIconId(deviceType, protocol));
        tvDeviceName.setText(name);
        if (mDeviceId2tvMainValue.get(deviceId) == null) {
            tvMainValue.setText(R.string.NoData);
            mDeviceId2tvMainValue.put(deviceId, tvMainValue);  // will be updated somewhere else
        }
        cbPaired.setText(R.string.pairedText);
        cbPaired.setText(""); // make an empty text
        cbPaired.setChecked(paired > 0);
        cbPaired.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (DEBUG) Log.d(TAG, "onPairingChanged");
            mPairingChangedInterface.onPairingChanged(deviceId, isChecked);
        });

        String equipment = new EquipmentDbHelper(mContext).getLinkedEquipmentStringFromDeviceId(deviceId);
        if (equipment != null) {
            tvEquipment.setText(equipment);
            tvEquipment.setVisibility(View.VISIBLE);
        } else {
            // tvEquipment.setVisibility(View.INVISIBLE);
            tvEquipment.setVisibility(View.GONE);
        }
    }

    public void updateMainField(long deviceId, String value) {
        TextView textView = mDeviceId2tvMainValue.get(deviceId);
        if (textView != null) {
            textView.setText(value);
        }
    }

    public Set<Long> getDeviceIds() {
        return mDeviceId2tvMainValue.keySet();
    }

    public interface PairingChangedInterface {
        void onPairingChanged(long deviceId, boolean paired);
    }

}
