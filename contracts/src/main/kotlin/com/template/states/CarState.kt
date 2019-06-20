package com.template.states

import com.template.contracts.CarContract
import com.template.contracts.TemplateContract
import net.corda.core.contracts.BelongsToContract
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party

// *********
// * State *
// *********
//@BelongsToContract(TemplateContract::class)
//data class CarState(val data: String, override val participants: List<AbstractParty> = listOf()) : ContractState

@BelongsToContract(CarContract::class)
data class CarState(val owningBank: Party,
                    val holdingDealer: Party,
                    val manufacturer: Party,
                    val vin: String,
                    val licensePlateNumber: String,
                    val make: String,
                    val model: String,
                    val dealershipLocation: String,
                    val linearId: UniqueIdentifier) : ContractState {
    override val participants: List<AbstractParty> = listOf(owningBank, holdingDealer, manufacturer)
}



