package com.utn.labmanager.fragments

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ScanViewModel : ViewModel() {


    // TODO: Implement the ViewModel
    companion object {

        fun getreagent(Barcode: String) : String {
            val db = Firebase.firestore
            val docRef = db.collection("reagents").document(Barcode)
            var retString : String = ""
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {

                        Log.d(ContentValues.TAG, document.get("Name").toString())
                        retString="${document.get("Name").toString()}".toString()
                        println("Reactivo leido ${document.get("Name").toString()}")
                        Log.d(ContentValues.TAG, "DocumentSnapshot data: ${document.data}")
                    } else {
                        Log.d(ContentValues.TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(ContentValues.TAG, "get failed with ", exception)
                }
            println("Reactivo leido: $retString")
            return retString

        }

        fun database_try(code : String)
        {
            val db = Firebase.firestore
            val reagents = db.collection("reagents")

            val data1 = hashMapOf(
                "Name" to "Colesterol",
                "Quantity" to 0,
            )
            //data1.get("name")
            //reagents.document(code).set(data1)
            reagents.document(code).update("Quantity", FieldValue.increment(1))
            println("Código: $code incrementado")


        }
    }
}