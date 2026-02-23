package com.sivvg.tradingservices.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sivvg.tradingservices.playload.AdminCreateRequest;
import com.sivvg.tradingservices.playload.RegisterRequest;

@Service
public interface AdminService {

	public AdminCreateRequest createAdmin(AdminCreateRequest request);

	public List<RegisterRequest> getAllUsers();

	public void deleteUser(Long id);

	public RegisterRequest updateUser(Long id, RegisterRequest req);

	public Map<String, Long> getDashboardCounts();

}
