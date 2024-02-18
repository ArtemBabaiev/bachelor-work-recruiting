package edu.chnu.recruiting.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.chnu.recruiting.models.security.Role;
import edu.chnu.recruiting.repositories.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;

	private Map<String, Role> rolesCache = new HashMap<String, Role>();

	public Role getRoleByName(String name) {
		if (rolesCache.containsKey(name)) {
			return rolesCache.get(name);
		}
		Role role = this.roleRepository.findByName(name);
		rolesCache.put(name, role);
		return role;
	}
}
