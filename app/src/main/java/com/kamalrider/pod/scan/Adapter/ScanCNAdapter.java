package com.kamalrider.pod.scan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kamalrider.pod.R;
import com.kamalrider.pod.scan.model.ScanCN;

import java.util.List;

public class ScanCNAdapter extends RecyclerView.Adapter<ScanCNAdapter.MyViewHolder> {

    private Context mContext;
    private List<ScanCN> mDataset;
    private OnItemClickListener onItemClickListener;

    @Override
    public ScanCNAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scan_cn_row,parent, false);
        final  MyViewHolder vh = new MyViewHolder(v,(OnItemClickListener) onItemClickListener );
        return  vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ScanCNAdapter.MyViewHolder holder, final int position) {
        holder.txtCn.setText(mDataset.get(position).getConno());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(position, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public  ScanCNAdapter(Context context, List<ScanCN> myDataset){
        this.mContext = context;
        this.mDataset = myDataset;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtCn;
        public ImageView closeBtn;

        public MyViewHolder(View itemView, final ScanCNAdapter.OnItemClickListener listener) {
            super(itemView);
            txtCn = itemView.findViewById(R.id.runsheetcnno);
            closeBtn = itemView.findViewById(R.id.close_btn);

            closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onDeleteClick(int position);

        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ScanCNAdapter.OnItemClickListener listener) {
        onItemClickListener = listener;


    }
}
