package com.polstat.mutasibps.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.polstat.mutasibps.R
import com.polstat.mutasibps.model.MutationRequest
import com.polstat.mutasibps.service.ApiService
import com.polstat.mutasibps.utils.SharedPreferencesManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MutationRequestAdapter(

    private var mutationList: List<MutationRequest>,
    private val onDetailClick: (MutationRequest) -> Unit
) : RecyclerView.Adapter<MutationRequestAdapter.MutationViewHolder>() {

    class MutationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNama: TextView = view.findViewById(R.id.tvNama)
        val tvUnitKerjaTujuan: TextView = view.findViewById(R.id.tvUnitKerjaTujuan)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnDetail: Button = view.findViewById(R.id.btnDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MutationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mutation_request, parent, false)
        return MutationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MutationViewHolder, position: Int) {
        val mutationRequest = mutationList[position]

            holder.tvNama.text = "ID Pengajuan: ${mutationRequest.id}"
            holder.tvUnitKerjaTujuan.text = "Unit Kerja Tujuan: ${mutationRequest.unitKerjaTujuan}"
            holder.tvStatus.text = mutationRequest.status

            // Color the status text based on the status value
            val statusColor = when (mutationRequest.status.lowercase()) {
                "approved" -> R.color.green
                "rejected" -> R.color.red
                else -> R.color.biru
            }
            holder.tvStatus.setTextColor(holder.itemView.context.getColor(statusColor))


        // Event click for mutation request details
        holder.btnDetail.setOnClickListener {
            onDetailClick(mutationRequest)
        }
    }

    override fun getItemCount(): Int = mutationList.size

    fun updateData(newList: List<MutationRequest>) {
        mutationList = newList
        notifyDataSetChanged()
    }
}
