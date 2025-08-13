package com.example.saferoute.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.saferoute.core.theme.DeepPurple
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

@Composable
fun EditProfileScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val uid = auth.currentUser?.uid

    var namaLengkap by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue(auth.currentUser?.email ?: "")) }
    var isLoading by remember { mutableStateOf(false) }

    // Ambil data user saat pertama kali
    LaunchedEffect(uid) {
        uid?.let {
            val snapshot = db.collection("users").document(it).get().await()
            namaLengkap = TextFieldValue(snapshot.getString("namaLengkap") ?: "")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = namaLengkap,
            onValueChange = { namaLengkap = it },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (uid != null) {
                    isLoading = true

                    // Update email dulu
                    auth.currentUser?.updateEmail(email.text)
                        ?.addOnSuccessListener {
                            // Jika sukses update email di FirebaseAuth, baru update Firestore
                            db.collection("users").document(uid)
                                .update(
                                    "namaLengkap", namaLengkap.text,
                                    "email", email.text
                                )
                                .addOnSuccessListener {
                                    Toast.makeText(context, "Profil diperbarui", Toast.LENGTH_SHORT).show()
                                    onBack()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context, "Gagal update profil di Firestore", Toast.LENGTH_SHORT).show()
                                }
                                .addOnCompleteListener { isLoading = false }
                        }
                        ?.addOnFailureListener {
                            Toast.makeText(context, "Gagal update email di FirebaseAuth", Toast.LENGTH_SHORT).show()
                            isLoading = false
                        }
                }
            }
            ,
            colors = ButtonDefaults.buttonColors(containerColor = DeepPurple),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan", color = androidx.compose.ui.graphics.Color.White)
        }

        if (isLoading) {
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

