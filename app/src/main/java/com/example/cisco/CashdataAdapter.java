package com.example.cisco;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CashdataAdapter extends RecyclerView.Adapter<CashdataAdapter.ViewHolder> {

    Context context;
    List<CashdataModel> order_list;

    public CashdataAdapter(Context context, List<CashdataModel> order_list) {
        this.context = context;
        this.order_list = order_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.orderitem_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (order_list != null && order_list.size() > 0) {
            CashdataModel model = order_list.get(position);
            holder.goaldata.setText(model.getGoal());
            holder.categorydata.setText(model.getCategory());
            holder.bookingdata.setText(model.getBooking());
        } else {
            return ;
        }
    }

    @Override
    public int getItemCount() {
        return order_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categorydata,goaldata,bookingdata;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categorydata = itemView.findViewById(R.id.categorydata);
            goaldata = itemView.findViewById(R.id.goaldata);
            bookingdata = itemView.findViewById(R.id.bookingdata);
        }
    }
}
