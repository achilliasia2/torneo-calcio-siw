package it.uniroma3.siw.torneo_calcio;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TorneoCalcioApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void loginPageLoads() throws Exception {
		mockMvc.perform(get("/login"))
				.andExpect(status().isOk());
	}

	@Test
	void publicPagesLoad() throws Exception {
		mockMvc.perform(get("/"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/registrazione"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/tornei"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/squadre"))
				.andExpect(status().isOk());
	}

	@Test
	@WithMockUser(roles = "ADMIN")
	void adminPagesLoad() throws Exception {
		mockMvc.perform(get("/admin/tornei/nuovo"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/admin/squadre/nuova"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/admin/partite/nuova"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/admin/arbitri"))
				.andExpect(status().isOk());
		mockMvc.perform(get("/admin/arbitri/nuovo"))
				.andExpect(status().isOk());
	}

}
