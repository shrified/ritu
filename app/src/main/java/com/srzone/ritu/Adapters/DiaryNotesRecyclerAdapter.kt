package com.srzone.ritu.Adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.srzone.ritu.Activities.DiaryNotesPreviewActivity
import com.srzone.ritu.Databases.Entities.Note
import com.srzone.ritu.Databases.Params
import com.srzone.ritu.R

class DiaryNotesRecyclerAdapter(
    var notesList: MutableList<Note?>,
    var listener: OnLongItemCLickedListener,
    var activity: Activity
) : RecyclerView.Adapter<DiaryNotesRecyclerAdapter.ViewHolder>() {

    interface OnLongItemCLickedListener {
        fun onItemLongClicked(id: Int)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.diary_notes_item, viewGroup, false)
        )
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val noteItem = this.notesList[position] ?: return
        
        viewHolder.dateTv.text = noteItem.date
        viewHolder.noteTv.text = noteItem.note
        
        viewHolder.itemView.setOnClickListener {
            openNotePreview(position)
        }
        
        viewHolder.itemView.setOnLongClickListener {
            listener.onItemLongClicked(noteItem.id)
            true
        }
    }

    private fun openNotePreview(position: Int) {
        val noteItem = this.notesList[position] ?: return
        val intent = Intent(this.activity, DiaryNotesPreviewActivity::class.java)
        intent.putExtra(Params.KEY_NOTE_ID, noteItem.id)
        this.activity.startActivity(intent)
    }

    override fun getItemCount(): Int {
        return this.notesList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTv: TextView = view.findViewById(R.id.dateRecyclerItem)
        val noteTv: TextView = view.findViewById(R.id.notesDescTv)
    }
}
