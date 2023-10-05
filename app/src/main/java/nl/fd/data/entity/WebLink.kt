package nl.fd.data.entity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import nl.fd.BuildConfig
import nl.fd.data.entity.article.ArticleMetaHeader
import nl.fd.data.entity.article.Content
import nl.fd.ui.misc.FdUrlUtils
import nl.fd.ui.misc.TeaserUtils
import org.parceler.Parcels
import org.slf4j.LoggerFactory
import java.util.Arrays
import java.util.Optional
import java.util.regex.Matcher
import java.util.regex.Pattern

class WebLink() : Parcelable {
    var url: String = ""
    var isExternal = false
    var preview: Array<Content>? = null
    var articleMeta: ArticleMetaHeader? = null
    var isFree = false
    var anchor: String? = null
    var query: String? = null
    var category: ArticleCategory? = null
    var offlineFolder: String? = null

    private constructor(
        url: String,
        title: String?,
        external: Boolean?,
        free: Boolean?,
        preview: Array<Content>?,
        articleMeta: ArticleMetaHeader?,
        category: ArticleCategory?,
        offlineFolder: String?
    ) : this() {
        this.preview = preview
        isFree = java.lang.Boolean.TRUE == free
        this.category = category
        val beursMatcher = IS_BEURS_PAGE.matcher(url)
        if (beursMatcher.matches()) {
            handleBeursPage(url, beursMatcher)
        } else {
            val internalMatcher = IS_INTERNAL.matcher(url)
            if (internalMatcher.matches()) {
                handleInternalPage(url, title, external, articleMeta, internalMatcher)
            } else if (articleMeta == null) {
                handleExternalPage(url, external)
            } else {
                handleRedirectPage(url, articleMeta, external)
            }
        }
        this.offlineFolder = offlineFolder
    }

    val articleId: Long
        get() = Optional.ofNullable(articleMeta)
            .map { obj: ArticleMetaHeader -> obj.articleId }
            .orElse(0L)
    val title: String?
        get() = Optional.ofNullable(articleMeta)
            .map { obj: ArticleMetaHeader -> obj.title }
            .orElse(null)

    fun openExternally(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            context.startActivity(intent)
        } catch (e: Exception) {
            log.error("Unable to open link {} externally", url, e)
        }
    }

    private fun handleExternalPage(url: String, external: Boolean?) {
        isExternal = Optional.ofNullable(external).orElse(true)
        this.url = url
    }

    private fun handleRedirectPage(
        url: String,
        articleMeta: ArticleMetaHeader?,
        external: Boolean?
    ) {
        isExternal = Optional.ofNullable(external).orElse(true)
        this.url = url
        this.articleMeta = articleMeta
    }

    private fun handleInternalPage(
        url: String,
        title: String?,
        external: Boolean?,
        articleMeta: ArticleMetaHeader?,
        internalMatcher: Matcher
    ) {
        query = internalMatcher.group(3)
        anchor = internalMatcher.group(4)
        isExternal = Optional.ofNullable(external).orElse(false)
        this.articleMeta = Optional.ofNullable(articleMeta)
            .orElseGet { parseArticleMeta(internalMatcher.group(2), title) }
        this.url =
            if (internalMatcher.group(1) == null) BuildConfig.BASE_URL + FdUrlUtils.stripLeadingSlash(
                url
            ) else url
    }

    private fun parseArticleMeta(url: String, title: String?): ArticleMetaHeader? {
        val matcher = IS_ARTICLE.matcher(url)
        if (matcher.matches()) {
            val idString = matcher.group(1)
            val articleId = TeaserUtils.safeParseLong(idString)
            if (articleId != 0L) {
                return ArticleMetaHeader.builder()
                    .articleId(articleId)
                    .title(title)
                    .build()
            }
        }
        return null
    }

    private fun handleBeursPage(url: String?, beursMatcher: Matcher) {
        this.url = Optional.ofNullable(beursMatcher.group(2))
            .filter { path: String -> !path.startsWith("/app") }
            .map { path: String -> beursMatcher.group(1) + "/app" + path }
            .orElse(url)
        isExternal = false
    }

    val isLoginPage: Boolean
        get() {
            val matcher = IS_LOGIN.matcher(url)
            return matcher.matches()
        }

    val isMyFDPage: Boolean
        get() {
            val matcher = IS_MY_FD.matcher(url)
            return matcher.matches()
        }

    val isArticleGift: Boolean
        get() {
            if (query == null) {
                return false
            }
            val giftMatcher = IS_GIFT.matcher(query)
            return articleMeta != null && giftMatcher.matches()
        }

    override fun toString(): String {
        return "WebLink(url=" + url + ", external=" + isExternal + ", preview=" + Arrays.deepToString(
            preview
        ) + ", articleMeta=" + articleMeta + ", free=" + isFree + ", anchor=" + anchor + ", query=" + query + ", category=" + this.category + ")"
    }

    override fun writeToParcel(dest: android.os.Parcel, flags: Int) {
        dest.writeString(url)
        dest.writeInt(if (isExternal) 1 else 0)
        //Fix parcelability
        if (preview != null) {
            dest.writeInt(preview!!.size)
            for(content in preview!!) {
                dest.writeParcelable(Parcels.wrap(content), flags)
            }
        } else {
            dest.writeInt(0)
        }

        dest.writeParcelable(Parcels.wrap(articleMeta), flags)
        dest.writeInt(if (isFree) 1 else 0)
        dest.writeString(anchor)
        dest.writeString(query)
        dest.writeParcelable(Parcels.wrap(category), flags)
        dest.writeString(offlineFolder)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(o: Any?): Boolean {
        if (o === this) return true
        if (o !is WebLink) return false
        val other = o
        if (!other.canEqual(this as Any)) return false
        return url == other.url
    }

    protected fun canEqual(other: Any?): Boolean {
        return other is WebLink
    }

    override fun hashCode(): Int {
        val prime = 59
        var result = 1
        result = result * prime + url.hashCode()
        return result
    }

    class WebLinkBuilder private constructor() {
        private lateinit var url: String
        private var title: String? = null
        private var external: Boolean? = null
        private var free: Boolean? = null
        private var preview: Array<Content>? = null
        private var articleMeta: ArticleMetaHeader? = null
        private var category: ArticleCategory? = null
        private var offlineFolder: String? = null
        private fun url(url: String): WebLinkBuilder {
            this.url = url
            return this
        }

        fun title(title: String?): WebLinkBuilder {
            this.title = title
            return this
        }

        fun external(external: Boolean?): WebLinkBuilder {
            this.external = external
            return this
        }

        fun free(free: Boolean?): WebLinkBuilder {
            this.free = free
            return this
        }

        fun preview(preview: Array<Content>?): WebLinkBuilder {
            this.preview = preview
            return this
        }

        fun articleMeta(articleMeta: ArticleMetaHeader?): WebLinkBuilder {
            this.articleMeta = articleMeta
            return this
        }

        fun category(category: ArticleCategory?): WebLinkBuilder {
            this.category = category
            return this
        }

        fun offlineFolder(offlineFolder: String?): WebLinkBuilder {
            this.offlineFolder = offlineFolder
            return this
        }

        fun build(): WebLink {
            return WebLink(url, title, external, free, preview, articleMeta, category, offlineFolder)
        }

        override fun toString(): String {
            return "WebLink.WebLinkBuilder(url=" + url + ", title=" + title + ", external=" + external + ", free=" + free + ", preview=" + Arrays.deepToString(
                preview
            ) + ", articleMeta=" + articleMeta + ", category=" + this.category + ")"
        }

        companion object {
            fun builderConstructor(url: String): WebLinkBuilder {
                return WebLinkBuilder().url(url)
            }
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(WebLink::class.java)
        private val IS_BEURS_PAGE =
            Pattern.compile("(http(?:s?)://beurs(?:\\.?\\w*)\\.fd\\.nl)(.*)")
        private val IS_INTERNAL =
            Pattern.compile("^(" + BuildConfig.BASE_URL + ")?([\\w\\-/.]*)(\\?[\\w\\d\\-=&]*)?(?:#(.*))?$")
        private val IS_ARTICLE =
            Pattern.compile("^(?:/?(?:[a-zA-Z\\-]*))?/?([\\d]+)(?:/[\\w\\-/]*)?$")
        private val IS_GIFT = Pattern.compile("^\\?[\\w\\d\\-=&]*gift=[\\w]+[\\w\\d\\-=&]*$")
        private val IS_LOGIN = Pattern.compile("^" + BuildConfig.BASE_URL + "\\.app/login(.*)")
        private val IS_MY_FD = Pattern.compile("^" + BuildConfig.BASE_URL + "mijn-fd(.*)")

        @JvmStatic
        fun of(url: String): WebLink {
            return WebLink(url, null, null, null, null, null, null, null)
        }

        @JvmStatic
        fun builder(url: String): WebLinkBuilder {
            return WebLinkBuilder.builderConstructor(url)
        }

        @JvmField val CREATOR : Parcelable.Creator<WebLink>  = object : Parcelable.Creator<WebLink> {
            override fun createFromParcel(parcel: Parcel): WebLink {
                val weblink = WebLink()
                weblink.url = parcel.readString()!!
                weblink.isExternal = parcel.readInt() == 1
                var contentSize = parcel.readInt()
                if (contentSize > 0) {
                    val content = ArrayList<Content>()
                    for (i in 1..contentSize) {
                        content.add(Parcels.unwrap(parcel.readParcelable(Content::class.java.classLoader)))
                    }
                    weblink.preview = content.toTypedArray()
                }

                weblink.articleMeta = Parcels.unwrap(parcel.readParcelable(ArticleMetaHeader::class.java.classLoader))
                weblink.isFree = parcel.readInt() == 1
                weblink.anchor = parcel.readString()
                weblink.query = parcel.readString()
                weblink.category = Parcels.unwrap(parcel.readParcelable(ArticleCategory::class.java.classLoader))
                weblink.offlineFolder = parcel.readString()
                return weblink
            }

            override fun newArray(size: Int): Array<WebLink?> {
                return arrayOfNulls(size)
            }
        }
    }
}