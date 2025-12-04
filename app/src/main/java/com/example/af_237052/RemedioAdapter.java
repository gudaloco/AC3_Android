package com.example.af_237052;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class RemedioAdapter extends RecyclerView.Adapter<RemedioAdapter.ViewHolder>  {
    private List<Remedio> remedios;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    public interface OnItemClickListener {
        void onItemClick(Remedio remedio);
    }
    public interface OnItemLongClickListener {
        void onItemLongClickListener(Remedio remedio, int position);
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
                .inflate(R.layout.item_remedio_checkbox, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Remedio f = remedios.get(position);

        String descricaoLabel = holder.itemView.getContext().getString(R.string.descricao);
        String horarioLabel = holder.itemView.getContext().getString(R.string.horario);

        if (f.getCheck()) {
            holder.itemView.setBackgroundColor(Color.parseColor("#C8E6C9"));
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }

        holder.txtNomeDescricao.setText(f.getNome() + " | " + descricaoLabel + ": " + f.getDescricao());
        holder.txtHorarioStatus.setText(horarioLabel + ": " + f.getHorario());

        holder.cbTomado.setOnCheckedChangeListener(null);
        holder.cbFinalizado.setOnCheckedChangeListener(null);

        holder.cbTomado.setChecked(f.getCheck());
        holder.cbFinalizado.setChecked(f.getFinalizado());

        holder.cbTomado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            f.setCheck(isChecked);
            atualizarRemedio(f, position);
        });

        holder.cbFinalizado.setOnCheckedChangeListener((buttonView, isChecked) -> {
            f.setFinalizado(isChecked);
            atualizarRemedio(f, position);
        });

        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) clickListener.onItemClick(f);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onItemLongClickListener(f, position);
            }
            return true;
        });
    }

    private void atualizarRemedio(Remedio remedio, int position) {
        if (remedio.getId() != null) {
            db.collection("remedios").document(remedio.getId())
                    .set(remedio)
                    .addOnSuccessListener(aVoid -> {
                        notifyItemChanged(position);
                    });
        }
    }

    @Override
    public int getItemCount() {
        return remedios.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNomeDescricao, txtHorarioStatus;
        CheckBox cbTomado, cbFinalizado;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNomeDescricao = itemView.findViewById(R.id.text_nome_descricao);
            txtHorarioStatus = itemView.findViewById(R.id.text_horario_status);
            cbTomado = itemView.findViewById(R.id.checkbox_tomado_item);
            cbFinalizado = itemView.findViewById(R.id.checkbox_finalizado);
        }
    }
}
