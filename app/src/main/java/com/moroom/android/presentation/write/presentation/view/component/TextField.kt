package com.moroom.android.presentation.write.presentation.view.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.moroom.android.R
import com.moroom.android.presentation.write.theme.Sky

@Composable
fun AddressInput(
    address: String,
    onAddressChange: (String) -> Unit,
    modifier: Modifier
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = address,
        enabled = false,
        textStyle = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
        onValueChange = { onAddressChange(it) },
        shape = RoundedCornerShape(8.dp),
        placeholder = { Text(stringResource(R.string.please_enter_address)) },
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Sky,
            focusedBorderColor = Sky,
            disabledBorderColor = Sky
        )
    )
}

@Composable
fun OutLinedMultiLineTextField(
    value: String,
    placeholder: String,
    onValueChanged: (String) -> Unit,
    minLines: Int = 2
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChanged(it) },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Sky,
            focusedBorderColor = Sky,
            disabledBorderColor = Sky
        ),
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(placeholder) },
        minLines = minLines
    )
}
