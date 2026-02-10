package fr.leboncoin.androidrecruitmenttestapp.domain.entity

data class Album(
    val id: Int,
    val title: String,
    val thumbnailUrl: String,
    val albumTag: String,
    val trackTag: String,
    val url: String,
    val isFavorite: Boolean,
)
