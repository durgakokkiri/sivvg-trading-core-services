package com.sivvg.tradingservices.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivvg.tradingservices.exceptions.HomeResourceAlreadyExistsException;
import com.sivvg.tradingservices.exceptions.HomeResourceNotFoundException;
import com.sivvg.tradingservices.exceptions.ResourceNotFoundException;
import com.sivvg.tradingservices.model.DailyTipEntity;
import com.sivvg.tradingservices.playload.DailyTipRequest;
import com.sivvg.tradingservices.repository.DailyTipRepository;
import com.sivvg.tradingservices.service.YahooFinanceClient;

@ExtendWith(MockitoExtension.class)
public class DailyTipServiceImplTest {

    @Mock
    private DailyTipRepository repo;

    @InjectMocks
    private DailyTipServiceImpl service;

    private DailyTipRequest equityReq;
    private DailyTipRequest futuresReq;
    private DailyTipRequest optionsReq;
    private DailyTipEntity tip;

    @BeforeEach
    public void setup() {

        // -------- EQUITY REQUEST --------
        equityReq = new DailyTipRequest();
        equityReq.setStockCode("INFY");
        equityReq.setStockPrice(1000.0);
        equityReq.setStockType("BUY");
        equityReq.setCategory("EQUITY");
        equityReq.setPlanType("BASIC");
        equityReq.setSectorType("IT");
        equityReq.setT1(1100.0);
        equityReq.setT2(1150.0);
        equityReq.setT3(1200.0);

        // -------- FUTURES REQUEST --------
        futuresReq = new DailyTipRequest();
        futuresReq.setStockCode("NIFTY-FUT");
        futuresReq.setStockPrice(21500.0);
        futuresReq.setStockType("BUY");
        futuresReq.setCategory("FUTURES");
        futuresReq.setPlanType("PREMIUM");
        futuresReq.setSectorType("INDEX");
        futuresReq.setT1(22000.0);
        futuresReq.setT2(22500.0);
        futuresReq.setT3(23000.0);

        // -------- OPTIONS REQUEST --------
        optionsReq = new DailyTipRequest();
        optionsReq.setStockCode("NIFTY");
        optionsReq.setStockPrice(100.0);
        optionsReq.setStockType("BUY");
        optionsReq.setCategory("OPTIONS");
        optionsReq.setPlanType("PAID");
        optionsReq.setSectorType("INDEX");
        optionsReq.setOptionType("CE");
        optionsReq.setStrikePrice(2250.0);
        optionsReq.setAtm(100);                 // 🔥 ATM is Integer
        optionsReq.setTargetPrice(150.0);

        // -------- ENTITY --------
        tip = new DailyTipEntity();
        tip.setId(1L);
        tip.setStockCode("INFY");
        tip.setCategory("EQUITY");
    }

    // ==================================================
    // SAVE TIP - EQUITY
    // ==================================================
    @Test
    public void saveTip_equity_success() {

        when(repo.existsByStockCode("INFY")).thenReturn(false);
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        DailyTipEntity result = service.saveTip(equityReq);

        assertThat(result.getStockCode()).isEqualTo("INFY");
        verify(repo).save(any());
    }

    // ==================================================
    // SAVE TIP - FUTURES
    // ==================================================
    @Test
    public void saveTip_futures_success() {

        when(repo.existsByStockCode("NIFTY-FUT")).thenReturn(false);
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        DailyTipEntity result = service.saveTip(futuresReq);

        assertThat(result.getCategory()).isEqualTo("FUTURES");
        assertThat(result.getT1()).isEqualTo(22000.0);
    }

    // ==================================================
    // SAVE TIP - OPTIONS
    // ==================================================
    @Test
    public  void saveTip_options_success() {

        when(repo.existsByStockCode("NIFTY")).thenReturn(false);
        when(repo.save(any())).thenAnswer(i -> i.getArgument(0));

        DailyTipEntity result = service.saveTip(optionsReq);

        assertThat(result.getCategory()).isEqualTo("OPTIONS");
        assertThat(result.getTargetPrice()).isEqualTo(150.0);
        assertThat(result.getT1()).isNull();   // equity targets disabled
    }

    // ==================================================
    // SAVE TIP - DUPLICATE
    // ==================================================
    @Test
    public void saveTip_duplicate_shouldFail() {

        when(repo.existsByStockCode("INFY")).thenReturn(true);

        assertThatThrownBy(() -> service.saveTip(equityReq))
                .isInstanceOf(HomeResourceAlreadyExistsException.class);
    }

    // ==================================================
    // GET ALL TIPS
    // ==================================================
    @Test
    public void getAllTips_success() {

        when(repo.findAll()).thenReturn(List.of(tip));

        List<DailyTipEntity> list = service.getAllTips();

        assertThat(list).hasSize(1);
        verify(repo).findAll();
    }

    // ==================================================
    // GET BY CATEGORY
    // ==================================================
    @Test
    public void getTipsByCategory_success() {

        when(repo.findByCategoryIgnoreCase("EQUITY"))
                .thenReturn(List.of(tip));

        List<DailyTipEntity> list = service.getTipsByCategory("EQUITY");

        assertThat(list).hasSize(1);
        verify(repo).findByCategoryIgnoreCase("EQUITY");
    }

    // ==================================================
    // UPDATE TIP
    // ==================================================
    @Test
    public void updateTip_success() {

        when(repo.findById(1L)).thenReturn(Optional.of(tip));
        when(repo.save(any())).thenReturn(tip);

        DailyTipEntity updated = service.updateTip(1L, equityReq);

        assertThat(updated.getStockCode()).isEqualTo("INFY");
        verify(repo).save(tip);
    }

    // ==================================================
    // UPDATE TIP - NOT FOUND
    // ==================================================
    @Test
    public void updateTip_notFound() {

        when(repo.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateTip(1L, equityReq))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    // ==================================================
    // DELETE TIP
    // ==================================================
    @Test
    public void deleteTip_success() {

        when(repo.findById(1L)).thenReturn(Optional.of(tip));

        service.deleteTip(1L);

        verify(repo).delete(tip);
    }

    // ==================================================
    // GET TIP STATUS
    // ==================================================
    @Test
    public void getTipStatus_success() {

        when(repo.findById(1L)).thenReturn(Optional.of(tip));

        DailyTipEntity result = service.getTipStatus(1L);

        assertThat(result).isNotNull();
    }
}