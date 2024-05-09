package br.usp.websemantica.model;

import org.apache.commons.httpclient.HttpStatus;

import io.swagger.annotations.ApiModelProperty;

public class CNSData {

	public static final Integer OK = HttpStatus.SC_OK;
	public static final Integer ERROR = HttpStatus.SC_INTERNAL_SERVER_ERROR;
	public static final Integer FORBIDDEN = HttpStatus.SC_FORBIDDEN;
	public static final Integer UNAUTHORIZED = HttpStatus.SC_UNAUTHORIZED;
	public static final Integer SERVICE_UNAVAILABLE = HttpStatus.SC_SERVICE_UNAVAILABLE;
	public static final Integer NOT_FOUND = HttpStatus.SC_NOT_FOUND;
	
	@ApiModelProperty(notes = "Status HTTP")
	private Integer code;
	
	@ApiModelProperty(notes = "Mensagem informativa")
	private String message;
		
	@ApiModelProperty(notes = "Nome do paciente")	
	private String nomePaciente;
	
	@ApiModelProperty(notes = "Cart�o Nacional de Sa�de")
	private String cns;
	
	@ApiModelProperty(notes = "Cadastro de Pessoa F�sica")
	private long cpf;
	
	@ApiModelProperty(notes = "Nome da m�e")
	private String nomeMae;
	
	@ApiModelProperty(notes = "Sexo")
	private String sexo;
	
	@ApiModelProperty(notes = "Data de nascimento")
	private String dataNasc;
	
	@ApiModelProperty(notes = "C�digo identificador da ra�a do paciente")
	private int codRaca;
	
	@ApiModelProperty(notes = "Ra�a do paciente")
	private String raca;
	
	@ApiModelProperty(notes = "Endere�o de resid�ncia")
	private String endereco;
	
	@ApiModelProperty(notes = "N�mero da resid�ncia")
	private String nroResidencia;
	
	@ApiModelProperty(notes = "Complemento do endere�o de resid�ncia")
	private String complementoEndereco;
	
	@ApiModelProperty(notes = "C�digo Postal")
	private String codPostal;
	
	@ApiModelProperty(notes = "Estado")
	private String estado;
	
	@ApiModelProperty(notes = "C�digo identificador do tipo de logradouro")
	private int codTipoLogradouro;
	
	@ApiModelProperty(notes = "Tipo de logradouro")
	private String tipoLogradouro;
	
	@ApiModelProperty(notes = "C�digo identificador do munic�pio de resid�ncia")
	private int codMunicipioResidencia;
	
	@ApiModelProperty(notes = "Munic�pio de resid�ncia")
	private String municipioResidencia;
	
	@ApiModelProperty(notes = "C�digo identificador do pa�s de resid�ncia")
	private int codPaisResidencia;
	
	@ApiModelProperty(notes = "Pa�s de resid�ncia")
	private String paisResidencia;
	
	@ApiModelProperty(notes = "C�digo identificador do munic�pio de nascimento")
	private int codMunicipioNascimento;
	
	@ApiModelProperty(notes = "Munic�pio de nascimento")
	private String municipioNascimento;
	
	@ApiModelProperty(notes = "C�digo identificador do pa�s de nascimento")
	private int codPaisNascimento;
	
	@ApiModelProperty(notes = "Pa�s de nascimento")
	private String paisNascimento;
	
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
		if(this.code==OK) {
			this.message = "SUCCESS";
		}
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNomePaciente() {
		return nomePaciente;
	}
	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}
	public String getCns() {
		return cns;
	}
	public void setCns(String cns) {
		this.cns = cns;
	}
	public long getCpf() {
		return cpf;
	}
	public void setCpf(long cpf) {
		this.cpf = cpf;
	}
	public String getNomeMae() {
		return nomeMae;
	}
	public void setNomeMae(String nomeMae) {
		this.nomeMae = nomeMae;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getDataNasc() {
		return dataNasc;
	}
	public void setDataNasc(String dataNasc) {
		this.dataNasc = dataNasc;
	}
	public int getCodRaca() {
		return codRaca;
	}
	public void setCodRaca(int codRaca) {
		this.codRaca = codRaca;
	}
	public String getEndereco() {
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getNroResidencia() {
		return nroResidencia;
	}
	public void setNroResidencia(String nroResidencia) {
		this.nroResidencia = nroResidencia;
	}
	public String getCodPostal() {
		return codPostal;
	}
	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getCodTipoLogradouro() {
		return codTipoLogradouro;
	}
	public void setCodTipoLogradouro(int codTipoLogradouro) {
		this.codTipoLogradouro = codTipoLogradouro;
	}
	public String getTipoLogradouro() {
		return tipoLogradouro;
	}
	public void setTipoLogradouro(String tipoLogradouro) {
		this.tipoLogradouro = tipoLogradouro;
	}
	public String getPaisResidencia() {
		return paisResidencia;
	}
	public void setPaisResidencia(String paisResidencia) {
		this.paisResidencia = paisResidencia;
	}
	public String getPaisNascimento() {
		return paisNascimento;
	}
	public void setPaisNascimento(String paisNascimento) {
		this.paisNascimento = paisNascimento;
	}
	public int getCodMunicipioResidencia() {
		return codMunicipioResidencia;
	}
	public void setCodMunicipioResidencia(int codMunicipioResidencia) {
		this.codMunicipioResidencia = codMunicipioResidencia;
	}
	public int getCodPaisResidencia() {
		return codPaisResidencia;
	}
	public void setCodPaisResidencia(int codPaisResidencia) {
		this.codPaisResidencia = codPaisResidencia;
	}
	public int getCodMunicipioNascimento() {
		return codMunicipioNascimento;
	}
	public void setCodMunicipioNascimento(int codMunicipioNascimento) {
		this.codMunicipioNascimento = codMunicipioNascimento;
	}
	public int getCodPaisNascimento() {
		return codPaisNascimento;
	}
	public void setCodPaisNascimento(int codPaisNascimento) {
		this.codPaisNascimento = codPaisNascimento;
	}
	public String getComplementoEndereco() {
		return complementoEndereco;
	}
	public String getMunicipioResidencia() {
		return municipioResidencia;
	}
	public void setMunicipioResidencia(String municipioResidencia) {
		this.municipioResidencia = municipioResidencia;
	}
	public String getMunicipioNascimento() {
		return municipioNascimento;
	}
	public void setMunicipioNascimento(String municipioNascimento) {
		this.municipioNascimento = municipioNascimento;
	}
	public void setComplementoEndereco(String complementoEndereco) {
		this.complementoEndereco = complementoEndereco;
	}
	public String getRaca() {
		return raca;
	}
	public void setRaca(String raca) {
		this.raca = raca;
	}
	
}
