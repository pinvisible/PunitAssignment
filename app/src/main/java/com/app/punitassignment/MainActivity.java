package com.app.punitassignment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.app.punitassignment.adapters.HitsAdapter;
import com.app.punitassignment.apis.ApiInterface;
import com.app.punitassignment.apis.RetrofitCall;
import com.app.punitassignment.models.Hit;
import com.app.punitassignment.models.Hits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

   RecyclerView recyclerView;
   ProgressBar progressBar;
   TextView tvNoData;
   SwipeRefreshLayout swipeRefresh;

   int page = 1;
   int checkedCount = 0;

   boolean isDataLoading;

   LinearLayoutManager linearLayoutManager;
   HitsAdapter hitsAdapter;
   List<Hit> hitList = new ArrayList<>();

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      defineViews();
      defineActions();
      fetchData();
   }

   private void defineActions() {

      linearLayoutManager = new LinearLayoutManager(getApplicationContext());
      recyclerView.setLayoutManager(linearLayoutManager);
      recyclerView.setHasFixedSize(true);
      hitsAdapter = new HitsAdapter(hitList, this);
      recyclerView.setAdapter(hitsAdapter);

      swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
         @Override
         public void onRefresh() {
            if (!isNetworkAvailable()) {
               Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_LONG).show();
               swipeRefresh.setRefreshing(false);
               return;
            }
            if (!isDataLoading) {
               page = 1;
               fetchData();
            }
         }
      });
      recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
         @Override
         public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            //load more data when scrolling stopped and if maximum 3 items left to scroll
            if (RecyclerView.SCREEN_STATE_OFF == newState && hitList.size() > 3) {
               if (!isDataLoading && linearLayoutManager.findLastVisibleItemPosition() >= hitList.size() - 3) {
                  if (!isNetworkAvailable()) {
                     Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_LONG).show();
                     swipeRefresh.setRefreshing(false);
                     return;
                  }
                  page++;
                  fetchData();
               }
            }
         }
      });
   }

   public void updatedSelectedCount(boolean isChecked) {
      if (isChecked) {
         checkedCount++;
      } else {
         checkedCount--;
      }
      if (checkedCount < 0) {
         checkedCount = 0;
      }
      if (checkedCount > 0) {
         setTitle("Selected Hits : " + checkedCount);
      } else {
         setTitle(getString(R.string.app_name));
      }
   }

   private void fetchData() {
      //check if Internet is connected otherwise display message
      if (isNetworkAvailable()) {
         isDataLoading = true;
         if (!swipeRefresh.isRefreshing()) {
            progressBar.setVisibility(View.VISIBLE);
         }
         tvNoData.setVisibility(View.GONE);

         Map<String, String> map = new HashMap<>();
         map.put("tags", "story");
         map.put("page", String.valueOf(page));

         //Call<MultipleResource> call = apiInterface.doGetListResources();

         RetrofitCall.getRetrofiCall().create(ApiInterface.class).getHits(map)
                 .enqueue(new Callback<Hits>() {
                    @Override
                    public void onResponse(Call<Hits> call, Response<Hits> response) {
                       if (response.isSuccessful()) {
                          Hits hits = response.body();
                          if (hits.getHits().size() > 0) {
                             if (page == 1) {
                                hitList.clear();
                                checkedCount = 0;
                                setTitle(getString(R.string.app_name));
                             }
                             hitList.addAll(hits.getHits());
                             hitsAdapter.notifyDataSetChanged();
                          } else if (page == 1) {
                             tvNoData.setText("No Data Available.");
                             tvNoData.setVisibility(View.VISIBLE);
                          } else {
                             page--;
                          }
                       } else {
                          tvNoData.setText(response.message());
                          tvNoData.setVisibility(View.VISIBLE);
                       }
                       if (swipeRefresh.isRefreshing()) {
                          swipeRefresh.setRefreshing(false);
                       }
                       progressBar.setVisibility(View.GONE);
                       isDataLoading = false;
                    }

                    @Override
                    public void onFailure(Call<Hits> call, Throwable t) {
                       t.printStackTrace();
                       if (page > 1) {
                          page--;
                       }
                       isDataLoading = false;
                       if (swipeRefresh.isRefreshing()) {
                          swipeRefresh.setRefreshing(false);
                       }
                    }
                 });


      } else {
         tvNoData.setText("No Internet Connection.");
         progressBar.setVisibility(View.GONE);
         if (page == 1) {
            tvNoData.setVisibility(View.VISIBLE);
         } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection.", Toast.LENGTH_LONG).show();
         }
         if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
         }
      }
   }

   void defineViews() {
      swipeRefresh = findViewById(R.id.swipeRefresh);
      recyclerView = findViewById(R.id.recyclerView);
      progressBar = findViewById(R.id.progressBar);
      tvNoData = findViewById(R.id.tvNoData);
   }

   boolean isNetworkAvailable() {
      ConnectivityManager connectivityManager = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
      return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
   }
}
