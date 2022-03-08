package com.example.ingredient.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.example.ingredient.R

class PurchaseConfirmationDialogFragment : androidx.fragment.app.DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.order_confirmation))
            .setPositiveButton(getString(R.string.ok)) { _,_ -> }
            .create()

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }

}
