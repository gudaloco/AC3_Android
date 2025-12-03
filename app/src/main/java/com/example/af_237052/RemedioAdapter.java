package com.example.af_237052;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RemedioAdapter extends RecyclerView.Adapter<RemedioAdapter.ViewHolder>  {
    private List<Remedio> remedios;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Remedio remedio);
    }
    public interface OnItemLongClickListener {
        void onItemLongClickListener(Remedio remedio);
    }
    public RemedioAdapter(List<Remedio> remedios) {
        this.remedios = remedios;
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }
    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Remedio f = remedios.get(position);
        holder.txt1.setText(f.getNome() + " | Descrição: " +f.getDescricao());
        holder.txt2.setText("Horario " + f.getHorario() + f.getCheck());
        /*if (f.getCheck()){
            holder.txt3.setText("Já Visto");
        } else {
            holder.txt3.setText("não Visto");
        }*/

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onItemClick(f);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) longClickListener.onItemLongClickListener(f);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return remedios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt1, txt2;

        public ViewHolder(View itemView) {
            super(itemView);
            txt1 = itemView.findViewById(android.R.id.text1);
            txt2 = itemView.findViewById(android.R.id.text2);
        }
    }
}
