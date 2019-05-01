package com.endeavour.saddam.pharmacyautomation;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.endeavour.saddam.pharmacyautomation.adapter.ChartAdapter;
import com.endeavour.saddam.pharmacyautomation.db.AppDatabase;
import com.endeavour.saddam.pharmacyautomation.db.dao.MedicineDAO;
import com.endeavour.saddam.pharmacyautomation.db.dto.StatsDTO;
import com.endeavour.saddam.pharmacyautomation.utils.Utils;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RevenueActivity extends AppCompatActivity {

    private ChartAdapter mAdapter;
    private MedicineDAO medicineDAO;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue);

        medicineDAO = AppDatabase.getInstance(this).medicineDAO();
        mHandler = new Handler();

        initView();
        initData();
    }

    private void initView() {
        mAdapter = new ChartAdapter(this, new ArrayList<BarData>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //final List<StatsDTO> stats = medicineDAO.selectSoldStats();
                final List<StatsDTO> stats = Utils.dummyData();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        loadData(stats);
                    }
                });
            }
        }).start();
    }

    private void loadData(List<StatsDTO> stats) {
        Map<String, List<BarEntry>> entriesMap = new LinkedHashMap<>();

        for (StatsDTO statsDTO : stats) {
            String label = Utils.formatLabel(statsDTO.getYear(), statsDTO.getMonth());
            List<BarEntry> entries = entriesMap.get(label);

            if (entries == null) {
                entries = new ArrayList<>();
                entriesMap.put(label, entries);
            }

            entries.add(new BarEntry(statsDTO.getDay(), statsDTO.getValue()));
        }

        List<BarData> barDataList = new ArrayList<>();

        for (Map.Entry<String, List<BarEntry>> entry : entriesMap.entrySet()) {
            BarDataSet barDataSet = new BarDataSet(entry.getValue(), entry.getKey());
            barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

            ArrayList<IBarDataSet> iBarDataSets = new ArrayList<>();
            iBarDataSets.add(barDataSet);

            BarData barData = new BarData(iBarDataSets);
            barData.setBarWidth(0.9f);


            barDataList.add(barData);
        }

        mAdapter.setData(barDataList);
        refresh();
    }

    private void refresh() {
        AppCompatTextView emptyText = findViewById(R.id.empty_text);
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
