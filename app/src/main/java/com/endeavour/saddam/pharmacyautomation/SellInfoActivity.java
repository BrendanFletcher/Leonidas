package com.endeavour.saddam.pharmacyautomation;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.endeavour.saddam.pharmacyautomation.adapter.LogAdapter;
import com.endeavour.saddam.pharmacyautomation.adapter.SellInfoAdapter;
import com.endeavour.saddam.pharmacyautomation.db.AppDatabase;
import com.endeavour.saddam.pharmacyautomation.db.dao.MedicineDAO;
import com.endeavour.saddam.pharmacyautomation.db.dto.SoldDTO;
import com.endeavour.saddam.pharmacyautomation.utils.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SellInfoActivity extends AppCompatActivity {

    private static final String TAG = "SellInfoActivity";

    //private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private Button mDisplayDate;
    private long time1 = 1547320498899L;
    private MedicineDAO medicineDAO;
    private SellInfoAdapter mAdapter;
    private Handler mHandler;
    private List<SoldDTO> sellInfoMedicineList = new ArrayList<SoldDTO>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_info);
        //mDisplayDate = (TextView) findViewById(R.id.tvDate);
        mDisplayDate = (Button) findViewById(R.id.btnDate);
        medicineDAO = AppDatabase.getInstance(this).medicineDAO();
        mHandler = new Handler();

        mAdapter = new SellInfoAdapter(this, mHandler, sellInfoMedicineList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_view_sell_info);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        //initData();
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SellInfoActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;

                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(year, month, day);
//                Date da = calendar.getTime();
//                String hel = fmt.format(da);


                //String date = month + "/" + day + "/" + year;
                String updatedDate = day + "/" + month + "/" + year;
                Log.d(TAG, "onDateSet: dd/mm/yyy: " + day + "/" + month + "/" + year);

//                String dtStart = "2010-10-15T09:27:37Z";
//                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//                try {
//                    Date date = null;
//                    try {
//                        date = fmt.parse(updatedDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                mDisplayDate.setText(updatedDate);
                Date date = new Date(year, month,day);
                updateUI();
                loadData(day, month, year);

//                //Calendar cal1 = Calendar.getInstance();
//                SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
//                Date date2 = new Date();
//                date2.setTime(time1);
//                int longDay  = Integer.parseInt((String) DateFormat.format("dd",   date2));
//                int monthNumber  = Integer.parseInt((String) DateFormat.format("MM",   date2));
//                int longYear         = Integer.parseInt((String) DateFormat.format("yyyy", date2));
//                boolean sameDay = longDay == day && monthNumber == month && longYear == year;
//                Log.d(TAG, " WELL : " + sameDay + " LONG_DAY " + longDay + " LONG_MONTH " + monthNumber
//                        + " LONG_YEAR " + longYear);
            }
        };
        initSingleDayData();
    }

    private void updateUI() {
        sellInfoMedicineList = new ArrayList<SoldDTO>();
        //mAdapter.updateTotalPriceView();
//        mAdapter = new SellInfoAdapter(this, mHandler, sellInfoMedicineList);
    }

    private void initSingleDayData() {
        Date date = getInitialTime();
        int day  = Integer.parseInt((String) DateFormat.format("dd",   date));
        int month  = Integer.parseInt((String) DateFormat.format("MM",   date));
        int year = Integer.parseInt((String) DateFormat.format("yyyy", date));
        loadData(day, month, year);
    }

    private void loadData(int day, int month, int year) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
       // Log.d(TAG, "onDateSet: mm/dd/yyy: " + dateFormat.format(date1));
        new Thread(new Runnable() {
            SoldDTO soldDTO = null;
            @Override
            public void run() {
                final List<SoldDTO> medicines;
                final List<SoldDTO> temp = new ArrayList<SoldDTO>();
                medicines = medicineDAO.selectSoldByName();
                mAdapter.setTotalPrice(0, 0);
                for (SoldDTO sDTO : medicines) {
                    Date date2 = sDTO.getTime();

                    int day2  = Integer.parseInt((String) DateFormat.format("dd",   date2));
                    int month2  = Integer.parseInt((String) DateFormat.format("MM",   date2));
                    int year2 = Integer.parseInt((String) DateFormat.format("yyyy", date2));
                    Log.d(TAG, "onLoadData: dd/mm/yyy: " + day2 + "/" + month2 + "/" + year2);
                    if(day == day2 && month == month2 && year == year2){
                        temp.add(sDTO);
                    }
                }
                //medicines.removeAll(temp);
                if(temp.size() >= 1){
                    soldDTO = temp.get(0);
                }
                int size = temp.size();
                for(int i=1; i< size; i++){
                    SoldDTO model = temp.get(i);
                    if(model.getMedicineName().equalsIgnoreCase(soldDTO.getMedicineName())){
                        soldDTO.setQuantity(soldDTO.getQuantity() + model.getQuantity());
                        soldDTO.setPrice(soldDTO.getPrice() + model.getPrice());
                    }
                    else{
                        sellInfoMedicineList.add(soldDTO);
                        //Constants.TOTAL_PRICE += soldDTO.getPrice();
                        mAdapter.updateTotalPrice(soldDTO.getPrice(), soldDTO.getQuantity());
                        soldDTO = new SoldDTO();
                        soldDTO = model;
//                        soldDTO.setQuantity(model.getQuantity());
//                        soldDTO.setPrice(model.getPrice());
                    }
                }
                if(soldDTO != null){
                    sellInfoMedicineList.add(soldDTO);
                    //Constants.TOTAL_PRICE += soldDTO.getPrice();
                    mAdapter.updateTotalPrice(soldDTO.getPrice(), soldDTO.getQuantity());
                }
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(sellInfoMedicineList);
                        refresh();
                        //Log.d("TAG", " SIZE: " + strList.size() + " and HELL: " + medicines.size());
                        //doup(medicines);
                    }
                });
            }
        }).start();
    }

    private Date getInitialTime() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = df.format(c);
        mDisplayDate.setText(formattedDate);
        return c;
    }

    private void refresh() {
        TextView emptyText = findViewById(R.id.sell_info_empty_text);

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

    private void initData() {
//        Date c = Calendar.getInstance().getTime();
//        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//        String formattedDate = df.format(c);
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        Log.d(TAG, " CURRENT_DATE : " + currentDate);
        mDisplayDate.setText(currentDate);

        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy");
        Date  d = new Date();
        d.setTime(time1);
        try {
            Date c = Calendar.getInstance().getTime();
            long milliseconds = c.getTime();
            Log.d(TAG, " LONG_TIME : " + milliseconds);
            if(fmt.format(d).equals(fmt.format(c))){
                Log.d(TAG, " TRUE");
            }
            else{
                Log.d(TAG, " d = " + fmt.format(d) + " c = " + fmt.format(c));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
