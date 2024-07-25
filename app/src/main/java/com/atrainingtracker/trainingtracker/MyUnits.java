package com.atrainingtracker.trainingtracker;

import androidx.annotation.NonNull;

import com.atrainingtracker.R;

public enum MyUnits {
    METRIC(R.string.units_type_metric), IMPERIAL(R.string.units_type_imperial);

    private int nameId;

    MyUnits(int nameId) {
        this.nameId = nameId;
    }

    public int getNameId() {
        return nameId;
    }

    @NonNull
    @Override
    public String toString() {
        return TrainingApplication.getAppContext().getString(nameId);
    }
}
