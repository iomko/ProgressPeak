import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maxkeppeker.sheets.core.icons.LibIcons
import com.maxkeppeker.sheets.core.models.base.UseCaseState
import com.maxkeppeker.sheets.core.models.base.rememberUseCaseState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarStyle
import com.maxkeppeler.sheets.emoji.EmojiDialog
import com.maxkeppeler.sheets.emoji.models.EmojiConfig
import com.maxkeppeler.sheets.emoji.models.EmojiSelection
import com.practice.progress_peak.screens.HabitConfiguration.HabitConfigurationEvent
import com.practice.progress_peak.screens.HabitConfiguration.HabitConfigurationViewModel
import com.practice.progress_peak.screens.MainHabitList.HabitType
import com.practice.progress_peak.utils.UiEvent



@Composable
fun GoalAndUnitTypeRow(
    goal: Int,
    onGoalChange: (Int) -> Unit,
    unitType: String,
    onUnitButtonClick: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextWithContent(text = "Goal and Unit type:") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = goal.toString(),
                onValueChange = { newText ->
                    val newGoal = newText.takeWhile { it.isDigit() }.toIntOrNull() ?: 0
                    onGoalChange(newGoal)
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .weight(1.5f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true
            )

            OutlinedButton(
                onClick = { onUnitButtonClick() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(text = unitType)
            }
        }
    }
}


@Composable
fun IconRow(icon: String, onIconButtonClick: () -> Unit) {
    TextWithContent(text = "Icon:") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            OutlinedButton(
                onClick = { onIconButtonClick() },
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = icon)
            }
        }
    }
}

@Composable
fun DateRow(
    nameText: String,
    buttonText: String,
    onButtonClick: () -> Unit,
) {
    TextWithContent(text = nameText) {
        OutlinedButton(
            onClick = onButtonClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            border = BorderStroke(2.dp, Color(255, 165, 0)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = buttonText)
        }
    }
}

@Composable
fun HabitTypeRow(
    habitType: Boolean,
    onHabitTypeSelected: (HabitType) -> Unit
) {
    val selectedColor = Color(255, 165, 0)
    val unselectedColor = Color.Transparent

    TextWithContent(text = "Habit type:") {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .clickable { onHabitTypeSelected(HabitType.Good) }
                    .background(
                        color = if (habitType == HabitType.Good.value) selectedColor else unselectedColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Good habit",
                    color = if (habitType == HabitType.Good.value) Color.White else Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .clickable { onHabitTypeSelected(HabitType.Bad) }
                    .background(
                        color = if (habitType == HabitType.Bad.value) selectedColor else unselectedColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Bad habit",
                    color = if (habitType == HabitType.Bad.value) Color.White else Color.Black
                )
            }
        }
    }
}

@Composable
fun NameRow(name: String, onNameChange: (String) -> Unit) {
    TextWithContent(text = "Name:") {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            placeholder = { Text(text = "Enter name") },
            singleLine = true
        )
    }
}

@Composable
fun TextWithContent(
    text: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Text(text = text)
        }
        content()
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitConfigurationScreen(
    popBack: () -> Unit,
    viewModel: HabitConfigurationViewModel = hiltViewModel()
) {

    var temporarySelectedStartDate = viewModel.selectedStartDate

    val dismissAndCloseStartDateSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ExpandStartDate(false))
    }

    val changeStartDateSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ChangeStartDate(temporarySelectedStartDate))
    }

    var temporarySelectedEndDate = viewModel.selectedStartDate

    val dismissAndCloseEndDateSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ExpandEndDate(false))
    }

    val changeEndDateSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ChangeEndDate(temporarySelectedEndDate))
    }

    var temporarySelectedIcon = viewModel.selectedIcon

    val dismissAndCloseIconSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ExpandIcon(false))
    }

    val changeIconSelection: UseCaseState.() -> Unit = {
        viewModel.onEvent(HabitConfigurationEvent.ChangeIcon(temporarySelectedIcon))
    }


    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect{ event ->
            when(event){
                is UiEvent.PopBack -> {
                    popBack()
                }
                else -> Unit
            }
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(255, 165, 0))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Habit Configuration",
                    color = Color.White,
                    fontSize = 20.sp,
                )
            }
        },
        bottomBar = {
            Column(){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { (viewModel::onEvent)(HabitConfigurationEvent.AddHabit) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(text = "Apply", color = Color.White)
                    }
                    Button(
                        onClick = { (viewModel::onEvent)(HabitConfigurationEvent.CancelHabit) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text(text = "Cancel", color = Color.White)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(255, 165, 0)),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Add your icon buttons here
                    IconButton(onClick = { /* Handle click */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Icon 1", tint = Color.White)
                    }
                    IconButton(onClick = { /* Handle click */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Icon 2", tint = Color.White)
                    }
                    IconButton(onClick = { /* Handle click */ }) {
                        Icon(Icons.Default.Build, contentDescription = "Icon 3", tint = Color.White)
                    }
                    IconButton(onClick = { /* Handle click */ }) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "Icon 4", tint = Color.White)
                    }
                }
            }

        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            NameRow(viewModel.name) { newName ->
                viewModel.onEvent(HabitConfigurationEvent.ChangeName(newName))
            }

            IconRow(
                icon = viewModel.selectedIcon,
                onIconButtonClick = { (viewModel::onEvent)(HabitConfigurationEvent.ExpandIcon(true)) }
            )

            if(viewModel.expandedIcon){
                EmojiDialog(
                    state = rememberUseCaseState(visible = true, onCloseRequest = {
                        (viewModel::onEvent)(HabitConfigurationEvent.ExpandIcon(false))
                    },
                        onDismissRequest = { (viewModel::onEvent)(HabitConfigurationEvent.ExpandIcon(false)) },
                        onFinishedRequest = { (viewModel::onEvent)(HabitConfigurationEvent.ChangeIcon(temporarySelectedIcon)) }),
                    selection = EmojiSelection.Unicode(
                        onPositiveClick = { emojiUnicode ->
                            temporarySelectedIcon = emojiUnicode
                        }
                    ),
                    config = EmojiConfig(icons = LibIcons.TwoTone)
                )
            }


            HabitTypeRow(viewModel.habitType) { habitType ->
                (viewModel::onEvent)(HabitConfigurationEvent.ChangeHabitType(habitType))
            }

            DateRow(
                buttonText = viewModel.selectedStartDate.toString(),
                onButtonClick = {
                    viewModel.onEvent(HabitConfigurationEvent.ExpandStartDate(true))
                },
                nameText = "Start date"
            )

            DateRow(
                buttonText = viewModel.selectedEndDate.toString(),
                onButtonClick = {
                    viewModel.onEvent(HabitConfigurationEvent.ExpandEndDate(true))
                },
                nameText = "End date"
            )

            if(viewModel.expandEndDate){
                CalendarDialog(
                    state = rememberUseCaseState(visible = true, true,
                        onCloseRequest = dismissAndCloseEndDateSelection,
                        onDismissRequest = dismissAndCloseEndDateSelection,
                        onFinishedRequest = changeEndDateSelection
                    ),
                    config = CalendarConfig(
                        yearSelection = true,
                        style = CalendarStyle.WEEK,
                    ),
                    selection = CalendarSelection.Date(
                        selectedDate = viewModel.selectedEndDate
                    ) { newDate ->
                        temporarySelectedEndDate = newDate
                    },
                )
            }

            if(viewModel.expandStartDate){

                CalendarDialog(
                    state = rememberUseCaseState(visible = true, true,
                        onCloseRequest = dismissAndCloseStartDateSelection,
                        onDismissRequest = dismissAndCloseStartDateSelection,
                        onFinishedRequest = changeStartDateSelection
                    ),
                    config = CalendarConfig(
                        yearSelection = true,
                        style = CalendarStyle.WEEK,
                    ),
                    selection = CalendarSelection.Date(
                        selectedDate = viewModel.selectedStartDate
                    ) { newDate ->
                        temporarySelectedStartDate = newDate
                    },
                )

            }

            GoalAndUnitTypeRow(
                goal = viewModel.selectedGoalAmount,
                onGoalChange = { goalAmount -> (viewModel::onEvent)(HabitConfigurationEvent.ChangeGoalAmount(goalAmount)) },
                unitType = viewModel.selectedUnitType,
                onUnitButtonClick = { (viewModel::onEvent)(HabitConfigurationEvent.ExpandUnitType(true)) }
            )


        }
    }
}



