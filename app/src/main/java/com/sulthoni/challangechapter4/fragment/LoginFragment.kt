package com.sulthoni.challange_chapter_4.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.sulthoni.challange_chapter_4.Constant
import com.sulthoni.challange_chapter_4.R


class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etEmail = requireView().findViewById<EditText>(R.id.etEmail)
        val etPassword = requireView().findViewById<EditText>(R.id.etPassword)
        val registerPref =
            this.requireActivity()
                .getSharedPreferences(Constant.Preferences.PREF_NAME, MODE_PRIVATE)
        val btnlogin = requireView().findViewById<Button>(R.id.btnLogin)
        btnlogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Email dan Password masih kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (email.isEmpty()) {
                Toast.makeText(requireContext(), "Email masih kosong", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(requireContext(), "Password masih kosong", Toast.LENGTH_SHORT).show()
            } else {
                val emailPreferences =
                    registerPref.getString(Constant.Preferences.KEY.EMAIL, "")
                val passwordPreferences =
                    registerPref.getString(Constant.Preferences.KEY.PASSWORD, "")
                if (email == emailPreferences && password == passwordPreferences) {
                    val action = LoginFragmentDirections.actionLoginFragmentToHomeFragment()
                    it.findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Anda Belum Daftar", Toast.LENGTH_SHORT).show()
                }
            }
            val tvRegister = requireView().findViewById<TextView>(R.id.tvRegis)
            tvRegister.setOnClickListener {
                val action = LoginFragmentDirections.actionSignInFragmentToSignUpFragment2()
                it.findNavController().navigate(action)
            }
        }
    }
}
