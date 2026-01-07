package com.eltonkola.desktop.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.eltonkola.desktop.desktopapp.generated.resources.Res
import com.eltonkola.desktop.desktopapp.generated.resources.send
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.jewel.foundation.theme.JewelTheme
import org.jetbrains.jewel.ui.component.CircularProgressIndicator
import org.jetbrains.jewel.ui.component.IconButton
import org.jetbrains.jewel.ui.component.Text
import org.jetbrains.jewel.ui.component.TextArea

@Composable
fun InputArea(
    showSuggestions: Boolean,
    isGenerating: Boolean,
    onSend: (String) -> Unit
) {
    val textState = rememberTextFieldState("")

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = JewelTheme.globalColors.panelBackground,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            // Suggestions
            if (showSuggestions) {
                SuggestionChips(onSuggestionClick = { onSend(it) })
            }

            Surface(
                modifier = Modifier.fillMaxWidth()
                    .shadow(2.dp, RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                color = JewelTheme.globalColors.panelBackground
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {

                    TextArea(
                        state = textState,
                        placeholder = { Text("Message AI Assistant...") },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp, max = 120.dp)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        readOnly = isGenerating
                    )


                    IconButton(
                        onClick = {
                            if (textState.text.isNotBlank() && !isGenerating) {
                                onSend(textState.text.toString())
                                textState.setTextAndPlaceCursorAtEnd("")
                            }
                        },
                        enabled = textState.text.isNotBlank() && !isGenerating,
                        modifier = Modifier.size(48.dp)
                    ) {
                        if (isGenerating) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                        } else {
                            Icon(
                                painter = painterResource(Res.drawable.send),
                                contentDescription = "Send",
                                tint = if (textState.text.isNotBlank())
                                    JewelTheme.globalColors.text.info
                                else
                                    JewelTheme.globalColors.text.disabled
                            )
                        }
                    }
                }
            }
        }
    }
}
