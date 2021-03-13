package com.example.cuadernodelprofesor.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuadernodelprofesor.R;
import com.example.cuadernodelprofesor.ui.fragments.ForoPostFragment;

import java.util.List;


public class Adapter_Post extends RecyclerView.Adapter<Adapter_Post.Holder> {
    public static Object ListItemClickListener;
    private List<Model_Post> elementos;
    private ListItemClickListener myOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int position);
    }

    public void setListItemClickListener(ListItemClickListener listener) {
        this.myOnClickListener = listener;
    }

    public Adapter_Post(List<Model_Post> elementos, ListItemClickListener myOnClickListener) {
        this.elementos = elementos;
        this.myOnClickListener = myOnClickListener;
    }


    public class Holder extends RecyclerView.ViewHolder {
        private TextView tituloPost, autorPost, contenidoPost;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tituloPost = itemView.findViewById(R.id.tituloPost);
            autorPost = itemView.findViewById(R.id.autorPost);
            contenidoPost = itemView.findViewById(R.id.contenidoReply);
            itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (myOnClickListener!=null){
                        int position = getAdapterPosition();
                        if (position!= RecyclerView.NO_POSITION){
                            myOnClickListener.onListItemClick(position);
                        }
                    }

                }
            });
        }


    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post, parent, false);
        // DEVOLVEMOS EL HOLDER, QUE NOS PIDE COMO PARÁMETRO UNA VISTA QUE ACABAMOS DE INFLAR CON EL LAYOUT DEL CARDVIEW QUE
        // HICIMOS ANTES
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        // ESTE MÉTODO SE VA A EJECUTAR TANTAS VEZ COMO TAMAÑO TENGA EL ItemList
        // POR ESO SE LE PASA LA POSICIÓN COMO PARÁMETRO ENTERO
        Model_Post elemento = elementos.get(position);

        // AHORA VAMOS A RELLENAR EL CARDVIEW CON NUESTROS ELEMENTOS
        // PARA ELLO ACCEDEMOS A CADA ELEMENTO A TRAVÉS DEL OBJETO holder QUE RECIBIMOS
        // COMO PARÁMETRO
        holder.tituloPost.setText(elemento.getTituloPost());
        holder.autorPost.setText(elemento.getAutorPost());
        holder.contenidoPost.setText(elemento.getContenidoPost());
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }
}