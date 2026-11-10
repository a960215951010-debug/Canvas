package tw.edu.pu.csim.timmy.canvas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.res.imageResource
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
        moveTo(50f, 100f)   // 移動到起始點 (50, 100)[cite: 8]
        lineTo(200f, 500f)  // 畫一條線到 (200, 500)[cite: 8]
        lineTo(450f, 400f)  // 畫一條線到 (450, 400)[cite: 8]
        close()             // 閉合路徑[cite: 8]
    }

    Canvas(
        modifier = modifier.fillMaxSize()
    ) {
        // 繪製洋紅色的圓，半徑 100，圓心 (100, 100)[cite: 2]
        drawCircle(
            Color.Magenta,
            100f,
            Offset(100f, 100f)
        )

        // 繪製藍色空心矩形，左上角 (300, 200)，寬度與高度 200[cite: 2]
        drawRect(
            Color.Blue,
            Offset(300f, 200f),
            Size(200f, 200f),
            style = Stroke(width = 5f) // 使用 style 控制外框樣式[cite: 2]
        )

        // 繪製紅色的線，從 (200, 800) 畫到右下角，線條粗細 10[cite: 6]
        drawLine(
            Color.Red,
            Offset(200f, 800f),
            Offset(size.width, size.height),
            strokeWidth = 10f
        )

        // 取得小小兵圖檔的寬度與高度[cite: 7]
        val minionsWidth = minionsImage.width
        val minionsHeight = minionsImage.height

        // 置中公式：(畫布寬高 - 圖片寬高) / 2f[cite: 7]
        val offsetX = (size.width - minionsWidth) / 2f
        val offsetY = (size.height - minionsHeight) / 2f

        // 將畫布起點移至置中位置後畫圖[cite: 7]
        translate(offsetX, offsetY) {
            drawImage(minionsImage)
        }

        // 繪製綠色路徑，透明度 1 (不透明)，線條粗細 10[cite: 8]
        drawPath(path, Color.Green, 1f, Stroke(width = 10f))
    }
  }