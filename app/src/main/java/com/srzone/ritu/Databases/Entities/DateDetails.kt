package com.srzone.ritu.Databases.Entities


class DateDetails {
    var fertileDays: String? = null
    var id: Int = 0
    var nextPeriod: String? = null
    var ovulationPeriod: String? = null
    var safeDays: String? = null

    constructor(str: String?, str2: String?, str3: String?, str4: String?) {
        this.fertileDays = str
        this.safeDays = str2
        this.nextPeriod = str3
        this.ovulationPeriod = str4
    }

    constructor()
}
