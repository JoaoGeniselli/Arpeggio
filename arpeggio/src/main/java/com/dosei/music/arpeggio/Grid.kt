package com.dosei.music.arpeggio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.inset
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
internal fun Grid(
    modifier: Modifier = Modifier,
    initialFret: Int = 1,
    scope: GridScope.() -> Unit
) {
    val sizes = DiagramTheme.sizes
    val colors = DiagramTheme.colors
    val typography = DiagramTheme.typography
    val frets = DiagramTheme.frets
    val strings = DiagramTheme.strings

    Canvas(modifier = modifier.fillMaxSize()) {
        val inset = sizes.position.toPx() / 2f
        val geometry = extractGeometry(
            inset = inset,
            positionSize = sizes.position,
            strokeWidth = sizes.strokeWidth,
            frets = frets,
            strings = strings
        )
        inset(horizontal = inset - sizes.strokeWidth.toPx()) {
            drawRect(colors.grid, size = Size(width = size.width, height = inset))
        }
        inset(
            left = inset,
            right = inset,
            top = inset,
            bottom = inset * 2 + 8.dp.toPx()
        ) {
            drawGrid(
                frets = frets,
                strings = strings,
                columnWidth = geometry.stringSpaceWidth,
                rowHeight = geometry.fretSpaceHeight,
                color = colors.grid,
                strokeWidth = sizes.strokeWidth
            )
        }
        GridScope(initialFret, colors, geometry, sizes, typography, strings, this)
            .apply(scope)
            .commit()
    }
}

internal class GridScope(
    initialFret: Int,
    private val colors: Colors,
    private val geometry: Geometry,
    private val sizes: Sizes,
    private val typography: Typography,
    strings: Int,
    private val drawScope: DrawScope
) {
    private val stringUsage = (0 until strings).map { it to false }.toMap().toMutableMap()
    private val fretDiff = initialFret.dec()

    fun commit() {
        stringUsage.forEach { entry ->
            val string = entry.key
            val isUsed = entry.value
            if (isUsed) {
                drawScope.run {
                    drawOpenStringIndicator(
                        geometry.centerOfStringIndicator(string),
                        colors.stringUsageIndicator,
                        sizes.strokeWidth,
                        sizes.position
                    )
                }
            } else {
                drawScope.run {
                    drawClosedStringIndicator(
                        geometry.topLeftOfStringIndicator(string),
                        geometry.bottomRightOfStringIndicator(string),
                        colors.stringUsageIndicator,
                        sizes.strokeWidth
                    )
                }
            }
        }
    }

    fun draw(component: Component) {
        when (component) {
            is Barre -> draw(barre = component)
            is OpenString -> draw(openString = component)
            is Position -> draw(position = component)
        }
    }

    fun draw(barre: Barre) {
        drawScope.drawBarre(
            initialStringCenter = geometry.centerOfString(barre.strings.first),
            finalStringCenter = geometry.centerOfString(barre.strings.last),
            fretCenter = geometry.centerOfFret(barre.fret.adjusted()),
            color = colors.position,
            positionSize = sizes.position
        )

        barre.finger?.let {
            drawScope.drawFingerIndicator(
                finger = it,
                fretCenter = geometry.centerOfFret(barre.fret.adjusted()),
                stringCenter = geometry.centerOfString(barre.strings.first),
                color = typography.fingerIndicator.color,
                positionSize = sizes.position,
                textSize = typography.fingerIndicator.fontSize
            )
        }
        stringUsage.putAll(barre.strings.map { it to true })
    }

    fun draw(position: Position) {
        val fretCenter = geometry.centerOfFret(position.fret.adjusted())
        val stringCenter = geometry.centerOfString(position.string)

        drawScope.drawPosition(
            fretCenter = fretCenter,
            stringLine = stringCenter,
            color = colors.position,
            positionSize = sizes.position
        )

        position.finger?.let {
            drawScope.drawFingerIndicator(
                finger = it,
                fretCenter = fretCenter,
                stringCenter = stringCenter,
                color = typography.fingerIndicator.color,
                positionSize = sizes.position,
                textSize = typography.fingerIndicator.fontSize
            )
        }
        stringUsage[position.string] = true
    }

    fun draw(openString: OpenString) {
        stringUsage[openString.string] = true
    }

    private fun Int.adjusted(): Int = minus(fretDiff)
}

@Preview(showBackground = true)
@Composable
private fun PreviewChordThumbnail() {
    Surface(modifier = Modifier.size(600.dp), color = Color.White) {
        Grid(
            modifier = Modifier.padding(50.dp),
            scope = {
                draw(
                    Barre(
                        fret = 7,
                        strings = 1..5,
                        finger = Finger.Index
                    )
                )
                draw(Position(fret = 3, string = 4, finger = Finger.Middle))
                draw(Position(fret = 4, string = 3, finger = Finger.Pinky))
                draw(Position(fret = 4, string = 2, finger = Finger.Ring))
            },
            initialFret = 7
        )
    }
}