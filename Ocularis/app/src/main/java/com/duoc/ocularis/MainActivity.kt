package com.duoc.ocularis

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.FragmentActivity
import com.duoc.ocularis.navigation.AppNavigation
import com.duoc.ocularis.ui.theme.OcularisTheme
import com.duoc.ocularis.viewmodel.AppViewModel

class MainActivity : FragmentActivity() {
    private val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val highContrast by appViewModel.isHighContrast.collectAsState()
            val textScale by appViewModel.textScaleFactor.collectAsState()

            OcularisTheme(
                highContrast = highContrast,
                textScale = textScale
            ) {
                AppNavigation(vm = appViewModel)
            }
        }
    }
}
