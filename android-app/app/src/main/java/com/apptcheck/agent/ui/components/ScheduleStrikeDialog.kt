package com.apptcheck.agent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleStrikeDialog(
    onDismiss: () -> Unit,
    onConfirm: (LocalDateTime, ZoneId, String) -> Unit
) {
    var dateText by remember { mutableStateOf(LocalDate.now().toString()) }
    var timeText by remember { mutableStateOf(LocalTime.of(9, 0).format(DateTimeFormatter.ofPattern("HH:mm"))) }
    var selectedZone by remember { mutableStateOf(ZoneId.systemDefault()) }
    var mode by remember { mutableStateOf("alert") }
    
    val zones = listOf(
        ZoneId.of("America/Los_Angeles"),
        ZoneId.of("America/Denver"),
        ZoneId.of("America/Chicago"),
        ZoneId.of("America/New_York"),
        ZoneId.of("UTC")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Schedule Future Strike") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Target Drop Time", style = MaterialTheme.typography.labelMedium)
                
                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = timeText,
                    onValueChange = { timeText = it },
                    label = { Text("Time (HH:mm)") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Timezone", style = MaterialTheme.typography.labelMedium)
                Column {
                    zones.forEach { zone ->
                        Row(
                            modifier = Modifier.fillMaxWidth(), 
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            RadioButton(selected = selectedZone == zone, onClick = { selectedZone = zone })
                            Text(zone.id, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                Text("Mode", style = MaterialTheme.typography.labelMedium)
                Row {
                    FilterChip(
                        selected = mode == "alert", 
                        onClick = { mode = "alert" }, 
                        label = { Text("Alert Only") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = mode == "booking", 
                        onClick = { mode = "booking" }, 
                        label = { Text("Auto-Book") }
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = { 
                try {
                    val ldt = LocalDateTime.of(LocalDate.parse(dateText), LocalTime.parse(timeText))
                    onConfirm(ldt, selectedZone, mode)
                } catch (e: Exception) {
                    // In real app, show error toast
                }
            }) { Text("Confirm Schedule") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
