package at.htlleonding.taskosaurus.view.components.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import at.htlleonding.taskosaurus.R

@Composable
fun CreateGroupDialog(
    groupName: String, onGroupNameChange: (String) -> Unit,
    isSubmitting: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.dialog_create_group_title)) },
        text = {
            Column {
                Text(stringResource(R.string.dialog_create_group_desc), Modifier.padding(bottom = 16.dp))
                OutlinedTextField(
                    value = groupName,
                    onValueChange = onGroupNameChange,
                    label = { Text(stringResource(R.string.label_group_name)) },
                    singleLine = true,
                    enabled = !isSubmitting
                )
            }
        },
        confirmButton = {
            Button(onClick = onConfirm, enabled = groupName.isNotBlank() && !isSubmitting) {
                if (isSubmitting) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                else Text(stringResource(R.string.btn_create))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isSubmitting) {
                Text(stringResource(R.string.btn_cancel))
            }
        }
    )
}