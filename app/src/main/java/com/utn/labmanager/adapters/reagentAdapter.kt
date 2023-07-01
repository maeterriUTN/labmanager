package com.utn.labmanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.utn.labmanager.R
import com.utn.labmanager.entities.reagent

//class reagentAdapter {
//}

class reagentAdapter(var reagentList : MutableList <reagent>,
                   var onClick : (Int) -> Unit) : RecyclerView.Adapter<reagentAdapter.ReagentHolder>() {
    class ReagentHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View

        init {
            this.view = v
        }
        fun setName (name : String){
            var txtName : TextView = view.findViewById(R.id.textView_reagent_name)
            txtName.text = name
        }
        fun setAuthor (quantity : String){
            var txtQuantity : TextView = view.findViewById(R.id.textView_reagent_quantity)
            txtQuantity.text = quantity
        }
        fun getCard() : CardView {
            return view.findViewById(R.id.cardReagent)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReagentHolder {
        val view =  LayoutInflater.from(parent.context).inflate(R.layout.item_reagent,parent,false)
        return (ReagentHolder(view))
    }

    override fun getItemCount(): Int {
        return reagentList.size
    }

    override fun onBindViewHolder(holder: ReagentHolder, position: Int) {
        holder.setName(reagentList[position].name)
        holder.setAuthor(reagentList[position].quantity.toString())
        holder.getCard().setOnClickListener {
            onClick(position)

        }
    }
}