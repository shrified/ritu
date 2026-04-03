package com.srzone.ritu.Activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.srzone.ritu.Adapters.DiaryNotesRecyclerAdapter
import com.srzone.ritu.Adapters.DiaryNotesRecyclerAdapter.OnLongItemCLickedListener
import com.srzone.ritu.Databases.Entities.Note
import com.srzone.ritu.Databases.NoteHandler
import com.srzone.ritu.R
import com.srzone.ritu.Utils.Utils
import com.srzone.ritu.databinding.ActivityNotesBinding
import java.util.Calendar
import java.util.Collections

class NotesActivity : AppCompatActivity(), OnLongItemCLickedListener {
    var binding: ActivityNotesBinding? = null
    var handler: NoteHandler? = null
    var savedNotes: Boolean = false
    var setListener: OnDateSetListener? = null

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val inflate = ActivityNotesBinding.inflate(getLayoutInflater())
        this.binding = inflate
        setContentView(inflate.getRoot())

        //        AdsGoogle adsGoogle = new AdsGoogle( this);
//        adsGoogle.Banner_Show((RelativeLayout) findViewById(R.id.banner), this);
//        adsGoogle.Interstitial_Show_Counter(this);
        setCurrentDate()
        this.binding!!.parentLayout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@NotesActivity.m112x3d0b51f4(view)
            }
        })
        this.binding!!.saveBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@NotesActivity.m113x261316f5(view)
            }
        })
        showNotesList()
        this.binding!!.cancelBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@NotesActivity.m114xf1adbf6(view)
            }
        })
        this.binding!!.addBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@NotesActivity.m115xf822a0f7(view)
            }
        })
        this.binding!!.dateTv.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                this@NotesActivity.m116xe12a65f8(view)
            }
        })
    }


    fun m112x3d0b51f4(view: View?) {
        Utils.hideKeyboard(this)
    }


    fun m113x261316f5(view: View?) {
        Utils.hideKeyboard(this)
        if (this.binding!!.dairyNoteInp.getText().toString().isEmpty()) {
            this.binding!!.dairyNoteInp.setError(getResources().getString(R.string.please_fill_all_fields))
        } else {
            addNote()
        }
    }


    fun m114xf1adbf6(view: View?) {
        Utils.hideKeyboard(this)
        showNotesList()
    }


    fun m115xf822a0f7(view: View?) {
        this.binding!!.inputArea.setVisibility(View.VISIBLE)
        this.binding!!.detailArea.setVisibility(View.GONE)
        this.binding!!.addBtn.setVisibility(View.GONE)
    }


    fun m116xe12a65f8(view: View?) {
        val split: Array<String?> = this.binding!!.dateTv.getText().toString().split("/".toRegex())
            .dropLastWhile { it.isEmpty() }.toTypedArray()
        val trim = split[0]!!.trim { it <= ' ' }
        val trim2 = split[1]!!.trim { it <= ' ' }
        val datePickerDialog = DatePickerDialog(
            this,
            16973940,
            this.setListener,
            split[2]!!.trim { it <= ' ' }.toInt(),
            trim2.toInt() - 1,
            trim.toInt()
        )
        datePickerDialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(0))
        datePickerDialog.show()
    }

    fun setCurrentDate() {
        val calendar = Calendar.getInstance()
        val i = calendar.get(1)
        val i2 = calendar.get(5)
        this.binding!!.dateTv.setText(i2.toString() + "/" + (calendar.get(2) + 1) + "/" + i)
    }

    public override fun onResume() {
        super.onResume()
        this.handler = NoteHandler(this)
        showNotesList()
    }

    private fun addNote() {
        this.handler!!.addNote(
            Note(
                this.binding!!.dateTv.getText().toString(),
                this.binding!!.dairyNoteInp.getText().toString()
            )
        )
        this.binding!!.inputArea.setVisibility(View.GONE)
        this.binding!!.detailArea.setVisibility(View.VISIBLE)
        this.binding!!.addBtn.setVisibility(View.VISIBLE)
        this.binding!!.dairyNoteInp.setText("")
        showNotesList()
        Toast.makeText(
            this,
            getResources().getString(R.string.saved_successfully),
            Toast.LENGTH_SHORT
        ).show()
        this.savedNotes = true
        Utils.hideKeyboard(this)
    }

    private fun showNotesList() {
        this.setListener = object : OnDateSetListener {
            override fun onDateSet(datePicker: DatePicker?, i: Int, i2: Int, i3: Int) {
                this@NotesActivity.m118x1de85726(datePicker, i, i2, i3)
            }
        }
        var arrayList: MutableList<Note?> = ArrayList<Note?>()
        val noteHandler = this.handler
        if (noteHandler != null) {
            arrayList = noteHandler.allNotes
        }
        if (arrayList.size > 0) {
            this.binding!!.diaryNotesRecycler.setLayoutManager(LinearLayoutManager(this))
            Collections.reverse(arrayList)
            this.binding!!.diaryNotesRecycler.setAdapter(
                DiaryNotesRecyclerAdapter(
                    arrayList,
                    this,
                    this
                )
            )
            this.binding!!.diaryNotesRecycler.scrollToPosition(0)
            this.binding!!.diaryNotesRecycler.setVisibility(View.VISIBLE)
            this.binding!!.emptyTv.setVisibility(View.GONE)
            this.binding!!.inputArea.setVisibility(View.GONE)
            this.binding!!.detailArea.setVisibility(View.VISIBLE)
            this.binding!!.addBtn.setVisibility(View.VISIBLE)
            return
        }
        this.binding!!.addBtn.setVisibility(View.GONE)
        this.binding!!.detailArea.setVisibility(View.GONE)
        this.binding!!.inputArea.setVisibility(View.VISIBLE)
        this.binding!!.emptyTv.setVisibility(View.VISIBLE)
        this.binding!!.diaryNotesRecycler.setVisibility(View.GONE)
    }


    fun m118x1de85726(datePicker: DatePicker?, i: Int, i2: Int, i3: Int) {
        val i4 = i2 + 1
        this.binding!!.dateTv.setText(i3.toString() + "/" + i4 + "/" + i)
        val calendar = Calendar.getInstance()
        calendar.set(1, i)
        calendar.set(2, i4 + (-1))
        calendar.set(5, i3)
    }

    override fun onItemLongClicked(i: Int) {
        AlertDialog.Builder(this).setTitle(getResources().getString(R.string.delete_confirmation))
            .setMessage(getResources().getString(R.string.are_you_sure_you_want_to_delete_this_history))
            .setPositiveButton(
                getResources().getString(R.string.yes),
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface?, i2: Int) {
                        this@NotesActivity.m117xbca8c07e(i, dialogInterface, i2)
                    }
                }).setNegativeButton(
                getResources().getString(R.string.no),
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i2: Int) {
                        `lambda$onItemLongClicked$7`(dialogInterface, i2)
                    }
                }).show()
    }


    fun m117xbca8c07e(i: Int, dialogInterface: DialogInterface?, i2: Int) {
        this.handler!!.deleteNotes(i.toString())
        showNotesList()
        Toast.makeText(
            this,
            getResources().getString(R.string.deleted_successfully),
            Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        fun `lambda$onItemLongClicked$7`(dialogInterface: DialogInterface, i: Int) {
            dialogInterface.dismiss()
        }
    }
}
