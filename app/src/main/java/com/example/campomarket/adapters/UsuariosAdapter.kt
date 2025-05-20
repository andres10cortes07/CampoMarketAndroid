package com.example.campomarket.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.campomarket.R
import com.example.campomarket.data.model.Usuario
import com.google.android.material.button.MaterialButton

class UsuariosAdapter(
    private val compradores: MutableList<Usuario>,
    private val onEditarClick: (Usuario) -> Unit,
    private val onEliminarClick: (Usuario) -> Unit
) : RecyclerView.Adapter<UsuariosAdapter.CompradorViewHolder>() {

    inner class CompradorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtNombre = view.findViewById<TextView>(R.id.txtNombreComprador)
        val txtTipoDoc = view.findViewById<TextView>(R.id.txtTipoDocComprador)
        val txtId = view.findViewById<TextView>(R.id.txtIdComprador)
        val txtCelular = view.findViewById<TextView>(R.id.txtCelularComprador)
        val txtCorreo = view.findViewById<TextView>(R.id.txtCorreoComprador)
        val txtDepartamento = view.findViewById<TextView>(R.id.txtDepartamentoComprador)
        val txtCiudad = view.findViewById<TextView>(R.id.txtCiudadComprador)
        val txtDireccion = view.findViewById<TextView>(R.id.txtDireccionComprador)

        val btnEditar = view.findViewById<MaterialButton>(R.id.btnEditarUsuario)
        val btnEliminar = view.findViewById<MaterialButton>(R.id.btnEliminarUsuario)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompradorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_usuarios, parent, false)
        return CompradorViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompradorViewHolder, position: Int) {
        val comprador = compradores[position]

        holder.txtNombre.text = "${comprador.nombres} ${comprador.apellidos}"
        holder.txtTipoDoc.text = comprador.tipoDocumento
        holder.txtId.text = comprador.numeroIdentificacion
        holder.txtCelular.text = comprador.celular
        holder.txtCorreo.text = comprador.correo
        holder.txtDepartamento.text = comprador.departamento
        holder.txtCiudad.text = comprador.ciudad
        holder.txtDireccion.text = comprador.direccion

        holder.btnEditar.setOnClickListener {
            onEditarClick(comprador)
        }
        holder.btnEliminar.setOnClickListener {
            onEliminarClick(comprador)
        }
    }

    override fun getItemCount(): Int = compradores.size

    fun actualizarLista(nuevaLista: List<Usuario>) {
        compradores.clear()
        compradores.addAll(nuevaLista)
        notifyDataSetChanged()
    }
}
