package com.plbtw_asek.restopedia;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;


import com.plbtw_asek.restopedia.API.ApiClient;
import com.plbtw_asek.restopedia.API.ApiInterface;
import com.plbtw_asek.restopedia.Model.KontenDatum;
import com.plbtw_asek.restopedia.Model.UserKonten;
import com.restopedia_team.restopedia.R;

import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabActivity3 extends Activity {

    String USERNAME;
    String API_KEY;

    private LinearLayoutManager layoutManager;

    private Button btnRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab3);

        Bundle b = getIntent().getExtras();
        USERNAME = b.getString("username");
        API_KEY = b.getString("api_key");

        btnRefresh = (Button) findViewById(R.id.btnRefresh);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyKonten();
            }
        });

        getMyKonten();
    }

    //////////////////////////////////////////////////// TAMPIL DATA PRIBADI ///////////////////////////////////////////

    void getMyKonten()
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<UserKonten> call = apiService.getMyKonten(USERNAME, API_KEY);
        call.enqueue(new Callback<UserKonten>() {
            @Override
            public void onResponse(Call<UserKonten> call, Response<UserKonten> response) {
                UserKonten userKonten = response.body(); //data Konten-----------------------------------------
                List<KontenDatum> restoList = userKonten.getKontenData();
                //textView.setText(userKonten.getKontenData().get(0).getDetailResto());

                layoutManager = new LinearLayoutManager(TabActivity3.this);

                RecyclerView recyclerView = (RecyclerView)
                        findViewById(R.id.recycler);
                recyclerView.setLayoutManager(layoutManager);

                RecyclerViewAdapter recyclerViewAdapter =
                        new RecyclerViewAdapter(restoList, 1);

                recyclerView.setAdapter(recyclerViewAdapter);
            }

            @Override
            public void onFailure(Call<UserKonten> call, Throwable t) {
                for (int i = 0; i < 10; i++) {
                    System.out.println("Load Track Failure Response");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(TabActivity3.this, SweetAlertDialog.WARNING_TYPE);
        sweetAlertDialog.setCanceledOnTouchOutside(false);
        sweetAlertDialog.setTitleText("Log out")
                .setContentText("Do you want to logout ?")
                .setConfirmText("Yes")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setCancelText("Cancel")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}
