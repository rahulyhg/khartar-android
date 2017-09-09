package com.vardhamaninfo.khartargaccha.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vardhamaninfo.khartargaccha.Model.Panchang;
import com.vardhamaninfo.khartargaccha.R;

import java.util.ArrayList;

public class PanchangAdapter extends RecyclerView.Adapter<PanchangAdapter.PanchangHolder> {

    private Context context;
    private ArrayList<Panchang> dates;

    public PanchangAdapter(Context context, ArrayList<Panchang> dates) {
        this.context = context;
        this.dates = dates;
    }

    class PanchangHolder extends RecyclerView.ViewHolder {

        TextView tvDate, tvDay, tvLunearYear, tvLunearDate, tvDescription;
        LinearLayout llShubh;

        public PanchangHolder(View itemView) {
            super(itemView);

            tvDate = (TextView) itemView.findViewById(R.id.recyclerview_panchang_tv_date);
            tvDay = (TextView) itemView.findViewById(R.id.recyclerview_panchang_tv_day);
            tvLunearYear = (TextView) itemView.findViewById(R.id.recyclerview_panchang_tv_lyear);
            tvLunearDate = (TextView) itemView.findViewById(R.id.recyclerview_panchang_tv_ldate);
            tvDescription = (TextView) itemView.findViewById(R.id.recyclerview_panchang_tv_description);

            llShubh = (LinearLayout) itemView.findViewById(R.id.recyclerview_panchang_ll_shubh);
        }
    }

    @Override
    public PanchangHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_panchang, parent, false);
        return new PanchangHolder(view);
    }

    @Override
    public void onBindViewHolder(PanchangHolder holder, int position) {

        Panchang panchang = dates.get(position);

        holder.tvDate.setText(panchang.getDate());
        holder.tvDay.setText(panchang.getDay());

        holder.tvLunearYear.setText("सम्वत् " + panchang.getLunarYear());
        holder.tvLunearDate.setText(panchang.getLunarMonth() + " " + panchang.getLunarCycle() + " " + panchang.getLunarDay());

        if (panchang.getDescription().equals("")) {
            holder.tvDescription.setText("कोई विवरण नहीं");
        } else {
            holder.tvDescription.setText(panchang.getDescription());
        }

        if (panchang.getShubhDin().equals("Yes")) {
            holder.llShubh.setVisibility(View.VISIBLE);
        } else {
            holder.llShubh.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }
}