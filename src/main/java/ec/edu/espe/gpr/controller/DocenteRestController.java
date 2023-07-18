package ec.edu.espe.gpr.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import ec.edu.espe.gpr.dao.IDocenteDao;
import ec.edu.espe.gpr.model.Cargo;
import ec.edu.espe.gpr.model.Docente;
import ec.edu.espe.gpr.response.DocenteResponseRest;
import ec.edu.espe.gpr.services.IDocenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {
		"https://yellow-river-0ca1d1510.3.azurestaticapps.net",
		"http://localhost:4200",
		"http://localhost:8088"
})
@RestController
@RequestMapping("/api/v1")
public class DocenteRestController {
	@Autowired
	private IDocenteService docenteservice;

	@GetMapping("/usuarionombre/{id}")
	public ResponseEntity<DocenteResponseRest> buscarUsuarios(@PathVariable String id) {
		ResponseEntity<DocenteResponseRest> responseEntity = docenteservice.buscarPorUsuario(id);
		return responseEntity;
	}

	@GetMapping("/usuarioid/{id}")
	public ResponseEntity<DocenteResponseRest> buscarporId(@PathVariable String id) {
		ResponseEntity<DocenteResponseRest> responseEntity = docenteservice.buscarPorIDEspe(id);
		return responseEntity;
	}

	@GetMapping("/docente")
	public ResponseEntity<DocenteResponseRest> searchUsuarios() {
		try {
			ResponseEntity<DocenteResponseRest> responseEntity = docenteservice.serach();
			return responseEntity;
		} catch (Exception c) {
			return null;
		}
	}

	@GetMapping("/docente/{idDocente}")
	public ResponseEntity<DocenteResponseRest> buscarporIdDocente(@PathVariable String idDocente) {
		ResponseEntity<DocenteResponseRest> responseEntity = docenteservice.buscarPorIdDocente(idDocente);
		return responseEntity;
	}

	@GetMapping("/docentePerfilU")
	public ResponseEntity<DocenteResponseRest> buscarUsuarioPerfil() {
		try {
			ResponseEntity<DocenteResponseRest> responseEntity = docenteservice.serachPorPerfil();
			return responseEntity;
		} catch (Exception c) {
			return null;
		}
	}

	@GetMapping(path = "/obtenerDocentePorCedula/{cedulaDocente}")
	public ResponseEntity<Docente> obtenerDocentePorCedula(@PathVariable String cedulaDocente) {
		try {
			Docente docente = this.docenteservice.buscarPorCedulaDocente(cedulaDocente);
			return ResponseEntity.ok(docente);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping("/docentes")
	public ResponseEntity<DocenteResponseRest> saveDocentes(

			@RequestParam("idDocente") String idDocente,
			@RequestParam("nombreDocente") String nombreDocente,
			@RequestParam("apellidoDocente") String apellidoDocente,
			@RequestParam("cedulaDocente") String cedulaDocente,
			@RequestParam("telefonoDocente") String telefonoDocente,
			@RequestParam("correoDocente") String correoDocente,
			@RequestParam("sexooDocente") String sexooDocente,
			@RequestParam("puestoDocente") String puestoDocente,
			@RequestParam("cargosAsignados") String strCargosAsignados) {

		Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm").create();
		List<Cargo> cargos = gson.fromJson(strCargosAsignados, new TypeToken<List<Cargo>>() {
		}.getType());

		Docente docente = new Docente();

		docente.setIdDocente(idDocente);
		docente.setNombreDocente(nombreDocente);
		docente.setApellidoDocente(apellidoDocente);
		docente.setCedulaDocente(cedulaDocente);
		docente.setTelefonoDocente(telefonoDocente);
		docente.setCorreoDocente(correoDocente);
		docente.setSexo(sexooDocente);
		docente.setPuestoTrabajoDocente(puestoDocente);

		ResponseEntity<DocenteResponseRest> responseEntity = docenteservice.save(docente, cargos);
		return responseEntity;
	}

	@PutMapping("/updatedocente/{id}")
	public ResponseEntity<DocenteResponseRest> updateCategories(@RequestBody Docente docente,
			@PathVariable Integer id) {
		ResponseEntity<DocenteResponseRest> responseEntity = docenteservice.update(docente, id);
		return responseEntity;
	}

	@PutMapping("/resetearPassword")
	public ResponseEntity<String> resetearPassword(@RequestBody String email) {
		try {
			this.docenteservice.resetearPassword(email);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().build();
		}
	}

}
