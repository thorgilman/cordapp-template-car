package com.template.states

import com.template.contracts.CarContract
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.CordaX500Name
import net.corda.testing.node.MockServices
import net.corda.testing.node.ledger
import org.junit.Test

class StateTests {
    private val ledgerServices = MockServices(listOf("com.template"))

    class DummyCommand : TypeOnlyCommandData()

    @Test
    fun mustIncludeIssueCommand() {


    }


}