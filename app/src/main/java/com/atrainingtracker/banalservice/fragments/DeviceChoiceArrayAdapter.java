package com.atrainingtracker.banalservice.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.atrainingtracker.R;
import com.atrainingtracker.banalservice.Protocol;
import com.atrainingtracker.banalservice.devices.DeviceType;
import com.atrainingtracker.banalservice.helpers.UIHelper;

import java.util.List;
import java.util.Objects;

public class DeviceChoiceArrayAdapter extends ArrayAdapter<DeviceType> {
    Context mContext;
    Protocol mProtocol;

    public DeviceChoiceArrayAdapter(Context context, int resourceId, List<DeviceType> items, Protocol protocol) {
        super(context, resourceId, items);
        mContext = context;
        mProtocol = protocol;
    }

    @SuppressLint("InflateParams")
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        DeviceType deviceType = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.device_choice_row, null);
            holder = new ViewHolder();
            holder.txtTitle = convertView.findViewById(R.id.title);
            holder.imageView = convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txtTitle.setText(UIHelper.getNameId(Objects.requireNonNull(deviceType)));

        // TODO use this (one text view with compound drawable instead of this complex fucking stuff)
        // holder.txtTitle.setCompoundDrawablesWithIntrinsicBounds(antDeviceType.getImageId(), 0, 0, 0);
        // TODO: READ ABOVE COMMENT!
        holder.imageView.setImageResource(UIHelper.getIconId(deviceType, mProtocol));
        return convertView;
    }

    /*private view holder class*/
    private static class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
    }
}
