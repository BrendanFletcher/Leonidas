package com.endeavour.saddam.pharmacyautomation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.endeavour.saddam.pharmacyautomation.adapter.AutoCompleteAdapter;
import com.endeavour.saddam.pharmacyautomation.db.AppDatabase;
import com.endeavour.saddam.pharmacyautomation.db.dao.MedicineDAO;
import com.endeavour.saddam.pharmacyautomation.db.dto.MedicineDTO;
import com.endeavour.saddam.pharmacyautomation.service.MedicineService;
import com.endeavour.saddam.pharmacyautomation.utils.Constants;
import com.endeavour.saddam.pharmacyautomation.utils.Utils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewMedicineActivity extends AppCompatActivity implements View.OnClickListener {

    private AutoCompleteTextView nameText;
    private AutoCompleteTextView typeText;
    private AutoCompleteTextView mfrText;
    private AutoCompleteTextView usesText;
    private AutoCompleteTextView locationText;
    private EditText mfgDateText;
    private EditText expDateText;
    private EditText priceText;
    private EditText quantityText;
    private EditText availableText;

    private boolean isUpdate;
    private Handler mHandler;
    private MedicineDAO medicineDAO;
    private MedicineDTO medicineDTO;
    private IntentIntegrator mIntegrator;
    private MedicineService medicineService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_medicine);

        mHandler = new Handler();
        mIntegrator = new IntentIntegrator(this);
        medicineDAO = AppDatabase.getInstance(this).medicineDAO();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(getString(R.string.suggest_base_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        medicineService = retrofit.create(MedicineService.class);

        Bundle bundle = getIntent().getExtras();
        isUpdate = bundle != null;

        if (isUpdate) {
            medicineDTO = bundle.getParcelable(Constants.Key.MEDICINE);
        } else {
            medicineDTO = new MedicineDTO();
        }

        initView();
        initData();
    }

    private void initView() {
        nameText = findViewById(R.id.input_name);
        typeText = findViewById(R.id.input_type);
        mfrText = findViewById(R.id.input_mfr);
        usesText = findViewById(R.id.input_uses);
        mfgDateText = findViewById(R.id.input_mfg_date);
        expDateText = findViewById(R.id.input_exp_date);
        priceText = findViewById(R.id.input_price);
        quantityText = findViewById(R.id.input_quantity);
        availableText = findViewById(R.id.input_available);
        locationText = findViewById(R.id.input_location);

        Button saveButton = findViewById(R.id.btn_save);
        saveButton.setOnClickListener(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        if (isUpdate) {
            fab.hide();
        } else {
            fab.show();
        }

        initAutoCompleteAdapter();
    }

    private void initData() {
        nameText.setText(medicineDTO.getName());
        typeText.setText(medicineDTO.getType());
        mfrText.setText(medicineDTO.getManufacturer());
        usesText.setText(medicineDTO.getUses());

        if (medicineDTO.getManufacturingDate() != null) {
            mfgDateText.setText(Utils.formatDate(medicineDTO.getManufacturingDate()));
        }
        if (medicineDTO.getExpireDate() != null) {
            expDateText.setText(Utils.formatDate(medicineDTO.getExpireDate()));
        }
        if (medicineDTO.getPrice() != null) {
            priceText.setText(String.valueOf(medicineDTO.getPrice()));
        }
        if (medicineDTO.getQuantity() != null) {
            quantityText.setText(String.valueOf(medicineDTO.getQuantity()));
        }
        if (medicineDTO.getAvailable() != null) {
            availableText.setText(String.valueOf(medicineDTO.getAvailable()));
        }

        locationText.setText(medicineDTO.getLocation());
    }

    private void initAutoCompleteAdapter() {
        AutoCompleteAdapter nameAdapter = new AutoCompleteAdapter(this);
        nameText.setAdapter(nameAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<String> types = medicineDAO.getTypes();
                final List<String> mfrs = medicineDAO.getManufacturers();
                final List<String> uses = medicineDAO.getUses();
                final List<String> locations = medicineDAO.getLocations();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        typeText.setAdapter(buildAdapter(types));
                        mfrText.setAdapter(buildAdapter(mfrs));
                        usesText.setAdapter(buildAdapter(uses));
                        locationText.setAdapter(buildAdapter(locations));
                    }
                });
            }
        }).start();
    }

    public ArrayAdapter<String> buildAdapter(List<String> list) {
        System.out.println(list);
        return new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            if (!validate()) {
                showMessage("Save Failed");
                return;
            }

            try {
                onSaveAction();
            } catch (Exception e) {
                showMessage(e.getMessage());
            }
        } else if (v.getId() == R.id.fab) {
            mIntegrator.initiateScan();
        }
    }

    private void onSaveAction() throws Exception {
        medicineDTO.setName(Utils.getText(nameText));
        medicineDTO.setType(Utils.getText(typeText));
        medicineDTO.setManufacturer(Utils.getText(mfrText));
        medicineDTO.setUses(Utils.getText(usesText));
        medicineDTO.setManufacturingDate(Utils.getDate(mfgDateText));
        medicineDTO.setExpireDate(Utils.getDate(expDateText));
        medicineDTO.setPrice(Utils.getNumber(priceText));
        medicineDTO.setQuantity(Utils.getNumber(quantityText));
        medicineDTO.setAvailable(Utils.getNumber(availableText));
        medicineDTO.setLocation(Utils.getText(locationText));


        new Thread(new Runnable() {
            @Override
            public void run() {
                final long[] ids = medicineDAO.upsert(medicineDTO);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (ids.length >= 1) {
                            showMessage("Save Successful");
                            //updateWarningList();
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra(Constants.Key.ID, ids[0]);
                            setResult(RESULT_OK, returnIntent);
                            finish();
                        } else {
                            showMessage("Can't Save");
                        }
                    }
                });
            }
        }).start();
    }

    private void updateWarningList() {
        new Thread(() -> {
            TextView textCartItemCount = (TextView) findViewById(R.id.cart_badge);
            int warningCount = medicineDAO.getWarningListCount();
           // int warningCount = medicineDAO.getToBeExpiredCount();
            Log.d("NewMedicineActivity", " = " + warningCount);
            if( warningCount> 0){
                Log.d("WarningList", " = " + warningCount);
                textCartItemCount.setText(String.valueOf(warningCount));
            }
        }).start();
    }

    public void showMessage(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
    }

    public boolean validate() {
        boolean valid = true;

        String name = nameText.getText().toString();
        String type = typeText.getText().toString();
        String mfr = mfrText.getText().toString();
        String uses = usesText.getText().toString();
        String mfgDate = mfgDateText.getText().toString();
        String expDate = expDateText.getText().toString();
        String price = priceText.getText().toString();
        String quantity = quantityText.getText().toString();
        String available = availableText.getText().toString();
        String location = locationText.getText().toString();

        if (Utils.isEmpty(name)) {
            nameText.setError("Can't be blank");
            valid = false;
        } else {
            nameText.setError(null);
        }

        if (Utils.isEmpty(type)) {
            typeText.setError("Can't be blank");
            valid = false;
        } else {
            typeText.setError(null);
        }

        if (Utils.isEmpty(mfr)) {
            mfrText.setError("Can't be blank");
            valid = false;
        } else {
            mfrText.setError(null);
        }

        if (Utils.isEmpty(uses)) {
            usesText.setError("Can't be blank");
            valid = false;
        } else {
            usesText.setError(null);
        }

        if (!Utils.isDate(mfgDate)) {
            mfgDateText.setError("Invalid date format");
            valid = false;
        } else {
            mfgDateText.setError(null);
        }

        if (!Utils.isDate(expDate)) {
            expDateText.setError("Invalid date format");
            valid = false;
        } else {
            expDateText.setError(null);
        }

        if (!Utils.isNumber(price)) {
            priceText.setError("Invalid number");
            valid = false;
        } else {
            priceText.setError(null);
        }

        if (!Utils.isNumber(quantity)) {
            quantityText.setError("Invalid quantity");
            valid = false;
        } else {
            quantityText.setError(null);
        }

        if (!Utils.isNumber(available)) {
            availableText.setError("Invalid number");
            valid = false;
        } else {
            availableText.setError(null);
        }

        if (Utils.isEmpty(location)) {
            locationText.setError("Can't be blank");
            valid = false;
        } else {
            locationText.setError(null);
        }

        return valid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null && result.getContents() != null) {
            medicineService.codeDetails(result.getContents()).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            onReadBarCode(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable throwable) {
                    Toast.makeText(NewMedicineActivity.this, throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void onReadBarCode(String code) {
        Elements elements = Jsoup.parse(code).select("td");

        String name = MessageFormat.format("{0} {1}", elements.get(1).text(), elements.get(5).text()).trim();
        String type = elements.get(7).text().trim();
        String mfr = elements.get(9).text().trim();

        if (!name.isEmpty()) {
            nameText.setText(name);
        }

        if (!type.isEmpty()) {
            typeText.setText(type);
        }

        if (!mfr.isEmpty()) {
            mfrText.setText(mfr);
        }
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
