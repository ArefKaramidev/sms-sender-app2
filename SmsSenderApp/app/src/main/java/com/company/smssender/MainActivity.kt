package com.company.smssender

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.company.smssender.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Fixed number of digits required for the phone number (Iranian mobile format)
    private val REQUIRED_LENGTH = 11

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOk.setOnClickListener {
            handleOkClick()
        }
    }

    private fun handleOkClick() {
        val rawInput = binding.etPhone.text?.toString()?.trim() ?: ""

        // 1) Empty check
        if (rawInput.isEmpty()) {
            binding.tilPhone.error = getString(R.string.error_empty)
            return
        }

        // 2) Digits-only check
        if (!rawInput.all { it.isDigit() }) {
            binding.tilPhone.error = getString(R.string.error_digits_only)
            return
        }

        // 3) Exact length check (not less, not more than 11 digits)
        if (rawInput.length != REQUIRED_LENGTH) {
            binding.tilPhone.error = getString(R.string.error_length)
            return
        }

        // All good -> clear error and open SMS app with prefilled message
        binding.tilPhone.error = null
        openSmsAppWithMessage(rawInput)
    }

    private fun openSmsAppWithMessage(phoneNumber: String) {
        val message = getString(R.string.sms_body_template)

        val smsIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phoneNumber")
            putExtra("sms_body", message)
        }

        // Safety check: make sure there is an app that can handle this intent
        if (smsIntent.resolveActivity(packageManager) != null) {
            startActivity(smsIntent)
        }
    }
}
