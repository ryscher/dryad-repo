/*
 */
package org.datadryad.journalstatistics.extractor;

import java.sql.SQLException;
import org.datadryad.api.DryadDataPackage;
import org.datadryad.test.ContextUnitTest;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dan Leehr <dan.leehr@nescent.org>
 */
public class DataPackageCountTest extends ContextUnitTest{
    /**
     * Test of extract method, of class DataPackageCount.
     * Data packages are items in collection identified by 'stats.datapkgs.coll'
     * having prism.publicationName as provided
     */
    @Test
    public void testCountDataPackages() throws SQLException {
        // Count the initial number of data packages
        String journalName = "Test Journal";
        DataPackageCount instance = new DataPackageCount(this.context);
        Integer initialCount = instance.extract(journalName);
        // Create a new data package, and assert the count goes up by one
        DryadDataPackage dataPackage = DryadDataPackage.create(context);
        dataPackage.setPublicationName(journalName);
        Integer expResult = initialCount + 1;
        Integer result = instance.extract(journalName);
        assertEquals(expResult, result);
    }
}
