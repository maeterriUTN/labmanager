package com.utn.labmanager.fragments

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.utn.labmanager.R
import com.utn.labmanager.adapters.reagentAdapter
import com.utn.labmanager.entities.reagent

class ListFragment : Fragment() {
    lateinit var v : View
    lateinit var ReagentList : RecyclerView
    lateinit var buttonPedir : Button
    lateinit var adapter : reagentAdapter
    var ReagentToView : MutableList<reagent> = mutableListOf()
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
        ReagentToView.add(reagent("123456789","Glucosa", 1))
        ReagentToView.add(reagent("987654321","Acido urico", 2))
        ReagentToView.add(reagent("987654321","Colesterol", 2))
        ReagentToView.add(reagent("987654321","AHCV", 3))
        ReagentToView.add(reagent("987654321","Vitamina D", 4))

        return v
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onStart() {
        super.onStart()
        adapter = reagentAdapter(ReagentToView){position ->
            onItemClick(position)
        }
        ReagentList.layoutManager= LinearLayoutManager(context)
        ReagentList.adapter=adapter
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

            }

}