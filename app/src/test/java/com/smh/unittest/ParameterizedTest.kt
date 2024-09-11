package com.smh.unittest

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isGreaterThan
import com.smh.unittest.utility.asCurrencyWith6Decimals
import com.smh.unittest.utility.formattedWithAbbreviations
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import kotlin.math.pow
import kotlin.random.Random

object SearchFilter {
    fun filter(input: String): List<String> {
        return if (input.length > 2) listOf("Test1")
        else emptyList()
    }
}

enum class DayOfWeek { Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday }

fun String.isValidPassword(): Boolean {
    val passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$"
    return this.matches(Regex(passwordPattern))
}

class ParameterizedTest {

    @ParameterizedTest
    @ValueSource(strings = ["fish", "car", "hotel"])
    fun testSearchFilter(input: String) {
        val result = SearchFilter.filter(input)
        assertThat(result.size).isEqualTo(1)
    }

    @ParameterizedTest
    @ValueSource(strings = ["fi", "ca", "ho"])
    fun testSearchFilter_empty(input: String) {
        val result = SearchFilter.filter(input)
        assertThat(result).isEmpty()
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["Saturday", "Sunday"])
    fun testIsWeekend(day: DayOfWeek) {
        assertThat(day == DayOfWeek.Saturday || day == DayOfWeek.Sunday).isEqualTo(true)
    }

    @ParameterizedTest
    @EnumSource(value = DayOfWeek::class, names = ["Monday", "Tuesday", "Friday"])
    fun testIsWeekday(day: DayOfWeek) {
        assertThat(day != DayOfWeek.Saturday && day != DayOfWeek.Sunday).isEqualTo(true)
    }

    @ParameterizedTest
    @CsvSource(
        "1234.123456, $1234.123456",
        "100.1, $100.10",
        "100.1234567, $100.123457",
        "0.1234, $0.1234",
        "-1234.56789, -$1234.56789"
    )
    fun test_asCurrencyWith6Decimals(input: Double, expected: String) {
        val result = input.asCurrencyWith6Decimals()
        assertThat(result).isEqualTo(expected)
    }

    @ParameterizedTest
    @CsvSource(
        "1234, '1.23K'",
        "1234567, '1.23M'",
        "1234567890, '1.23Bn'",
        "1234567890123, '1.23Tr'",
        "-1234, '-1.23K'",
        "-1234567, '-1.23M'",
        "999, '999.00'",
        "0.1234, '0.12'",
        "-999, '-999.00'"
    )
    fun test_formattedWithAbbreviations(input: Double, expected: String) {
        val result = input.formattedWithAbbreviations()
        assertThat(expected, result)
    }

    @ParameterizedTest
    @CsvSource(
        "'Password1!', true",        // Valid
        "'Password!', true",         // Valid (no digit but not required)
        "'password!', false",        // No uppercase
        "'PASSWORD!', false",        // No lowercase
        "'Passw1', false",           // Less than 8 characters
        "'Password1', false",        // No special character
        "'P@ssword!', true",         // Valid
        "'12345678!', false"         // No uppercase or lowercase
    )
    fun `test password validation`(password: String, expected: Boolean) {
        val result = password.isValidPassword()
        assertThat(result).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun provideRandomNumbers() = List(3) { Random.nextInt(0, 100) }
    }


    @ParameterizedTest
    @MethodSource("provideRandomNumbers")
    fun testRandomNumbers(input: Int) {
        assertThat(input).isGreaterThan(10)
    }
}