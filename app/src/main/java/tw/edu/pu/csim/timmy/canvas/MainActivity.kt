package tw.edu.pu.csim.timmy.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Animatable
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
import androidx.compose.ui.graphics.Path
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

    // 建立一個新的路徑物件[cite: 8]
    val path = Path().apply {
        moveTo(50f, 100f)
        lineTo(200f, 500f)
        lineTo(450f, 400f)
        close()
    }

    // 宣告一個用來控制小小兵 X 座標的動畫變數，初始位置在 50f
    val minionsX = remember { Animatable(50f) }

    // 使用 LaunchedEffect 讓小小兵每秒往右移動 10px
    LaunchedEffect(Unit) {
        // 目標讓 X 座標移動到畫面右側（例如 800f），以每秒 10px 的速度計算持續時間
        // 假設總共移動 750px，速度 10px/s 需要 75 秒，這裡設定線性動畫
        minionsX.animateTo(
            targetValue = 800f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 75000, easing = LinearEasing)
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
            // 1. 顯示畫布寬高文字（對應老師照片中的頂端文字）
            drawContext.canvas.nativeCanvas.drawText(
                "畫布寬度: ${size.width}, 長度: ${size.height}",
                50f, 120f,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textSize = 50f
                }
            )

            // 2. 畫小小兵圖片（X 座標改用 minionsX.value 即時更新達成移動效果）
            translate(minionsX.value, 180f) {
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