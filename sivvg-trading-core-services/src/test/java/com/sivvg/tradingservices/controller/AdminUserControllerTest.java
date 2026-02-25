package com.sivvg.tradingservices.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sivvg.tradingservices.configuration.TestSecurityConfig;
import com.sivvg.tradingservices.model.MarketHolidayEntity;
import com.sivvg.tradingservices.playload.AdminReplyDTO;
import com.sivvg.tradingservices.playload.ContactResponseDTO;
import com.sivvg.tradingservices.playload.RegisterRequest;
import com.sivvg.tradingservices.repository.MarketHolidayRepository;
import com.sivvg.tradingservices.service.AdminService;
import com.sivvg.tradingservices.service.ContactUSService;
import com.sivvg.tradingservices.service.DailyTipService;
import com.sivvg.tradingservices.service.MarketHolidayService;
import com.sivvg.tradingservices.service.NotificationService;
import com.sivvg.tradingservices.service.YahooFinanceClient;

@WebMvcTest(AdminUserController.class) // 🔹 CHANGED CONTROLLER NAME HERE
@AutoConfigureMockMvc(addFilters = true)
@Import(TestSecurityConfig.class)
public class AdminUserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	// ===== Mock Beans =====
	@MockitoBean
	private AdminService adminService;

	@MockitoBean
	private DailyTipService dailyTipService;

	@MockitoBean
	private YahooFinanceClient yahooFinanceClient;

	@MockitoBean
	private MarketHolidayService marketHolidayService;

	@MockitoBean
	private NotificationService notificationService;

	@MockitoBean
	private ContactUSService contactService;

	@MockitoBean
	private MarketHolidayRepository repo;

	// ================= GET ALL USERS =================
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void getAllUsers_success() throws Exception {

		RegisterRequest r = new RegisterRequest();
		r.setUsername("durga");

		when(adminService.getAllUsers()).thenReturn(List.of(r));

		mockMvc.perform(get("/api/v1/admin/users")).andExpect(status().isOk())
				.andExpect(jsonPath("$[0].username").value("durga"));
	}

	// ================= DELETE USER =================
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void deleteUser_success() throws Exception {

		doNothing().when(adminService).deleteUser(1L);

		mockMvc.perform(delete("/api/v1/admin/users/1").with(csrf())).andExpect(status().isOk())
				.andExpect(content().string("User deleted successfully"));
	}

	// ================= UPDATE USER =================
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void updateUser_success() throws Exception {

		RegisterRequest req = new RegisterRequest();
		req.setUsername("updated");

		when(adminService.updateUser(eq(1L), any(RegisterRequest.class))).thenReturn(req);

		mockMvc.perform(put("/api/v1/admin/users/1").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("updated"));
	}

	// ================= DASHBOARD COUNTS =================
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void getUserCounts_success() throws Exception {

		Map<String, Long> map = new HashMap<>();
		map.put("totalUsers", 10L);

		when(adminService.getDashboardCounts()).thenReturn(map);

		mockMvc.perform(get("/api/v1/admin/user-counts")).andExpect(status().isOk())
				.andExpect(jsonPath("$.totalUsers").value(10));
	}

	// ================= UPLOAD HOLIDAYS SUCCESS =================
	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	public void uploadHolidays_success() throws Exception {

		String csvData = """
				date,description,year
				2026-01-26,Republic Day,2026
				2026-08-15,Independence Day,2026
				""";

		MockMultipartFile file = new MockMultipartFile("file", "holidays.csv", "text/csv", csvData.getBytes());

		when(repo.existsByHolidayDate(any(LocalDate.class))).thenReturn(false);
		when(repo.save(any(MarketHolidayEntity.class))).thenReturn(new MarketHolidayEntity());

		mockMvc.perform(multipart("/api/v1/admin/upload").file(file).with(csrf())).andExpect(status().isOk())
				.andExpect(content().string("✅ 2 holidays uploaded successfully!"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetAllContacts() throws Exception {

		ContactResponseDTO dto1 = new ContactResponseDTO();
		dto1.setId(1L);
		dto1.setMessage("Need help");
		dto1.setStatus("NEW");

		ContactResponseDTO dto2 = new ContactResponseDTO();
		dto2.setId(2L);
		dto2.setMessage("Callback");
		dto2.setStatus("IN_PROGRESS");

		List<ContactResponseDTO> list = Arrays.asList(dto1, dto2);

		when(contactService.getAllContacts()).thenReturn(list);

		mockMvc.perform(get("/api/v1/admin/contacts")).andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2)).andExpect(jsonPath("$[0].message").value("Need help"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testGetContactById() throws Exception {

		ContactResponseDTO dto = new ContactResponseDTO();
		dto.setId(1L);
		dto.setMessage("Test message");
		dto.setStatus("NEW");

		when(contactService.getContactById(1L)).thenReturn(dto);

		mockMvc.perform(get("/api/v1/admin/contacts/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value("Test message")).andExpect(jsonPath("$.status").value("NEW"));
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN" })
	void testReplyToContact() throws Exception {

		AdminReplyDTO replyDTO = new AdminReplyDTO();
		replyDTO.setReplyMessage("Issue resolved");
		replyDTO.setStatus("RESOLVED");

		doNothing().when(contactService).replyToContact(eq(1L), any(AdminReplyDTO.class));

		mockMvc.perform(put("/api/v1/admin/contacts/1/reply").with(csrf()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(replyDTO))).andExpect(status().isOk())
				.andExpect(content().string("Reply updated successfully"));
	}

}
