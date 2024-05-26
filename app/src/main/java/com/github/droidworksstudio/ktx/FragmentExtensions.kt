package com.github.droidworksstudio.ktx

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.showKeyboard() {
    val view = view?.findFocus() ?: return
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    @Suppress("DEPRECATION")
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Fragment.hideKeyboard() {
    val view = view?.findFocus() ?: return
    val imm: InputMethodManager? =
        view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

fun Fragment.showLongToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.showShortToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.restartApp() {
    val packageManager = requireContext().packageManager
    val intent = packageManager.getLaunchIntentForPackage(requireContext().packageName)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    if (intent != null) {
        startActivity(intent)
    }
    requireActivity().finish()
}