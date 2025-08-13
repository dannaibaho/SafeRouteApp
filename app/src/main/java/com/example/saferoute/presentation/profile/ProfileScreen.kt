package com.example.saferoute.presentation.profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.saferoute.core.theme.DeepPurple
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

@Composable
fun ProfileScreen(
    onEditProfile: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()

    val currentUser = auth.currentUser
    var namaLengkap by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(currentUser?.email ?: "") }
    var fotoUrl by remember { mutableStateOf<String?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    // Ambil data user dari Firestore
    LaunchedEffect(currentUser?.uid) {
        currentUser?.uid?.let { uid ->
            val snapshot = db.collection("users").document(uid).get().await()
            namaLengkap = snapshot.getString("namaLengkap") ?: ""
            fotoUrl = snapshot.getString("fotoUrl")
        }
    }

    // Launcher untuk memilih gambar
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            uploadProfilePicture(
                uri = it,
                uid = currentUser?.uid ?: "",
                storage = storage,
                db = db,
                onSuccess = { url ->
                    fotoUrl = url
                    Toast.makeText(context, "Foto profil diperbarui", Toast.LENGTH_SHORT).show()
                },
                onError = { e ->
                    Toast.makeText(context, "Gagal upload foto: $e", Toast.LENGTH_SHORT).show()
                },
                onLoading = { loading ->
                    isUploading = loading
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Foto profil
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable { imagePickerLauncher.launch("image/*") },
            contentAlignment = Alignment.Center
        ) {
            if (fotoUrl != null) {
                Image(
                    painter = rememberAsyncImagePainter(fotoUrl),
                    contentDescription = "Foto Profil",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text("Pilih Foto", color = Color.White)
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = namaLengkap,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = email,
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(Modifier.height(24.dp))

        if (isUploading) {
            CircularProgressIndicator(color = DeepPurple)
            Spacer(Modifier.height(24.dp))
        }

        // Tombol Edit Profil
        OutlinedButton(
            onClick = onEditProfile,
            colors = ButtonDefaults.buttonColors(containerColor = DeepPurple),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Edit Profil", color = Color.White)
        }

        Spacer(Modifier.height(12.dp))

        // Tombol Ganti Password
        OutlinedButton(
            onClick = onChangePassword,
            colors = ButtonDefaults.buttonColors(containerColor = DeepPurple),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ganti Password", color = Color.White)
        }

        Spacer(Modifier.height(12.dp))

        // Tombol Logout
        OutlinedButton(
            onClick = {
                auth.signOut()
                onLogout()
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout", color = Color.White)
        }


    }
}

private fun uploadProfilePicture(
    uri: Uri,
    uid: String,
    storage: FirebaseStorage,
    db: FirebaseFirestore,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit,
    onLoading: (Boolean) -> Unit
) {
    onLoading(true)
    val ref = storage.reference.child("profile_photos/$uid.jpg")
    ref.putFile(uri)
        .addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener { url ->
                db.collection("users").document(uid)
                    .update("fotoUrl", url.toString())
                    .addOnSuccessListener {
                        onSuccess(url.toString())
                        onLoading(false)
                    }
                    .addOnFailureListener { e ->
                        onError(e.message ?: "Error update Firestore")
                        onLoading(false)
                    }
            }
        }
        .addOnFailureListener { e ->
            onError(e.message ?: "Error upload Storage")
            onLoading(false)
        }
}
