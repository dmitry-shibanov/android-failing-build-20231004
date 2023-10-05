package nl.fd.ui.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import nl.fd.BuildConfig;
import nl.fd.R;
import nl.fd.data.entity.ArticleCategory;
import nl.fd.data.entity.card.Card;
import nl.fd.data.entity.card.teaser.Teaser;
import nl.fd.data.entity.home.HomeSectionsResponse;
import nl.fd.databinding.FgCardlistBinding;
import nl.fd.ui.adapter.CardListAdapter;
import nl.fd.ui.misc.ErrorSituation;
import nl.fd.ui.misc.RecyclerViewScrollHelper;

@Slf4j
public abstract class AbstractCardListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, SilentRefreshable {

    protected FgCardlistBinding binding;

    private boolean needsData = true;

    CardListAdapter adapter;


    private int scrollFinishPos;

    protected abstract CardListAdapter createAdapter(Activity activity);

    @Override
    public void onAttach(@NonNull Context context) {
        log.debug("onAttach ({})", getClass().getSimpleName());
        super.onAttach(context);
        adapter = createAdapter(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log.debug("onCreateView ({})", getClass().getSimpleName());
        binding = FgCardlistBinding.inflate(inflater);

        binding.swipeRefresh.setColorSchemeResources(R.color.primary, R.color.primary_dark);
        binding.swipeRefresh.setProgressBackgroundColorSchemeResource(R.color.background_middle);
        binding.swipeRefresh.setOnRefreshListener(this);

        var layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        binding.contentCardsRecyclerView.setLayoutManager(layoutManager);
        binding.contentCardsRecyclerView.setAdapter(adapter);
        binding.contentCardsRecyclerView.setItemAnimator(null);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        log.debug("onViewCreated ({})", getClass().getSimpleName());
        super.onViewCreated(view, savedInstanceState);
        adapter.attachToLifecycle(getViewLifecycleOwner().getLifecycle());
    }

    @Override
    public void onStart() {
        log.debug("onStart ({})", getClass().getSimpleName());
        super.onStart();
        if (adapter.shouldUpdate()) {
            needsData = true;
            loadData();
        }
    }

    @Override
    public void onResume() {
        log.debug("onResume ({}) -> isVisible {}", getClass().getSimpleName(), isVisible());
        super.onResume();
        scrollToLastViewedArticle();
    }


    protected abstract Single<HomeSectionsResponse> doFetchData();

    protected void doFetchLiveData() {}

    protected abstract List<Card> extractData(HomeSectionsResponse response);

    protected boolean useLiveData() {
        return false;
    }

    public void handleReload() {
        //Do nothing
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        log.debug("onActivityResult ({})", getClass().getSimpleName());
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void loadLiveData() {
        if (!needsData) {
            log.debug("Requesting a getHomePage, but 'needsData' was false.");
            return;
        }

        needsData = false;
        doFetchLiveData();
    }

    private void loadData() {
        if (useLiveData()) {
            loadLiveData();
        } else {
            if (!needsData) {
                log.debug("Requesting a getHomePage, but 'needsData' was false.");
                return;
            }

            needsData = false;

            var disposable = doFetchData().subscribe
                    (response -> handleSuccess(
                            extractData(response)),
                    throwable -> {
                        log.warn("Exception fetching data!", throwable);
                        handleFailure();
                    });
            compositeDisposable.add(disposable);
        }
    }

    protected void handleFailure() {
        binding.swipeRefresh.setRefreshing(false);
        needsData = true;

        binding.setError(new ErrorSituation(getString(R.string.no_live_data_message),
                button -> {
                    binding.setError(null);
                    loadData();
                }));

        Toast.makeText(getContext(), getString(R.string.try_again_later), Toast.LENGTH_LONG)

                .show();
    }

    protected void handleSuccess(List<Card> cardList) {
        adapter.updateCardList(cardList);
        handleReload();
        binding.swipeRefresh.setRefreshing(false);
        needsData = false;
    }

    private void scrollToLastViewedArticle() {
        if (scrollFinishPos != 0) {
            int cardPos = adapter.getCardPositionFromTeaserPosition(scrollFinishPos);
            log.debug("Scrolling to position {} to match finish position of article swipe", cardPos);

            if (BuildConfig.DEBUG && cardPos > -1) {
                Card scrollToCard = adapter.getCardList().get(cardPos);
                log.info("Card at target scroll position {}: {}", cardPos, scrollToCard);
            }

            RecyclerViewScrollHelper.scrollToPosition(binding.contentCardsRecyclerView, cardPos);
            scrollFinishPos = 0;
        }
    }

    @Override
    public void onRefresh() {
        binding.swipeRefresh.setRefreshing(true);
        needsData = true;
        loadData();
    }

    @Override
    public void refreshSilently() {
        refreshSilently(true);
    }

    public void refreshSilently(boolean scrollToTop) {
        if (binding != null && scrollToTop) {
            binding.contentCardsRecyclerView.smoothScrollToPosition(0); // smooth scrolling triggers `onScroll` callbacks, `scrollToPosition` doesn't
        }
        needsData = true;
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        adapter.onDestroy(this);
        needsData = true;
    }
}
