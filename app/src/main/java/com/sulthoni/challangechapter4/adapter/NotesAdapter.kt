package com.sulthoni.challange_chapter_4.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.roomapp.data.NotesDatabase
import com.sulthoni.challange_chapter_4.model.Notes
import com.sulthoni.challangechapter4.MainActivity
import com.sulthoni.challangechapter4.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class NotesAdapter(val listNote: List<Notes>) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapter.ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    private var DB: NotesDatabase? = null
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tvTitle: TextView = holder.itemView.findViewById(R.id.tvTitle)
        val tvData: TextView = holder.itemView.findViewById(R.id.tvDataNotes)
        val btnUpdate: ImageButton = holder.itemView.findViewById(R.id.btnUpdate)
        val btnDelete: ImageButton = holder.itemView.findViewById(R.id.btnDelete)
        tvTitle.text = listNote[position].title
        tvData.text = listNote[position].data

        DB = NotesDatabase.getInstance(holder.itemView.context)

        btnDelete.setOnClickListener {
            val v = LayoutInflater.from(it.context).inflate(R.layout.dialog_delete, null, false)
            val dialogBuilder = AlertDialog.Builder(it.context)
            dialogBuilder.setView(v)

            val btnCancel: Button = v.findViewById(R.id.btnCancel)
            val btnAgree: Button = v.findViewById(R.id.btnAgree)
            val dialog = dialogBuilder.create()

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
            btnAgree.setOnClickListener {
                GlobalScope.async {
                    val result = DB?.NotesDao()?.deleteData(listNote[position])
                    (holder.itemView.context as MainActivity).runOnUiThread {
                        if (result != 0) {
                            Toast.makeText(it.context, "Data Dihapus", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(it.context, "Data Gagal Dihapus", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                }
                dialog.dismiss()
            }
        }
        btnUpdate.setOnClickListener {
            val v = LayoutInflater.from(it.context).inflate(R.layout.dialog_edit, null, false)
            val dialogBuilder = AlertDialog.Builder(it.context)
            dialogBuilder.setView(v)

            val dialog = dialogBuilder.create()
            val btnSave: Button = v.findViewById(R.id.btnSave)
            val etEditTitle: EditText = v.findViewById(R.id.etEditTitle)
            val etEditData: EditText = v.findViewById(R.id.etEditData)

            etEditTitle.setText(listNote[position].title)
            etEditData.setText(listNote[position].data)

            dialog.show()
            dialog.setCancelable(true)

            btnSave.setOnClickListener {
                if (etEditTitle.text.toString().isEmpty()) {
                    etEditTitle.error = "Judul tidak boleh kosong"
                    etEditTitle.requestFocus()
                    return@setOnClickListener
                } else if (etEditTitle.text.toString().isEmpty()) {
                    etEditTitle.error = "Isi catatan tidak boleh kosong"
                    etEditTitle.requestFocus()
                    return@setOnClickListener
                }

                val objectNote = Notes(
                    id = listNote[position].id,
                    title = etEditTitle.text.toString(),
                    data = etEditTitle.text.toString()
                )
                GlobalScope.async {
                    val result = DB?.NotesDao()?.updateData(objectNote)

                    (holder.itemView.context as MainActivity).runOnUiThread {
                        if (result != 0) {
                            Toast.makeText(it.context, "Updated", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(it.context, "failed to update", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                dialog.dismiss()
            }
        }
    }

    override fun getItemCount(): Int {
        return listNote.size
    }

}
