package com.app.punitassignment.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.punitassignment.MainActivity;
import com.app.punitassignment.R;
import com.app.punitassignment.models.Hit;

import java.util.ArrayList;
import java.util.List;

public class HitsAdapter extends RecyclerView.Adapter<HitsAdapter.MyViewHolder> {

   List<Hit> hitList = new ArrayList<>();
   MainActivity mainActivity;

   public HitsAdapter(List<Hit> hitList, MainActivity mainActivity) {
      this.hitList = hitList;
      this.mainActivity = mainActivity;
   }

   @NonNull
   @Override
   public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
      View listItem = layoutInflater.inflate(R.layout.row_hits, parent, false);
      MyViewHolder viewHolder = new MyViewHolder(listItem);
      return viewHolder;
   }

   @Override
   public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
      final Hit hit = hitList.get(position);
      holder.tvTitle.setText(hit.getTitle());
      holder.tvCreatedAt.setText(hit.getCreatedAt());
      holder.swSelect.setChecked(hit.isChecked());
      holder.llMain.setBackgroundColor(hit.isChecked() ? mainActivity.getResources().getColor(R.color.colorPrimaryLight) :
              Color.WHITE);
      holder.llMain.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if (hit.isChecked()) {
               hit.setChecked(false);
            } else {
               hit.setChecked(true);
            }
            hitList.set(position, hit);
            notifyDataSetChanged();
            mainActivity.updatedSelectedCount(hit.isChecked());
         }
      });
      /*holder.swSelect.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if (holder.swSelect.isChecked()) {
               hit.setChecked(false);
            } else {
               hit.setChecked(true);
            }
            hitList.set(position, hit);
            notifyDataSetChanged();
            mainActivity.updatedSelectedCount(hit.isChecked());
         }
      });*/
   }

   @Override
   public int getItemCount() {
      return hitList.size();
   }

   public static class MyViewHolder extends RecyclerView.ViewHolder {
      public TextView tvTitle, tvCreatedAt;
      public Switch swSelect;
      public LinearLayout llMain;

      public MyViewHolder(@NonNull View itemView) {
         super(itemView);
         tvCreatedAt = itemView.findViewById(R.id.tvCreatedAt);
         llMain = itemView.findViewById(R.id.llMain);
         swSelect = itemView.findViewById(R.id.swSelect);
         tvTitle = itemView.findViewById(R.id.tvTitle);
      }
   }
}
