package com.example.cuadernodelprofesor.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuadernodelprofesor.R;

import java.util.List;


// EN ESTE ADAPTADOR NO NECESITAMOS EL ON CLICK LISTENER PORQUE NO SE VA A PULSAR EN LAS CARDVIEW DE LAS RESPUESTAS
public class Adapter_Reply extends RecyclerView.Adapter<Adapter_Reply.Holder> {
    private List<Model_Reply> elementos;

    public interface ListItemClickListener{
        void onListItemClick(int position);
    }


    public Adapter_Reply(List<Model_Reply> elementos) {
        this.elementos = elementos;
    }



    public class Holder extends RecyclerView.ViewHolder {
        private TextView autorReply, contenidoReply;

        public Holder(@NonNull View itemView) {
            super(itemView);
            autorReply = itemView.findViewById(R.id.autorReply);
            contenidoReply = itemView.findViewById(R.id.contenidoReply);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_reply, parent, false);
        // DEVOLVEMOS EL HOLDER, QUE NOS PIDE COMO PARÁMETRO UNA VISTA QUE ACABAMOS DE INFLAR CON EL LAYOUT DEL CARDVIEW QUE
        // HICIMOS ANTES
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        // ESTE MÉTODO SE VA A EJECUTAR TANTAS VEZ COMO TAMAÑO TENGA EL ItemList
        // POR ESO SE LE PASA LA POSICIÓN COMO PARÁMETRO ENTERO
        Model_Reply elemento = elementos.get(position);

        // AHORA VAMOS A RELLENAR EL CARDVIEW CON NUESTROS ELEMENTOS
        // PARA ELLO ACCEDEMOS A CADA ELEMENTO A TRAVÉS DEL OBJETO holder QUE RECIBIMOS
        // COMO PARÁMETRO
        holder.autorReply.setText(elemento.getAutorReply());
        holder.contenidoReply.setText(elemento.getContenidoReply());
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }
}