package nl.fd.data.entity.home;

public enum HomeSectionType {
    /**
     * Used for categories like BEURS, FUTURES and PERSOONLIJK.
     */
    DEFAULT,

    /**
     * Used for the NEWS category.
     */
    COMPACT,

    /**
     * This section is only present when there is breaking news to be displayed at the top of the home page.
     */
    BREAKING,

    /**
     * The opening section of the home page, usually contains 2 desked teasers.
     */
    OPENING,

    /**
     * The highlighted section of the home page, contains either 3, 6 or 9 desked teasers.
     */
    HIGHLIGHTED,

    /**
     * Section that simply contains one ad unit.
     */
    SINGLE_AD_UNIT,

    /**
     * An advertorial section, there can be more than one of these sections present on the home page at once.
     */
    PARTNERS,

    /**
     * Custom category-like sections on the home page that can be manually desked, be given a manual title and a url.
     */
    SPECIALS,

    /**
     * A section of podcasts with their own type of cards on the home page that can be manually desked.
     */
    PODCASTS,

    /**
     * A section of newsletters with their own type of cards on the home page.
     */
    NEWSLETTERS
}
