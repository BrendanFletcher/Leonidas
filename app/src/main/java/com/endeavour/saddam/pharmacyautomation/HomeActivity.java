package com.endeavour.saddam.pharmacyautomation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SearchView;
import android.widget.TextView;

import com.endeavour.saddam.pharmacyautomation.adapter.MedicineAdapter;
import com.endeavour.saddam.pharmacyautomation.db.AppDatabase;
import com.endeavour.saddam.pharmacyautomation.db.dao.MedicineDAO;
import com.endeavour.saddam.pharmacyautomation.db.dto.MedicineDTO;
import com.endeavour.saddam.pharmacyautomation.utils.Constants;
import com.endeavour.saddam.pharmacyautomation.utils.DateConverter;
import com.endeavour.saddam.pharmacyautomation.WarningListActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener{

    private static final String TAG = HomeActivity.class.getName();

    private MedicineDAO medicineDAO;
    private MedicineAdapter mAdapter;
    private Handler mHandler;

    private MenuItem mSelectedMenu;
    private MenuItem mSearchItem;
    private MenuItem mNotificationItem;
    private int mCartItemCount = 0;
    private TextView textCartItemCount;
    private List<MedicineDTO> strList = new ArrayList<MedicineDTO>();
    //ArrayList<MedicineDTO>medicineDTO = new ArrayList<>();
    private MedicineDTO medicineDTO[] = new MedicineDTO[6453];
    private MedicineDTO mDTO;
    FloatingActionButton fab;
    private PopupWindow mNotificationPopup;
    private int navigationType = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        medicineDAO = AppDatabase.getInstance(this).medicineDAO();
        mHandler = new Handler();

        fab = findViewById(R.id.fab);
        //fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startChildActivityIntent = new Intent(HomeActivity.this, NewMedicineActivity.class);
                startActivityForResult(startChildActivityIntent, Constants.Request.NEW_MEDICINE);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //initD();
       // new InitPrimaryData().execute("");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    if(medicineDAO.getTotalRows() > 1){
//                        Log.d("HomeActivity ", String.valueOf(medicineDAO.getTotalRows()));
//                    }
//                    else {
//                        Log.d("HomeActivity ", " Fuck " + String.valueOf(medicineDAO.getTotalRows()));
//                    }
//                }catch (Exception ex){
//                    ex.printStackTrace();
//                }
//            }
//        });

//        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                       // initPrimaryData();
//                        Log.d(TAG, " ROWS = " + medicineDAO.getTotalRows());
//                        if(!isDatabaseExist(HomeActivity.this, AppDatabase.DATABASE_NAME)) {
//                            Log.d("HomeActivity", "DB not exists");
//                            initPrimaryData();    // use it to load data primarily
//                        }
//                    } catch (Exception ex){
//                        ex.printStackTrace();
//                    }
//                }
//            });
////            if(!isDatabaseExist(this, AppDatabase.DATABASE_NAME)) {
////                Log.d("HomeActivity", "DB not exists");
////                initPrimaryData();    // use it to load data primarily
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        finally {
//            mAdapter = new MedicineAdapter(this, mHandler, strList, Constants.NORMAL_TYPE);
//        }

        //new GithubQueryTask().execute("");
        mAdapter = new MedicineAdapter(this, mHandler, strList, Constants.NORMAL_TYPE);

        //Collections.addAll(strList, medicineDTO);
        //mAdapter = new MedicineAdapter(this, mHandler, strList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        MenuItem home = navigationView.getMenu().findItem(R.id.nav_home);
        home.setChecked(true);
//        if(medicineDAO.getTotalRows() > 1){
//            Log.d("Total rows = " + medicineDAO.getTotalRows(), " Columns");
//        }
        onNavigationItemSelected(home);
    }

    private void initD(){
        new Thread(() -> {
            if(medicineDAO.getTotalRows() < 1){
                try{
                    Log.d("HomeActivity", " DB not exists");
                    //initPrimaryData();    // use it to load data primarily
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(
                                new InputStreamReader(getAssets().open("Medicines_all_column_format.txt")));

                        // do reading, usually loop until end of file reading
                        String mLine;
                        String forms[];
                        int i = 0;
                        while ((mLine = reader.readLine()) != null) {
                            forms = mLine.split("_");
                            mDTO = new MedicineDTO();
                            mDTO.setName(forms[0]);
                            mDTO.setType(forms[1]);
                            mDTO.setManufacturer(forms[2]);
                            mDTO.setUses(forms[3]);
                            mDTO.setManufacturingDate(Constants.DATE_FORMAT.parse(forms[4]).getTime());
                            mDTO.setExpireDate(Constants.DATE_FORMAT.parse(forms[5]).getTime());
                            mDTO.setPrice((int)Double.parseDouble(forms[6]));
                            mDTO.setQuantity(Integer.parseInt(forms[7]));
                            mDTO.setAvailable(Integer.parseInt(forms[8]));
                            mDTO.setLocation(forms[9]);
                            strList.add(mDTO);
                            // medicineDAO.upsert(medicineDTO);

                            //updateDB(mDTO);
                        }
                    } catch (IOException e) {
                    } finally {
                        if (reader != null) {
                            try {
                                //updateEmptyText();
                                mAdapter.setData(strList, navigationType);
                                reader.close();
                            } catch (IOException e) {
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                       // refresh("INIT");
                        for (MedicineDTO mdto: strList) {
                            updateDB(mDTO);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void updateEmptyText() {
        TextView emptyText = findViewById(R.id.empty_text);
        emptyText.setVisibility(View.GONE);
    }

    private static boolean isDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public class InitPrimaryData extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            int totalRows = 0;
            try{
                totalRows = medicineDAO.getTotalRows();
                //Log.d(TAG, " ROWS = " + medicineDAO.getTotalRows());

            } catch (Exception ex){
                ex.printStackTrace();
            }
            return totalRows;
        }

        @Override
        protected void onPostExecute(Integer count) {
            try{
                if(count < 1) {
                    Log.d("HomeActivity", " DB not exists");
                    //initPrimaryData();    // use it to load data primarily
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(
                                new InputStreamReader(getAssets().open("Medicines_all_column_format.txt")));

                        // do reading, usually loop until end of file reading
                        String mLine;
                        String forms[];
                        int i = 0;
                        while ((mLine = reader.readLine()) != null) {
                            forms = mLine.split("_");
                            mDTO = new MedicineDTO();
                            mDTO.setName(forms[0]);
                            mDTO.setType(forms[1]);
                            mDTO.setManufacturer(forms[2]);
                            mDTO.setUses(forms[3]);
                            mDTO.setManufacturingDate(Constants.DATE_FORMAT.parse(forms[4]).getTime());
                            mDTO.setExpireDate(Constants.DATE_FORMAT.parse(forms[5]).getTime());
                            mDTO.setPrice((int)Double.parseDouble(forms[6]));
                            mDTO.setQuantity(Integer.parseInt(forms[7]));
                            mDTO.setAvailable(Integer.parseInt(forms[8]));
                            mDTO.setLocation(forms[9]);
                            strList.add(mDTO);
                           // medicineDAO.upsert(medicineDTO);

                            updateDB(mDTO);
                        }
                    } catch (IOException e) {
                    } finally {
                        if (reader != null) {
                            try {
                                mAdapter.setData(strList, navigationType);
                                reader.close();
                            } catch (IOException e) {
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
//            if (textCartItemCount != null) {
//                if( count> 0){
//                    Log.d("WarningListVal", " = " + count);
//                    textCartItemCount.setText(String.valueOf(count));
//                }
//                else{
//                    if (textCartItemCount.getVisibility() != View.GONE) {
//                        textCartItemCount.setVisibility(View.GONE);
//                    }
//                }
//            }
        }
    }

    private void initPrimaryData() throws Exception{
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getAssets().open("Medicines_all_column_format.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            String forms[];
            int i = 0;
            while ((mLine = reader.readLine()) != null) {
                forms = mLine.split("_");
                mDTO = new MedicineDTO();
                mDTO.setName(forms[0]);
                mDTO.setType(forms[1]);
                mDTO.setManufacturer(forms[2]);
                mDTO.setUses(forms[3]);
                mDTO.setManufacturingDate(Constants.DATE_FORMAT.parse(forms[4]).getTime());
                mDTO.setExpireDate(Constants.DATE_FORMAT.parse(forms[5]).getTime());
                mDTO.setPrice((int)Double.parseDouble(forms[6]));
                mDTO.setQuantity(Integer.parseInt(forms[7]));
                mDTO.setAvailable(Integer.parseInt(forms[8]));
                mDTO.setLocation(forms[9]);
                strList.add(mDTO);

                updateDB(mDTO);
            }
        } catch (IOException e) {
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateDB(final MedicineDTO medicineDTO) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final long[] ids = medicineDAO.upsert(medicineDTO);
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (ids.length >= 1) {
//                            Log.d("YES","Save Successful");
//                        } else {
//                            Log.d("NO","Can't Save");
//                        }
//                    }
//                });
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.Request.NEW_MEDICINE && resultCode == RESULT_OK) {
            new UpdateWarningTask().execute("");
            long id = data.getLongExtra(Constants.Key.ID, 0);
            if (id > 0 && mSelectedMenu != null) {
                onNavigationItemSelected(mSelectedMenu);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d(TAG, " ITEM_ADDED");
        getMenuInflater().inflate(R.menu.home, menu);

        mNotificationItem = menu.findItem(R.id.app_bar_notification);
        View notificationView = mNotificationItem.getActionView();
        textCartItemCount = (TextView) notificationView.findViewById(R.id.cart_badge);
        mAdapter.setWarningCount(textCartItemCount);
        //WarningListActivity.warnCount(textCartItemCount);
        //setupBadge();
        new UpdateWarningTask().execute("");
        notificationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(mNotificationItem);
            }
        });

        mSearchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) mSearchItem.getActionView();
        searchView.setOnQueryTextListener(this);

        return true;
    }

    public class UpdateWarningTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            int warningCount = 0;
            try {
                warningCount = medicineDAO.getWarningListCount();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return warningCount;
        }

        // COMPLETED (3) Override onPostExecute to display the results in the TextView
        @Override
        protected void onPostExecute(Integer count) {
            if (textCartItemCount != null) {
                if( count> 0){
                    Log.d("WarningListVal", " = " + count);
                    if(textCartItemCount.getVisibility() != View.VISIBLE){
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                    textCartItemCount.setText(String.valueOf(count));
                }
                else{
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                }
            }
        }
    }

    private void setupBadge() {
        new Thread(() -> {

            //long tmp = DateConverter.fromDate(Calendar.getInstance().getTime());
            //long time= System.currentTimeMillis();
            //int mCartItemCount = medicineDAO.getWarningListCount();

            Log.d("WarningList", " ItemAdded");
            int warningCount = medicineDAO.getWarningListCount();
            if( warningCount> 0){
                mCartItemCount = warningCount;
                Log.d("WarningList", " = " + mCartItemCount);
                textCartItemCount.setText(String.valueOf(warningCount));
            }

            if (textCartItemCount != null) {
                if (mCartItemCount == 0) {
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } //else {
//                    textCartItemCount.setText(String.valueOf(Math.min(warningCount, 99)));
//                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
//                        textCartItemCount.setVisibility(View.VISIBLE);
//                    }
//                }
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.app_bar_location: {
                startActivity(new Intent(this, MapsActivity.class));
                return true;
            }
            case R.id.app_bar_notification: {
                // Do something
//                Activity activity = this;
//                Intent intent = new Intent(activity, MapsActivity.class);
//                intent.putExtra(Constants.Key.LOCATION, Constants.Key.LOCATION);
//                activity.startActivity(intent);
                startActivity(new Intent(this, WarningListActivity.class));
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        onLoadMedicine(mSelectedMenu.getItemId(), query);
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return mSearchItem.collapseActionView();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        this.mSelectedMenu = item;
        int id = item.getItemId();

        if (id == R.id.nav_log) {
            startActivity(new Intent(this, LogActivity.class));
        } else if (id == R.id.nav_revenue) {
            startActivity(new Intent(this, RevenueActivity.class));
        } else if (id == R.id.nav_calculation) {
            startActivity(new Intent(this, SellInfoActivity.class));
        }else {
            setTitle(item.getTitle());
            onLoadMedicine(id);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onLoadMedicine(final int nav_id) {
        onLoadMedicine(nav_id, "");
    }

    private void onLoadMedicine(final int nav_id, final String keyword) {
        if(nav_id == R.id.nav_home){
            setVisibility(true);
        }
        else{
            setVisibility(false);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<MedicineDTO> medicines;
                final String query = MessageFormat.format("%{0}%", keyword);

                if (nav_id == R.id.nav_home) {
                    navigationType = Constants.HOME_TYPE;
                    medicines = medicineDAO.selectAll(query);
                    if(medicines.size() < 1){
                        BufferedReader reader = null;
                        try {
                            reader = new BufferedReader(
                                    new InputStreamReader(getAssets().open("Medicines_all_column_format.txt")));

                            // do reading, usually loop until end of file reading
                            String mLine;
                            String forms[];
                            int i = 0;
                            while ((mLine = reader.readLine()) != null) {
                                forms = mLine.split("_");
                                mDTO = new MedicineDTO();
                                mDTO.setName(forms[0]);
                                mDTO.setType(forms[1]);
                                mDTO.setManufacturer(forms[2]);
                                mDTO.setUses(forms[3]);
                                try {
                                    mDTO.setManufacturingDate(Constants.DATE_FORMAT.parse(forms[4]).getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    mDTO.setExpireDate(Constants.DATE_FORMAT.parse(forms[5]).getTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                mDTO.setPrice((int)Double.parseDouble(forms[6]));
                                mDTO.setQuantity(Integer.parseInt(forms[7]));
                                mDTO.setAvailable(Integer.parseInt(forms[8]));
                                mDTO.setLocation(forms[9]);
                                medicines.add(mDTO);
                                strList.add(mDTO);
                                medicineDAO.upsert(mDTO);

                                //updateDB(mDTO);
                            }
                        } catch (IOException e) {
                        } finally {
                            if (reader != null) {
                                try {
//                                    for (MedicineDTO mdro: medicines) {
//                                        updateDB(mDTO);
//                                    }
                                    reader.close();
                                } catch (IOException e) {
                                }
                            }
                            //mAdapter.notifyDataSetChanged();
                        }
                    }
                } else if (nav_id == R.id.nav_available) {
                    navigationType = Constants.AVAILABLE_TYPE;
                    medicines = medicineDAO.selectAvailable(query);
                } else if (nav_id == R.id.nav_expired) {
                    navigationType = Constants.EXPIRED_TYPE;
                    medicines = medicineDAO.selectExpired(query);
                } else if (nav_id == R.id.nav_sold_out) {
                    navigationType = Constants.SOLD_OUT_TYPE;
                    medicines = medicineDAO.selectSoldOut(query);
                } else if (nav_id == R.id.nav_wish_list) {
                    navigationType = Constants.WISHLIST_TYPE;
                    medicines = medicineDAO.selectWishList(query);
                } else {
                    medicines = new ArrayList<>();
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(medicines,navigationType);
                        refresh("LOAD");
                        //Log.d("TAG", " SIZE: " + strList.size() + " and HELL: " + medicines.size());
                        //doup(medicines);
                    }
                });
            }
        }).start();
    }

    private void doup(List<MedicineDTO> medicines) {
        for (MedicineDTO mdto: medicines) {
            updateDB(mdto);
        }
        Log.d("INDBINSERT"," SIZE = " + medicines.size());
    }

    @SuppressLint("RestrictedApi")
    private void setVisibility(boolean flag) {
        if(flag){
            fab.setVisibility(View.VISIBLE);
        }
        else{
            fab.setVisibility(View.GONE);
        }
    }

    private void refresh(String val) {
        TextView emptyText = findViewById(R.id.empty_text);

        if (mAdapter.isEmpty()) {
            emptyText.setVisibility(View.VISIBLE);
        } else {
            emptyText.setVisibility(View.GONE);
        }
        Log.d("REFRESHED "," " + val);
        mAdapter.notifyDataSetChanged();
    }

}
