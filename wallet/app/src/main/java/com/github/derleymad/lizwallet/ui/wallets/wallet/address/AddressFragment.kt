package com.github.derleymad.lizwallet.ui.wallets.wallet.address

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.databinding.FragmentAddressBinding
import com.github.derleymad.lizwallet.databinding.FragmentWalletDetailsBinding
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder

class AddressFragment : Fragment() {
    private var _binding: FragmentAddressBinding? = null
    private val binding get() = _binding!!



    val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddressBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    private fun generateQRCode(text: String) {
        val barcodeEncoder = BarcodeEncoder()
        try {
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(text, BarcodeFormat.QR_CODE, 400, 400)
            binding.qrCodeImageView.setImageBitmap(bitmap)
            binding.qrCodeImageView.visibility = View.VISIBLE
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }

    private fun copyTextToClipboard(text: String) {
        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("address", text)
        clipboard.setPrimaryClip(clip)
        Snackbar.make(binding.root,"Texto copiado",
            Snackbar.LENGTH_SHORT).show()
    }
    private fun shareText(text: String) {
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
            type = "text/plain"
        }
       startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }


        homeViewModel.newReceiveAddress.observe(viewLifecycleOwner){value->
            if(value!=null){
                generateQRCode(value)
                binding.address.text = value
                binding.address.setOnClickListener{
                    copyTextToClipboard(value)
                }
                binding.shareButton.setOnClickListener {
                    try{
                        shareText(value)
                    }catch (e: Exception){
                        Log.i("share info",e.toString())
                    }
                }
            }
        }

    }
}