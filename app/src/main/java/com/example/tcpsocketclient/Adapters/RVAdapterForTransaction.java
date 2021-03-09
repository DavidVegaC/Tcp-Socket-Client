package com.example.tcpsocketclient.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcpsocketclient.Entities.TransactionEntity;
import com.example.tcpsocketclient.R;

import java.util.List;

public class RVAdapterForTransaction extends RecyclerView.Adapter<RVAdapterForTransaction.ErrorViewHolder> implements View.OnClickListener {

    private List<TransactionEntity> lecturasx;
    public RVAdapterForTransaction(List<TransactionEntity> lecturas){
        this.lecturasx = lecturas;
    }
    private  View.OnClickListener listener;

    @NonNull
    @Override
    public ErrorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_for_transaction, parent, false);
        ErrorViewHolder pvh = new ErrorViewHolder(v);
        v.setOnClickListener(this);
        return pvh;
    }



    @Override
    public void onBindViewHolder(@NonNull ErrorViewHolder holder, int position) {
        holder.txtCabecera.setText("Ticket: " + lecturasx.get(position).getNumeroTransaccion() + " | " + "Estación: Pionero | Bomba: " + lecturasx.get(position).getIdBomba());
        holder.txtFechaTransaccion.setText(lecturasx.get(position).getFechaInicio() + " | Inicio: " + lecturasx.get(position).getHoraInicio() +" - Fin: "+ lecturasx.get(position).getHoraFin());
        holder.txtCantidadGalones.setText("" + lecturasx.get(position).getVolumen() + " gal" + " | Temperatura: °C " + lecturasx.get(position).getTemperatura());
        holder.txtPlaca.setText("" + lecturasx.get(position).getPlaca());

    }

    @Override
    public int getItemCount() {
        return lecturasx.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }
    @Override
    public void onClick(View v) {
        if(listener!= null){
            listener.onClick(v);
        }
    }

    public static class ErrorViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView txtCabecera ;
        TextView txtFechaTransaccion;
        TextView txtCantidadGalones;
        TextView txtPlaca;

        ErrorViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvTransaction);
            txtCabecera = (TextView)itemView.findViewById(R.id.txtCabecera);
            txtFechaTransaccion = (TextView)itemView.findViewById(R.id.txtFechaTransaccion);
            txtCantidadGalones = (TextView)itemView.findViewById(R.id.txtCantidadGalones);
            txtPlaca = (TextView)itemView.findViewById(R.id.txtPlaca);
        }
    }


}