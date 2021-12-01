package com.svetlana.kuro.notesapp.domain.repo

import com.google.gson.annotations.SerializedName

data class MovieDBEntity(
    @SerializedName("results")
    val results: Array<MovieDBItem>?
) {
    constructor() : this(emptyArray())

    data class MovieDBItem(
        @SerializedName("id")
        val id: Int,
        @SerializedName("original_title")
        val originalTitle: String,
        @SerializedName("overview")
        val overview: String,
        @SerializedName("poster_path")
        val posterLink: String
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MovieDBEntity

        if (results != null) {
            if (other.results == null) return false
            if (!results.contentEquals(other.results)) return false
        } else if (other.results != null) return false

        return true
    }

    override fun hashCode(): Int {
        return results?.contentHashCode() ?: 0
    }
}