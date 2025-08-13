package com.example.saferoute.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.saferoute.core.theme.DeepPurple
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ChangePasswordScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("Password Baru") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Konfirmasi Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (newPassword != confirmPassword) {
                    Toast.makeText(context, "Password tidak sama", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (newPassword.length < 6) {
                    Toast.makeText(context, "Password minimal 6 karakter", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                isLoading = true
                auth.currentUser?.updatePassword(newPassword)
                    ?.addOnSuccessListener {
                        Toast.makeText(context, "Password diperbarui", Toast.LENGTH_SHORT).show()
                        onBack()
                    }
                    ?.addOnFailureListener {
                        Toast.makeText(context, "Gagal update password", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnCompleteListener { isLoading = false }
            },
            colors = ButtonDefaults.buttonColors(containerColor = DeepPurple),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Password", color = androidx.compose.ui.graphics.Color.White)
        }

        if (isLoading) {
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}
