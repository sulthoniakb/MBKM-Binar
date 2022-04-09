package com.sulthoni.challange_chapter_4.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.roomapp.data.NotesDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sulthoni.challange_chapter_4.Constant
import com.sulthoni.challange_chapter_4.R
import com.sulthoni.challange_chapter_4.adapter.NotesAdapter
import com.sulthoni.challange_chapter_4.model.Notes
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private var DB: NotesDatabase? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        DB = NotesDatabase.getInstance(view.context.applicationContext)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerview)

        recyclerView.layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
        fetchData()

        val sharedPreferences:SharedPreferences = requireActivity().getSharedPreferences(Constant
            .Preferences.PREF_NAME, Context.MODE_PRIVATE
        )

        val username = sharedPreferences.getString(Constant.Preferences.KEY.USERNAME, "")
        val tvWelcome: TextView = requireView().findViewById(R.id.tvWelcome)
        tvWelcome.text = "Welcome, $username!"

        val tvLogout: TextView = requireView().findViewById(R.id.tvLogout)
        tvLogout.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            it.findNavController().navigate(action)
        }
        val fabInput: FloatingActionButton = requireView().findViewById(R.id.floatingActionButton)
        fabInput.setOnClickListener {
            val view = LayoutInflater.from(it.context).inflate(R.layout.dialog_add, null, false)
            val dialogBuilder = AlertDialog.Builder(it.context)
            dialogBuilder.setView(view)

            val dialog = dialogBuilder.create()
            val btnInput: Button = view.findViewById(R.id.btnSave)
            val etJudul: EditText = view.findViewById(R.id.etTitle)
            val etIsi: EditText = view.findViewById(R.id.etData)

            dialog.show()
            dialog.setCancelable(true)
            btnInput.setOnClickListener {
                val objectNote = Notes(
                    null,
                    etJudul.text.toString(),
                    etIsi.text.toString()
                )

                activity?.runOnUiThread {
                    GlobalScope.async {
                        val result = DB?.NotesDao()?.insertData(objectNote)
                        if (result != 0.toLong()) {
                            Toast.makeText(it.context, "Berhasil Menambahkan", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            Toast.makeText(it.context, "Gagal Menambahkan", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                    fetchData()
                }
                fetchData()
                dialog.dismiss()
            }
            fetchData()
        }
        fetchData()


    }

    fun fetchData() {
        val swipeRefresh: SwipeRefreshLayout = requireView().findViewById(R.id.refresh_layout)
        swipeRefresh.setOnRefreshListener {
            fetchData()
            swipeRefresh.isRefreshing = false
        }

        GlobalScope.launch {
            val list = DB?.NotesDao()?.getAllNotes()
            activity?.runOnUiThread {
                val adapter = NotesAdapter(list as ArrayList<Notes>)
                val recyclerView: RecyclerView = requireView().findViewById(R.id.recyclerview)
                recyclerView.adapter = adapter
            }
        }
    }
}