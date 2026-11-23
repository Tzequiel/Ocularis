package com.duoc.ocularis.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.duoc.ocularis.ui.screens.LoginScreen
import com.duoc.ocularis.ui.theme.OcularisTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysHeaderAndInputs() {
        composeTestRule.setContent {
            OcularisTheme {
                LoginScreen()
            }
        }

        composeTestRule.onNodeWithText("Bienvenido a Ocularis").assertIsDisplayed()
        composeTestRule.onNodeWithText("Iniciar Sesión").assertIsDisplayed()
    }

    @Test
    fun loginScreen_allowsRoleSelection() {
        composeTestRule.setContent {
            OcularisTheme {
                LoginScreen()
            }
        }

        composeTestRule.onNodeWithText("Especialista").assertIsDisplayed()
        composeTestRule.onNodeWithText("Administrador").assertIsDisplayed()
        composeTestRule.onNodeWithText("Paciente").assertIsDisplayed()
    }
}
