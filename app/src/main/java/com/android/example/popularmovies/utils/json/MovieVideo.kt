package com.android.example.popularmovies.utils.json

import android.net.Uri
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

private const val YOUTUBE_URL_BASE = "https://www.youtube.com/watch?v="
class MovieVideo {
    var id: String? = null
    var name: String? = null
    var key: String? = null
    var size = 0
    var type: String? = null
    var site: String? = null
    var isoIdentifier: String? = null

    constructor()
    constructor(id: String, name: String, key: String, size: Int, type: String, site: String, isoIdentifier: String) {
        this.id = id
        this.name = name
        this.key = key
        this.size = size
        this.type = type
        this.site = site
        this.isoIdentifier = isoIdentifier
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as MovieVideo
        return size == that.size && id == that.id && name == that.name && key == that.key &&
                type == that.type && site == that.site && isoIdentifier == that.isoIdentifier
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name, key, size, type, site, isoIdentifier)
    }

    //TODO: Parse Uris for sites other than YouTube
    val videoUri: Uri
        get() {
            if (site!!.toLowerCase() == "YouTube".toLowerCase()) {
                return Uri.parse(YOUTUBE_URL_BASE + key)
            } //TODO: Parse Uris for sites other than YouTube
            throw RuntimeException("Parsing videos from sites other than YouTube not implemented")
        }

    class VideoAdapter {
        @FromJson
        fun videoFromJson(videoMap: Map<String?, String>): MovieVideo {
            val video = MovieVideo()
            video.isoIdentifier = (videoMap[ISO_LANG] + ISO_DIVIDER
                    + videoMap[ISO_COUNTRY])
            video.id = videoMap[ID]
            video.key = videoMap[KEY]
            video.name = videoMap[NAME]
            video.site = videoMap[SITE]
            video.size = requireNotNull(videoMap[SIZE]).toInt()
            video.type = videoMap[TYPE]
            return video
        }

        @ToJson
        fun videoToJson(video: MovieVideo): Map<String, String?> {
            val map = HashMap<String, String?>()
            val isoIdentifiers = video.isoIdentifier!!.split(ISO_DIVIDER.toString().toRegex()).toTypedArray()
            map[ID] = video.id
            map[ISO_LANG] = isoIdentifiers[0]
            map[ISO_COUNTRY] = isoIdentifiers[1]
            map[KEY] = video.key
            map[NAME] = video.name
            map[SITE] = video.site
            map[SIZE] = video.size.toString()
            map[TYPE] = video.type
            return map
        }

        companion object {
            const val ISO_LANG = "iso_639_1"
            const val ISO_COUNTRY = "iso_3166_1"
            const val ID = "id"
            const val KEY = "key"
            const val NAME = "name"
            const val SITE = "site"
            const val SIZE = "size"
            const val TYPE = "type"
            const val ISO_DIVIDER = '_'
        }
    }
}