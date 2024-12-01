package com.example.myapplication.Screens.Login

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.zIndex
import com.example.myapplication.R

@Composable
fun LoadingScreen(){
    val transition= rememberInfiniteTransition("Loading Transition")
    var rotationval = transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = InfiniteRepeatableSpec(animation = tween(durationMillis = 2000, easing = LinearEasing)),
        label = "rotation"
    )
    var scaleVal=transition.animateFloat(
        initialValue = .17f,
        targetValue = .2f,
        animationSpec = InfiniteRepeatableSpec(
            animation = keyframes {
                durationMillis=1000
                .17f at 0
                .2f at 500
                .17f at 1000

            }
        ),
        label = "scale"
    )
    Box(modifier = Modifier.fillMaxSize()
        .background(Color.Black.copy(alpha = .5f))
        .zIndex(1f)
        .clickable(enabled = false,onClick = {}, indication = null, interactionSource = remember { MutableInteractionSource() })
    ,
        contentAlignment = Alignment.Center
    ){
        Image(painter = painterResource(R.drawable.loading2), contentDescription = "loading",
            modifier = Modifier.scale(scaleVal.value).clip(RoundedCornerShape(50))
                .graphicsLayer { rotationZ=rotationval.value }

        )

    }

}