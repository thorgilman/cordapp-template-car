package com.template.contracts


import com.template.contracts.CarContract
import com.template.states.CarState
import net.corda.core.contracts.Command
import net.corda.core.contracts.TypeOnlyCommandData
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.CordaX500Name
import net.corda.finance.POUNDS
import net.corda.finance.contracts.asset.Cash
import net.corda.testing.core.ALICE_NAME
import net.corda.testing.core.TestIdentity
import net.corda.testing.node.MockServices
import net.corda.testing.node.ledger
import org.junit.Test

class ContractTests {
    private val ledgerServices = MockServices(listOf("com.template"))

    class DummyCommand : TypeOnlyCommandData()

    @Test
    fun mustIncludeIssueCommand() {

        val bank = TestIdentity(CordaX500Name(organisation = "Dealership", locality = "New York", country = "US"))
        val dealership = TestIdentity(CordaX500Name(organisation = "Manufacturer", locality = "New York", country = "US"))
        val manufacturer = TestIdentity(CordaX500Name(organisation = "Bank of America", locality = "New York", country = "US"))
        
        val publicKeys = listOf(bank, dealership, manufacturer).map{it.publicKey}

        val carState = CarState(bank.party, dealership.party, manufacturer.party, "123vin", "123abcd", "make1", "model1", "loc1", UniqueIdentifier())

        ledgerServices.ledger {
            transaction {
                output(CarContract.ID, carState)
                command(publicKeys, CarContract.Commands.Issue())
                this.verifies()
            }
            transaction {
                output(CarContract.ID, carState)
                command(listOf(dealership.publicKey), DummyCommand())
                this.fails()
            }

        }
    }


}