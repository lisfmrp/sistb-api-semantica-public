package br.usp.websemantica.model;

import org.apache.commons.httpclient.HttpStatus;

import io.swagger.annotations.ApiModelProperty;

public class ResponseApi {

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
	
	@ApiModelProperty(notes = "Chave simétrica criptografada (codificada em base64)")
	private String key;
	
	@ApiModelProperty(notes = "Vetor de inicialização para o algoritmo de criptografia simétrica")
	private String initVector;
	
	@ApiModelProperty(notes = "Resultado criptografado (codificado em base64)")
	private Object obj;
	
	@ApiModelProperty(notes = "Hash MD5 do resultado descriptografado para verificação de integridade")
	private String hashMD5;
	
	@ApiModelProperty(notes = "Assinatura da API para o Hash MD5 (codificada em base64)")
	private String hashMD5Signature;
	
	@ApiModelProperty(notes = "Assinatura da API para os dados descriptografados (codificada em base64)")
	private String objSignature;
	
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
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getHashMD5() {
		return hashMD5;
	}
	public void setHashMD5(String hashMD5) {
		this.hashMD5 = hashMD5;
	}
	public String getHashMD5Signature() {
		return hashMD5Signature;
	}
	public void setHashMD5Signature(String hashMD5Signature) {
		this.hashMD5Signature = hashMD5Signature;
	}
	public String getObjSignature() {
		return objSignature;
	}
	public void setObjSignature(String objSignature) {
		this.objSignature = objSignature;
	}
	public String getInitVector() {
		return initVector;
	}
	public void setInitVector(String initVector) {
		this.initVector = initVector;
	}
	
}
