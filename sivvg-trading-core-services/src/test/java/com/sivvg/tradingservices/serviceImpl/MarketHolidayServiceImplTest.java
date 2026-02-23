package com.sivvg.tradingservices.serviceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sivvg.tradingservices.repository.MarketHolidayRepository;

@ExtendWith(MockitoExtension.class)
public class MarketHolidayServiceImplTest {

	@Mock
	private MarketHolidayRepository repo;

	@InjectMocks
	private MarketHolidayServiceImpl service;

	// =====================================================
	// WEEKEND - SATURDAY
	// =====================================================
	@Test
	public void isMarketHoliday_shouldReturnTrue_whenSaturday() {

		LocalDate saturday = LocalDate.of(2026, 1, 24); // Saturday

		boolean result = service.isMarketHoliday(saturday);

		assertThat(result).isTrue();
		verify(repo, never()).existsByHolidayDate(any());
	}

	// =====================================================
	// WEEKEND - SUNDAY
	// =====================================================
	@Test
	public void isMarketHoliday_shouldReturnTrue_whenSunday() {

		LocalDate sunday = LocalDate.of(2026, 1, 25); // Sunday

		boolean result = service.isMarketHoliday(sunday);

		assertThat(result).isTrue();
		verify(repo, never()).existsByHolidayDate(any());
	}

	// =====================================================
	// WEEKDAY - OFFICIAL HOLIDAY IN DB
	// =====================================================
	@Test
	public void isMarketHoliday_shouldReturnTrue_whenWeekdayButHolidayInDatabase() {

		LocalDate gandhiJayanti = LocalDate.of(2025, 10, 2); // Thursday

		when(repo.existsByHolidayDate(gandhiJayanti)).thenReturn(true);

		boolean result = service.isMarketHoliday(gandhiJayanti);

		assertThat(result).isTrue();
		verify(repo).existsByHolidayDate(gandhiJayanti);
	}

	// =====================================================
	// WEEKDAY - NORMAL WORKING DAY
	// =====================================================
	@Test
	public void isMarketHoliday_shouldReturnFalse_whenWeekdayAndNotHoliday() {

		LocalDate workingDay = LocalDate.of(2025, 10, 3); // Friday

		when(repo.existsByHolidayDate(workingDay)).thenReturn(false);

		boolean result = service.isMarketHoliday(workingDay);

		assertThat(result).isFalse();
		verify(repo).existsByHolidayDate(workingDay);
	}

	// =====================================================
	// TODAY METHOD (isMarketHoliday())
	// =====================================================
	@Test
	public void isMarketHoliday_todayMethod_shouldCallDateVersion() {

		// We cannot control system date easily, so just verify it does not crash
		when(repo.existsByHolidayDate(any())).thenReturn(false);

		boolean result = service.isMarketHoliday();

		assertThat(result).isFalse(); // assuming today is not weekend & not holiday
		verify(repo).existsByHolidayDate(any());
	}
}
