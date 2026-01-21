package com.example.realidadaumentada.ui.screen

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.realidadaumentada.ui.navigation.ARScreen
import com.google.ar.core.Frame
import com.google.ar.core.TrackingFailureReason
import io.github.sceneview.model.ModelInstance
import io.github.sceneview.rememberCameraNode
import io.github.sceneview.rememberCollisionSystem
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberNode
import io.github.sceneview.rememberNodes
import io.github.sceneview.rememberView
import androidx.compose.ui.Modifier
import com.example.realidadaumentada.util.Utils
import com.google.ar.core.Config
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.isValid
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.node.Node
import io.github.sceneview.rememberMaterialLoader
import io.github.sceneview.rememberOnGestureListener

@Composable
fun ARScreen(navController: NavController, model: String){
    val engine = rememberEngine()
    val modelLoader = rememberModelLoader(engine = engine)
    val materialLoader = rememberMaterialLoader(engine = engine)
    val cameraNode = rememberARCameraNode(engine = engine)
    val childNodes = rememberNodes()
    val view = rememberView(engine = engine)
    val collisionSystem = rememberCollisionSystem(view = view)
    val planeRenderer = remember{
        mutableStateOf(true)
    }
    val modelInstance = remember {
        mutableListOf<ModelInstance>()
    }
    val trackingFailureReason = remember {
        mutableStateOf<TrackingFailureReason?>(null)
    }
    val frame = remember {
        mutableStateOf<Frame?>(null)
    }

    ARScene(
        modifier = Modifier.fillMaxSize(),
        childNodes = childNodes,
        engine = engine,
        modelLoader = modelLoader,
        collisionSystem = collisionSystem,
        planeRenderer = planeRenderer.value,
        cameraNode = cameraNode,
        materialLoader = materialLoader,
        onTrackingFailureChanged = {
            trackingFailureReason.value = it
        },
        onSessionUpdated = {
            _, updateFrame ->
            frame.value = updateFrame
        },
        sessionConfiguration = {session, config ->
            config.depthMode = when(session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)){
                true -> Config.DepthMode.AUTOMATIC
                false -> Config.DepthMode.DISABLED
            }
            config.lightEstimationMode = Config.LightEstimationMode.ENVIRONMENTAL_HDR
        },
        onGestureListener = rememberOnGestureListener(
            onSingleTapConfirmed = onSingleTapConfirmed@{ e: MotionEvent, node: Node? ->
                val currentFrame = frame.value
                if (currentFrame == null) {
                    Log.w("ARScreen", "Frame not ready, tap ignored")
                    return@onSingleTapConfirmed
                }

                if (node == null) {
                    val hitTestResult = currentFrame.hitTest(e.x, e.y)
                    hitTestResult?.firstOrNull { it.isValid(depthPoint = false, point = false) }?.createAnchorOrNull()?.let {
                        val nodeMode = Utils.createAnchorNode(
                            engine = engine,
                            modelLoader = modelLoader,
                            materialLoader = materialLoader,
                            modelInstance = modelInstance,
                            anchor = it,
                            model = Utils.getModelForAlphabet(model)
                        )
                        childNodes += nodeMode
                    }
                }
            }
        )
    )
}