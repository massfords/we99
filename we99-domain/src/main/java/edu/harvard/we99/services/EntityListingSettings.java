package edu.harvard.we99.services;

/**
 * @author mford
 */
public class EntityListingSettings {
    public static final int PAGE_SIZE = 100;

    public static int pageSize() {
        return PAGE_SIZE;
    }

    public static int pageToFirstResult(int page) {
        return page * PAGE_SIZE;
    }

    private EntityListingSettings() {}

}
