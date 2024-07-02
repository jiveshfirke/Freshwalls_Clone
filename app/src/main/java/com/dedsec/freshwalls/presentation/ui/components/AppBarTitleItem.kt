package com.dedsec.freshwalls.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dedsec.freshwalls.presentation.ui.theme.black
import com.dedsec.freshwalls.presentation.ui.theme.grey

@Composable
fun AppBarTitleItem(title: String, isSelected: Boolean, onClick: () -> Unit) {
    var textWidth by remember {
        mutableIntStateOf(0)
    }
    Column(
        modifier = Modifier
            .wrapContentSize()
            .clickable { onClick() },
        content = {
            Text(
                text = title,
                style = TextStyle(
                    color = if (isSelected) black else grey,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                onTextLayout = { textLayoutResult ->
                    textWidth = textLayoutResult.size.width
                }
            )
            HorizontalDivider(
                thickness = 3.dp,
                modifier = Modifier
                    .width(with(LocalDensity.current) { textWidth.toDp() })
                    .alpha(if (isSelected) 1f else 0f)
                    .clip(shape = RoundedCornerShape(percent = 100)),
                color = if (isSelected) black else grey
            )
        })
}