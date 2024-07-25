package com.atrainingtracker.trainingtracker.exporter;

import androidx.annotation.NonNull;

import com.atrainingtracker.R;
import com.atrainingtracker.trainingtracker.TrainingApplication;

public enum ExportType {
    FILE(R.string.SD_card, FileFormat.values()), DROPBOX(R.string.Dropbox, new FileFormat[]{FileFormat.CSV, FileFormat.GC, FileFormat.GPX, FileFormat.TCX}), COMMUNITY(R.string.Community, new FileFormat[]{FileFormat.RUNKEEPER, FileFormat.STRAVA, FileFormat.TRAINING_PEAKS});

    private final int uiId;
    private final FileFormat[] exportToFileFormats;

    ExportType(int uiId, FileFormat[] exportToFileFormats) {
        this.uiId = uiId;
        this.exportToFileFormats = exportToFileFormats;
    }

    @NonNull
    @Override
    public String toString() {
        return TrainingApplication.getAppContext().getString(uiId);
    }

    public int getUiId() {
        return uiId;
    }

    public FileFormat[] getExportToFileFormats() {
        return exportToFileFormats;
    }
}
