
package com.example.flagquiz.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flagquiz.R
import com.example.flagquiz.ui.theme.FlagQuizTheme

class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlagQuizTheme {
                Scaffold { innerPadding ->
                    WelcomeBody(
                        innerPadding = innerPadding,
                        onLoginClick = {
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        },
                        onSignupClick = {
                            startActivity(Intent(this, SignupActivity::class.java))
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeBody(
    innerPadding: PaddingValues,
    onLoginClick: () -> Unit,
    onSignupClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFCC80)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.flags),
            contentDescription = "Flags Image",
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.48f)
                .padding(bottom = 6.dp)
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Welcome to the Flag Quiz!",
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "(Dive into the fascinating world of flags!)",
                fontFamily = FontFamily.Cursive,
                fontSize = 23.sp,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF97B57), contentColor = Color.White)
                ) {
                    Text("Log In", fontSize = 16.sp)
                }
                Button(
                    onClick = onSignupClick,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                ) {
                    Text("Sign Up", fontSize = 16.sp)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewWelcomeScreen() {
    FlagQuizTheme {
        Scaffold { innerPadding ->
            WelcomeBody(innerPadding = innerPadding, onLoginClick = {}, onSignupClick = {})
        }
    }
}
