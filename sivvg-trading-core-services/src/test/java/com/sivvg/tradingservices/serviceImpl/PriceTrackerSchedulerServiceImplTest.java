package com.sivvg.tradingservices.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.repository.DailyTipHistoryRepository;
import com.sivvg.tradingservices.repository.DailyTipRepository;
import com.sivvg.tradingservices.service.MarketHolidayService;
import com.sivvg.tradingservices.service.NotificationService;
import com.sivvg.tradingservices.service.YahooFinanceClient;
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)

public class PriceTrackerSchedulerServiceImplTest {

    @Mock
    private YahooFinanceClient yahooFinanceClient;

    @Mock
    private DailyTipRepository tipRepository;

    @Mock
    private DailyTipHistoryRepository historyRepo;

    @Mock
    private NotificationService notificationService;

    @Mock
    private MarketHolidayService marketHolidayService;

    @Spy
    @InjectMocks
    private PriceTrackerSchedulerServiceImpl scheduler;

    private DailyTipEntity tip;

    @BeforeEach
    public  void setup() {
        tip = new DailyTipEntity();
        tip.setStockCode("TCS");
        tip.setCategory("EQUITY");
        tip.setT1(110.0);
        tip.setT1Reached(false);
    }

    // 1️⃣ Holiday → Skip
    @Test
    public  void trackPrices_shouldSkipWhenHoliday() {
        when(marketHolidayService.isMarketHoliday()).thenReturn(true);

        scheduler.trackPrices();

        verifyNoInteractions(tipRepository, yahooFinanceClient, notificationService);
    }

    // 2️⃣ Outside Market Hours → Skip
    @Test
    public  void trackPrices_shouldSkipOutsideMarketHours() {

        doReturn(LocalTime.of(8, 30)).when(scheduler).getCurrentTime();
        when(marketHolidayService.isMarketHoliday()).thenReturn(false);

        scheduler.trackPrices();

        verifyNoInteractions(tipRepository, yahooFinanceClient, notificationService);
    }

    // 3️⃣ No Tips → Notification
    @Test
    void trackPrices_shouldSendNoTipsNotification() {

        doReturn(LocalTime.of(10, 0)).when(scheduler).getCurrentTime();
        when(marketHolidayService.isMarketHoliday()).thenReturn(false);
        when(tipRepository.findAll()).thenReturn(List.of());

        scheduler.trackPrices();

        verify(notificationService)
                .sendGeneralNotification("NO_TIPS", "Today no tips provided");
    }

    @Test
    public  void trackPrices_shouldHitT1Target() {

        DailyTipEntity tip = new DailyTipEntity();

        tip.setStockCode("TCS");
        tip.setCategory("EQUITY");
        tip.setStockType("BUY");

        tip.setStockPrice(100.0);     // 🔴 IMPORTANT
        tip.setCurrentPrice(110.0);   // 🔴 IMPORTANT

        tip.setT1(115.0);
        tip.setT1Reached(false);

        doReturn(LocalTime.of(10, 0)).when(scheduler).getCurrentTime();
        when(marketHolidayService.isMarketHoliday()).thenReturn(false);
        when(tipRepository.findAll()).thenReturn(List.of(tip));
        when(yahooFinanceClient.fetchCurrentPrice("TCS")).thenReturn(115.0);

        scheduler.trackPrices();

        assertThat(tip.getT1Reached()).isTrue();

        verify(notificationService)
                .sendTargetNotification("TCS", "EQUITY", "T1", 115.0);

        verify(tipRepository).save(tip);
    }


    // 5️⃣ Archive success
    @Test
    public void archiveAt4PM_shouldArchiveAndDelete() {

        when(tipRepository.findByCreatedAtBetween(any(), any()))
                .thenReturn(List.of(tip));

        scheduler.archiveAt4PM();

        verify(historyRepo).save(any());
        verify(tipRepository).deleteAll(any());
    }
}
