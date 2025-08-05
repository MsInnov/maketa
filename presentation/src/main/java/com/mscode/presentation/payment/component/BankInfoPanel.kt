package com.mscode.presentation.payment.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.mscode.presentation.R
import com.mscode.presentation.payment.model.BankInfo
import com.mscode.presentation.payment.model.CardType
import com.mscode.presentation.payment.model.UiEvent
import com.mscode.presentation.payment.model.UiState
import com.mscode.presentation.payment.viewmodel.PaymentViewModel

@Composable
fun BankInfoPanel(
    viewModel: PaymentViewModel,
    onValidate: (BankInfo) -> Unit
) {
    val uiState = viewModel.uiState.collectAsState().value
    var cardType by remember { mutableStateOf(CardType.Visa) }
    var cardNumber by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

    // États d'erreur
    var cardNumberError by remember { mutableStateOf(false) }
    var expiryDateError by remember { mutableStateOf(false) }
    var cardHolderNameError by remember { mutableStateOf(false) }
    var cvvError by remember { mutableStateOf(false) }
    cardNumberError = uiState is UiState.BankInfoError && uiState.cardNumberError
    expiryDateError = uiState is UiState.BankInfoError && uiState.expiryDateError
    cardHolderNameError = uiState is UiState.BankInfoError && uiState.cardHolderNameError
    cvvError = uiState is UiState.BankInfoError && uiState.cvvError
    if(uiState is UiState.BankInfoVerified) {
        onValidate(
            BankInfo(
                cardType,
                cardNumber,
                expiryDate,
                cardHolderName,
                cvv
            )
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.payment_infos_banks),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CardType.values().forEach { type ->
                OutlinedButton(
                    onClick = { cardType = type },
                    border = BorderStroke(
                        2.dp,
                        if (cardType == type) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline
                    ),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (cardType == type)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else Color.Transparent
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(type.displayName)
                }
            }
        }

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it.take(16).filter { c -> c.isDigit() } },
            label = { Text(stringResource(R.string.payment_card_number)) },
            leadingIcon = { Icon(Icons.Default.CreditCard, null) },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            isError = cardNumberError,
            supportingText = {
                if (cardNumberError) Text(stringResource(R.string.payment_minimal_numbers_warning))
            },
            shape = RoundedCornerShape(12.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                value = expiryDate,
                onValueChange = { expiryDate = it.take(5) },
                label = { Text("MM/AA") },
                modifier = Modifier.weight(1f),
                isError = expiryDateError,
                supportingText = {
                    if (expiryDateError) Text(stringResource(R.string.payment_date_expired_warning))
                },
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = cvv,
                onValueChange = { cvv = it.take(3).filter { c -> c.isDigit() } },
                label = { Text("CVV") },
                modifier = Modifier.weight(1f),
                isError = cvvError,
                supportingText = {
                    if (cvvError) Text(stringResource(R.string.payment_cvv_not_valid_warning))
                },
                shape = RoundedCornerShape(12.dp)
            )
        }

        OutlinedTextField(
            value = cardHolderName,
            onValueChange = { cardHolderName = it },
            label = { Text(stringResource(R.string.payment_card_owner)) },
            modifier = Modifier.fillMaxWidth(),
            isError = cardHolderNameError,
            supportingText = {
                if (cardHolderNameError) Text(stringResource(R.string.payment_field_required))
            },
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.onEvent(
                    UiEvent.VerifyBankInfo(
                        cardNumber,
                        expiryDate,
                        cardHolderName,
                        cvv
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(R.string.payment_validate), fontWeight = FontWeight.Bold)
        }
    }
}