package com.mileston.walk.walkmilestonandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mileston.walk.walkmilestonandroid.models.DataSteps;
import com.mileston.walk.walkmilestonandroid.models.StepsCount;

import java.util.ArrayList;
import java.util.List;

public class AdapterData1 extends RecyclerView.Adapter<AdapterData1.CustomRecyclerView>{

    public DataSteps itemList;
    public List<StepsCount> dataResumenList;

    private RequestQueue mRequestQueue;
    private Context context;


    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    public AdapterData1(Context context, DataSteps itemList) {
        this.mRequestQueue = SingletonRequestQueue.getInstance(context).getRequestQueue();
        this.context = context;
        this.itemList = itemList;
    }

    public AdapterData1(DataSteps itemList) {
        this.itemList = itemList;
    }


    public AdapterData1(List<StepsCount> dataResumenList) {
        this.dataResumenList = dataResumenList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_HEADER;
            default:
                return TYPE_CELL;
        }
    }

    @NonNull
    @Override
    public CustomRecyclerView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        View layoutView;

        switch (viewType) {
            case TYPE_HEADER: {
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_big, parent, false);
                CustomRecyclerView rcv = new CustomRecyclerView(layoutView);
                return rcv;
            }
            case TYPE_CELL: {
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_small, parent, false);
                CustomRecyclerView rcv = new CustomRecyclerView(layoutView);
                return rcv;
            }
        }
        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull final CustomRecyclerView holder, int position) {
        DataSteps myData = itemList;

        //StepsCount dataStep = dataResumenList.get(position);

       // Log.d("getDatatodo", String.valueOf(dataStep.getStepCount()));

        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                holder.txtDate.setText(myData.getDate());
                holder.txtResumen.setText("resumen de actividades");
                holder.txtStep.setText(myData.getSteps());
                holder.txtKm.setText(myData.getKmrecorridos());
                holder.txtWm.setText("wm adapter");
                break;
            case TYPE_CELL:
                holder.chart.setDrawBarShadow(false);
                holder.chart.setDrawValueAboveBar(true);
                holder.chart.setMaxVisibleValueCount(50);
                holder.chart.setPinchZoom(false);
                holder.chart.setDrawGridBackground(true);

                ArrayList<BarEntry> barEntries = new ArrayList<>();

               // barEntries.add(new BarEntry(1, dataResumen.get(5)));
                barEntries.add(new BarEntry(2, 44f));
                barEntries.add(new BarEntry(3, 30f));
                barEntries.add(new BarEntry(4, 36f));

                BarDataSet barDataSet = new BarDataSet(barEntries, "Data Set");
                barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

                BarData data = new BarData(barDataSet);
                data.setBarWidth(0.9f);

                holder.chart.setData(data);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }


    //Customizar el RecyclerView del diseño.
    public class CustomRecyclerView extends RecyclerView.ViewHolder {
        TextView txtDate;
        TextView txtResumen;
        TextView txtStep;
        TextView txtKm;
        TextView txtWm;
        BarChart chart;

        CustomRecyclerView(View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txt_date);
            txtResumen = itemView.findViewById(R.id.txt_resumen);
            txtStep = itemView.findViewById(R.id.txt_step);
            txtKm = itemView.findViewById(R.id.txt_km);
            txtWm = itemView.findViewById(R.id.txt_wm);
            chart = itemView.findViewById(R.id.chart);
        }
    }
}