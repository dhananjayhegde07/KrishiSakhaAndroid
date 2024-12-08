package com.example.myapplication.Screens.Login.home

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.myapplication.DTO.DetectReq
import com.example.myapplication.DTO.DetectRes
import com.example.myapplication.DataBase.DataBaseObject
import com.example.myapplication.R
import com.example.myapplication.retrofit.Retrofit
import com.example.myapplication.singleton.GlobalStates
import com.example.myapplication.singleton.userDetail
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(){
    val camPermission = rememberPermissionState(Manifest.permission.CAMERA)
    val sheetState= rememberModalBottomSheetState()
    val scope= rememberCoroutineScope()
    LaunchedEffect(Unit) {
        val x = withContext(Dispatchers.IO){
            DataBaseObject.INSTANCE?.diseaseDao()?.get(userDetail.username)
        }
        Log.d("TAG", "CameraScreen: ${x.toString()}")
    }
    when(camPermission.status) {
        PermissionStatus.Granted -> {
            CameraView()
        }

        is PermissionStatus.Denied -> {
            Column {
                Button(
                    onClick = {
                        camPermission.launchPermissionRequest()
                    }
                ) {
                    Text("grant permission")
                }
            }
        }
    }

}

@Composable
fun CameraView(){
    val localcontext= LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController= remember { LifecycleCameraController(localcontext) }
    val triggerReq=remember { mutableStateOf(false) }
    val outputFile = File(localcontext.cacheDir, "captured_image.jpg")
    val types= remember { mutableStateOf(arrayOf<String>()) }
    val selectedType=remember { mutableStateOf<String?>(null) }
    val isEnabled=remember { mutableStateOf(true) }
    val showResults=remember { mutableStateOf(false) }
    val result=remember { mutableStateOf<DetectRes?>(null) }

    LaunchedEffect(triggerReq.value) {
        Log.d("TAG", "CameraView: ${outputFile.length()}")
        if (triggerReq.value){
            GlobalStates.globalStates.loading()
            result.value=GetDetect(outputFile,selectedType.value)
            GlobalStates.globalStates.notLoading()
            showResults.value=true
            outputFile.delete()
            triggerReq.value=false
        }
    }

    LaunchedEffect(Unit) {
        try {
            types.value= Retrofit.api.getTypes().types?:arrayOf<String>()
        }catch(e: Exception){
            Log.d("TAG", "CameraView: ${e.toString()}")
        }
    }
    Box(){
        AndroidView(
            modifier = Modifier
            ,
            factory = {context->
                PreviewView(context).apply {
                    layoutParams= LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT)
                    setBackgroundColor(android.graphics.Color.BLACK)
                    implementationMode= PreviewView.ImplementationMode.COMPATIBLE
                    scaleType= PreviewView.ScaleType.FILL_CENTER
                    controller=cameraController
                    cameraController.imageCaptureFlashMode = ImageCapture.FLASH_MODE_AUTO
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
    ) {
        // Spacer to push the Box to the center
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Takes up available space to push the content below
            contentAlignment = Alignment.Center // Center the Text inside the Box
        ) {
            Text(
                text = "Tap inside the box to capture",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // Centered Box
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(300.dp)
                .border(color = Color.White, width = 2.dp, shape = RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(Color.Transparent)
                .clickable(enabled = isEnabled.value, onClick = {
                    isEnabled.value = false;
                    val outputOptions = ImageCapture.OutputFileOptions
                        .Builder(outputFile)
                        .build()
                    cameraController.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(localcontext),
                        object : ImageCapture.OnImageSavedCallback {
                            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                triggerReq.value = true
                                isEnabled.value = true;
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Log.d("TAG", "onCaptureSuccess: ${exception.toString()}")
                                isEnabled.value = true
                            }
                        }
                    )
                })
        )

        // Spacer to push the Row to the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Takes up available space to push the content below
            contentAlignment = Alignment.Center // Center the Text inside the Box
        ) {
            Text(
                text = "Place the Object inside the Box",
                color = Color.White,
                fontSize = 16.sp
            )
        }

        // Row at the bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
            ,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .fillMaxHeight(.7f)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
                    .clickable(onClick = {
                        if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
                            return@clickable
                        }
                        cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    })
            ){
                Icon(painter = painterResource(R.drawable.camera_rotate_svgrepo_com), contentDescription = "file",
                    modifier = Modifier.scale(.5f)
                    )
            }
            SelectTypes(selectedType,types.value)
            Image_select(selectedType,result,showResults,outputFile)
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 10.dp)){
        Box(
            Modifier
                .height(40.dp)
                .width(40.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .align(Alignment.TopStart)
                .clickable {
                    GlobalStates.globalStates.navController?.navigate("home") {
                        popUpTo("camera", { inclusive = true })
                    }
                }
        ){
            Icon(painter = painterResource(R.drawable.go_back_svgrepo_com), contentDescription = "",
                modifier = Modifier.scale(.7f)
            )
        }
    }
    if (showResults.value){
        ResultsPage(showResults,result,outputFile)
    }
}


@Composable
fun Image_select(
    selectedType: MutableState<String?>,
    result: MutableState<DetectRes?>,
    showResults: MutableState<Boolean>,
    outputFile: File
){
    var cx = remember { mutableStateOf<Uri?>(null) }
    val context=LocalContext.current
    var launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri? -> cx.value = uri
    }

    LaunchedEffect(cx.value) {
        if (cx.value != null) {
            Log.d("TAG", "Image_select: ${cx.value}")
            GlobalStates.globalStates.loading()
            val file = uriToFile(cx.value!!,context)
            result.value= GetDetect(file,selectedType.value)
            GlobalStates.globalStates.notLoading()
            outputFile.writeBytes(file.readBytes())
            showResults.value=true
            cx.value=null
        }
    }
    Box(
        Modifier
            .fillMaxHeight(.7f)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .clickable(onClick = {
                launcher.launch("image/*")
            })
    ){
        Icon(painter = painterResource(R.drawable.file_send_svgrepo_com), contentDescription = "image",
            modifier = Modifier.scale(.5f)
            )
    }


}

suspend fun GetDetect(image : File,type: String?): DetectRes?{
    val file=RequestBody.create(MediaType.parse("image/*"),image)
    val body=MultipartBody.Part.createFormData("image",image.name,file)
    try {
       return Retrofit.api.detect(body, DetectReq(image.name,image.length().toString(),type))
    }
    catch (e: Exception){
        return null
    }
}

@Composable
fun SelectTypes(selectedType: MutableState<String?>, types: Array<String>) {
    // State to control the dropdown visibility
    var expanded = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight(.7f)
            .clip(RoundedCornerShape(30.dp))
            .background(Color.White)
            .clickable { expanded.value = !expanded.value } // Toggle dropdown visibility
    ) {
        // Display selected type or placeholder text
        Text(
            text = selectedType.value ?: "Select a type",
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            color = Color.Black,
        )

        // Show the dropdown menu when expanded is true
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
            modifier = Modifier
        ) {
            types.forEach { type ->
                DropdownMenuItem(
                    onClick = {
                        selectedType.value = type
                        expanded.value = false // Close the dropdown after selection
                    },
                    text = {Text(text = type)}
                )
            }
            DropdownMenuItem(
                onClick = {
                    selectedType.value = null
                    expanded.value = false
                },
                text = {Text("None")}
            )
        }
    }
}

fun uriToFile(uri: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val file = File(context.cacheDir, "selected_image.jpg")

    contentResolver.openInputStream(uri)?.use { inputStream: InputStream ->
        FileOutputStream(file).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }

    return file
}