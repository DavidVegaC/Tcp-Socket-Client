package com.example.tcpsocketclient.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
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
        holder.txtCabecera.setText("Ticket: " + lecturasx.get(position).getNumeroTransaccion() + " | " + "Estación: Pionero | Bomba: " + lecturasx.get(position).getNombreManguera());
        holder.txtFechaTransaccion.setText(lecturasx.get(position).getFechaInicio() + " | Inicio: " + lecturasx.get(position).getHoraInicio() +" - Fin: "+ lecturasx.get(position).getHoraFin());
        holder.txtCantidadGalones.setText("" + lecturasx.get(position).getVolumen() + " gal" + " | Temperatura: °C " + lecturasx.get(position).getTemperatura());
        holder.txtPlaca.setText("" + lecturasx.get(position).getPlaca());
        if(lecturasx.get(position).getEstadoMigracion().equals("M"))
            holder.ly_migracion_estado.setBackgroundResource(R.drawable.bg_para_migracion_3);
        else
            holder.ly_migracion_estado.setBackgroundResource(R.drawable.bg_para_migracion_1);

        holder.lyVerFotoVehiculo.setTag(""+position);
        holder.lyVerFotoVehiculo.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return lecturasx.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
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
        LinearLayout lyVerFotoVehiculo, ly_migracion_estado;

        ErrorViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cvTransaction);
            txtCabecera = (TextView)itemView.findViewById(R.id.txtCabecera);
            txtFechaTransaccion = (TextView)itemView.findViewById(R.id.txtFechaTransaccion);
            txtCantidadGalones = (TextView)itemView.findViewById(R.id.txtCantidadGalones);
            txtPlaca = (TextView)itemView.findViewById(R.id.txtPlaca);
            lyVerFotoVehiculo = (LinearLayout)itemView.findViewById(R.id.lyVerFotoVehiculo);
            ly_migracion_estado= (LinearLayout)itemView.findViewById(R.id.ly_migracion_estado);

       }

    }



}