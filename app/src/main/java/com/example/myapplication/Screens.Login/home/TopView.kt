package com.example.myapplication.Screens.Login.home

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.R
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.SharedPreference
import com.example.myapplication.singleton.userDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun TopView(drawerState: DrawerState){
    val activity= LocalContext.current as Activity
    Row (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxHeight().fillMaxWidth().padding(end = 20.dp, start = 5.dp)

    ){
       Icon(painter = painterResource(R.drawable.exit_left_svgrepo_com), contentDescription = "exit",
           tint = Color.Red,
           modifier = Modifier.height(30.dp).width(40.dp).clip(RoundedCornerShape(10.dp))
               .clickable(onClick = {
               activity.finish()
           })
           )
        Text(
            text = "Krishisakha",
            fontSize = 16.sp
        )

    }
}


@Composable
fun CustomDrawerContent(onMenuItemClicked: (String) -> Unit, onLogOut: () -> Unit) {
    val edit = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(0.8f)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFE8F5E9)) // Light green background
    ) {
        // Profile Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color(0xFF81C784)) // Light Green background
                .padding(10.dp)
        ) {
            Text(
                text = "Profile",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Icon(
                painter = painterResource(R.drawable.edit_3_svgrepo_com),
                contentDescription = null,
                modifier = Modifier
                    .height(20.dp)
                    .clickable {
                        edit.value = !edit.value
                    },
                tint = Color.White
            )
        }

        // Profile Content
        Column(Modifier.padding(start = 10.dp)) {
            ProfileNormal(edit)
        }

        // Logout and Change Password buttons
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    SharedPreference.sharedPreferences.edit().clear().apply()
                    onLogOut()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)) // Green color for logout button
            ) {
                Text("Logout", color = Color.White)
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = {
                    GlobalStates.globalStates.navController?.navigate("reset")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)) // Green color for password change button
            ) {
                Text("Change P/W", color = Color.White)
            }
        }
    }
}

@Composable
fun ProfileNormal(edit: MutableState<Boolean>) {
    val scope = rememberCoroutineScope()
    val name = remember { mutableStateOf(userDetail.principal?.name ?: "") }
    val username = remember { mutableStateOf(userDetail.principal?.username ?: "") }
    val email = remember { mutableStateOf(userDetail.principal?.email ?: "") }
    val phone = remember { mutableStateOf(userDetail.principal?.phone ?: "") }
    val pin = remember { mutableStateOf(userDetail.principal?.pin ?: "") }

    Spacer(Modifier.height(10.dp))

    // Profile fields
    Text("Name", fontSize = 18.sp, color = Color(0xFF388E3C)) // Green header
    Spacer(Modifier.fillMaxWidth(.7f).height(1.dp).border(color = Color(0xFF388E3C), width = 1.dp)) // Green border
    Row(
        modifier = Modifier.height(70.dp).padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GetInputField(name, edit)
    }

    Spacer(Modifier.height(10.dp))

    Text("Username", fontSize = 18.sp, color = Color(0xFF388E3C)) // Green header
    Spacer(Modifier.fillMaxWidth(.7f).height(1.dp).border(color = Color(0xFF388E3C), width = 1.dp)) // Green border
    Row(
        modifier = Modifier.height(70.dp).padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GetInputField(username, edit)
    }

    Text("E-mail", fontSize = 18.sp, color = Color(0xFF388E3C)) // Green header
    Spacer(Modifier.fillMaxWidth(.7f).height(1.dp).border(color = Color(0xFF388E3C), width = 1.dp)) // Green border
    Row(
        modifier = Modifier.height(70.dp).padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GetInputField(email, edit)
    }

    Text("Phone", fontSize = 18.sp, color = Color(0xFF388E3C)) // Green header
    Spacer(Modifier.fillMaxWidth(.7f).height(1.dp).border(color = Color(0xFF388E3C), width = 1.dp)) // Green border
    Row(
        modifier = Modifier.height(70.dp).padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GetInputField(phone, edit)
    }

    Text("PIN code", fontSize = 18.sp, color = Color(0xFF388E3C)) // Green header
    Spacer(Modifier.fillMaxWidth(.7f).height(1.dp).border(color = Color(0xFF388E3C), width = 1.dp)) // Green border
    Row(
        modifier = Modifier.height(70.dp).padding(start = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        GetInputField(pin, edit)
    }

    Spacer(Modifier.height(20.dp))

    if (edit.value) {
        Box(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Button(
                onClick = {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            DataBaseObject.INSTANCE?.userDao()?.updateUser(
                                name.value,
                                phone.value,
                                email.value,
                                pin.value,
                                userDetail.username
                            )
                        }
                        userDetail.principal = withContext(Dispatchers.IO) {
                            DataBaseObject.INSTANCE?.userDao()?.getPrincipal(userDetail.username)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C)) // Green button
            ) {
                Text("Save", color = Color.White)
            }
        }
    }
}

@Composable
fun GetInputField(msg: MutableState<String>, edit: MutableState<Boolean>) {
    TextField(
        value = msg.value,
        enabled = edit.value,
        singleLine = true,
        onValueChange = { msg.value = it },
        modifier = Modifier
            .fillMaxWidth(.8f)
            .height(55.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color(0x85BDBDBD),
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFF388E3C), // Green cursor
            disabledContainerColor = Color(0x2F656464)
        ),
        placeholder = { Text("Type here...") },
        textStyle = LocalTextStyle.current.copy(fontSize = 17.sp)
    )
}



