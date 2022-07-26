package org.openlmis.core.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.inject.AbstractModule;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlmis.core.LMISTestRunner;
import org.openlmis.core.R;
import org.openlmis.core.exceptions.ViewNotMatchException;
import org.openlmis.core.model.RnRForm;
import org.openlmis.core.model.repository.PTVRepository;
import org.openlmis.core.model.repository.RnrFormRepository;
import org.robolectric.RuntimeEnvironment;


import roboguice.RoboGuice;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

@RunWith(LMISTestRunner.class)
public class PTVRequisitionPresenterTest {

    private PTVRequisitionPresenter ptvRequisitionPresenter;
    private PTVRequisitionPresenter.PTVRequisitionView ptvRequisitionView;
    private PTVRepository ptvRepository;
    private RnrFormRepository rnrFormRepository;

    @Before
    public void setUp() throws ViewNotMatchException {
        ptvRequisitionPresenter = RoboGuice.getInjector(RuntimeEnvironment.application).getInstance(PTVRequisitionPresenter.class);
        ptvRequisitionView = mock(PTVRequisitionPresenter.PTVRequisitionView.class);
        ptvRepository = mock(PTVRepository.class);
        rnrFormRepository = mock(RnrFormRepository.class);
        RoboGuice.overrideApplicationInjector(RuntimeEnvironment.application, new MyTestModule());
        ptvRequisitionPresenter.attachView(ptvRequisitionView);
    }

    @Test
    public void shouldUpdateFormUISuccess() {
        ptvRequisitionPresenter.rnRForm = createRnrForm(RnRForm.Emergency.No);
        ptvRequisitionPresenter.updateFormUI();

        assertThat(ptvRequisitionPresenter.ptvReportViewModel.services.size(), is(0));
        assertThat(ptvRequisitionPresenter.getCompleteErrorMessage(), is(R.string.hint_ptv_complete_failed));
    }

    @NonNull
    private RnRForm createRnrForm(RnRForm.Emergency emergency) {
        RnRForm rnRForm = new RnRForm();
        rnRForm.setEmergency(emergency.Emergency());
        return rnRForm;
    }

    private class MyTestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(PTVRepository.class).toInstance(ptvRepository);
            bind(RnrFormRepository.class).toInstance(rnrFormRepository);
        }
    }
}
