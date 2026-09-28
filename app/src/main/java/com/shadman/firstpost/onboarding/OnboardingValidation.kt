package com.shadman.firstpost.onboarding

import com.shadman.firstpost.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Pure functions used by the onboarding steps. Kept out of the composables
// and the ViewModel's setters so the rules (what a valid username looks
// like, how age is computed) live in exactly one place.

private val USERNAME_REGEX = Regex("^[a-z0-9]{3,30}$")

fun isValidUsername(username: String): Boolean = USERNAME_REGEX.matches(username)

// "Maya Chen" -> "mayachen". Only a starting point — the field stays editable.
fun suggestUsername(name: String): String {
    val cleaned = name.lowercase(Locale.ROOT).filter { it.isLetterOrDigit() }
    return cleaned.take(30).ifBlank { "user" }
}

// "Maya Chen" -> "MC", used for the avatar placeholder before a photo exists.
fun initialsFrom(name: String): String =
    name.split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .joinToString("") { it.first().uppercase() }

private val DATE_OF_BIRTH_FORMAT = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)

fun formatDateOfBirth(millis: Long): String = DATE_OF_BIRTH_FORMAT.format(millis)

fun ageFromDateOfBirth(millis: Long): Int {
    val dob = Calendar.getInstance().apply { timeInMillis = millis }
    val today = Calendar.getInstance()
    var age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
    if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) age--
    return age
}

// Opens the date picker on a plausible birthday instead of today's date.
fun defaultDateOfBirthMillis(): Long =
    Calendar.getInstance().apply { add(Calendar.YEAR, -18) }.timeInMillis

// "Maya Chen" -> "Maya", for the Say hello caption templates.
fun firstNameOf(name: String): String =
    name.trim().substringBefore(' ').ifBlank { "there" }

fun captionSuggestions(firstName: String): List<String> =
    Constants.SayHello.CAPTION_TEMPLATES.map { it.format(firstName) }
