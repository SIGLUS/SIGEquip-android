package org.openlmis.core.persistence.migrations;

import org.openlmis.core.persistence.Migration;

public class AddIsKitColumnToProduct extends Migration {

    @Override
    public void up() {
        // The default value should be set as 0 instead of false. Updated it in the later migration
        execSQL("ALTER TABLE 'products' ADD COLUMN isKit BOOLEAN DEFAULT false");
    }
}
