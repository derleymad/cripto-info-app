package com.github.derleymad.lizwallet.utils.extentions

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.github.derleymad.lizwallet.R

private val navOptionsSlide = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    .setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_right)
    .build()

fun NavController.nanWalletToWalletDetails(destinationId: Int = 0) {
    this.navigate(R.id.action_walletsFragment_to_walletDetailsFragment, null, navOptionsSlide)
}


fun NavController.navPortifiloToAdd(destinationId: Int = 0) {
    this.navigate(R.id.action_mainFragment_to_adicionarPatrimonioFragment, null, navOptionsSlide)
}
fun NavController.navMainToWallet(destinationId: Int = 0) {
    this.navigate(R.id.action_mainFragment_to_walletsFragment, null, navOptionsSlide)
}
fun NavController.navAddPortifilioToConectWatchOnly(destinationId: Int = 0) {
    this.navigate(R.id.action_adicionarPatrimonioFragment_to_walletsFragment, null, navOptionsSlide)
}

fun NavController.navDetailsToAdress(destinationId: Int = 0) {
    this.navigate(R.id.action_walletDetailsFragment_to_addressFragment, null, navOptionsSlide)
}
fun NavController.navMainToCurrentFragment(destinationId: Int = 0){
    this.navigate(R.id.action_mainFragment_to_currencyFragment,null, navOptionsSlide)
}