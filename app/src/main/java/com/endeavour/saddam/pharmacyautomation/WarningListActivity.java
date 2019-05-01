package com.endeavour.saddam.pharmacyautomation;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.endeavour.saddam.pharmacyautomation.adapter.MedicineAdapter;
import com.endeavour.saddam.pharmacyautomation.db.AppDatabase;
import com.endeavour.saddam.pharmacyautomation.db.dao.MedicineDAO;
import com.endeavour.saddam.pharmacyautomation.db.dto.MedicineDTO;
import com.endeavour.saddam.pharmacyautomation.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class WarningListActivity extends AppCompatActivity {
    String TAG = WarningListActivity.class.getName();
    private MedicineDAO medicineDAO;
    private MedicineAdapter mAdapter;
    private Handler mHandler;
    private List<MedicineDTO> warningList = new ArrayList<MedicineDTO>();
    private static TextView textCartItemCount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_list);
        medicineDAO = AppDatabase.getInstance(this).medicineDAO();
        mHandler = new Handler();
        mAdapter = new MedicineAdapter(this, mHandler, warningList, Constants.WARNING_TYPE);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recycler_view_warning);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        initData();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                warningList = medicineDAO.selectWarningList();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(warningList, Constants.WARNING_TYPE);
                        refresh();
                    }
                });
            }
        }).start();
    }
//    public static void warnCount(TextView warningCount){
//        textCartItemCount = warningCount;
//    }
//
//    public static TextView getWarnCount(){
//        return textCartItemCount;
//    }

    private void refresh() {
        TextView emptyText = findViewById(R.id.empty_text);

        if (mAdapter.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.Request.NEW_MEDICINE && resultCode == RESULT_OK) {
            new UpdateWarningList().execute("");
            long id = data.getLongExtra(Constants.Key.ID, 0);
//            if (id > 0 && mSelectedMenu != null) {
//                onNavigationItemSelected(mSelectedMenu);
//            }
        }
    }

    public class UpdateWarningList extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            int warningListCount = 0;
            try {
                warningList = medicineDAO.selectWarningList();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return warningList.size();
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(Integer count) {
            if( count> 0){
                Log.d("WarningListUpdatedVal", " = " + count);
                mAdapter.setData(warningList, Constants.WARNING_TYPE);
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}
