package eu.monniot.memoArcher.db;

import android.provider.BaseColumns;

/**
 * This class describe a database entry and should match the {@link eu.monniot.memoArcher.Bow} model
 * 
 * @author François
 */
public final class LandmarkEntry implements BaseColumns {

    // Prevent the BowEntry class from being instantiated
    private LandmarkEntry() {}

    /**
     * The table name offered by this provider
     */
    public static final String TABLE_NAME = "landmarks";
    
    /*
     * Column definitions
     */
    
    public static final String COLUMN_NAME_BOW_ID = "bow_id";
    public static final String COLUMN_NAME_MARK = "mark";
    public static final String COLUMN_NAME_DISTANCE = "distance";


    /**
     * The default sort order for this table
     */
    public static final String DEFAULT_SORT_ORDER = "_ID DESC";


}
