package com.challenge.code.tmgtwittertweets;

import android.app.Application;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.challenge.code.tmgtwittertweets.repository.TwitterRepository;
import com.challenge.code.tmgtwittertweets.viewmodel.TwitterViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TwitterViewModelTest {
    private TwitterViewModel viewModel;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Mock
    TwitterRepository repo;

    @Mock
    Application application;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        viewModel = new TwitterViewModel(repo, application);
    }

    @Test
    public void SearchTwitterTest_ProcessingUpdated(){
        viewModel.SearchTwitter("keyword");
        assertEquals(Boolean.TRUE, viewModel.getObservableProcessingStatus().getValue());
    }

    @Test
    public void SearchTwitterTest_RepositoryGetValue(){
        viewModel.SearchTwitter("keyword");
        Mockito.verify(repo).searchTwitterByKeyword("keyword");
    }
}