package com.template.contracts


import com.template.contracts.CarContract
import com.template.states.CarState
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.CordaX500Name
import net.corda.finance.POUNDS
import net.corda.finance.contracts.asset.Cash
import net.corda.testing.node.MockServices
import net.corda.testing.node.ledger
import org.junit.Test

class ContractTests {
    private val ledgerServices = MockServices(listOf("com.template"))

    class DummyCommand : TypeOnlyCommandData()

    @Test
    fun mustIncludeIssueCommand() {

        val dealershipParty = ledgerServices.identityService.wellKnownPartyFromX500Name(CordaX500Name("Dealership","London","GB"))!!
        val manufacturerParty = ledgerServices.identityService.wellKnownPartyFromX500Name(CordaX500Name("Manufacturer","New York","US"))!!
        val bankParty = ledgerServices.identityService.wellKnownPartyFromX500Name(CordaX500Name("Bank of America","New York","C=US"))!!
        val publicKeys = listOf(dealershipParty, manufacturerParty, bankParty).map{it.owningKey}

        val carState = CarState(bankParty, dealershipParty, manufacturerParty, "123vin", "123abcd", "make1", "model1", "loc1", UniqueIdentifier())

        ledgerServices.ledger {
            transaction {

                output(CarContract.ID, carState)
                command(publicKeys, CarContract.Commands.Issue())
                this.verifies()
            }
            transaction {
                output(CarContract.ID, carState)
                command(listOf(dealershipParty.owningKey), DummyCommand())
                this.fails()
            }

        }
    }


}