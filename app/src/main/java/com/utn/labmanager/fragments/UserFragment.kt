package com.utn.labmanager.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.utn.labmanager.MainActivity
import com.utn.labmanager.R
import com.utn.labmanager.ScanActivity

class UserFragment : Fragment() {
     var userlogged : String = "no user"
    private lateinit var v : View
    lateinit var textUser : TextView
    lateinit var btnLogout : Button
    private lateinit var mActivity: MainActivity
    private lateinit var auth: FirebaseAuth
    companion object {
        fun newInstance() = UserFragment()
    }

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v= inflater.inflate(R.layout.fragment_user, container, false)
        textUser=v.findViewById(R.id.textView_user)
        btnLogout=v.findViewById(R.id.button_logout)
        val userlogged2= FirebaseAuth.getInstance().currentUser
        if (userlogged2 != null) {
            userlogged=userlogged2.email.toString()
        }

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        mActivity= context as MainActivity
        textUser.setText(userlogged)
        btnLogout.setOnClickListener {

            auth = FirebaseAuth.getInstance()
            auth.signOut()
            var intent = Intent(context, MainActivity::class.java)
            startActivity(intent)
            val currentUser: FirebaseUser? = auth.currentUser
            if (currentUser != null) {
                // El usuario está autenticado, puedes obtener sus detalles
                val uid = currentUser.uid
                val displayName = currentUser.displayName
                val email = currentUser.email
                val photoUrl = currentUser.photoUrl

                // Realiza las operaciones necesarias con los detalles del usuario
                // ...

                // Ejemplo de impresión de los detalles del usuario
                println("UID del usuario: $uid")
                println("Nombre de usuario: $displayName")
                println("Correo electrónico: $email")
                println("URL de la foto de perfil: $photoUrl")
            } else {
                // El usuario no está autenticado
                println("El usuario no está autenticado")
            }


        }

    }



}