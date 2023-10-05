package nl.fd.data.entity

import android.os.Parcelable
import android.text.TextUtils
import kotlinx.parcelize.Parcelize

@Parcelize
class WebLinkMetadata(
    var id: String? = null,
    var bookmark: Boolean? = null,
    var commentsEnabled: Boolean? = null,
    var shareTitle: String? = null,
    var shareUrl: String? = null
) : Parcelable {

    override fun toString(): String {
        return "WebLinkMetadata(id=" + id + ", bookmark=" + bookmark + ", commentsEnabled=" + commentsEnabled + ", shareTitle=" + shareTitle + ", shareUrl=" + shareUrl + ")"
    }

    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is WebLinkMetadata) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        val `this$id`: Any? = id
        val `other$id`: Any? = other.id
        return if (if (`this$id` == null) `other$id` != null else `this$id` != `other$id`) false else true
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is WebLinkMetadata
    }

    override fun hashCode(): Int {
        val PRIME = 59
        var result = 1
        val `$id`: Any? = id
        result = result * PRIME + (`$id`?.hashCode() ?: 43)
        return result
    }

    class WebLinkMetadataBuilder internal constructor() {
        private var id: String? = null
        private var bookmark: Boolean? = null
        private var commentsEnabled: Boolean? = null
        private var shareTitle: String? = null
        private var shareUrl: String? = null
        fun id(id: String?): WebLinkMetadataBuilder {
            this.id = id
            return this
        }

        fun bookmark(bookmark: Boolean?): WebLinkMetadataBuilder {
            this.bookmark = bookmark
            return this
        }

        fun commentsEnabled(commentsEnabled: Boolean?): WebLinkMetadataBuilder {
            this.commentsEnabled = commentsEnabled
            return this
        }

        fun shareTitle(shareTitle: String?): WebLinkMetadataBuilder {
            this.shareTitle = shareTitle
            return this
        }

        fun shareUrl(shareUrl: String?): WebLinkMetadataBuilder {
            this.shareUrl = shareUrl
            return this
        }

        fun build(): WebLinkMetadata {
            return WebLinkMetadata(id, bookmark, commentsEnabled, shareTitle, shareUrl)
        }

        override fun toString(): String {
            return "WebLinkMetadata.WebLinkMetadataBuilder(id=" + id + ", bookmark=" + bookmark + ", commentsEnabled=" + commentsEnabled + ", shareTitle=" + shareTitle + ", shareUrl=" + shareUrl + ")"
        }
    }

    companion object {
        @JvmStatic
        fun updateMeta(existing: WebLinkMetadata?, update: WebLinkMetadata?): WebLinkMetadata? {
            return if (existing == null) {
                update
            } else if (existing != null && update == null) {
                existing
            } else if (TextUtils.equals(existing.id, update!!.id)) {
                builder()
                    .id(update.id)
                    .shareUrl(if (TextUtils.isEmpty(update.shareUrl)) existing.shareUrl else update.shareUrl)
                    .shareTitle(if (TextUtils.isEmpty(update.shareTitle)) existing.shareTitle else update.shareTitle)
                    .bookmark(if (update.bookmark == null) existing.bookmark else update.bookmark)
                    .commentsEnabled(if (update.commentsEnabled == null) existing.commentsEnabled else update.commentsEnabled)
                    .build()
            } else {
                update
            }
        }

        @JvmStatic
        fun builder(): WebLinkMetadataBuilder {
            return WebLinkMetadataBuilder()
        }
    }
}