package com.endeavour.saddam.pharmacyautomation;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.endeavour.saddam.pharmacyautomation.adapter.LogAdapter;
import com.endeavour.saddam.pharmacyautomation.db.AppDatabase;
import com.endeavour.saddam.pharmacyautomation.db.dao.MedicineDAO;
import com.endeavour.saddam.pharmacyautomation.db.dto.SoldDTO;
import com.endeavour.saddam.pharmacyautomation.db.dto.SoldMedicineDTO;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity {

    private MedicineDAO medicineDAO;
    private LogAdapter mAdapter;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        medicineDAO = AppDatabase.getInstance(this).medicineDAO();
        mHandler = new Handler();

        mAdapter = new LogAdapter(this, new ArrayList<SoldDTO>());

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        initData();
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<SoldDTO> soldMedicines = medicineDAO.selectSoldMedicineLog();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(soldMedicines);
                        refresh();
                    }
                });
            }
        }).start();
    }

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
}
