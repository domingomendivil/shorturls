package shorturls.constants;

/**
 * Constants used for configuring the creation of short URLs.
 * 
 *
 */
public class Constants {
	/**
	 * The base URL is the first part of the short URL. 
	 * E.g. for the following short URL: "http://me.li/zXKafl0" the baseURL is "http://me.li"
	 */
    public static final String BASE_URL="BASE_URL";
    

    /**
     * Factory class used for instantiating the events class 
     * responsible for sending events
     * 
     */

    public static final String EVENTS_FACTORY="EVENTS_FACTORY";
    
    /**
     * Alphabet used when generating codes for short URLs.
     */
    public static final String RANDOM_ALPHABET="RANDOM_ALPHABET";
    

    /**
     * Legth of the Random code generated for short URLs
     */
    public static final String RANDOM_LENGTH="RANDOM_LENGTH";
    
    /**
     * Writer Factory class used for instantiating the Writer data layer. 
     */
    public static final String WRITER_FACTORY="WRITER_FACTORY";

    /**
     * Indicates whether cache is enabled for used or not
     */
    public static final String CACHE_ENABLED="CACHE_ENABLED";
    
    
    /**
     * Deleter Factory class used for instantiating the Deleter data layer
     */
    
    public static final String QUERY_FACTORY="QUERY_FACTORY";
    
    /**
     * Cache Factory class used for instantiating the Cache layer
     */
    public static final String CACHE_FACTORY="CACHE_FACTORY";

    /**
     * Deleter Factory class used for instantiating the Deleter data layer
     */
    public static final String DELETER_FACTORY="DELETER_FACTORY";
    
    /**
     * Indicates whether short urls will only be obtained from cache
     * or not. If false, short urls will go to database
     */
    public static final String CACHE_ONLY="CACHE_ONLY";
 
}
