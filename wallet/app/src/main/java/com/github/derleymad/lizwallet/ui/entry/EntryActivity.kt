package com.github.derleymad.lizwallet.ui.entry

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.github.derleymad.lizwallet.MainActivity
import com.github.derleymad.lizwallet.databinding.ActivityEntryBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.Executor


class EntryActivity : AppCompatActivity() {
    private val REQUEST_CONFIRM_DEVICE_CREDENTIAL = 123
    private lateinit var binding: ActivityEntryBinding

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView


    private lateinit var executors: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authenticateWithDeviceCredential()
        binding.cardView.setOnClickListener {
            authenticateWithDeviceCredential()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CONFIRM_DEVICE_CREDENTIAL) {
            if (resultCode == Activity.RESULT_OK) {
                val intent = Intent(this@EntryActivity,MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
            }
        }
    }

    private fun authenticateWithDeviceCredential() {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (keyguardManager.isDeviceSecure) {
            val intent = keyguardManager.createConfirmDeviceCredentialIntent(
                "AnyWallet",
                "Desbloqueie seu celular"
            )

            if (intent != null) {
                startActivityForResult(intent, REQUEST_CONFIRM_DEVICE_CREDENTIAL)
            } else {
                // O dispositivo não está seguro ou não há método de autenticação configurado
                // Trate conforme necessário
            }
        } else {
            // O dispositivo não está configurado com método de autenticação seguro
            // Trate conforme necessário
        }
    }


    private fun isDeviceSecure(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return keyguardManager.isDeviceSecure
    }


    fun createBiometricListener(){
        executors = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this,executors,object :
            BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)

            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

            }

        })
    }
    fun createPromptInfo(){
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autenticação por biometria")
            .setSubtitle("Use sua impressão digital para acessar o aplicativo")
            .setNegativeButtonText("Cancelar")
            .build()
    }

    fun checkBiometria(){

        val biometricManager = BiometricManager.from(this)
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)){
            BiometricManager.BIOMETRIC_SUCCESS -> {
                createBiometricListener()
                createPromptInfo()
                biometricPrompt.authenticate(promptInfo)
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {

            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {

            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {

            }
        }
    }
}