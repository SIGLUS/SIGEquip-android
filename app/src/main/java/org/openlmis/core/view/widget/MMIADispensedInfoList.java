package org.openlmis.core.view.widget;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.openlmis.core.R;
import org.openlmis.core.model.BaseInfoItem;
import org.openlmis.core.presenter.MMIARequisitionPresenter;
import org.roboguice.shaded.goole.common.collect.FluentIterable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.openlmis.core.utils.Constants.ATTR_TABLE_DISPENSED_DS5;
import static org.openlmis.core.utils.Constants.ATTR_TABLE_DISPENSED_DS4;
import static org.openlmis.core.utils.Constants.ATTR_TABLE_DISPENSED_DS3;
import static org.openlmis.core.utils.Constants.ATTR_TABLE_DISPENSED_DS2;
import static org.openlmis.core.utils.Constants.ATTR_TABLE_DISPENSED_DS1;
import static org.openlmis.core.utils.Constants.ATTR_TABLE_DISPENSED_DS;
import static org.openlmis.core.utils.Constants.ATTR_TABLE_DISPENSED_DT2;
import static org.openlmis.core.utils.Constants.ATTR_TABLE_DISPENSED_DT1;
import static org.openlmis.core.utils.Constants.ATTR_TABLE_DISPENSED_DT;
import static org.openlmis.core.utils.Constants.ATTR_TABLE_DISPENSED_DM;

public class MMIADispensedInfoList extends LinearLayout {
    private TextView dsTotal;
    private TextView dtTotal;
    private TextView dmTotal;
    private TextView withinTotal;
    private TextView total;
    private TextView adjustment;
    private List<EditText> dsLists = new ArrayList<>();
    private List<EditText> dtLists = new ArrayList<>();
    private List<EditText> dmLists = new ArrayList<>();
    private List<EditText> withinLists = new ArrayList<>();
    private List<EditText> editTexts = new ArrayList<>();
    private String ATTR_TABLE_DISPENSED_KEY;
    private List<BaseInfoItem> dataList;
    private Map<String, List<BaseInfoItem>> tableMap = new HashMap<>();
    private Map<String, String> currentInfos = new HashMap<>();
    private Map<String, String> lastInfos = new HashMap<>();

    private Map<String, String> currentAndPreviousMap = new HashMap<>();

    private MMIARequisitionPresenter presenter;

    public MMIADispensedInfoList(Context context) {
        super(context);
        init();
    }

    public MMIADispensedInfoList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void initView(List<BaseInfoItem> list, MMIARequisitionPresenter presenter) {
        this.dataList = list;
        this.presenter = presenter;
        for (BaseInfoItem item : list) {
            List<BaseInfoItem> tableList = tableMap.get(item.getTableName()) == null ? new ArrayList<>() : tableMap.get(item.getTableName());
            tableList.add(item);
            tableMap.put(item.getTableName(), tableList);
        }
        if (tableMap.get(ATTR_TABLE_DISPENSED_KEY) == null || tableMap.get(ATTR_TABLE_DISPENSED_KEY).size() ==0) return;
        init();
        getCurrentBaseInfoItems();
        getLastBaseInfoItems();
        addDispensedView(tableMap.get(ATTR_TABLE_DISPENSED_KEY));
    }

    private void init() {
        ATTR_TABLE_DISPENSED_KEY = getString(R.string.table_dispensed_key);

        currentAndPreviousMap.put(ATTR_TABLE_DISPENSED_DS5, ATTR_TABLE_DISPENSED_DS4);
        currentAndPreviousMap.put(ATTR_TABLE_DISPENSED_DS4, ATTR_TABLE_DISPENSED_DS3);
        currentAndPreviousMap.put(ATTR_TABLE_DISPENSED_DS3, ATTR_TABLE_DISPENSED_DS2);
        currentAndPreviousMap.put(ATTR_TABLE_DISPENSED_DS2, ATTR_TABLE_DISPENSED_DS1);
        currentAndPreviousMap.put(ATTR_TABLE_DISPENSED_DS1, ATTR_TABLE_DISPENSED_DS);
        currentAndPreviousMap.put(ATTR_TABLE_DISPENSED_DT2, ATTR_TABLE_DISPENSED_DT1);
        currentAndPreviousMap.put(ATTR_TABLE_DISPENSED_DT1, ATTR_TABLE_DISPENSED_DT);
    }

    private String getString(int id) {
        return getResources().getString(id);
    }


    private void getCurrentBaseInfoItems() {
        if (tableMap.get(ATTR_TABLE_DISPENSED_KEY) == null) return;
        List<BaseInfoItem> items = getDispensedBaseInfoItems(tableMap.get(ATTR_TABLE_DISPENSED_KEY));
        for (BaseInfoItem item : items) {
            currentInfos.put(item.getName(), item.getValue());
        }
    }

    private void getLastBaseInfoItems() {

        if (presenter.getLastRnrForm() == null || presenter.getLastRnrForm().getBaseInfoItemList() == null) return;
        List<BaseInfoItem> items = getDispensedBaseInfoItems(new ArrayList<>(presenter.getLastRnrForm().getBaseInfoItemList()));
        if (items == null || items.size() == 0) return;
        for (BaseInfoItem item : items) {
            lastInfos.put(item.getName(), item.getValue());
        }
    }

    public List<BaseInfoItem> getDataList() {
        return dataList;
    }

    public boolean isCompleted() {
        for (EditText editText : editTexts) {
            if (TextUtils.isEmpty(editText.getText().toString())) {
                editText.setError(getString(R.string.hint_error_input));
                editText.requestFocus();
                return false;
            }
        }
        return true;
    }

    private List<BaseInfoItem> getDispensedBaseInfoItems(List<BaseInfoItem> items) {
        return FluentIterable.from(items)
                .filter(baseInfoItem -> ATTR_TABLE_DISPENSED_KEY.equals(baseInfoItem.getTableName()))
                .toList();
    }

    private String getValue(String cellName) {
        if (lastInfos.size() > 0 && presenter.getRnRForm().isDraft()) {
            String val = lastInfos.get(currentAndPreviousMap.get(cellName));
            updateDataList(cellName, val);
            return val;
        } else {
            return currentInfos.get(cellName);
        }
    }

    private void updateDataList(String key, String value) {
        dataList = FluentIterable.from(dataList).filter(item -> {
            if (key.equals(item.getName())) {
                item.setValue(value);
            }
            return true;
        }).toList();
    }

    private void addDispensedView(List<BaseInfoItem> list) {
        sortedByDisplayOrder(list);
        initDSColumn(list);
        initDTDMColumn(list);

        withinTotal = getTextView(R.id.type_dispensed_total_within);
        total = getTextView(R.id.type_dispensed_total_total);
        adjustment = getTextView(R.id.type_dispensed_adjustment_text);

        updateTotalInfo();
    }


    private BaseInfoItem getBaseInfoItemFromListByKey(List<BaseInfoItem> list, String key) {
        for (BaseInfoItem item : list) {
            if (key.equals(item.getName())) {
                return item;
            }
        }
        return null;
    }

    private void initEditTextCell(EditText editText, List<BaseInfoItem> list, String key) {
        editText.setText(getValue(key));
        editText.addTextChangedListener(new EditTextWatcher(getBaseInfoItemFromListByKey(list, key)));
        editText.setFocusable(lastInfos.size() == 0);
        editText.setEnabled(lastInfos.size() == 0);
        if (key.startsWith(ATTR_TABLE_DISPENSED_DS)) {
            dsLists.add(editText);
        } else {
            dtLists.add(editText);
        }
        editTexts.add(editText);
    }

    private void initDSColumn(List<BaseInfoItem> list) {
        initEditTextCell(getEditText(R.id.type_dispensed_ds_from_last4), list, ATTR_TABLE_DISPENSED_DS5);
        initEditTextCell(getEditText(R.id.type_dispensed_ds_from_last3), list, ATTR_TABLE_DISPENSED_DS4);
        initEditTextCell(getEditText(R.id.type_dispensed_ds_from_last2), list, ATTR_TABLE_DISPENSED_DS3);
        initEditTextCell(getEditText(R.id.type_dispensed_ds_from_last), list, ATTR_TABLE_DISPENSED_DS2);
        initEditTextCell(getEditText(R.id.type_dispensed_ds_from_within), list, ATTR_TABLE_DISPENSED_DS1);


        EditText dsWithin = getEditText(R.id.type_dispensed_ds_within);
        BaseInfoItem item = getBaseInfoItemFromListByKey(list, ATTR_TABLE_DISPENSED_DS);
        dsWithin.setText(item.getValue());
        dsWithin.addTextChangedListener(new EditTextWatcher(item));
        dsLists.add(dsWithin);

        withinLists.add(dsWithin);
        editTexts.add(dsWithin);
        dsTotal = getTextView(R.id.type_dispensed_ds_total);
    }

    private void initDTDMColumn(List<BaseInfoItem> list) {
        initEditTextCell(getEditText(R.id.type_dispensed_dt_from_last), list, ATTR_TABLE_DISPENSED_DT2);
        initEditTextCell(getEditText(R.id.type_dispensed_dt_from_within), list, ATTR_TABLE_DISPENSED_DT1);
        EditText dtWithin = getEditText(R.id.type_dispensed_dt_within);
        BaseInfoItem dtItem = getBaseInfoItemFromListByKey(list, ATTR_TABLE_DISPENSED_DT);
        dtWithin.setText(dtItem.getValue());
        dtWithin.addTextChangedListener(new EditTextWatcher(dtItem));
        dtLists.add(dtWithin);
        withinLists.add(dtWithin);
        editTexts.add(dtWithin);
        dtTotal = getTextView(R.id.type_dispensed_dt_total);

        EditText dmWithin = getEditText(R.id.type_dispensed_dm_within);
        BaseInfoItem dmItem = getBaseInfoItemFromListByKey(list, ATTR_TABLE_DISPENSED_DM);
        dmWithin.setText(dmItem.getValue());
        dmWithin.addTextChangedListener(new EditTextWatcher(dmItem));
        dmLists.add(dmWithin);
        withinLists.add(dmWithin);
        editTexts.add(dmWithin);
        dmTotal = getTextView(R.id.type_dispensed_dm_total);
    }

    private EditText getEditText(@IdRes int id) {
        return (EditText) findViewById(id);
    }

    private TextView getTextView(@IdRes int id) {
        return (TextView) findViewById(id);
    }

    public String getWithinTotal() {
        return getTypesTotal(withinLists);
    }

    private String getTreatmentTotal() {
        return String.valueOf(Integer.parseInt(getTypesTotal(dmLists))
                + Integer.parseInt(getTypesTotal(dtLists))
                + Integer.parseInt(getTypesTotal(dsLists)));
    }

    private String getTypesTotal(List<EditText> list) {
        int sum = 0;
        if (list == null || list.size() == 0) {
            return "0";
        }
        try {
            for (EditText item : list) {
                sum += Integer.parseInt(item.getText().toString());
            }
        } catch (NumberFormatException e) {
        }

        return String.valueOf(sum);
    }

    private String getAdjustment() {
        DecimalFormat df = new DecimalFormat("#.##");
        int treatmentTotal = Integer.parseInt(getTreatmentTotal());
        int withinTotal = Integer.parseInt(getTypesTotal(withinLists));
        return df.format((treatmentTotal == 0 || withinTotal == 0) ? 0 : (treatmentTotal * 1.0) / withinTotal);
    }

    private void updateTotalInfo() {
        dsTotal.setText(getTypesTotal(dsLists));
        dtTotal.setText(getTypesTotal(dtLists));
        dmTotal.setText(getTypesTotal(dmLists));
        withinTotal.setText(getTypesTotal(withinLists));
        total.setText(getTreatmentTotal());
        adjustment.setText(getAdjustment());
    }

    class EditTextWatcher implements TextWatcher {

        private final BaseInfoItem item;

        public EditTextWatcher(BaseInfoItem item) {
            this.item = item;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            item.setValue(editable.toString());
            if (ATTR_TABLE_DISPENSED_KEY.equals(item.getTableName())) {
                updateTotalInfo();
            }
        }
    }

    private void sortedByDisplayOrder(List<BaseInfoItem> list) {
        Collections.sort(list, (o1, o2) -> o1.getDisplayOrder() - o2.getDisplayOrder());
    }

}
