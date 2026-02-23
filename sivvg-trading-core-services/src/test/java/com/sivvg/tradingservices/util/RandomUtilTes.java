package com.sivvg.tradingservices.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class RandomUtilTes {
	
	// ===== generateUserId =====

	@Test
	public void generateUserId_success() {

		String userId = RandomUtil.generateUserId("durga");

		assertThat(userId).isNotNull();
		assertThat(userId.length()).isEqualTo(6);
		assertThat(userId.substring(0, 3)).isEqualTo("DUR");
		assertThat(userId.substring(3)).matches("\\d{3}");
	}

	@Test
	public void generateUserId_shortUsername_shouldThrowException() {

		assertThatThrownBy(() -> RandomUtil.generateUserId("ab")).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("at least 3 letters");
	}

	@Test
	public void generateUserId_nullUsername_shouldThrowException() {

		assertThatThrownBy(() -> RandomUtil.generateUserId(null)).isInstanceOf(IllegalArgumentException.class);
	}
	
	
	

	@Test
	public   void generateUserId_shortUsername_shouldThrowException1() {

        assertThatThrownBy(() -> RandomUtil.generateUserId("ab"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 3");
    }

	
	
	
	
	// ===== generateOtpNumeric =====

	@Test
	public void generateOtpNumeric_success() {

		String otp = RandomUtil.generateOtpNumeric(6);

		assertThat(otp).isNotNull();
		assertThat(otp.length()).isEqualTo(6);
		assertThat(otp).matches("\\d{6}");
	}

	@Test
	public void generateOtpNumeric_zeroLength_shouldThrowException() {
	    assertThatThrownBy(() -> RandomUtil.generateOtpNumeric(0))
	        .isInstanceOf(IllegalArgumentException.class)
	        .hasMessageContaining("must be greater than zero");
	}

	// ===== generatePassword =====

	@Test
	public void generatePassword_success() {

		String password = RandomUtil.generatePassword(6);

		assertThat(password).isNotNull();
		assertThat(password.length()).isEqualTo(6);
		assertThat(password).matches("[A-Za-z0-9@#$%&*!?]{6}");
	}

	@Test
	public void generatePassword_smallLength_shouldThrowException() {

		assertThatThrownBy(() -> RandomUtil.generatePassword(4)).isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("at least 6");
	}
}
