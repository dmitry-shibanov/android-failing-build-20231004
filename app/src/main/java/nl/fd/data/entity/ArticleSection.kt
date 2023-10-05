package nl.fd.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
enum class ArticleSection(val sectionName: String, val path: String): Parcelable {
    ECONOMY(
        "economie",
        "/economie"),
    CORPORATE(
        "bedrijfsleven",
        "/bedrijfsleven"),
    POLITICS(
        "politiek",
        "/politiek"),
    FINANCIAL_MARKETS(
        "financiele-markten",
        "/financiele-markten"),
    SOCIETY(
        "samenleving",
        "/samenleving"),
    TECH_AND_INNOVATION(
        "tech-en-innovatie",
        "/tech-en-innovatie"),
    OPINION(
        "opinie",
        "/opinie");

    constructor() : this("", "")

    companion object {
        @JvmStatic
        fun ofPath(path: String): Optional<ArticleSection> {
            return Arrays.stream(values())
                .filter { section: ArticleSection -> section.path == path }
                .findFirst()
        }

        @JvmStatic
        fun ofName(name: String): Optional<ArticleSection> {
            return Arrays.stream(values())
                .filter { section: ArticleSection -> section.sectionName == name }
                .findFirst()
        }
    }
}