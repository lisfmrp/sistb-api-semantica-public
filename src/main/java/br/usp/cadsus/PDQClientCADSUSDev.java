package br.usp.cadsus;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.JAXBElement;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import org.hl7.v3.ActClassControlAct;
import org.hl7.v3.AdxpAdditionalLocator;
import org.hl7.v3.AdxpCity;
import org.hl7.v3.AdxpCountry;
import org.hl7.v3.AdxpHouseNumber;
import org.hl7.v3.AdxpPostalCode;
import org.hl7.v3.AdxpState;
import org.hl7.v3.AdxpStreetName;
import org.hl7.v3.AdxpStreetNameType;
import org.hl7.v3.CD;
import org.hl7.v3.CS;
import org.hl7.v3.CommunicationFunctionType;
import org.hl7.v3.EN;
import org.hl7.v3.EnGiven;
import org.hl7.v3.EntityClassDevice;
import org.hl7.v3.II;
import org.hl7.v3.MCCIMT000100UV01Device;
import org.hl7.v3.MCCIMT000100UV01Receiver;
import org.hl7.v3.MCCIMT000100UV01Sender;
import org.hl7.v3.PDQObjectFactory;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201310UV02OtherIDs;
import org.hl7.v3.PRPAMT201310UV02Person;
import org.hl7.v3.ST;
import org.hl7.v3.TS;
import org.hl7.v3.XActMoodIntentEvent;

import br.usp.websemantica.model.CNSData;
import ihe.iti.pdqv3._2007.PDQSupplierPortType;
import ihe.iti.pdqv3._2007.PDQSupplierService;

public class PDQClientCADSUSDev {

	private static final PDQSupplierService SERVICE;
	
	static {

		// CONFIGURA A JVM PARA IGNORAR O CERTIFICADOS INVALIDOS
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
			}
		} };

		// Install the all-trusting trust manager
		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (NoSuchAlgorithmException e1) {
			throw new RuntimeException(e1);
		} catch (KeyManagementException e) {
			throw new RuntimeException(e);
		}

		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

		SERVICE = new PDQSupplierService();

		// Handlers para acionar os parametros de autenticacao no cabecalho da mensagem
		final WSSUsernameTokenSecurityHandler handler = new WSSUsernameTokenSecurityHandler("CADSUS.CNS.PDQ.PUBLICO", "kUXNmiiii#RDdlOELdoe00966");
		SERVICE.setHandlerResolver(new HandlerResolver() {
			@Override
			@SuppressWarnings("rawtypes")
			public List<Handler> getHandlerChain(PortInfo arg0) {
				List<Handler> handlerList = new ArrayList<Handler>();
				handlerList.add(handler);
				return handlerList;
			}
		});
	}
	
	public PDQClientCADSUSDev() {
	}

	@SuppressWarnings("unchecked")
	public CNSData callService(String tipoParametroBusca, String parametroBusca) {
		PDQSupplierPortType pdq = SERVICE.getPDQSupplierPortSoap12();

		PRPAIN201305UV02 body = new PRPAIN201305UV02();
		body.setITSVersion("XML_1.0");
		
		// Parte fixa
		// <id root="2.16.840.1.113883.4.714" extension="123456"/>
		II ii = new II();
		ii.setRoot("2.16.840.1.113883.4.714");
		ii.setExtension("123456");
		body.setId(ii);
		
		// <creationTime value="20070428150301"/>
		TS ts = new TS();
		ts.setValue(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
		body.setCreationTime(ts);
		
		// <interactionId root="2.16.840.1.113883.1.6"
		// extension="PRPA_IN201305UV02"/>
		II intId = new II();
		intId.setRoot("2.16.840.1.113883.1.6");
		intId.setExtension("PRPA_IN201305UV02");
		body.setInteractionId(intId);
		
		// <processingCode code="T"/>
		CS processingCode = new CS();
		processingCode.setCode("T");
		body.setProcessingCode(processingCode);
		
		// <processingModeCode code="T"/>
		body.setProcessingModeCode(processingCode);
		
		// <acceptAckCode code="AL"/>
		CS acceptAckCode = new CS();
		acceptAckCode.setCode("AL");
		body.setAcceptAckCode(acceptAckCode);
		
		// <receiver typeCode="RCV">
		MCCIMT000100UV01Receiver receiver = new MCCIMT000100UV01Receiver();
		receiver.setTypeCode(CommunicationFunctionType.RCV);
		
		// <device classCode="DEV" determinerCode="INSTANCE">
		MCCIMT000100UV01Device deviceReceiver = new MCCIMT000100UV01Device();
		deviceReceiver.setClassCode(EntityClassDevice.DEV);
		deviceReceiver.setDeterminerCode("INSTANCE");
		
		// <id root="2.16.840.1.113883.3.72.6.5.100.85"/>
		II idDeviceReceiver = new II();
		idDeviceReceiver.setRoot("2.16.840.1.113883.3.72.6.5.100.85");
		deviceReceiver.getId().add(idDeviceReceiver);
		
		// </device>
		receiver.setDevice(deviceReceiver);
		body.getReceiver().add(receiver);
		// </receiver>
		
		// <sender typeCode="SND">
		MCCIMT000100UV01Sender sender = new MCCIMT000100UV01Sender();
		sender.setTypeCode(CommunicationFunctionType.SND);
		
		// <device classCode="DEV" determinerCode="INSTANCE">
		MCCIMT000100UV01Device deviceSender = new MCCIMT000100UV01Device();
		deviceSender.setClassCode(EntityClassDevice.DEV);
		deviceSender.setDeterminerCode("INSTANCE");
		
		// <id root="2.16.840.1.113883.3.72.6.2"/>
		II idDeviceSender = new II();
		idDeviceSender.setRoot("2.16.840.1.113883.3.72.6.2");
		deviceSender.getId().add(idDeviceSender);
		
		// <name>SIGA-SAUDE</name>
		EN nameSender = new EN();
		nameSender.getContent().add("SIGA");
		deviceSender.getName().add(nameSender);
		
		// </device>
		sender.setDevice(deviceSender);
		body.setSender(sender);
		// </sender>

		// <controlActProcess classCode="CACT" moodCode="EVN">
		PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlAct = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
		controlAct.setClassCode(ActClassControlAct.CACT);
		controlAct.setMoodCode(XActMoodIntentEvent.EVN);
		body.setControlActProcess(controlAct);
		
		// <code code="PRPA_TE201305UV02" codeSystem="2.16.840.1.113883.1.6"/>
		CD code = new CD();
		code.setCode("PRPA_TE201305UV02");
		code.setCodeSystem("2.16.840.1.113883.1.6");
		controlAct.setCode(code);
		
		// <queryByParameter>
		PRPAMT201306UV02QueryByParameter queryByParamenter = new PRPAMT201306UV02QueryByParameter();
		
		// <queryId root="1.2.840.114350.1.13.28.1.18.5.999"
		// extension="1840997084" />
		II queryId = new II();
		queryId.setRoot("1.2.840.114350.1.13.28.1.18.5.999");
		queryId.setExtension("1840997084");
		queryByParamenter.setQueryId(queryId);
		
		// <statusCode code="new"/>
		CS statusCode = new CS();
		statusCode.setCode("new");
		queryByParamenter.setStatusCode(statusCode);
		
		// <responseModalityCode code="R"/>
		CS responseModalityCode = new CS();
		responseModalityCode.setCode("R");
		queryByParamenter.setResponseModalityCode(responseModalityCode);
		
		// <responsePriorityCode code="I"/>
		CS responsePriorityCode = new CS();
		responsePriorityCode.setCode("I");
		queryByParamenter.setResponsePriorityCode(responsePriorityCode);
		
		// <parameterList>
		PRPAMT201306UV02ParameterList parameterList = new PRPAMT201306UV02ParameterList();
		queryByParamenter.setParameterList(parameterList);
		
		// <livingSubjectId>
		PRPAMT201306UV02LivingSubjectId livingSubjectId = new PRPAMT201306UV02LivingSubjectId();
		parameterList.getLivingSubjectId().add(livingSubjectId);
		
		
		if(tipoParametroBusca.equals("cns")) {
			// - PESQUISA POR CNS
			II cnsParameter = new II();
			cnsParameter.setRoot("2.16.840.1.113883.13.236");
			cnsParameter.setExtension(parametroBusca);
			livingSubjectId.getValue().add(cnsParameter);
			ST cnsSemanticsText = new ST();
			cnsSemanticsText.getContent().add("LivingSubject.id");
			livingSubjectId.setSemanticsText(cnsSemanticsText);
		} else if(tipoParametroBusca.equals("cpf")) {
			// - PESQUISA POR CPF		
			II cpfParameter = new II();
			cpfParameter.setRoot("2.16.840.1.113883.13.237");
			cpfParameter.setExtension(parametroBusca);
			livingSubjectId.getValue().add(cpfParameter);
			ST cpfSemanticsText = new ST();
			cpfSemanticsText.getContent().add("LivingSubject.id");
			livingSubjectId.setSemanticsText(cpfSemanticsText);					
		}	
		
		JAXBElement<PRPAMT201306UV02QueryByParameter> jaxbQuery = new PDQObjectFactory()
				.createPRPAIN201305UV02QUQIMT021001UV01ControlActProcessQueryByParameter(queryByParamenter);
		controlAct.setQueryByParameter(jaxbQuery);

		PRPAIN201306UV02 retorno = pdq.pdqSupplierPRPAIN201305UV02(body);
		CNSData result = new CNSData();
		
		try {
			PRPAMT201310UV02Person patientPerson = retorno.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient()
				.getPatientPerson().getValue();
			
			JAXBElement<EnGiven> obj = (JAXBElement<EnGiven>) patientPerson.getName().get(0).getContent().get(0);
			
			result.setNomePaciente(obj.getValue().getContent().get(0).toString());
			
			//IDs
			String cpf = "";
			String cns = "";
			for (PRPAMT201310UV02OtherIDs otherId : patientPerson.getAsOtherIDs()) {
				for (II id : otherId.getId()) {			
					//CNS
					if ("2.16.840.1.113883.13.236".equals(id.getRoot())) {
						cns += "," + id.getExtension();
					}
					/*if ("2.16.840.1.113883.13.236.1".equals(id.getRoot())) {
						System.out.println("Tipo: " + id.getExtension());
					}*/
					if ("2.16.840.1.113883.13.237".equals(id.getRoot())) {
						cpf = id.getExtension();
					}
				}
			}
			cns = cns.substring(1);
			result.setCns(cns);
			
			String dataNasc = patientPerson.getBirthTime().getValue().substring(0, 4) + "-" 
					+ patientPerson.getBirthTime().getValue().substring(4, 6) + "-"
					+ patientPerson.getBirthTime().getValue().substring(6, 8);
			result.setDataNasc(dataNasc);
			
			JAXBElement<EnGiven> nomeMae = (JAXBElement<EnGiven>) patientPerson.getPersonalRelationship().get(0).getRelationshipHolder1().getValue().getName().get(0).getContent().get(0);
			result.setNomeMae(nomeMae.getValue().getContent().get(0).toString());

			result.setSexo(patientPerson.getAdministrativeGenderCode().getCode());			
			
			if(!cpf.equals(""))
				result.setCpf(Long.parseLong(cpf));			
			
			if(patientPerson.getRaceCode().get(0).getCode() != null) {
				result.setCodRaca(Integer.parseInt(patientPerson.getRaceCode().get(0).getCode()));
			}
			
			for(int i=0; i<patientPerson.getAddr().get(0).getContent().size(); i++) {
				JAXBElement<?> elem = (JAXBElement<?>) patientPerson.getAddr().get(0).getContent().get(i);
				//System.out.println(elem.getValue().getClass().getSimpleName());
				
				if(elem.getValue().getClass().getSimpleName().equals("AdxpCity")) {
					JAXBElement<AdxpCity> item = (JAXBElement<AdxpCity>) patientPerson.getAddr().get(0).getContent().get(i);
					result.setCodMunicipioResidencia(Integer.parseInt(item.getValue().getContent().get(0).toString()));
				} else if(elem.getValue().getClass().getSimpleName().equals("AdxpCountry")) {
					JAXBElement<AdxpCountry> item = (JAXBElement<AdxpCountry>) patientPerson.getAddr().get(0).getContent().get(i);
					result.setCodPaisResidencia(Integer.parseInt(item.getValue().getContent().get(0).toString()));
				} else if(elem.getValue().getClass().getSimpleName().equals("AdxpHouseNumber")) {
					JAXBElement<AdxpHouseNumber> item = (JAXBElement<AdxpHouseNumber>) patientPerson.getAddr().get(0).getContent().get(i);
					result.setNroResidencia(item.getValue().getContent().get(0).toString());
				} else if(elem.getValue().getClass().getSimpleName().equals("AdxpStreetName")) {
					JAXBElement<AdxpStreetName> item = (JAXBElement<AdxpStreetName>) patientPerson.getAddr().get(0).getContent().get(i);
					result.setEndereco(item.getValue().getContent().get(0).toString());
				} else if(elem.getValue().getClass().getSimpleName().equals("AdxpStreetNameType")) {
					JAXBElement<AdxpStreetNameType> item = (JAXBElement<AdxpStreetNameType>) patientPerson.getAddr().get(0).getContent().get(i);
					result.setCodTipoLogradouro(Integer.parseInt(item.getValue().getContent().get(0).toString()));								
				} else if(elem.getValue().getClass().getSimpleName().equals("AdxpAdditionalLocator")) {
					JAXBElement<AdxpAdditionalLocator> item = (JAXBElement<AdxpAdditionalLocator>) patientPerson.getAddr().get(0).getContent().get(i);
					result.setComplementoEndereco(item.getValue().getContent().get(0).toString());
				} else if(elem.getValue().getClass().getSimpleName().equals("AdxpState")) {
					JAXBElement<AdxpState> item = (JAXBElement<AdxpState>) patientPerson.getAddr().get(0).getContent().get(i);
					result.setEstado(item.getValue().getContent().get(0).toString());
				} else if(elem.getValue().getClass().getSimpleName().equals("AdxpPostalCode")) {
					JAXBElement<AdxpPostalCode> item = (JAXBElement<AdxpPostalCode>) patientPerson.getAddr().get(0).getContent().get(i);
					result.setCodPostal(item.getValue().getContent().get(0).toString());
				}
			}
			
			for(int i=0; i<patientPerson.getBirthPlace().getValue().getAddr().getContent().size(); i++) {
				JAXBElement<?> elem = (JAXBElement<?>) patientPerson.getBirthPlace().getValue().getAddr().getContent().get(i);
				//System.out.println(elem.getValue().getClass().getSimpleName());
				
				if(elem.getValue().getClass().getSimpleName().equals("AdxpCity")) {
					JAXBElement<AdxpCity> item = (JAXBElement<AdxpCity>) patientPerson.getBirthPlace().getValue().getAddr().getContent().get(i);
					result.setCodMunicipioNascimento(Integer.parseInt(item.getValue().getContent().get(0).toString()));
				} else if(elem.getValue().getClass().getSimpleName().equals("AdxpCountry")) {
					JAXBElement<AdxpCountry> item = (JAXBElement<AdxpCountry>) patientPerson.getBirthPlace().getValue().getAddr().getContent().get(i);
					result.setCodPaisNascimento(Integer.parseInt(item.getValue().getContent().get(0).toString()));
				}
			}
		} catch(java.lang.IndexOutOfBoundsException e) {
			return null;
		}	
				
		return result;
	}

}
