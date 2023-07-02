package com.utn.labmanager.fragments

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utn.labmanager.R
import com.utn.labmanager.adapters.reagentAdapter
import com.utn.labmanager.entities.reagent
import kotlinx.coroutines.launch

class ListFragment : Fragment() {
    lateinit var v : View
    lateinit var ReagentList : RecyclerView
    lateinit var buttonPedir : Button
    lateinit var adapter : reagentAdapter

    companion object {
        fun newInstance() = ListFragment()
    }

    private lateinit var viewModel: ListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_list, container, false)
        ReagentList = v.findViewById(R.id.view_reagents)
        buttonPedir =v.findViewById(R.id.button_pedir)
        ListViewModel.listInit()

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)


        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        adapter = reagentAdapter(ListViewModel.ReagentToView){position ->
            onItemClick(position)
        }
        ReagentList.layoutManager= LinearLayoutManager(context)
        ReagentList.adapter=adapter
        lifecycleScope.launch {
            ListViewModel.ListFlow.collect { NewList ->

                adapter.notifyDataSetChanged()
                 Toast.makeText(context, "Datos actualizados", Toast.LENGTH_SHORT).show()
            }
        }
        buttonPedir.setOnClickListener {

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Prueba de mandar los reactivos")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)

        }
    }

    fun onItemClick (pos : Int){

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Quitar reactivo")
        builder.setMessage(
            "Confirme quitar una unidad de ${ListViewModel.ReagentToView[pos].name} de la lista"
        )


        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            Toast.makeText(
                context,
                android.R.string.yes, Toast.LENGTH_SHORT
            ).show()
            ListViewModel.database_decrement(ListViewModel.ReagentToView[pos].code,pos)

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            Toast.makeText(
                context,
                android.R.string.no, Toast.LENGTH_SHORT
            ).show()

        }

        builder.show()

            }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(actualizacionPeriodica, 1000)

    }

    private val handler = Handler()
    private val actualizacionPeriodica = object : Runnable {
        override fun run() {

            adapter.notifyDataSetChanged()
            handler.postDelayed(this, 1000) // 1000 ms = 1 segundo
        }
    }
}

