package com.srzone.ritu.Databases.Entities


class Likes {
    var heading: String? = null
    var id: Int = 0
    var title: String? = null

    constructor()

    constructor(i: Int, str: String?) {
        this.id = i
        this.title = str
    }

    constructor(str: String?, i: Int) {
        this.id = i
        this.heading = str
    }
}
