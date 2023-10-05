package nl.fd.data.entity.article;

public interface ContentComparable {

    /**
     * Does `other` refer to the same content item?
     * @param other
     * @return
     */
    boolean sameItem(Object other);

    /**
     * Is the content of `other` exactly the same?
     * @param other
     * @return
     */
    boolean sameContents(Object other);

}
