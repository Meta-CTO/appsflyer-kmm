package com.metacto.kmm.appsflyer.model

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DeepLinkMetadataTest {

    @Test
    fun testParseJsonDestination_withValidJSON() {
        val jsonInput = """{"deep_link_destination":"/auth/install-preauth"}"""
        val result = jsonInput.parseJsonDestination()
        assertEquals("/auth/install-preauth", result)
    }

    @Test
    fun testParseJsonDestination_withJSONContainingPathWithParameters() {
        val jsonInput = """{"deep_link_destination":"/property/tt1ysu65br0a1u0up45b4awx__af_channel=share__af_referrer_customer_id=gesiws1opky86sz97o6wvo9m"}"""
        val result = jsonInput.parseJsonDestination()
        assertEquals("/property/tt1ysu65br0a1u0up45b4awx", result)
    }

    @Test
    fun testParseJsonDestination_withJSONContainingWhitespace() {
        val jsonInput = """  {"deep_link_destination":"/auth/install-preauth"}  """
        val result = jsonInput.parseJsonDestination()
        assertEquals("/auth/install-preauth", result)
    }

    @Test
    fun testParseJsonDestination_withInvalidJSON() {
        val invalidJsonInput = """{"deep_link_destination":"/auth/install-preauth"""
        val result = invalidJsonInput.parseJsonDestination()
        assertNull(result)
    }

    @Test
    fun testParseJsonDestination_withEmptyDestinationValue() {
        val jsonInput = """{"deep_link_destination":""}"""
        val result = jsonInput.parseJsonDestination()
        assertEquals("", result)
    }

    @Test
    fun testParseJsonDestination_withMissingDeepLinkDestinationKey() {
        val jsonInput = """{"other_key":"value"}"""
        val result = jsonInput.parseJsonDestination()
        assertNull(result)
    }

    @Test
    fun testParseNormalDestination_withSimpleKeyValueFormat() {
        val normalInput = "deep_link_destination=/property/tt1ysu65br0a1u0up45b4awx__af_channel=share"
        val result = normalInput.parseNormalDestination()
        assertEquals("/property/tt1ysu65br0a1u0up45b4awx", result)
    }

    @Test
    fun testParseNormalDestination_withFullParameterString() {
        val normalInput = "deep_link_destination=/property/tt1ysu65br0a1u0up45b4awx__af_channel=share__af_referrer_customer_id=gesiws1opky86sz97o6wvo9m__af_referrer_name=Mahmoud Elshamy__username=shamyyoun"
        val result = normalInput.parseNormalDestination()
        assertEquals("/property/tt1ysu65br0a1u0up45b4awx", result)
    }

    @Test
    fun testParseNormalDestination_withOnlyDestinationValue() {
        val normalInput = "deep_link_destination=/auth/install-preauth"
        val result = normalInput.parseNormalDestination()
        assertEquals("/auth/install-preauth", result)
    }

    @Test
    fun testParseNormalDestination_returnsNullWhenDestinationKeyNotFound() {
        val normalInput = "af_channel=share__af_referrer_customer_id=gesiws1opky86sz97o6wvo9m"
        val result = normalInput.parseNormalDestination()
        assertNull(result)
    }

    @Test
    fun testParseNormalDestination_withEmptyString() {
        val result = "".parseNormalDestination()
        assertNull(result)
    }

    @Test
    fun testParseNormalDestination_withDestinationAtDifferentPosition() {
        val normalInput = "af_channel=share__deep_link_destination=/home__af_referrer_customer_id=test"
        val result = normalInput.parseNormalDestination()
        assertEquals("/home", result)
    }
}
