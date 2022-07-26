package org.openlmis.core.network;

import android.content.Context;

import org.openlmis.core.LMISApp;
import org.openlmis.core.R;

public final class SyncErrorsMap {

    private static final String PROGRAM_CONFIG_ERROR = "Program configuration missing";
    private static final String INVALID_PRODUCT_CODES = "Invalid product codes";
    private static final String PREVIOUS_FORM_NOT_FILLED = "Please finish all R&R of previous period(s)";
    private static final String USER_UNAUTHORIZED = "User does not have permission";
    private static final String ERROR_RNR_PERIOD_DUPLICATE = "RnR for this period has been submitted";
    private static final String ERROR_RNR_PERIOD_INVALID = "Submitted period is not next period in schedule";
    private static final String ERROR_RNR_FIELD_MANDATORY_NEGATIVE = "product's field is negative or null, please validate movements";
    private static final String ERROR_RNR_VALIDATION_EQUATION_NOT_EQUAL = "product quantity is not match";
    private static final String ERROR_RNR_REPORT_START_DATE_INVALID = "The report submit date must be later than the facility's reports start date";

    private SyncErrorsMap() {

    }

    public static String getDisplayErrorMessageBySyncErrorMessage(String errorMessage) {
        if (errorMessage == null) {
            return null;
        }
        Context context = LMISApp.getContext();
        if (errorMessage.equals(context.getString(R.string.hint_network_error))) {
            return null;
        }
        if (errorMessage.contains(PROGRAM_CONFIG_ERROR)) {
            return context.getString(R.string.period_configuration_missing);
        }
        if (errorMessage.contains(INVALID_PRODUCT_CODES)) {
            String[] errorString = errorMessage.split(" ");
            return context.getString(R.string.product_code_invalid, errorString[errorString.length - 1]);
        }
        if (errorMessage.contains(PREVIOUS_FORM_NOT_FILLED)) {
            return context.getString(R.string.rnr_previous_not_filled);
        }
        if (errorMessage.contains(USER_UNAUTHORIZED)) {
            return context.getString(R.string.unauthorized_operation);
        }
        if (errorMessage.contains(context.getString(R.string.sync_server_error))) {
            return context.getString(R.string.sync_server_error);
        }

        if (errorMessage.contains(ERROR_RNR_PERIOD_DUPLICATE)) {
            return context.getString(R.string.error_rnr_period_duplicate);
        }
        if (errorMessage.contains(ERROR_RNR_PERIOD_INVALID)) {
            return context.getString(R.string.error_rnr_period_invalid);
        }
        if (errorMessage.contains(ERROR_RNR_FIELD_MANDATORY_NEGATIVE)
                || errorMessage.contains(ERROR_RNR_VALIDATION_EQUATION_NOT_EQUAL)
                || errorMessage.contains(ERROR_RNR_REPORT_START_DATE_INVALID)) {
            return context.getString(R.string.error_rnr_field_mandatory_negative);
        }

        return context.getString(R.string.sync_server_error);
    }
}
