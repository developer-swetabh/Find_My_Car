package com.example.findmycar.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.findmycar.R;
import com.example.findmycar.contract.MainContract;
import com.example.findmycar.model.Parking;
import com.example.findmycar.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ParkingAdapter extends RecyclerView.Adapter<ParkingAdapter.ViewHolder> {
    private List<Parking> mDataList;
    private MainContract.IParkingPresenter mPresenter;

    public ParkingAdapter(MainContract.IParkingPresenter presenter) {
        mPresenter = presenter;
        mDataList = new ArrayList<Parking>();
    }

    @NonNull
    @Override
    public ParkingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_parking_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParkingAdapter.ViewHolder holder, int position) {
        final Parking model = mDataList.get(position);
        holder.tv_Date.setText(Utils.covertToDate(model.getTimeOfParking()));
        StringBuilder builder = new StringBuilder();
        builder.append(model.getExtraInfo());
        builder.append(" ");
        builder.append(model.getAddress());
        holder.tv_Desc.setText(builder);
        holder.ll_ViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.viewOnMap(model.getLatitude(), model.getLongitude());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void updateList(List<Parking> list) {
        mDataList = list;
        notifyDataSetChanged();
    }

    public void updateList(Parking parking) {
        mDataList.add(0, parking);
        notifyItemInserted(0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_Date;
        private TextView tv_Desc;
        private LinearLayout ll_ViewOnMap;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Date = itemView.findViewById(R.id.tv_date);
            tv_Desc = itemView.findViewById(R.id.tv_desc);
            ll_ViewOnMap = itemView.findViewById(R.id.ll_view_on_map);
        }
    }
}
