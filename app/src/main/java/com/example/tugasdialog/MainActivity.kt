package com.example.tugasdialog

import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasdialog.databinding.ActivityMainBinding


class MainActivity() : AppCompatActivity(), Parcelable {
    private lateinit var binding : ActivityMainBinding
    private var daySave = -1
    private var monthSave  = -1
    private var yearSave = -1
    private var hourSave = -1
    private var minuteSave = -1
    private val listOfMonth = arrayOf("Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember")

    constructor(parcel: Parcel) : this() {
        daySave = parcel.readInt()
        monthSave = parcel.readInt()
        yearSave = parcel.readInt()
        hourSave = parcel.readInt()
        minuteSave = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(daySave)
        parcel.writeInt(monthSave)
        parcel.writeInt(yearSave)
        parcel.writeInt(hourSave)
        parcel.writeInt(minuteSave)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MainActivity> {
        override fun createFromParcel(parcel: Parcel): MainActivity {
            return MainActivity(parcel)
        }

        override fun newArray(size: Int): Array<MainActivity?> {
            return arrayOfNulls(size)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val absensi = resources.getStringArray(R.array.absensi)

        with(binding) {
            calendar.setOnDateChangeListener { _, year, month, day ->
                daySave = day
                monthSave = month
                yearSave = year
            }
            val adapterAbsensi = ArrayAdapter(
                this@MainActivity,
                android.R.layout.simple_spinner_dropdown_item,
                absensi
            )
            adapterAbsensi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAbsensi.adapter = adapterAbsensi

            jam.setOnTimeChangedListener { _, hour, minute ->
                hourSave = hour
                minuteSave = minute
            }

            spinnerAbsensi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position > 0) {
                        keterangan.visibility = View.VISIBLE
                    } else {
                        keterangan.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {

                }
            }

            btnSubmit.setOnClickListener {
                if (daySave == -1 || monthSave == -1 || yearSave == -1) {
                    Toast.makeText(
                        this@MainActivity,
                        "Pilih Tanggal Terlebih Dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (hourSave == -1 || minuteSave == -1) {
                    Toast.makeText(
                        this@MainActivity,
                        "Pilih Jam Terlebih Dahulu",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val selectedTime = String.format("%02d:%02d", hourSave, minuteSave)
                    val monthConvert = if (monthSave in 0..11) listOfMonth[monthSave] else ""
                    val selectedDate = String.format("%02d %s %04d", daySave, monthConvert, yearSave)

                    Toast.makeText(
                        this@MainActivity,
                        "Presensi Berhasil Dilakukan Pada $selectedDate jam $selectedTime",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

}