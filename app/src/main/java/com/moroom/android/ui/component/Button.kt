package com.moroom.android.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moroom.android.ui.theme.Sky

@Composable
fun CompleteButton(
    buttonText: String,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = Sky),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(bottom = 8.dp)
    ) {
        Text(buttonText)
    }
}

@Preview(showBackground = true)
@Composable
fun CompleteButtonPreview() {
    CompleteButton(
        buttonText = "작성",
        enabled = true,
        onClick = { /* 클릭 동작 */ },
        modifier = Modifier
            .padding(16.dp)
    )
}