package org.openlmis.core.presenter;

import com.google.inject.Inject;

import org.joda.time.DateTime;
import org.openlmis.core.LMISApp;
import org.openlmis.core.R;
import org.openlmis.core.exceptions.LMISException;
import org.openlmis.core.model.Period;
import org.openlmis.core.model.ProgramDataForm;
import org.openlmis.core.model.ProgramDataFormBasicItem;
import org.openlmis.core.model.repository.ProgramBasicItemsRepository;
import org.openlmis.core.model.repository.ProgramDataFormRepository;
import org.openlmis.core.model.repository.ProgramRepository;
import org.openlmis.core.utils.Constants;
import org.openlmis.core.view.BaseView;
import org.openlmis.core.view.viewmodel.RapidTestReportViewModel;

import java.util.Calendar;
import java.util.List;

import lombok.Getter;
import roboguice.inject.ContextSingleton;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@ContextSingleton
public class RapidTestReportFormPresenter extends BaseReportPresenter {
    @Inject
    ProgramDataFormRepository programDataFormRepository;

    @Inject
    ProgramBasicItemsRepository programBasicItemsRepository;

    @Inject
    ProgramRepository programRepository;

    @Getter
    protected RapidTestReportViewModel viewModel;

    @Override
    public void attachView(BaseView v) {

    }

    public Observable<RapidTestReportViewModel> loadViewModel(final long formId, final Period period) {
        return Observable.create((Observable.OnSubscribe<RapidTestReportViewModel>) subscriber -> {
            try {
                if (formId == 0) {
                    Period reportPeriod = period;
                    if (LMISApp.getInstance().getFeatureToggleFor(R.bool.feature_training)) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(period.getEnd().toDate());
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int date = calendar.get(Calendar.DATE);
                        calendar.set(year, month, date, 23, 59, 59);
                        reportPeriod = new Period(period.getBegin(), new DateTime(calendar.getTime()));
                    }
                    generateNewRapidTestForm(reportPeriod);
                    saveForm();
                    List<ProgramDataFormBasicItem> basicItems = programBasicItemsRepository.createInitProgramForm(viewModel.getRapidTestForm(), reportPeriod.getEnd().toDate());
                    viewModel.setBasicItems(basicItems);
                    saveForm();
                } else {
                    convertProgramDataFormToRapidTestReportViewModel(programDataFormRepository.queryById(formId));
                }
                subscriber.onNext(viewModel);
                subscriber.onCompleted();
            } catch (LMISException e) {
                subscriber.onError(e);
                e.reportToFabric();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    private void convertProgramDataFormToRapidTestReportViewModel(ProgramDataForm programDataForm) {
        viewModel = new RapidTestReportViewModel(programDataForm);
    }

    private void generateNewRapidTestForm(Period period) {
        viewModel = new RapidTestReportViewModel(period);
        viewModel.setStatus(RapidTestReportViewModel.Status.INCOMPLETE);
    }

    public Observable<RapidTestReportViewModel> onSaveDraftForm() {
        return Observable.create((Observable.OnSubscribe<RapidTestReportViewModel>) subscriber -> {
            try {
                saveForm();
                subscriber.onNext(viewModel);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<RapidTestReportViewModel> onAuthoriseDraftForm() {
        return Observable.create((Observable.OnSubscribe<RapidTestReportViewModel>) subscriber -> {
            try {
                saveForm();
                syncService.requestSyncImmediatelyByTask();
                subscriber.onNext(viewModel);
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public void saveForm() {
        try {
            viewModel.convertFormViewModelToDataModel(programRepository.queryByCode(Constants.RAPID_TEST_CODE));
            programDataFormRepository.batchCreateOrUpdate(viewModel.getRapidTestForm());
        } catch (Exception e) {
            new LMISException(e).reportToFabric();
        }
    }

    @Override
    public void deleteDraft() {
        if (viewModel.getRapidTestForm().getId() != 0L) {
            try {
                programDataFormRepository.delete(viewModel.getRapidTestForm());
            } catch (Exception e) {
                new LMISException(e).reportToFabric();
            }
        }
    }

    @Override
    public boolean isDraft() {
        return viewModel.isDraft();
    }

    public boolean isSubmitted() {
        return viewModel.isSubmitted();
    }

    public void addSignature(String sign) {
        viewModel.addSignature(sign);
    }
}
