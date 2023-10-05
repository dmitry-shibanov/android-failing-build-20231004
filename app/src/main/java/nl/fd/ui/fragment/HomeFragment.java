package nl.fd.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.util.CollectionUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.disposables.Disposable;
import lombok.extern.slf4j.Slf4j;
import nl.fd.app.FdApplication;
import nl.fd.data.entity.DateTimeDeserializer;
import nl.fd.data.entity.card.Card;
import nl.fd.data.entity.card.CardDeserializer;
import nl.fd.data.entity.card.TeaserGroup;
import nl.fd.data.entity.card.teaser.Teaser;
import nl.fd.data.entity.home.HomeSection;
import nl.fd.data.entity.home.HomeSectionType;
import nl.fd.data.entity.home.HomeSectionsResponse;
import nl.fd.ui.adapter.CardListAdapter;
import nl.fd.ui.adapter.HomeAdapter;

@Slf4j
public class HomeFragment extends AbstractCardListFragment {

    private Disposable stockEventSubscription;

    Context context;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected CardListAdapter createAdapter(Activity activity) {
        return new HomeAdapter(activity);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        FdApplication.get(context).component().inject(HomeFragment.this);
        super.onAttach(context);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        var res = super.onCreateView(inflater, container, savedInstanceState);

        return res;
    }

    @Override
    protected Single<HomeSectionsResponse> doFetchData() {
        Single<HomeSectionsResponse> single = Single.create(new SingleOnSubscribe<HomeSectionsResponse>() {
                    @Override
                    public void subscribe(SingleEmitter<HomeSectionsResponse> emitter) throws Exception {
                        HomeSectionsResponse response = null;
                        try (InputStream input = context.getAssets().open("home_response.json")){
                            String text = new BufferedReader(
                                    new InputStreamReader(input, StandardCharsets.UTF_8))
                                    .lines()
                                    .collect(Collectors.joining("\n"));
                            GsonBuilder gsonBuilder = new GsonBuilder()
                            .registerTypeAdapter(Card.class, new CardDeserializer())
                            .registerTypeAdapter(DateTime.class, new DateTimeDeserializer());

                            Gson gson = gsonBuilder.create();

                            response = gson.fromJson(text, HomeSectionsResponse.class);
                        } catch (IOException e) {

                        }
                        emitter.onSuccess(response);
                    }
                });
        return single;
    }

    @Override
    protected List<Card> extractData(HomeSectionsResponse response) {
        // Only categories defined in ArticleCategory enum are supported
        List<Card> filteredTeaserList = new ArrayList<>();

        for (HomeSection section: response.getSections()) {
            if (!CollectionUtils.isEmpty(section.getCardList()))  {
                addSectionToListIfRequired(filteredTeaserList, section);
            }
        }
        return filteredTeaserList;
    }

    private void addSectionToListIfRequired(List<Card> filteredTeaserList, HomeSection section) {
        switch (section.getType() != null ? section.getType() : HomeSectionType.DEFAULT) {
            case OPENING:
            case HIGHLIGHTED:
            case SPECIALS:
            case DEFAULT:
            case PODCASTS:
                filteredTeaserList.add(new TeaserGroup(section.getType().name(), section.getType(), convertCardListToTeaserList(section.getCardList()), section.getAnalyticsSectionId()));
                break;
            default:
        }
    }

    @NonNull
    private List<Teaser> convertCardListToTeaserList(List<Card> cardList) {
        return cardList.stream().filter(Teaser.class::isInstance).map(Teaser.class::cast).collect(Collectors.toList());
    }

    @Override
    public void onResume() {
        super.onResume();
        Optional.ofNullable(adapter)
                .ifPresent(CardListAdapter::inView);
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseStockTicker();
        Optional.ofNullable(adapter)
                .ifPresent(CardListAdapter::outOfView);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Optional.ofNullable(adapter)
                    .ifPresent(CardListAdapter::outOfView);
        } else {
            Optional.ofNullable(adapter)
                    .ifPresent(CardListAdapter::inView);
        }
    }

    private void pauseStockTicker() {
        if (stockEventSubscription != null && !stockEventSubscription.isDisposed()) {
            log.debug("StockTicker was on, stopping!");
            stockEventSubscription.dispose();
            stockEventSubscription = null;
        }
    }

}
