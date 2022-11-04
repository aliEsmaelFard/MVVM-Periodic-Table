package com.alief.periodictable.model

data class ElementItem(
    val atomicMass: String,
    val atomicNumber: Int,
    val atomicRadius: Int,
    val block: String,
    val boilingPoint: Int,
    val bondingType: String,
    val cpkHexColor: String,
    val density: Double,
    val electronAffinity: Int,
    val electroEgativity: Double,
    val electronicConfiguration: String,
    val group: Int,
    val groupBlock: String,
    val ionRadius: String,
    val ionizationEnergy: Int,
    val meltingPoint: Int,
    val name: String,
    val oxidationStates: Int,
    val period: Int,
    val standardState: String,
    val symbol: String,
    val vanDerWaalsRadius: Int,
    val yearDiscovered: Int
)