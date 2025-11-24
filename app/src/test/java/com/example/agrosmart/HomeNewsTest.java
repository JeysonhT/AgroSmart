package com.example.agrosmart;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.example.agrosmart.FakeHomeViewModel;
import com.example.agrosmart.HomeFragmentFactory;
import com.example.agrosmart.presentation.ui.fragment.Home_Fragment;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@Config(sdk = 36)
@RunWith(RobolectricTestRunner.class)
public class HomeNewsTest {

    @Test
    public void testHomeFragmentUIAndRecyclerView() {
        FakeHomeViewModel fakeViewModel = new FakeHomeViewModel();

        FragmentScenario<Home_Fragment> scenario = FragmentScenario.launchInContainer(
                Home_Fragment.class,
                null,
                R.style.Theme_AgroSmart,
                new HomeFragmentFactory(fakeViewModel)
        );

        scenario.onFragment(fragment -> {
            View view = fragment.getView();
            Assert.assertNotNull(view);

            // RecyclerView
            RecyclerView recyclerView = view.findViewById(R.id.noticeRecyclerView);
            Assert.assertNotNull(recyclerView);
            Assert.assertNotNull(recyclerView.getAdapter());
            Assert.assertEquals(1, recyclerView.getAdapter().getItemCount());

            // Botones
            Assert.assertNotNull(view.findViewById(R.id.localNews));
        });
    }
}
