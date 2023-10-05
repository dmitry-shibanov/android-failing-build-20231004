package nl.fd.ui.activity;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

/**
 * Android 8 (SDK 26) requires that all apps that use the ContentResolver / ContentObserver specify
 * a ContentProvider. Even if we don't use it, we still need to declare a provider in the manifest.
 * From the release notes:
 *
 * <p><em>
 * Content change notifications
 * Android 8.0 (API level 26) changes how ContentResolver.notifyChange() and registerContentObserver(Uri, boolean, ContentObserver) behave for apps targeting Android 8.0.
 *
 * These APIs now require that a valid ContentProvider is defined for the authority in all Uris. Defining a valid ContentProvider with relevant permissions will help defend your app against content changes from malicious apps, and prevent you from leaking potentially private data to malicious apps.
 * </em></p>
 */
public class FDContentProvider extends ContentProvider {

    public static final String NOT_YET_IMPLEMENTED = "Not yet implemented";

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException(NOT_YET_IMPLEMENTED);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException(NOT_YET_IMPLEMENTED);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException(NOT_YET_IMPLEMENTED);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.
        throw new UnsupportedOperationException(NOT_YET_IMPLEMENTED);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException(NOT_YET_IMPLEMENTED);
    }
}
