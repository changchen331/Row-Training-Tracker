package com.atrainingtracker.trainingtracker.fragments.preferences;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.ListFragment;

import com.atrainingtracker.R;
import com.atrainingtracker.trainingtracker.TrainingApplication;
import com.atrainingtracker.trainingtracker.database.WorkoutSummariesDatabaseManager;
import com.atrainingtracker.trainingtracker.database.WorkoutSummariesDatabaseManager.WorkoutSummaries;
import com.atrainingtracker.trainingtracker.dialogs.EditFancyWorkoutNameDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class FancyWorkoutNameListFragment extends ListFragment {
    public static final String TAG = FancyWorkoutNameListFragment.class.getName();
    protected static final String[] FROM = {WorkoutSummaries.FANCY_NAME, WorkoutSummaries.COUNTER};
    protected static final int[] TO = {R.id.tvWorkoutNameHashKey, R.id.tvCounter};
    private static final boolean DEBUG = TrainingApplication.DEBUG & false;
    protected Cursor mCursor;
    protected SimpleCursorAdapter mAdapter;
    protected IntentFilter mFancyWorkoutNamesChangedFilter = new IntentFilter(EditFancyWorkoutNameDialog.FANCY_WORKOUT_NAME_CHANGED_INTENT);

    // onAttach

    // onCreate

    // onCreateView

    // onActivityCreated

    // onStart

    // onResume

    // onPause

    // onStop

    // onDestroyView

    // onDestroy

    // onDetach
    BroadcastReceiver mFancyWorkoutNamesChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mCursor.requery();
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (DEBUG) Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        SQLiteDatabase db = WorkoutSummariesDatabaseManager.getInstance().getOpenDatabase();

        mCursor = db.query(WorkoutSummaries.TABLE_WORKOUT_NAME_PATTERNS, null, null, null, null, null, WorkoutSummaries.COUNTER + " DESC");

        mAdapter = new SimpleCursorAdapter(requireContext(), R.layout.workout_name_counter_row, mCursor, FROM, TO);
        setListAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.workout_name_schemes_list_layout, null);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> showEditFancyWorkoutNameDialog(-1));

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setOnItemClickListener((parent, view, position, id) -> {
            // Log.i(TAG, "onItemClick: position=" + position + ", id=" + id);
            showEditFancyWorkoutNameDialog(id);
        });

        registerForContextMenu(getListView());
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onResume() {
        super.onResume();
        requireContext().registerReceiver(mFancyWorkoutNamesChangedReceiver, mFancyWorkoutNamesChangedFilter, Context.RECEIVER_EXPORTED);
    }

    @Override
    public void onPause() {
        super.onPause();
        requireContext().unregisterReceiver(mFancyWorkoutNamesChangedReceiver);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        WorkoutSummariesDatabaseManager.getInstance().closeDatabase();
        mCursor.close();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        if (DEBUG) Log.i(TAG, "onCreateContextMenu");

        MenuInflater inflater = this.requireActivity().getMenuInflater();
        inflater.inflate(R.menu.delete, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        // int position = info.position;
        long id = Objects.requireNonNull(info).id;

        if (DEBUG) Log.i(TAG, "onContextItemSelected: id=" + id);

        switch (item.getItemId()) {
            case R.id.itemDelete:
                showReallyDeleteDialog(id);
                return true;
        }

        return false;
    }

    @SuppressLint("Range")
    private void showReallyDeleteDialog(final long id) {
        String fancyName = "";
        SQLiteDatabase db = WorkoutSummariesDatabaseManager.getInstance().getOpenDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.query(WorkoutSummaries.TABLE_WORKOUT_NAME_PATTERNS, null, WorkoutSummaries.C_ID + " =? ", new String[]{Long.toString(id)}, null, null, null);
        if (cursor.moveToFirst()) {
            fancyName = cursor.getString(cursor.getColumnIndex(WorkoutSummaries.FANCY_NAME));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.delete).setMessage(requireContext().getString(R.string.really_delete_workout_name_scheme, fancyName)).setIcon(android.R.drawable.ic_menu_delete).setPositiveButton(R.string.delete, (dialog, whichButton) -> {
            WorkoutSummariesDatabaseManager.deleteFancyName(id);
            mCursor.requery();
            mAdapter.notifyDataSetChanged();

            dialog.dismiss();
        }).setNegativeButton(R.string.Cancel, (dialog, which) -> dialog.dismiss());
        // Create the AlertDialog object and return it
        builder.create().show();
    }

    private void showEditFancyWorkoutNameDialog(final long id) {
        EditFancyWorkoutNameDialog dialog = EditFancyWorkoutNameDialog.newInstance(id);
        dialog.show(requireFragmentManager(), EditFancyWorkoutNameDialog.TAG);
    }
}
