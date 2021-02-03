package com.example.getyourdrink

import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.getyourdrink.api.IngredientSimple

fun formatDescription(descrtiption: String): Spanned {
    val sb = StringBuilder()
    sb.append(descrtiption)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}

fun formatIngredients(ingredients: List<IngredientSimple>): Spanned {
    val sb = StringBuilder()
    sb.append("Składniki:<br>")
    for (ingr in ingredients) {
        sb.append("- " + ingr.name + "<br>")
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        return HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}