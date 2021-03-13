package com.example.cuadernodelprofesor.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuadernodelprofesor.R;

import java.util.List;


public class Adapter_Alumnos extends RecyclerView.Adapter<Adapter_Alumnos.Holder> {
    private List<Model_Alumnos> elementos;
    final private ListItemClickListener myOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(int position);
    }

    public Adapter_Alumnos(List<Model_Alumnos> elementos, ListItemClickListener onClickListener) {
        this.elementos = elementos;
        this.myOnClickListener = onClickListener;
    }



    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nombreAlumno, apellidosAlumno;

        public Holder(@NonNull View itemView) {
            super(itemView);
            nombreAlumno = itemView.findViewById(R.id.nombreAlumno);
            apellidosAlumno = itemView.findViewById(R.id.apellidosAlumno);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            myOnClickListener.onListItemClick(position);
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_alumnosclases, parent, false);
        // DEVOLVEMOS EL HOLDER, QUE NOS PIDE COMO PARÁMETRO UNA VISTA QUE ACABAMOS DE INFLAR CON EL LAYOUT DEL CARDVIEW QUE
        // HICIMOS ANTES
        return new Holder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        // ESTE MÉTODO SE VA A EJECUTAR TANTAS VEZ COMO TAMAÑO TENGA EL ItemList
        // POR ESO SE LE PASA LA POSICIÓN COMO PARÁMETRO ENTERO
        Model_Alumnos elemento = elementos.get(position);

        // AHORA VAMOS A RELLENAR EL CARDVIEW CON NUESTROS ELEMENTOS
        // PARA ELLO ACCEDEMOS A CADA ELEMENTO A TRAVÉS DEL OBJETO holder QUE RECIBIMOS
        // COMO PARÁMETRO
        holder.nombreAlumno.setText(elemento.getNombreAlumnos());
        holder.apellidosAlumno.setText(elemento.getApellidosAlumnos());
    }

    @Override
    public int getItemCount() {
        return elementos.size();
    }
}