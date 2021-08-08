package com.soban.draslaafun

data class Model_pharmacyProductList (
    val productImage: String = "",
    val productName: String = "",
    val productUsage: String = "",
    val productPrice: String = "",
    val productCuttedPrice: String = "",
    val productDiscount: String = "",
    val productIndicatio: String = "",
    val directionofUse: String = "",
    val productFormula: String = "",
    val type: String = "",
    var isExpanded: Boolean = true
        )