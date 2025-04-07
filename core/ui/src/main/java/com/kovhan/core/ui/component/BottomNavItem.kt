package com.kovhan.core.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun RowScope.BottomNavItem(
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
    isEnabled: Boolean = true,
    bottomBarTab: BottomBarTab,
    title: String,
    onClick: () -> Unit = {},
) {
    val image = if (isSelected) {
        bottomBarTab.iconSelected
    } else {
        bottomBarTab.iconUnSelected
    }

//    val pictureSize = HasherMaterialTheme.dimensions.bottomBarIconSize
//    val topMarginSizeSize = HasherMaterialTheme.dimensions.space_4
//
//    NavigationBarItem(
//        modifier = modifier
//            .padding(
//                bottom = HasherMaterialTheme.dimensions.bottomBarBottomPadding,
//                top = HasherMaterialTheme.dimensions.bottomBarTopPadding
//            ),
//        enabled = isEnabled,
//        selected = isSelected,
//        alwaysShowLabel = false,
//        onClick = onClick,
//        colors = NavigationBarItemDefaults.colors(
//            indicatorColor = HasherMaterialTheme.colors.transparent
//        ),
//        icon = {
//            ConstraintLayout {
//                val (picture, label) = createRefs()
//
//                Image(
//                    modifier = Modifier
//                        .constrainAs(picture) {
//                            height = Dimension.value(pictureSize)
//                            width = Dimension.value(pictureSize)
//                            centerHorizontallyTo(parent)
//                            top.linkTo(parent.top)
//                        },
//                    contentScale = ContentScale.Fit,
//                    painter = painterResource(image.invoke()),
//                    contentDescription = title,
//                )
//
//                Text(
//                    text = title,
//                    style = HasherMaterialTheme.typography.smallLabelNormal.copy(
//                        lineHeight = HasherMaterialTheme.dimensions.line_height_16_8,
//                        letterSpacing = HasherMaterialTheme.dimensions.text_spacer_0_2,
//                        color = HasherMaterialTheme.colors.primaryTextColor
//                    ),
//                    modifier = Modifier
//                        .constrainAs(label) {
//                            top.linkTo(picture.bottom, margin = topMarginSizeSize)
//                            centerHorizontallyTo(parent)
//                        }
//                )
//            }
//        }
//    )
}
