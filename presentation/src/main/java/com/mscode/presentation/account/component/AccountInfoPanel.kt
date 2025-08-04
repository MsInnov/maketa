package com.mscode.presentation.account.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mscode.presentation.R
import com.mscode.presentation.home.screen.lightBackground
import com.mscode.presentation.theme.Primary
import kotlinx.coroutines.withContext

@Composable
fun AccountInfoPanel(
    onClose: () -> Unit,
    username: String,
    email: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackground),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.account_my_account),
                    style = typography.titleLarge,
                    color = Primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                InfoRow(label = stringResource(R.string.account_user_name), value = username)
                Spacer(modifier = Modifier.height(8.dp))
                InfoRow(label = stringResource(R.string.account_user_email), value = email)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(
                    onClick = onClose,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.filter_apply))
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = typography.labelMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = typography.bodyLarge,
            color = Color.Black
        )
    }
}
