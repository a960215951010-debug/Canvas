package tw.edu.pu.csim.timmy.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import tw.edu.pu.csim.timmy.canvas.ui.theme.CanvasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CanvasTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TestCanvas(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun TestCanvas(modifier: Modifier = Modifier) {
    // 載入圖片資源（小小兵）[cite: 7]
    val minionsImage: ImageBitmap = ImageBitmap.imageResource(id = R.drawable.minions)

    // 宣告小小兵的起始 X 與 Y 座標動畫變數
    val minionsX = remember { Animatable(50f) }
    val minionsY = remember { Animatable(180f) }

    // 使用 LaunchedEffect 讓小小兵同時往右下方移動
    LaunchedEffect(Unit) {
        // 同步執行 X 與 Y 軸的動畫
        minionsX.animateTo(
            targetValue = 550f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 50000, easing = LinearEasing)
            )
        )
    }

    LaunchedEffect(Unit) {
        minionsY.animateTo(
            targetValue = 680f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 50000, easing = LinearEasing)
            )
        )
    }

    // 使用 Box 搭配 verticalScroll，讓畫布長度可以達到 1920 並且支援上下滑動
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(1920.dp) // 將畫布高度強制指定為 1920
        ) {
            // 1. 顯示畫布寬高文字
            drawContext.canvas.nativeCanvas.drawText(
                "畫布寬度: ${size.width}, 長度: ${size.height}",
                50f, 120f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 50f
                }
            )

            // 2. 畫小小兵圖片（使用 minionsX 與 minionsY 的即時動畫數值）
            translate(minionsX.value, minionsY.value) {
                drawImage(minionsImage)
            }

            // 3. 繪製藍色空心矩形（位於圖片右側）[cite: 2]
            drawRect(
                Color.Blue,
                Offset(350f, 220f),
                Size(200f, 200f),
                style = Stroke(width = 5f)
            )

            // 4. 繪製洋紅色的圓
            val circleCenter = Offset(350f, 700f)
            drawCircle(
                Color.Magenta,
                100f,
                circleCenter
            )

            // 5. 繪製紅色的線，從洋紅色圓心畫到更下方
            drawLine(
                Color.Red,
                circleCenter,
                Offset(size.width, 1700f),
                strokeWidth = 10f
            )
        }
    }
}