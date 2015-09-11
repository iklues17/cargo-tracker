package com.sds.fsf.auth.web.rest.admin;

import com.codahale.metrics.annotation.Timed;
import com.sds.fsf.auth.domain.Client;
import com.sds.fsf.auth.repository.ClientRepository;
import com.wordnik.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;

import java.util.*;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminClientController {

	private final Logger log = LoggerFactory.getLogger(AdminClientController.class);

	@Inject
	private ClientRepository clientRepository;

	/**
	 * POST /clients -> create or update clients.
	 */
	@ApiOperation(hidden = true, value = "ONLY FOR ADMIN MENU")
	@RequestMapping(value = "/clients", method = RequestMethod.POST, produces = MediaType.TEXT_PLAIN_VALUE)
	@Timed
	public ResponseEntity<?> createClients(@RequestBody List<Client> clients) {
		for (Client client : clients) {
			Client clietFromDB = clientRepository.findOne(client.getClientId());
			if (null != clietFromDB) {
				client.setClientGrantDefaultUserAuthorities(clietFromDB
						.getClientGrantDefaultUserAuthorities());
			}
		}
		clientRepository.save(clients);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/**
	 * DELETE /clients -> delete clients.
	 */
	@ApiOperation(hidden = true, value = "ONLY FOR ADMIN MENU")
	@RequestMapping(value = "/clients", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<String> deleteClients(
			@RequestParam Map<String, String> params) {
		String[] clientIds = params.get("clientIds").split(",");
		for (String clientId : clientIds) {
			if (null != clientId && clientId.length() > 0) {
				clientRepository.delete(clientId);
			}
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * GET /clients -> get all clients.
	 */
	@ApiOperation(hidden = true, value = "ONLY FOR ADMIN MENU")
	@RequestMapping(value = "/clients", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@Timed
	public ResponseEntity<List<Client>> getClients() {
		List<Client> clients = clientRepository.findAll();
		if (clients.size() == 0) {
			clients.add(new Client());
		}
		return new ResponseEntity<List<Client>>(clients, HttpStatus.OK);
	}
}
