package com.template

import com.template.contracts.CarContract
import com.template.flows.CarIssueInitiator
import com.template.flows.CarIssueResponder
import com.template.states.CarState
import net.corda.core.identity.CordaX500Name
import net.corda.core.transactions.SignedTransaction
import net.corda.core.utilities.getOrThrow
import net.corda.testing.internal.chooseIdentityAndCert
import net.corda.testing.node.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class FlowTests {
    lateinit var mockNetwork: MockNetwork
    lateinit var bank: StartedMockNode
    lateinit var dealership: StartedMockNode
    lateinit var manufacturer: StartedMockNode

    @Before
    fun setup() {
        mockNetwork = MockNetwork(listOf("com.template"),
                notarySpecs = listOf(MockNetworkNotarySpec(CordaX500Name("Notary","London","GB"))))
        bank = mockNetwork.createNode(MockNodeParameters(legalName = CordaX500Name(organisation = "Dealership", locality = "New York", country = "US")))
        dealership = mockNetwork.createNode(MockNodeParameters(legalName = CordaX500Name(organisation = "Manufacturer", locality = "New York", country = "US")))
        manufacturer = mockNetwork.createNode(MockNodeParameters(legalName = CordaX500Name(organisation = "Bank of America", locality = "New York", country = "US")))

        val startedNodes = arrayListOf(bank, dealership, manufacturer)
        // For real nodes this happens automatically, but we have to manually register the flow for tests
        startedNodes.forEach {
            it.registerInitiatedFlow(CarIssueResponder::class.java)
        }
        mockNetwork.runNetwork()
    }

    @After
    fun tearDown() {
        mockNetwork.stopNodes()
    }

    @Test
    fun flowReturnsCorrectlyFormedPartiallySignedTransaction() {
        val bankParty = bank.info.chooseIdentityAndCert().party
        val dealershipParty = dealership.info.chooseIdentityAndCert().party
        val manufacturerParty = manufacturer.info.chooseIdentityAndCert().party

        val flow = CarIssueInitiator(bankParty, dealershipParty, manufacturerParty, "123vin", "123abcd", "make1", "model1", "loc1")
        val future = manufacturer.startFlow(flow)

        mockNetwork.runNetwork()
        // Return the unsigned(!) SignedTransaction object from the IOUIssueFlow.
        val ptx: SignedTransaction = future.getOrThrow()
        // Print the transaction for debugging purposes.
        println(ptx.tx)
        // Check the transaction is well formed...
        // No outputs, one input IOUState and a command with the right properties.
        assert(ptx.tx.inputs.isEmpty())
        assert(ptx.tx.outputs.single().data is CarState)

        val carState = ptx.tx.outputStates.single() as CarState
        val command = ptx.tx.commands.single()
        assert(command.value is CarContract.Commands.Issue)
        assert(command.signers.toSet() == carState.participants.map { it.owningKey }.toSet())
        ptx.verifyRequiredSignatures()
    }
}