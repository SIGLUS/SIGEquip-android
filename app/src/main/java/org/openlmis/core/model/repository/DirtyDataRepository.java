package org.openlmis.core.model.repository;

import android.content.Context;

import com.google.inject.Inject;
import com.j256.ormlite.stmt.DeleteBuilder;

import org.openlmis.core.LMISApp;
import org.openlmis.core.exceptions.LMISException;
import org.openlmis.core.model.DirtyDataItemInfo;
import org.openlmis.core.persistence.DbUtil;
import org.openlmis.core.persistence.GenericDao;
import org.openlmis.core.persistence.LmisSqliteOpenHelper;
import org.openlmis.core.utils.DateUtil;
import org.roboguice.shaded.goole.common.collect.FluentIterable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DirtyDataRepository {

    private static final String TAG = DirtyDataRepository.class.getSimpleName();

    GenericDao<DirtyDataItemInfo> deleteItemInfoGenericDao;
    private Context context;

    @Inject
    DbUtil dbUtil;
    @Inject
    StockRepository stockRepository;
    @Inject
    RnrFormRepository rnrFormRepository;
    @Inject
    ProgramRepository programRepository;

    @Inject
    public DirtyDataRepository(Context context) {
        deleteItemInfoGenericDao = new GenericDao<>(DirtyDataItemInfo.class, context);
        this.context = context;
    }

    public void saveAll(List<DirtyDataItemInfo> dirtyDataItemsInfo) {
        try {
            List<DirtyDataItemInfo> itemInfos = listAll();
            Map<String, DirtyDataItemInfo> productCodeMapItem = new HashMap<>();
            for(DirtyDataItemInfo info : itemInfos) {
                productCodeMapItem.put(info.getProductCode(), info);
            }
            dbUtil.withDaoAsBatch(DirtyDataItemInfo.class, (DbUtil.Operation<DirtyDataItemInfo, Void>) dao -> {
                for (DirtyDataItemInfo item : dirtyDataItemsInfo) {
                    if (productCodeMapItem.containsKey(item.getProductCode())) {
                        DirtyDataItemInfo dbItem = productCodeMapItem.get(item.getProductCode());
                        dbItem.setFullyDelete(item.isFullyDelete());
                        dbItem.setSynced(false);
                        dbItem.setJsonData(item.getJsonData());
                        dao.createOrUpdate(dbItem);
                    } else {
                        dao.createOrUpdate(item);
                    }
                }
                return null;
            });

        } catch (LMISException e) {
            e.printStackTrace();
        }
    }

    public void save(DirtyDataItemInfo item) {
        try {
            List<DirtyDataItemInfo> itemInfos = listAll();
            Map<String, DirtyDataItemInfo> productCodeMapItem = new HashMap<>();
            for(DirtyDataItemInfo info : itemInfos) {
                productCodeMapItem.put(info.getProductCode(), info);
            }
            if (productCodeMapItem.containsKey(item.getProductCode())) {
                DirtyDataItemInfo dbItem = productCodeMapItem.get(item.getProductCode());
                dbItem.setSynced(false);
                dbItem.setJsonData(item.getJsonData());
                deleteItemInfoGenericDao.createOrUpdate(dbItem);
            } else {
                deleteItemInfoGenericDao.createOrUpdate(item);
            }

        } catch (LMISException e) {
            e.printStackTrace();
        }
    }

    private boolean hasSavedNeedUpdate(DirtyDataItemInfo fromDB, DirtyDataItemInfo fromRule) {
        return fromDB.getProductCode().equals(fromRule.getProductCode());
    }

    private List<DirtyDataItemInfo> listAll() {
        try {
            return deleteItemInfoGenericDao.queryForAll();
        } catch (LMISException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<DirtyDataItemInfo> listunSyced() {
        try {
            return FluentIterable.from(deleteItemInfoGenericDao.queryForAll())
                    .filter(dirtyDataItemInfo -> !dirtyDataItemInfo.isSynced()).toList();
        } catch (LMISException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean hasBackedData(List<DirtyDataItemInfo> infos) {
        return infos != null && infos.size() > 0;
    }

    public boolean hasOldDate() {
        Date dueDateShouldDataLivedInDB = DateUtil.dateMinusMonth(DateUtil.getCurrentDate(), 1);
        List<DirtyDataItemInfo> infos = listAll();
        if (hasBackedData(infos)) {
            for (DirtyDataItemInfo itemInfo : infos) {
                if (itemInfo.getCreatedAt().before(dueDateShouldDataLivedInDB)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteOldData() {
        Date dueDateShouldDataLivedInDB = DateUtil.dateMinusMonth(DateUtil.getCurrentDate(), 1);
        try {
            dbUtil.withDao(DirtyDataItemInfo.class, dao -> {
                DeleteBuilder<DirtyDataItemInfo, String> deleteBuilder = dao.deleteBuilder();
                deleteBuilder.where().le("createdAt", dueDateShouldDataLivedInDB);
                deleteBuilder.delete();
                return null;
            });
        } catch (LMISException e) {
            e.printStackTrace();
        }
    }

    public void deleteDraftForDirtyData() {
        String deleteDraftLotItems = "DELETE FROM draft_lot_items";
        String deleteDraftInventory = "DELETE FROM draft_inventory";
        LmisSqliteOpenHelper.getInstance(LMISApp.getContext()).getWritableDatabase().execSQL(deleteDraftLotItems);
        LmisSqliteOpenHelper.getInstance(LMISApp.getContext()).getWritableDatabase().execSQL(deleteDraftInventory);
    }

}
