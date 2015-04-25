package edu.harvard.we99.services;

/**
 * @author mford
 */
public class EntityListingSettings {

    public static int pageToFirstResult(int page, int pageSize) {
        return page * pageSize;
    }

    private EntityListingSettings() {}

}
