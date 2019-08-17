package com.lufstanza.app.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.lufstanza.app.R;
import com.lufstanza.app.models.Schedules;
import com.lufstanza.app.utils.AppController;
import com.lufstanza.app.utils.ItemClickListener;
import com.lufstanza.app.views.MapActivity;

import java.util.List;
import java.util.StringTokenizer;

public class SchedulesAdapter extends RecyclerView.Adapter<SchedulesAdapter.HeroViewHolder> {

    Context mCtx;
    List<Object> scheduleList;
    ItemClickListener itemClickListener;
    private static int SCHEDULE_TYPE = 1;
    private static int DEMACATOR_TYPE = 2;
    private int number = 1;
    private Activity activity;
    //private

    public SchedulesAdapter(Context mCtx, List<Object> scheduleList, Activity activity) {
        this.mCtx = mCtx;
        this.scheduleList = scheduleList;
       // this.itemClickListener = itemClickListener;
        this.activity = activity;
    }

    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if(viewType == SCHEDULE_TYPE)
            view = LayoutInflater.from(mCtx).inflate(R.layout.schedule_each, parent, false);
        else
            view = LayoutInflater.from(mCtx).inflate(R.layout.demacator_layout, parent, false);

        return new HeroViewHolder(view, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        Object recyclerViewItem = scheduleList.get(position);
        return (recyclerViewItem instanceof Schedules) ? SCHEDULE_TYPE : DEMACATOR_TYPE;
    }

    //public void initializeSchd

    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        Object object = scheduleList.get(position);
        if(object instanceof Schedules){
            Schedules schedules = (Schedules)scheduleList.get(position);
            holder.departure.setText(schedules.getDepartureAirport()+ " | "+schedules.getDepartureTime());
            holder.arrival.setText(schedules.getArrivalAirport()+ " | "+schedules.getArrivalTime());
            holder.each_schedule.setTag(schedules.getDepartureAirport()+"_"+schedules.getArrivalAirport());
            holder.each_schedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//  activity.startActivity(intent);
                    StringTokenizer tokens = new StringTokenizer(v.getTag().toString(), "_");
                    AppController.DEPARTURE = tokens.nextToken();// this will contain "Fruit"
                    AppController.ARRIVAL = tokens.nextToken();
                    activity.startActivity(new Intent(activity, MapActivity.class));

                    //AppController.doToast(v.getTag().toString());
                }
            });
        } else {
            try{
                AppController.Demacator demacator = (AppController.Demacator)scheduleList.get(position);
                holder.number.setText(String.valueOf(demacator.getNumber()));
                number++;
            }catch (Exception e){

            }
        }
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    class HeroViewHolder extends RecyclerView.ViewHolder {
        TextView departure, arrival, date, number;
        View each_schedule;

        public HeroViewHolder(View itemView, int i) {
            super(itemView);
            if(i == SCHEDULE_TYPE){
                departure = itemView.findViewById(R.id.departure);
                arrival = itemView.findViewById(R.id.arrival);
                date = itemView.findViewById(R.id.date);
                each_schedule = itemView.findViewById(R.id.each_schedule);
            } else {
                number = itemView.findViewById(R.id.number);
            }

        }
    }
}