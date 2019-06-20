package com.template.states

import com.template.contracts.CarContract
import groovy.util.GroovyTestCase.assertEquals
import net.corda.core.contracts.Amount
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.CordaX500Name
import net.corda.core.identity.Party
import net.corda.testing.node.MockServices
import net.corda.testing.node.ledger
import org.junit.Test

class StateTests {
    private val ledgerServices = MockServices(listOf("com.template"))

    class DummyCommand : TypeOnlyCommandData()

    @Test
    fun hasAllFieldsOfCorrectType() {
        assertEquals(CarState::class.java.getDeclaredField("owningBank").type, Party::class.java)
        assertEquals(CarState::class.java.getDeclaredField("holdingDealer").type, Party::class.java)
        assertEquals(CarState::class.java.getDeclaredField("manufacturer").type, Party::class.java)
        assertEquals(CarState::class.java.getDeclaredField("vin").type, String::class.java)
        assertEquals(CarState::class.java.getDeclaredField("licensePlateNumber").type, String::class.java)
        assertEquals(CarState::class.java.getDeclaredField("make").type, String::class.java)
        assertEquals(CarState::class.java.getDeclaredField("model").type, String::class.java)
        assertEquals(CarState::class.java.getDeclaredField("dealershipLocation").type, String::class.java)
        assertEquals(CarState::class.java.getDeclaredField("linearId").type, UniqueIdentifier::class.java)
    }
}