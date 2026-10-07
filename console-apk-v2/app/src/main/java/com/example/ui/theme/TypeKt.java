package com.example.ui.theme;

import androidx.compose.material3.Typography;
import androidx.compose.ui.graphics.Shadow;
import androidx.compose.ui.graphics.drawscope.DrawStyle;
import androidx.compose.ui.text.PlatformTextStyle;
import androidx.compose.ui.text.TextStyle;
import androidx.compose.ui.text.font.FontFamily;
import androidx.compose.ui.text.font.FontStyle;
import androidx.compose.ui.text.font.FontSynthesis;
import androidx.compose.ui.text.font.FontWeight;
import androidx.compose.ui.text.font.GenericFontFamily;
import androidx.compose.ui.text.font.SystemFontFamily;
import androidx.compose.ui.text.intl.LocaleList;
import androidx.compose.ui.text.style.BaselineShift;
import androidx.compose.ui.text.style.LineHeightStyle;
import androidx.compose.ui.text.style.TextDecoration;
import androidx.compose.ui.text.style.TextGeometricTransform;
import androidx.compose.ui.text.style.TextIndent;
import androidx.compose.ui.text.style.TextMotion;
import androidx.compose.ui.unit.TextUnitKt;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: Type.kt */
@Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\"\u0011\u0010\u0000\u001a\u00020\u0001¢\u0006\b\n\u0000\u001a\u0004\b\u0002\u0010\u0003¨\u0006\u0004"}, d2 = {"Typography", "Landroidx/compose/material3/Typography;", "getTypography", "()Landroidx/compose/material3/Typography;", "app"}, k = 2, mv = {2, 2, 0}, xi = 48)
/* loaded from: classes3.dex */
public final class TypeKt {
    private static final Typography Typography;

    static {
        GenericFontFamily serif = FontFamily.Companion.getSerif();
        FontWeight bold = FontWeight.Companion.getBold();
        GenericFontFamily genericFontFamily = serif;
        TextStyle textStyle = new TextStyle(0L, TextUnitKt.getSp(30), bold, (FontStyle) null, (FontSynthesis) null, genericFontFamily, (String) null, TextUnitKt.getSp(-0.5d), (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(36), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16645977, (DefaultConstructorMarker) null);
        GenericFontFamily serif2 = FontFamily.Companion.getSerif();
        FontWeight bold2 = FontWeight.Companion.getBold();
        GenericFontFamily genericFontFamily2 = serif2;
        TextStyle textStyle2 = new TextStyle(0L, TextUnitKt.getSp(24), bold2, (FontStyle) null, (FontSynthesis) null, genericFontFamily2, (String) null, TextUnitKt.getSp(0), (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(32), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16645977, (DefaultConstructorMarker) null);
        SystemFontFamily systemFontFamily = FontFamily.Companion.getDefault();
        FontWeight semiBold = FontWeight.Companion.getSemiBold();
        SystemFontFamily systemFontFamily2 = systemFontFamily;
        TextStyle textStyle3 = new TextStyle(0L, TextUnitKt.getSp(20), semiBold, (FontStyle) null, (FontSynthesis) null, systemFontFamily2, (String) null, TextUnitKt.getSp(0), (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(28), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16645977, (DefaultConstructorMarker) null);
        SystemFontFamily systemFontFamily3 = FontFamily.Companion.getDefault();
        FontWeight semiBold2 = FontWeight.Companion.getSemiBold();
        SystemFontFamily systemFontFamily4 = systemFontFamily3;
        TextStyle textStyle4 = new TextStyle(0L, TextUnitKt.getSp(16), semiBold2, (FontStyle) null, (FontSynthesis) null, systemFontFamily4, (String) null, TextUnitKt.getSp(0.1d), (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(24), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16645977, (DefaultConstructorMarker) null);
        SystemFontFamily systemFontFamily5 = FontFamily.Companion.getDefault();
        FontWeight normal = FontWeight.Companion.getNormal();
        SystemFontFamily systemFontFamily6 = systemFontFamily5;
        TextStyle textStyle5 = new TextStyle(0L, TextUnitKt.getSp(15), normal, (FontStyle) null, (FontSynthesis) null, systemFontFamily6, (String) null, TextUnitKt.getSp(0.2d), (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(22), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16645977, (DefaultConstructorMarker) null);
        SystemFontFamily systemFontFamily7 = FontFamily.Companion.getDefault();
        FontWeight normal2 = FontWeight.Companion.getNormal();
        SystemFontFamily systemFontFamily8 = systemFontFamily7;
        TextStyle textStyle6 = new TextStyle(0L, TextUnitKt.getSp(14), normal2, (FontStyle) null, (FontSynthesis) null, systemFontFamily8, (String) null, TextUnitKt.getSp(0.2d), (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(20), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16645977, (DefaultConstructorMarker) null);
        SystemFontFamily systemFontFamily9 = FontFamily.Companion.getDefault();
        FontWeight medium = FontWeight.Companion.getMedium();
        SystemFontFamily systemFontFamily10 = systemFontFamily9;
        TextStyle textStyle7 = new TextStyle(0L, TextUnitKt.getSp(14), medium, (FontStyle) null, (FontSynthesis) null, systemFontFamily10, (String) null, TextUnitKt.getSp(0.1d), (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(20), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16645977, (DefaultConstructorMarker) null);
        SystemFontFamily systemFontFamily11 = FontFamily.Companion.getDefault();
        FontWeight medium2 = FontWeight.Companion.getMedium();
        SystemFontFamily systemFontFamily12 = systemFontFamily11;
        TextStyle textStyle8 = new TextStyle(0L, TextUnitKt.getSp(12), medium2, (FontStyle) null, (FontSynthesis) null, systemFontFamily12, (String) null, TextUnitKt.getSp(0.3d), (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(16), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16645977, (DefaultConstructorMarker) null);
        SystemFontFamily systemFontFamily13 = FontFamily.Companion.getDefault();
        FontWeight medium3 = FontWeight.Companion.getMedium();
        SystemFontFamily systemFontFamily14 = systemFontFamily13;
        Typography = new Typography(null, null, textStyle, null, textStyle2, null, textStyle3, textStyle4, null, textStyle5, textStyle6, null, textStyle7, textStyle8, new TextStyle(0L, TextUnitKt.getSp(11), medium3, (FontStyle) null, (FontSynthesis) null, systemFontFamily14, (String) null, TextUnitKt.getSp(0.4d), (BaselineShift) null, (TextGeometricTransform) null, (LocaleList) null, 0L, (TextDecoration) null, (Shadow) null, (DrawStyle) null, 0, 0, TextUnitKt.getSp(14), (TextIndent) null, (PlatformTextStyle) null, (LineHeightStyle) null, 0, 0, (TextMotion) null, 16645977, (DefaultConstructorMarker) null), 2347, null);
    }

    public static final Typography getTypography() {
        return Typography;
    }
}
