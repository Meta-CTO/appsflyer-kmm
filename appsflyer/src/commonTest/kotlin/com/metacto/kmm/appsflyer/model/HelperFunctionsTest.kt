package com.metacto.kmm.appsflyer.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class HelperFunctionsTest {

    @Test
    fun testHasDescopeToken_withDtKey() {
        val extras: Map<Any?, *> = mapOf("dt" to "sometoken")
        assertTrue(hasDescopeToken(extras))
    }

    @Test
    fun testHasDescopeToken_withoutDtKey() {
        val extras: Map<Any?, *> = mapOf("other_key" to "value")
        assertFalse(hasDescopeToken(extras))
    }

    @Test
    fun testHasDescopeToken_withNullExtras() {
        assertFalse(hasDescopeToken(null))
    }

    @Test
    fun testHasDescopeToken_withEmptyExtras() {
        val extras: Map<Any?, *> = emptyMap<Any?, Any?>()
        assertFalse(hasDescopeToken(extras))
    }

    @Test
    fun testHasLoginType_withLtKey() {
        val extras: Map<Any?, *> = mapOf("lt" to "email")
        assertTrue(hasLoginType(extras))
    }

    @Test
    fun testHasLoginType_withoutLtKey() {
        val extras: Map<Any?, *> = mapOf("other_key" to "value")
        assertFalse(hasLoginType(extras))
    }

    @Test
    fun testHasLoginType_withNullExtras() {
        assertFalse(hasLoginType(null))
    }

    @Test
    fun testHasLoginType_withEmptyExtras() {
        val extras: Map<Any?, *> = emptyMap<Any?, Any?>()
        assertFalse(hasLoginType(extras))
    }

    @Test
    fun testGetLoginType_withEmail() {
        val extras: Map<Any?, *> = mapOf("lt" to "email")
        assertEquals(LoginType.EMAIL, getLoginType(extras))
    }

    @Test
    fun testGetLoginType_withPhone() {
        val extras: Map<Any?, *> = mapOf("lt" to "phone")
        assertEquals(LoginType.PHONE, getLoginType(extras))
    }

    @Test
    fun testGetLoginType_withInvalidValue() {
        val extras: Map<Any?, *> = mapOf("lt" to "invalid")
        assertNull(getLoginType(extras))
    }

    @Test
    fun testGetLoginType_withNullExtras() {
        assertNull(getLoginType(null))
    }

    @Test
    fun testGetLoginType_withEmptyExtras() {
        val extras: Map<Any?, *> = emptyMap<Any?, Any?>()
        assertNull(getLoginType(extras))
    }

    @Test
    fun testGetLoginType_withMissingKey() {
        val extras: Map<Any?, *> = mapOf("other_key" to "value")
        assertNull(getLoginType(extras))
    }

    @Test
    fun testGetAfSub1_withValidValue() {
        val extras: Map<Any?, *> = mapOf("af_sub1" to "test_value")
        assertEquals("test_value", getAfSub1(extras))
    }

    @Test
    fun testGetAfSub1_withNullExtras() {
        assertNull(getAfSub1(null))
    }

    @Test
    fun testGetAfSub1_withEmptyExtras() {
        val extras: Map<Any?, *> = emptyMap<Any?, Any?>()
        assertNull(getAfSub1(extras))
    }

    @Test
    fun testGetAfSub1_withMissingKey() {
        val extras: Map<Any?, *> = mapOf("other_key" to "value")
        assertNull(getAfSub1(extras))
    }

    @Test
    fun testGetAfSub1_withNumericValue() {
        val extras: Map<Any?, *> = mapOf("af_sub1" to 12345)
        assertEquals("12345", getAfSub1(extras))
    }
}
