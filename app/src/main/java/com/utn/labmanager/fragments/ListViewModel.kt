package com.utn.labmanager.fragments

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.utn.labmanager.entities.reagent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ListViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    companion object {
        var ReagentToView: MutableList<reagent> = mutableListOf()
        private val _ListFlow = MutableStateFlow(ReagentToView)
        val ListFlow: StateFlow<List<reagent>> get() = _ListFlow


        fun listInit () {
            val db = Firebase.firestore
            val collectionRef = db.collection("reagents")
            val documentList: MutableList<DocumentSnapshot> = mutableListOf()
            collectionRef.get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        documentList.add(document)
                    }

                    for (document in documentList) {
                        val documentName = document.get("Name")
                        if(document.get("Quantity").toString().toInt()>0)
                        {   val index= ReagentToView.indexOfFirst { elemento -> elemento.code == document.id}
                            if (index==-1) {
                                ReagentToView.add(
                                    reagent(
                                        document.id,
                                        document.get("Name").toString(),
                                        document.get("Quantity").toString().toInt()
                                    )

                                )
                                _ListFlow.value = ReagentToView

                            }
                            else{
                                ReagentToView[index].quantity=document.get("Quantity").toString().toInt()

                            }
                            }
                        else{
                            val index= ReagentToView.indexOfFirst { elemento -> elemento.code == document.id}
                            if(index!=-1){
                            ReagentToView.removeAt(index)}
                        }
                        println("documento hallado: $documentName")
                    }
                }
                .addOnFailureListener { exception ->
                    // Manejo de errores
                    println("Error al obtener los documentos: $exception")
                }



        }


        fun database_decrement(code : String,pos : Int)
        {
            val db = Firebase.firestore
            val reagents = db.collection("reagents")
            reagents.document(code).update("Quantity", FieldValue.increment(-1))
            ReagentToView[pos].quantity=ReagentToView[pos].quantity-1
            if (ReagentToView[pos].quantity<=0){
                ReagentToView.removeAt(pos)
            }
            println("Código: $code decrementado")


        }
     fun generate_data_string() : String{
         var output :String = ""
         for (index in ReagentToView )
         {
             output=output+ index.code + " " + index.name + " " + index.quantity.toString() +"\n"

         }
         return output
     }

    }
}