package com.endeavour.saddam.pharmacyautomation.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryName;

public interface MedicineService {

    @GET("/auto-suggest/autsrch.php")
    Call<String[]> suggestName(@Query("term") String term);

    @GET("/pdts.php")
    Call<ResponseBody> codeDetails(@QueryName String code);
}
