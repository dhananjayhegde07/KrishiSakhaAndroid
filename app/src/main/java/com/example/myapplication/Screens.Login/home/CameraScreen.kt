package com.example.myapplication.Screens.Login.home

import android.graphics.Color
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.myapplication.DTO.DetectReq
import com.example.myapplication.retrofit.Retrofit
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreen(){

    val camPermission = rememberPermissionState(android.Manifest.permission.CAMERA)

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
    LaunchedEffect(triggerReq.value) {
        Log.d("TAG", "CameraView: ${outputFile.length()}")
        if (triggerReq.value){
            GetDetect(outputFile)
            outputFile.delete()
            triggerReq.value=false;
        }
    }

    Column() {
        Text(
            text = "Tap on the preview to capture",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .height(50.dp)
        )
        AndroidView(
            modifier = Modifier
                .weight(1f)
            ,
            factory = {context->
                PreviewView(context).apply {
                    layoutParams= LinearLayout.LayoutParams(MATCH_PARENT,MATCH_PARENT)
                    setBackgroundColor(Color.BLACK)
                    implementationMode= PreviewView.ImplementationMode.COMPATIBLE
                    scaleType= PreviewView.ScaleType.FILL_CENTER
                    controller=cameraController
                    cameraController.imageCaptureFlashMode = ImageCapture.FLASH_MODE_AUTO
                    cameraController.bindToLifecycle(lifecycleOwner)

                    setOnClickListener {
                        isEnabled=false;
                        val outputOptions = ImageCapture.OutputFileOptions.Builder(outputFile).build()
                        cameraController.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(localcontext),
                            object : ImageCapture.OnImageSavedCallback {
                                override  fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                                    triggerReq.value=true
                                    isEnabled=true;
                                }

                                override fun onError(exception: ImageCaptureException) {
                                    Log.d("TAG", "onCaptureSuccess: ${exception.toString()}")
                                    isEnabled=true
                                }
                            }
                        )
                    }
                }
            }
        )
        Log.d("TAG", "CameraScreen: launched")
        Row (
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            Button(
                onClick = {
                    if(cameraController.cameraSelector== CameraSelector.DEFAULT_BACK_CAMERA){
                        cameraController.cameraSelector= CameraSelector.DEFAULT_FRONT_CAMERA
                        return@Button
                    }
                    cameraController.cameraSelector= CameraSelector.DEFAULT_BACK_CAMERA
                }
            ) {
                Text("Switch")
            }
            Image_select()
        }


    }

}


@Composable
fun Image_select(){
    var cx = remember { mutableStateOf<Uri?>(null) }
    var launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri? -> cx.value = uri
    }

    LaunchedEffect(cx.value) {
        if (cx.value!=null){
            Log.d("TAG", "Image_select: ${cx.value}")
        }
    }
    Button(onClick = {
        launcher.launch("image/*")
    }) {
        Text("Select")
    }



}

suspend fun GetDetect(image : File){
    val file=RequestBody.create(MediaType.parse("image/*"),image)
    val body=MultipartBody.Part.createFormData("image",image.name,file)
    try {
        Retrofit.api.detect(body, DetectReq("","",""))
    }
    catch (e: Exception){

    }
}