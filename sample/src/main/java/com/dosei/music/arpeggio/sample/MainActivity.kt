package com.dosei.music.arpeggio.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dosei.music.arpeggio.*
import com.dosei.music.arpeggio.sample.ui.theme.ArpeggioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArpeggioTheme {
                Content()
            }
        }
    }
}

@Composable
fun Content() {
    Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
        DiagramTheme {
            Diagram(
                modifier = Modifier.padding(16.dp),
                name = "Bm",
                components = listOf(
                    Barre(fret = 7, strings = 0..5, finger = Finger.Index),
                    Position(fret = 9, string = 2, finger = Finger.Pinky),
                    Position(fret = 9, string = 1, finger = Finger.Ring),
                )
            )
        }
    }
}

@Composable
fun GuitarThumbnailTheme(
    content: @Composable () -> Unit
) {
    DiagramTheme(
        typography = Typography(
            name = TextStyle().copy(fontSize = 14.sp),
            firstFretIndicator = TextStyle(fontSize = 14.sp),
            fingerIndicator = TextStyle(fontSize = 8.sp)
        ),
        sizes = Sizes(position = 10.dp),
        content = content
    )
}

@Composable
fun UkuleleThumbnailTheme(
    content: @Composable () -> Unit
) {
    DiagramTheme(
        frets = 4,
        strings = 4,
        typography = Typography(
            name = TextStyle(fontSize = 14.sp, color = Color.Red, fontWeight = FontWeight.Bold),
            firstFretIndicator = TextStyle(fontSize = 14.sp),
            fingerIndicator = TextStyle(fontSize = 8.sp)
        ),
        sizes = Sizes(position = 13.dp),
        content = content
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ArpeggioTheme {
        Content()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewGuitarThumbnail() {
    ArpeggioTheme {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.size(220.dp, 200.dp)) {
            GuitarThumbnailTheme {
                Diagram(
                    modifier = Modifier.padding(8.dp),
                    name = "Bm",
                    components = listOf(
                        Barre(fret = 7, strings = 0..5),
                        Position(fret = 9, string = 2),
                        Position(fret = 9, string = 1),
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUkuleleThumbnail() {
    ArpeggioTheme {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.size(220.dp, 200.dp)) {
            UkuleleThumbnailTheme {
                Diagram(
                    modifier = Modifier.padding(8.dp),
                    name = "E",
                    components = listOf(
                        Position(fret = 1, string = 0),
                        Position(fret = 2, string = 3),
                        Position(fret = 4, string = 2),
                        OpenString(1)
                    )
                )
            }
        }
    }
}