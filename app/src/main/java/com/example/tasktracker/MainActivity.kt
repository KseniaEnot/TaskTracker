package com.example.tasktracker

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(
                content = {padding: PaddingValues ->
                    TextInput(modifier = Modifier
                                            .fillMaxSize()
                                            .padding(padding))
                }
            )
        }
    }

    @Composable
    fun TextInput(modifier: Modifier = Modifier) {
        var phoneNumber by remember { mutableStateOf("") }
        var isError by remember { mutableStateOf(false) }

        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            TextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Номер друга") },
                isError = isError,
                supportingText = {
                    if (isError) {
                        Text(
                            text = "Введите корректный номер телефона"
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(50.dp))

            Button(
                onClick = {
                    // явный Intent
                    if (isValidPhone(phoneNumber)) {
                        val intent = Intent(this@MainActivity, Activity2::class.java).apply{
                            //putExtra("EXTRA_KEY_PHON_NAMPER", text)
                            putExtras(
                                    Bundle().apply{
                                        putString("EXTRA_KEY_PHONE_NAMPER", phoneNumber)}
                            )
                        }
                        startActivity(intent)
                    }else{
                        isError = true
                    }
                }
            )
            {
                Text(text = "Открыть вторую Activity")
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (isValidPhone(phoneNumber)) {
                        val intent = Intent(Intent.ACTION_DIAL).apply {
                            data = "tel:$phoneNumber".toUri()
                        }
                        startActivity(intent)
                    }else{
                        isError = true
                    }
                }
            )
            {
                Text(text = "Позвонить другу")
            }
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (isValidPhone(phoneNumber)) {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT, phoneNumber)
                        }
                        startActivity(intent)
                    }else{
                        isError = true
                    }
                }
            )
            {
                Text(text = "Поделиться через…")
            }
        }
    }
    fun isValidPhone(phoneNumber: String): Boolean{
        val regex = Regex("^\\+?[0-9]{10,15}$")
        val cleaned = phoneNumber.replace(Regex("[\\s\\-()]"), "")
        return regex.matches(cleaned)
    }

    @Composable
    @Preview(showBackground = true, showSystemUi = true)
    private fun Prev(){
        TextInput(Modifier
            .fillMaxSize()
            .padding(PaddingValues()))
    }
}

