package com.sivvg.tradingservices.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MessageConstantsTest {

    // ---------------- VALIDATION ----------------
    @Test
    public void testValidationMessages() {
        assertEquals("Username is required", MessageConstants.USERNAME_REQUIRED);
        assertEquals("Username must be 3 to 50 characters",
                MessageConstants.USERNAME_LENGTH);

        assertEquals("Email is required", MessageConstants.EMAIL_REQUIRED);
        assertEquals("Invalid email format", MessageConstants.INVALID_EMAIL);

        assertEquals("Phone number is required", MessageConstants.PHONE_REQUIRED);
        assertEquals("Invalid phone number", MessageConstants.INVALID_PHONE);

        assertEquals("Gender must be MALE, FEMALE or OTHER",
                MessageConstants.GENDER_INVALID);
        assertEquals("DOB must be in yyyy-mm-dd format",
                MessageConstants.DOB_INVALID);

        assertEquals("Role not found", MessageConstants.ROLE_NOT_FOUND);
        assertEquals("Role must be USER or ADMIN",
                MessageConstants.ROLE_INVALID);

        assertEquals("Status not found", MessageConstants.STATUS_NOT_FOUND);
        assertEquals("Status must be ACTIVE, INACTIVE or BLOCKED",
                MessageConstants.STATUS_INVALID);
    }

    // ---------------- LOGIN ----------------
    @Test
    public void testLoginMessages() {
        assertEquals("UserId is required", MessageConstants.USERID_REQUIRED);
        assertEquals("Invalid UserId format",
                MessageConstants.INVALID_USERID);

        assertEquals("Password is required",
                MessageConstants.PASSWORD_REQUIRED);
        assertEquals("Password cannot be less than 6 characters",
                MessageConstants.PASSWORD_MIN_LENGTH);
    }

    // ---------------- OTP ----------------
    @Test
    public  void testOtpMessages() {
        assertEquals("OTP is required", MessageConstants.OTP_REQUIRED);
        assertEquals("OTP not generated. Please login again",
                MessageConstants.OTP_NOT_GENERATED);
        assertEquals("Invalid OTP", MessageConstants.INVALID_OTP);
        assertEquals("OTP expired. Please request new OTP",
                MessageConstants.OTP_EXPIRED);
        assertEquals("OTP sent to registered email and phone number",
                MessageConstants.OTP_SENT);
    }

    // ---------------- FORGOT / RESET PASSWORD ----------------
    @Test
    public void testForgotAndResetPasswordMessages() {
        assertEquals("Email or phone number is required",
                MessageConstants.EMAIL_OR_PHONE_REQUIRED);
        assertEquals("Enter valid email or Indian phone number",
                MessageConstants.INVALID_EMAIL_OR_PHONE);

        assertEquals("New password is required",
                MessageConstants.NEW_PASSWORD_REQUIRED);
        assertEquals("Confirm password is required",
                MessageConstants.CONFIRM_PASSWORD_REQUIRED);
        assertEquals("Passwords do not match",
                MessageConstants.PASSWORDS_NOT_MATCH);

        assertEquals("Forgot password OTP sent successfully",
                MessageConstants.FORGOT_OTP_SENT);
        assertEquals("Password reset successful",
                MessageConstants.PASSWORD_RESET_SUCCESS);
    }

    // ---------------- USER / COMMON ----------------
    @Test
    public void testUserAndCommonMessages() {
        assertEquals("User not found",
                MessageConstants.USER_NOT_FOUND);
        assertEquals("User already exists",
                MessageConstants.USER_ALREADY_EXISTS);

        assertEquals("Something went wrong. Please try again",
                MessageConstants.SOMETHING_WENT_WRONG);
        assertEquals("Invalid userId or password",
                MessageConstants.INVALID_CREDENTIALS);
    }

    // ---------------- TOKEN ----------------
    @Test
    public  void testTokenMessages() {
        assertEquals("Unable to generate token",
                MessageConstants.TOKEN_GENERATION_FAILED);

        assertEquals("User not found with given email or phone number",
                MessageConstants.USER_NOT_FOUND_EMAIL_PHONE);

        assertEquals("User not found with given email or phone number",
                MessageConstants.USER_NOT_FOUND_WITH_INPUT);
    }
}
