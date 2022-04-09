package com.sulthoni.challange_chapter_4.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.findNavController
import com.sulthoni.challange_chapter_4.Constant
import com.sulthoni.challange_chapter_4.R
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val registerPref =
            this.requireActivity()
                .getSharedPreferences(Constant.Preferences.PREF_NAME, MODE_PRIVATE)

        btnRegis.setOnClickListener {
            val Username = requireView().findViewById<EditText>(R.id.etUsername).text.toString()
            val Email = requireView().findViewById<EditText>(R.id.etEmailRegis).text.toString()
            val Password =
                requireView().findViewById<EditText>(R.id.etPasswordRegis).text.toString()
            val KonfPass = requireView().findViewById<EditText>(R.id.etKonfPass).text.toString()

            if (Email.isEmpty() || Password.isEmpty() || Username.isEmpty() || KonfPass.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Tidak boleh ada isian yang kosong",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (Password.length < 8) {
                Toast.makeText(
                    requireContext(),
                    "Password harus lebih dari 8 karakter",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (KonfPass != Password) {
                Toast.makeText(requireContext(), "Password tidak sesuai", Toast.LENGTH_SHORT).show()
            } else if (!Password.matches(Regex("(?=.*[a-z])(?=.*[A-Z]).+"))) {
                Toast.makeText(
                    requireContext(),
                    "Password harus mengandung upper case dan lowercase lemah",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (!Email
                    .matches(Regex("^[a-zA-Z0-9_.]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$"))
            ) {
                Toast.makeText(requireContext(), "Email salah", Toast.LENGTH_SHORT).show()
            } else if (registerPref.getString(Constant.Preferences.KEY.USERNAME, "") == Email
            ) {
                Toast.makeText(requireContext(), "Akun sudah ada", Toast.LENGTH_SHORT)
                    .show()
            } else {
                val editor = registerPref.edit()
                editor.putString(Constant.Preferences.KEY.EMAIL, Email)
                editor.putString(Constant.Preferences.KEY.PASSWORD, Password)
                editor.putString(Constant.Preferences.KEY.USERNAME, Username)
                editor.apply()

                val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                it.findNavController().navigate(action)
            }
        }
    }

}