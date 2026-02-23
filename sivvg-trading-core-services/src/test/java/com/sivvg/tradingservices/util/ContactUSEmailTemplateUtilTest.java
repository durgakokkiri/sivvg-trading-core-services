package com.sivvg.tradingservices.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.sivvg.tradingservices.model.User;

public class ContactUSEmailTemplateUtilTest {

    private User createUser() {
        User user = new User();
        user.setUsername("Baswa");
        return user;
    }

    // ---------- SUBJECT TESTS ----------

    @Test
    @DisplayName("Subject for CALLBACK type")
    public void testSubjectForCallback() {
        String subject = ContactUSEmailTemplateUtil.subject("CALLBACK");
        assertEquals("We received your callback request", subject);
    }

    @Test
    @DisplayName("Subject for CHAT type")
    public void testSubjectForChat() {
        String subject = ContactUSEmailTemplateUtil.subject("CHAT");
        assertEquals("Thanks for reaching out to us", subject);
    }

    @Test
    @DisplayName("Subject for EMAIL type")
    public void testSubjectForEmail() {
        String subject = ContactUSEmailTemplateUtil.subject("EMAIL");
        assertEquals("Thank you for writing to us", subject);
    }

    @Test
    @DisplayName("Subject for default type")
    public  void testSubjectForDefault() {
        String subject = ContactUSEmailTemplateUtil.subject("UNKNOWN");
        assertEquals("Thank you for contacting us", subject);
    }

    // ---------- BODY TESTS ----------

    @Test
    @DisplayName("Email body for CALLBACK type")
    public  void testBodyForCallback() {
        User user = createUser();

        String body = ContactUSEmailTemplateUtil.body("CALLBACK", user);

        assertTrue(body.contains("Hi Baswa"));
        assertTrue(body.contains("received your request for a callback"));
        assertTrue(body.contains("SIVGG Support Team"));
    }

    @Test
    @DisplayName("Email body for CHAT type")
    public void testBodyForChat() {
        User user = createUser();

        String body = ContactUSEmailTemplateUtil.body("CHAT", user);

        assertTrue(body.contains("Hi Baswa"));
        assertTrue(body.contains("experts will connect with you"));
        assertTrue(body.contains("SIVGG Team"));
    }

    @Test
    @DisplayName("Email body for EMAIL type")
    public void testBodyForEmail() {
        User user = createUser();

        String body = ContactUSEmailTemplateUtil.body("EMAIL", user);

        assertTrue(body.contains("Hi Baswa"));
        assertTrue(body.contains("received your message"));
        assertTrue(body.contains("SIVGG Team"));
    }

    @Test
    @DisplayName("Email body for default type")
    public  void testBodyForDefault() {
        User user = createUser();

        String body = ContactUSEmailTemplateUtil.body("UNKNOWN", user);

        assertTrue(body.contains("Hi Baswa"));
        assertTrue(body.contains("We will get back to you soon"));
        assertTrue(body.contains("SIVGG Team"));
    }
}
