package com.moroom.android.presentation.write.presentation.view.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.moroom.android.presentation.write.data.model.CheckItem
import com.moroom.android.presentation.write.theme.Sky

@Composable
fun CheckItems(
    checkItems: List<CheckItem>,
    onCheckBoxClicked: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 10.dp)
        .border(width = 1.dp, color = Sky, shape = RoundedCornerShape(8.dp))
    ) {
        items(
            count = checkItems.size,
            key = { index: Int -> checkItems[index].listText }
        ) { index ->
            val item = checkItems[index]
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.listText,
                    modifier = Modifier.padding(start = 8.dp)
                )
                Checkbox(
                    colors = CheckboxDefaults.colors(checkedColor = Sky, uncheckedColor = Sky),
                    checked = item.isChecked,
                    onCheckedChange = { isChecked ->
                        onCheckBoxClicked(item.listText, isChecked)
                    }
                )
            }
        }
    }
}