package com.example.ingredient.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.example.ingredient.R

class PurchaseConfirmationDialogFragment(var msg:String) : androidx.fragment.app.DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(msg)
            .setPositiveButton(getString(R.string.ok)) { _,_ -> }
            .create()

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }

}
