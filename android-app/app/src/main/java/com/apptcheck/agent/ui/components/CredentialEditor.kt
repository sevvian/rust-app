package com.apptcheck.agent.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import java.util.UUID

// Data class matching the Rust/Go Credential structure
data class CredentialUiModel(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val username: String = "",
    val password: String = "",
    val email: String = "",
    val site: String = "spl"
)

/**
 * CredentialEditor provides a full UI for managing library credentials.
 */
@Composable
fun CredentialEditor(
    credentials: Map<String, CredentialUiModel>,
    onSave: (CredentialUiModel) -> Unit,
    onDelete: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingCredential by remember { mutableStateOf<CredentialUiModel?>(null) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Library Credentials", style = MaterialTheme.typography.titleLarge)
            IconButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Credential")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (credentials.isEmpty()) {
            Text("No credentials saved.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
        } else {
            LazyColumn(modifier = Modifier.heightIn(max = 400.dp)) {
                items(credentials.values.toList()) { cred ->
                    CredentialItem(
                        credential = cred,
                        onEdit = { editingCredential = it },
                        onDelete = { onDelete(it.id) }
                    )
                }
            }
        }
    }

    // Dialog for Adding/Editing
    if (showAddDialog || editingCredential != null) {
        val initial = editingCredential ?: CredentialUiModel()
        CredentialDialog(
            initialCredential = initial,
            onDismiss = {
                showAddDialog = false
                editingCredential = null
            },
            onConfirm = {
                onSave(it)
                showAddDialog = false
                editingCredential = null
            }
        )
    }
}

@Composable
fun CredentialItem(
    credential: CredentialUiModel,
    onEdit: (CredentialUiModel) -> Unit,
    onDelete: (CredentialUiModel) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(credential.name, style = MaterialTheme.typography.titleMedium)
                Text("Site: ${credential.site.uppercase()} | Card: ${credential.username}", 
                     style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { onEdit(credential) }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { onDelete(credential) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun CredentialDialog(
    initialCredential: CredentialUiModel,
    onDismiss: () -> Unit,
    onConfirm: (CredentialUiModel) -> Unit
) {
    var name by remember { mutableStateOf(initialCredential.name) }
    var user by remember { mutableStateOf(initialCredential.username) }
    var pass by remember { mutableStateOf(initialCredential.password) }
    var email by remember { mutableStateOf(initialCredential.email) }
    var site by remember { mutableStateOf(initialCredential.site) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialCredential.name.isEmpty()) "Add Credential" else "Edit Credential") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Friendly Name (e.g. My Card)") })
                OutlinedTextField(value = user, onValueChange = { user = it }, label = { Text("Library Card #") })
                OutlinedTextField(
                    value = pass, 
                    onValueChange = { pass = it }, 
                    label = { Text("PIN") },
                    visualTransformation = PasswordVisualTransformation()
                )
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email (Required for SPL)") })
                
                Text("Library System", style = MaterialTheme.typography.labelMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = site == "spl", onClick = { site = "spl" })
                    Text("SPL")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = site == "kcls", onClick = { site = "kcls" })
                    Text("KCLS")
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(initialCredential.copy(
                    name = name,
                    username = user,
                    password = pass,
                    email = email,
                    site = site
                ))
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
