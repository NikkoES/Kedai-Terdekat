package id.kosanit.nearcoffee.util

import android.app.*
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class DialogUtil {


    companion object {

        lateinit var progressDialog: ProgressDialog

        fun openDialog(context: Context) {
            progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Loading...")
            progressDialog.show()
        }

        fun closeDialog() {
            progressDialog.dismiss()
        }

        fun dialogParam(dialog: Dialog) {
            val params : WindowManager.LayoutParams = dialog.window.attributes
            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height  = WindowManager.LayoutParams.MATCH_PARENT
            dialog.window.attributes = params
            dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        fun showToast(context: Context, message: String, duration: Int) {
            Toast.makeText(context, message, duration).show()
        }

        fun showSnack(context: Activity, message: String, listener: View.OnClickListener) {
            Snackbar.make(context.findViewById<View>(android.R.id.content), message, Snackbar.LENGTH_LONG)
                .setAction("Close", listener)
                .show()
        }

        fun dialogDate(context: Context, etDate: EditText, format: String) {
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            val tz = TimeZone.getTimeZone("Asia/Jakarta")
            dateFormat.timeZone = tz
            val newCalendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)
                    etDate.setText(dateFormat.format(newDate.time))
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        fun dialogDatePast(context: Context, etDate: EditText, format: String) {
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            val tz = TimeZone.getTimeZone("Asia/Jakarta")
            dateFormat.timeZone = tz
            val newCalendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)
                    val bulan = convertBulan(newDate.time.month)
                    val tgl = dateFormat.format(newDate.time)

                    etDate.setText(tgl)
                    etDate.error = null
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.maxDate = Date().time
            datePickerDialog.show()
        }

        fun dialogDateFuture(context: Context, etDate: EditText, format: String) {
            val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
            val tz = TimeZone.getTimeZone("Asia/Jakarta")
            dateFormat.timeZone = tz
            val newCalendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(context,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val newDate = Calendar.getInstance()
                    newDate.set(year, monthOfYear, dayOfMonth)
                    val bulan = convertBulan(newDate.time.month)
                    val tgl = dateFormat.format(newDate.time)

                    etDate.setText(tgl)
                    etDate.error = null
                },
                newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = Date().time
            datePickerDialog.show()
        }

        fun dialogJam(context: Context, etWaktu: EditText) {
            val mcurrentTime = Calendar.getInstance()
            val hour = mcurrentTime.get(Calendar.HOUR_OF_DAY)
            val minute = mcurrentTime.get(Calendar.MINUTE)

            val mTimePicker: TimePickerDialog
            mTimePicker = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                    var hour = selectedHour % 12
                    if (hour == 0){
                        hour = 12
                    }
                    etWaktu.setText(
                        String.format(
                            "%02d:%02d %s", hour, selectedMinute,
                            if (selectedHour < 12){
                                "AM"
                            } else {
                                "PM"
                            }
                        )
                    )
                    etWaktu.error = null
                }, hour, minute, true
            )
            mTimePicker.show()
        }

        fun convertBulan(num: Int): String {
            var bulan = ""
            when (num) {
                0 -> bulan = "Januari"
                1 -> bulan = "Februari"
                2 -> bulan = "Maret"
                3 -> bulan = "April"
                4 -> bulan = "Mei"
                5 -> bulan = "Juni"
                6 -> bulan = "Juli"
                7 -> bulan = "Agustus"
                8 -> bulan = "September"
                9 -> bulan = "Oktober"
                10 -> bulan = "November"
                11 -> bulan = "Desember"
            }
            return bulan
        }

        fun dialogArray(context: Context, title: String, items: Array<String>, clickListener: DialogInterface.OnClickListener) {
            val builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(items, clickListener)

            val dialog = builder.create()
            dialog.show()
        }

        fun dialogMultipleArray(context: Context, title: String, cancelable: Boolean, items: Array<String>,  checkedItem: BooleanArray, multiChoiceClickListener: DialogInterface.OnMultiChoiceClickListener, pilihListener: DialogInterface.OnClickListener, cancelListener: DialogInterface.OnClickListener, clearListener: DialogInterface.OnClickListener) {
            val builder = AlertDialog.Builder(context)
                .setTitle(title)
                .setCancelable(cancelable)
                .setMultiChoiceItems(items, checkedItem, multiChoiceClickListener)
                .setPositiveButton("Pilih", pilihListener)
                .setNegativeButton("Batal", cancelListener)
                .setNeutralButton("Clear", clearListener)

            val dialog = builder.create()
            dialog.show()
        }

        fun dialogMessage(context: Context, message: String, cancelable: Boolean, onClickListener: DialogInterface.OnClickListener){
            val builder = AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", onClickListener)
                .setCancelable(cancelable)
            val dialog = builder.create()
            dialog.show()
        }

        fun dialogYesNo(context: Context, message: String, cancelable: Boolean, onPositiveClick: DialogInterface.OnClickListener, onNegativeClick: DialogInterface.OnClickListener){
            val builder = AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Ya", onPositiveClick)
                .setNegativeButton("Tidak", onNegativeClick)
                .setCancelable(cancelable)
            val dialog = builder.create()
            dialog.show()
        }

        fun dialogErrors(response: String?): String{
            val responseObject = JSONObject(response);
            val keys = responseObject.keys()
            var messages = ""

            for (i in keys) {
                messages += responseObject.getString(i)
                    ?.replace("\"", "")
                    ?.replace("[", "")
                    ?.replace("]", "")
                if(keys.hasNext()){
                    messages += "\n\n"
                }
            }

            return messages
        }

    }

}