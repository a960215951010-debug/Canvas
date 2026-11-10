package tw.edu.pu.csim.timmy.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay
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

    // 控制遊戲是否正在進行的狀態（true 代表正在跑，false 代表停止）
    var gameOver by remember { mutableStateOf(false) }

    // 小小兵的 X 與 Y 座標狀態
    var minionsX by remember { mutableFloatStateOf(50f) }
    var minionsY by remember { mutableFloatStateOf(180f) }

    // 當遊戲未停止 (gameOver 為 false) 時，透過迴圈讓小小兵持續移動
    LaunchedEffect(gameOver) {
        if (!gameOver) {
            while (true) {
                delay(100L) // 每 0.1 秒更新一次
                minionsX += 5f
                minionsY += 10f
            }
        }
    }

    // 使用 Box 搭配 verticalScroll，讓畫布長度可以達到 1920 並且支援上下滑動
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // 控制遊戲開始與停止的按鈕（對應老師畫面中的按鈕）
        Button(
            onClick = {
                gameOver = !gameOver
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text(if (gameOver) "開始遊戲" else "停止遊戲")
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1920.dp)
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
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

                // 2. 畫小小兵圖片（使用即時變動的 minionsX 與 minionsY）
                translate(minionsX, minionsY) {
                    drawImage(minionsImage)
                }

                // 3. 繪製藍色空心矩形[cite: 2]
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
}