package com.srzone.ritu.Model


class FeaturedBlog : Blog {
    var detail: String? = null

    constructor(
        str: String?,
        str2: String?,
        i: String?,
        str3: String?,
        str4: String?,
        z: Boolean
    ) : super(str, str2, i, str4, z) {
        this.detail = str3
    }

    constructor(str: String?, i: String?, str2: String?, z: Boolean) : super(str, i) {
        this.detail = str2
        this.isDark = z
    }

    constructor(str: String?, i: String?, z: Boolean) : super(str, i) {
        this.isDark = z
    }

    constructor()
}
