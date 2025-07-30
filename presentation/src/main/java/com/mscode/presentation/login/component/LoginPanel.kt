package com.mscode.presentation.login.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.mscode.presentation.home.screen.lightBackground
import com.mscode.presentation.login.model.UiEvent
import com.mscode.presentation.login.model.UiState
import com.mscode.presentation.login.model.UiState.Logged
import com.mscode.presentation.login.viewmodel.LoginViewModel

@Composable
fun LoginPanel(
    viewModel: LoginViewModel,
    onClose: () -> Unit,
    onGoToMenu: @Composable () -> Unit
) {
    val uiState = viewModel.uiState.collectAsState()
    if(uiState.value == Logged) {
        onGoToMenu()
        return
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackground),
        contentAlignment = Alignment.Center
    ) {
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.85f)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Connexion", style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(16.dp))
                var user by remember { mutableStateOf("johnd") }
                TextField(
                    value = user,
                    onValueChange = { user = it },
                    label = { Text("Nom d'utilisateur") },
                    singleLine = true,
                    isError = uiState.value == UiState.ErrorLogin || uiState.value == UiState.Error
                )

                Spacer(modifier = Modifier.height(12.dp))
                var pass by remember { mutableStateOf("m38rmF\$") }
                TextField(
                    value = pass,
                    onValueChange = { pass = it },
                    label = { Text("Mot de passe") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    isError = uiState.value == UiState.ErrorPass || uiState.value == UiState.Error
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { viewModel.onEvent(UiEvent.Login(user, pass)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Se connecter")
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(onClick = onClose) {
                    Text("Fermer")
                }
            }
        }
    }
}