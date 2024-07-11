package com.moroom.android.ui.write.presentation.view.screen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.moroom.android.R
import com.moroom.android.ui.nav.MainActivity
import com.moroom.android.ui.search.SearchActivity
import com.moroom.android.ui.write.data.model.CheckItem
import com.moroom.android.ui.write.domain.model.WrittenReview
import com.moroom.android.ui.write.presentation.controller.WriteViewModel
import com.moroom.android.ui.write.presentation.view.component.AddressInput
import com.moroom.android.ui.write.presentation.view.component.CheckItems
import com.moroom.android.ui.write.presentation.view.component.CompleteButton
import com.moroom.android.ui.write.presentation.view.component.DropdownMenu
import com.moroom.android.ui.write.presentation.view.component.OutLinedMultiLineTextField
import com.moroom.android.ui.write.theme.Sky


@Composable
fun WriteScreen(viewModel: WriteViewModel = viewModel()) {
    val context = LocalContext.current
    val writeCompleteState by viewModel.writeCompleteState.collectAsState()

    val floorOptions = stringArrayResource(id = R.array.floor_array)
    val yearOptions = stringArrayResource(id = R.array.year_array)
    val rentTypeOptions = stringArrayResource(id = R.array.rent_type_array)

    var address by remember { mutableStateOf("") }
    var floor by remember { mutableStateOf(floorOptions[0]) }
    var year by remember { mutableStateOf(yearOptions[0]) }
    var rentType by remember { mutableStateOf(rentTypeOptions[0]) }
    val checkItems by viewModel.checkItems.collectAsState()
    var pros by remember { mutableStateOf("") }
    var cons by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        viewModel.fetchCheckItems()
    }

    LaunchedEffect(writeCompleteState) {
        handleWriteCompleteState(writeCompleteState, context)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AddressSection(
            address = address,
            onAddressChange = { address = it }
        )
        PropertyDetailsSection(
            onFloorChange = { floor = it },
            onYearChange = { year = it },
            onRentTypeChange = { rentType = it }
        )
        CheckItemsSection(
            checkItems,
            viewModel,
            modifier = Modifier.weight(1f)
        )
        ProsConsSection(
            pros = pros,
            cons = cons,
            onProsChange = { pros = it },
            onConsChange = { cons = it }
        )
        CompleteButtonSection(
            viewModel = viewModel,
            address = address,
            floor = floor,
            year = year,
            rentType = rentType,
            pros = pros,
            cons = cons,
            checkItems = checkItems,
            Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun AddressSection(
    address: String,
    onAddressChange: (String) -> Unit
) {
    val context = LocalContext.current

    val getSearchResult = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        handleAddressSearchResult(result, onAddressChange)
    }

    Text(
        text = stringResource(R.string.enter_address_1),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 3.dp)
    )

    AddressInput(
        address = address,
        onAddressChange = onAddressChange,
        modifier = Modifier.clickable {
            val intent = Intent(context.applicationContext, SearchActivity::class.java)
            getSearchResult.launch(intent)
        }
    )
}

@Composable
fun PropertyDetailsSection(
    onFloorChange: (String) -> Unit,
    onYearChange: (String) -> Unit,
    onRentTypeChange: (String) -> Unit
) {
    val floorOptions = stringArrayResource(id = R.array.floor_array)
    val yearOptions = stringArrayResource(id = R.array.year_array)
    val rentTypeOptions = stringArrayResource(id = R.array.rent_type_array)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Sky, shape = RoundedCornerShape(8.dp))
    ) {
        DropdownMenu(
            modifier = Modifier.weight(1f),
            options = floorOptions,
            onOptionSelected = onFloorChange
        )
        DropdownMenu(
            modifier = Modifier.weight(1f),
            options = yearOptions,
            onOptionSelected = onYearChange
        )
        Text(
            text = "년도까지 거주",
            fontSize = 10.sp,
            modifier = Modifier
                .padding(top = 22.dp)
                .wrapContentWidth()
        )
        DropdownMenu(
            modifier = Modifier.weight(1f),
            options = rentTypeOptions,
            onOptionSelected = onRentTypeChange
        )
    }
}

@Composable
fun CheckItemsSection(
    checkItems: List<CheckItem>,
    viewModel: WriteViewModel,
    modifier: Modifier
) {
    CheckItems(
        checkItems = checkItems,
        modifier = modifier,
        onCheckBoxClicked = { text: String, isChecked: Boolean ->
            viewModel.onCheckBoxClicked(text, isChecked)
        }
    )
}

@Composable
fun ProsConsSection(
    pros: String,
    cons: String,
    onProsChange: (String) -> Unit,
    onConsChange: (String) -> Unit
) {
    Text(
        text = stringResource(R.string.goodThing),
        fontWeight = FontWeight.Bold
    )
    OutLinedMultiLineTextField(
        value = pros,
        placeholder = stringResource(id = R.string.goodThing),
        onValueChanged = onProsChange
    )

    Text(
        text = stringResource(R.string.badThing),
        fontWeight = FontWeight.Bold,
    )
    OutLinedMultiLineTextField(
        value = cons,
        placeholder = stringResource(R.string.say_badThing),
        onValueChanged = onConsChange
    )
}

@Composable
fun CompleteButtonSection(
    viewModel: WriteViewModel,
    address: String,
    floor: String,
    year: String,
    rentType: String,
    pros: String,
    cons: String,
    checkItems: List<CheckItem>,
    modifier: Modifier
) {
    val isButtonEnabled = remember(address, pros, cons) {
        address.isNotEmpty() && pros.isNotEmpty() && cons.isNotEmpty()
    }

    CompleteButton(
        buttonText = "작성 완료",
        enabled = isButtonEnabled,
        modifier = modifier,
        onClick = {
            viewModel.onWriteComplete(
                WrittenReview(
                    address = address,
                    year = year,
                    floor = floor,
                    rentType = rentType,
                    pros = pros,
                    cons = cons,
                    checkItems = checkItems
                )
            )
        }
    )
}

fun handleWriteCompleteState(writeCompleteState: Result<Unit>?, context: Context) {
    writeCompleteState?.let { result ->
        if (result.isSuccess) {
            Toast.makeText(context, R.string.completed, Toast.LENGTH_LONG).show()
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "실패", Toast.LENGTH_LONG).show()
        }
    }
}

fun handleAddressSearchResult(result: ActivityResult, onAddressUpdate: (String) -> Unit) {
    if (result.resultCode == Activity.RESULT_OK) {
        result.data?.getStringExtra("data")?.let { address ->
            onAddressUpdate(address)
        }
    }
}