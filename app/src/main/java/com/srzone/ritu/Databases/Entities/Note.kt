package com.srzone.ritu.Databases.Entities


class Note {
    var date: String? = null
    var id: Int = 0
    var note: String? = null

    constructor()

    constructor(str: String?, str2: String?) {
        this.date = str
        this.note = str2
    }
}
