package eu.monniot.memoArcher.db;

import android.provider.BaseColumns;

/**
 * This class describe a database entry and should match the {@link eu.monniot.memoArcher.Bow} model
 * 
 * @author François
 */
public final class BowEntry implements BaseColumns {

    // Prevent the BowEntry class from being instantiated
    private BowEntry() {}

    /**
     * The table name offered by this provider
     */
    public static final String TABLE_NAME = "bows";
    
    /*
     * Column definitions
     */
    
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_MARK_UNIT = "mark_unit";
    public static final String COLUMN_NAME_DISTANCE_UNIT = "distance_unit";


    /**
     * The default sort order for this table
     */
    public static final String DEFAULT_SORT_ORDER = "_ID DESC";


}
