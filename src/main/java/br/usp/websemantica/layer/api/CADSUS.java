package br.usp.websemantica.layer.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.usp.cadsus.PDQClientCADSUSDev;
import br.usp.websemantica.layer.security.SecurityService;
import br.usp.websemantica.model.CNSCidadeEstado;
import br.usp.websemantica.model.CNSData;
import br.usp.websemantica.model.CNSPais;
import br.usp.websemantica.model.ResponseApi;
import br.usp.websemantica.model.SistemaExterno;
import br.usp.websemantica.service.CNSService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;

@RestController
@RequestMapping("/cadsus")
@Api(value="cadsus-api", description="Consultas ao webservice CADSUS")
@ApiResponses(value = {
        @ApiResponse(code = 200, message = "Recurso retornado com sucesso"),
        @ApiResponse(code = 401, message = "Não autorizado"),
        @ApiResponse(code = 403, message = "Acesso Proibido"),
        @ApiResponse(code = 404, message = "Recurso não encontrado")
})
public class CADSUS {
	
	static final Logger log = Logger.getLogger(CADSUS.class);
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private CNSService cnsService;
		
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Obtém dados do CADSUS pelo CNS", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/getByCNS"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public CNSData getDataByCns(@RequestParam(value="cns", required=true) String cns,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		CNSData response = new CNSData();
		SistemaExterno sistemaExterno = this.securityService.getSistemaExternoByApiKey(req.getParameter("api_key"));
  	  
    	if(sistemaExterno == null || sistemaExterno.getBloqueado() == 1 || sistemaExterno.getCadsus() == 0) {    		
    		response.setCode(ResponseApi.UNAUTHORIZED);
			response.setMessage("Não autorizado");
			return response;
    	}
				
		PDQClientCADSUSDev c = new PDQClientCADSUSDev();	
		response = c.callService("cns", cns);	
		
		if(response != null) {
			response = this.getCodigosCADSUS(response);			
			response.setCode(ResponseApi.OK);
		} else {	
			response = new CNSData();
			response.setCode(ResponseApi.OK);
			response.setMessage("NOT_FOUND");
		}
				
		resp.setStatus(response.getCode());	
		return response;
	}	
	
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Obtém dados do CADSUS pelo CPF", authorizations = {@Authorization(value="apiKeyAuth")})
	@RequestMapping(value={"/getByCPF"}, method=RequestMethod.GET, produces={MediaType.APPLICATION_JSON})
	public CNSData getDataByCpf(@RequestParam(value="cpf", required=true) String cpf,
			HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		CNSData response = new CNSData();
		SistemaExterno sistemaExterno = this.securityService.getSistemaExternoByApiKey(req.getParameter("api_key"));
  	  
    	if(sistemaExterno == null || sistemaExterno.getBloqueado() == 1 || sistemaExterno.getCadsus() == 0) {    		
    		response.setCode(ResponseApi.UNAUTHORIZED);
			response.setMessage("Não autorizado");
			return response;
    	}
				
		PDQClientCADSUSDev c = new PDQClientCADSUSDev();	
		response = c.callService("cpf", cpf);	
							
		if(response != null) {
			response = this.getCodigosCADSUS(response);			
			response.setCode(ResponseApi.OK);
		} else {	
			response = new CNSData();
			response.setCode(ResponseApi.OK);
			response.setMessage("NOT_FOUND");
		}
				
		resp.setStatus(response.getCode());	
		return response;
	}		
		
	private CNSData getCodigosCADSUS(CNSData response) {
		response.setRaca(cnsService.getRacaById(response.getCodRaca()).getDescricao());
		response.setTipoLogradouro(cnsService.getTipoLogradouroById(response.getCodTipoLogradouro()).getDescricao());
		
		CNSPais paisNascimento = cnsService.getPaisById(response.getCodPaisNascimento());
		CNSPais paisResidencia = cnsService.getPaisById(response.getCodPaisResidencia());		
									
		if(paisNascimento != null) {
			response.setPaisNascimento(paisNascimento.getDescricao());
			if(response.getCodPaisNascimento() == 10) {
				CNSCidadeEstado localNascimento = cnsService.getCidadeEstadoByCodIBGE(response.getCodMunicipioNascimento());	
				response.setMunicipioNascimento(localNascimento.getCidade() + " - " + localNascimento.getUf());
			} else {
				response.setMunicipioNascimento("Exterior");
			}
		} 
		
		if(paisResidencia != null) {
			response.setPaisResidencia(paisResidencia.getDescricao());
			if(response.getCodPaisResidencia() == 10) {
				CNSCidadeEstado localResidencia = cnsService.getCidadeEstadoByCodIBGE(response.getCodMunicipioResidencia());
				response.setMunicipioResidencia(localResidencia.getCidade() + " - " + localResidencia.getUf());			
			} else {
				response.setMunicipioResidencia("Exterior");
			}
		}
		return response;
	}
	
}
