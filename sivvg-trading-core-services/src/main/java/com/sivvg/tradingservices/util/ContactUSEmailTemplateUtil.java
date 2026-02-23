package com.sivvg.tradingservices.util;

import com.sivvg.tradingservices.model.User;

public final class ContactUSEmailTemplateUtil {

	private ContactUSEmailTemplateUtil() {
		// utility class
	}

	public static String subject(String type) {

		if (type == null) {
			return "Thank you for contacting us";
		}

		return switch (type.toUpperCase()) {
		case "CALLBACK" -> "We received your callback request";
		case "CHAT" -> "Thanks for reaching out to us";
		case "EMAIL" -> "Thank you for writing to us";
		default -> "Thank you for contacting us";
		};
	}

	public static String body(String type, User user) {

		String name = user != null && user.getUsername() != null ? user.getUsername() : "Customer";

		if (type == null) {
			type = "";
		}

		return switch (type.toUpperCase()) {

		case "CALLBACK" -> """
				Hi %s,

				Thank you for contacting SIVGG.

				We have received your request for a callback.
				Our team will reach out to you shortly.

				Regards,
				SIVGG Support Team
				""".formatted(name);

		case "CHAT" -> """
				Hi %s,

				Thanks for reaching out to SIVGG.

				Our experts will connect with you during working hours.

				Best regards,
				SIVGG Team
				""".formatted(name);

		case "EMAIL" -> """
				Hi %s,

				Thank you for writing to us.

				We have received your message and our team is reviewing it.
				We will get back to you shortly.

				Warm regards,
				SIVGG Team
				""".formatted(name);

		default -> """
				Hi %s,

				Thank you for contacting us.
				We will get back to you soon.

				Regards,
				SIVGG Team
				""".formatted(name);
		};
	}
}
