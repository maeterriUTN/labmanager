package com.utn.labmanager.fragments

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ScanViewModel : ViewModel() {


    // TODO: Implement the ViewModel
    companion object {
        fun getreagent(Barcode: String) {
            println("Código leido: $Barcode")
        }

        fun database_try()
        {
            val db = Firebase.firestore
            val reagents = db.collection("reagents")

            val data1 = hashMapOf(
                "Name" to "Colesterol",
                "Quantity" to 0,
            )
            //data1.get("name")
            reagents.document("987654321").set(data1)


        }
    }
}