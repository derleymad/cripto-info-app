package com.github.derleymad.lizwallet.ui.wallets.bottomsheet
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.github.derleymad.lizwallet.R
import com.github.derleymad.lizwallet.databinding.FragmentBottomSheetBinding
import com.github.derleymad.lizwallet.databinding.FragmentWalletsBinding
import com.github.derleymad.lizwallet.ui.home.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class BottomSheetFragment : BottomSheetDialogFragment() {


    private var _binding: FragmentBottomSheetBinding? = null
    private val binding get() = _binding!!
    val homeViewModel: HomeViewModel by activityViewModels()

    interface BottomSheetListener {
        fun onXpubSaved(xpub: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val xpub = binding.editTextXpub.text.toString()
            saveXpubToSharedPreferences(xpub)
            val snack = Snackbar.make(requireActivity().findViewById(android.R.id.content),"Xpub importada com sucesso!",Snackbar.LENGTH_SHORT)
            snack.setBackgroundTint(resources.getColor(R.color.green))
            snack.setTextColor(resources.getColor(R.color.white))
            snack.show()
            homeViewModel.initBitoinKit()
            dismiss()
        }

    }
    private fun saveXpubToSharedPreferences(xpub: String) {
        val sharedPreferences =
            context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString("xpub", xpub)
        editor?.apply()
    }
}
