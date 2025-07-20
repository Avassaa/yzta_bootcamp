package com.yzta.bootcampgroup84.interfaces


sealed  class Screens(val screenName:String) {
    data object LoginScreen : Screens("LoginScreen")
    data object DashboardScreen : Screens("DashboardScreen")
    data object RegisterScreen : Screens("RegisterScreen")
    data object SettingsScreen : Screens("SettingsScreen")
    data object MenuScreen: Screens("MenuScreen")
}
