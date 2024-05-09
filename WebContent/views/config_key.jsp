<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    
	<title>SISTB - API Sem�ntica - Configura��o de Chaves</title>
</head>

<body>		
	<div class="col-md-12 mt-4">	
		<h3 style="text-align:center;" class="mb-3">SISTB - API Sem�ntica - Configura��o de Chaves</h3>
					
		<div class="col-md-8 offset-md-2">
			<c:if test="${param.unauthorized == null}">
				<c:if test="${param.error != null}">
				     <div class="alert alert-danger alert-dismissible fade show" role="alert">
				 	  	Erro ao registrar chave p�blica. Por favor, verifique o formato da chave.
				 	  	<button type="button" class="close" data-dismiss="alert" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
				  	</div>
				</c:if>
				
				<c:if test="${param.errorKeySize != null}">
				     <div class="alert alert-danger alert-dismissible fade show" role="alert">
				 	  	Erro ao registrar chave p�blica. A chave deve ter, no m�nimo, 2048 bits..
				 	  	<button type="button" class="close" data-dismiss="alert" aria-label="Close">
							<span aria-hidden="true">&times;</span>
						</button>
				  	</div>
				</c:if>
		
				<div class="jumbotron">
					<p><a href="${pageContext.request.contextPath}/swagger-ui.html#" target="_blank">Documenta��o da API</a></p>
					
					<p class="font-weight-bold">Para obter uma chave de API:</p>
					<ul>
						<li>Forne�a uma chave p�blica RSA de 2048 bits</li>
						<li>Se a chave for v�lida, a chave de API ser� exibida na tela (codificada em Base64 e criptografada com a chave p�blica informada)</li>
						<li>Reverta a codifica��o Base64</li>
						<li>Utilize sua chave privada (par) para descriptograr a chave de API</li>				
						<li>Utilize a chave de API para enviar requisi��es para a API Sem�ntica do SisTB</li>
					</ul>
				</div>
							
				<c:if test="${not empty sistemaExterno.chavePublica}">	
					<div class="jumbotron">
						<h4 class="font-weight-bold mb-4">Suas chaves</h4>
						<p class="font-weight-bold">Chave de API criptografada:</p>
						<textarea rows="6" class="form-control-plaintext" readonly="readonly">${apiKeyCriptografada}</textarea>
						
						<p class="font-weight-bold">Chave P�blica:</p>
						<textarea rows="10" class="form-control-plaintext mb-4" readonly="readonly">${sistemaExterno.chavePublica}</textarea>
					</div>
				</c:if>
				
				<c:if test="${sistemaExterno.permitirNovaChavePublica eq 1}">		
					<div class="jumbotron">								
						<h4 class="font-weight-bold mb-4">Cadastrar/Alterar Chave P�blica</h4>						
						<p class="font-weight-bold">Exemplo de chave</p>
						<img src="${pageContext.request.contextPath}/resources/imagens/exemplo_chave_publica.png" class="img-fluid img-thumbnail mb-3"/>
						
						<form action="keyConfig" method="POST">
							<input type="hidden" name="configKey" value="${param.configKey}"/>
																    									
							<div class="form-group">
				      	  		<label for="query" class="font-weight-bold">Chave P�blica</label>
					        	<textarea rows="10" id="chavePublica" name="chavePublica" class="form-control" required></textarea>
					    	</div>
							
							<p style="text-align:center;">
								<button type="submit" class="btn btn-primary">Enviar</button>
							</p>
						</form>
					</div>
				</c:if>
				
				<c:if test="${sistemaExterno.permitirNovaChavePublica eq 0}">	
					<div class="alert alert-danger" role="alert">
				 		O gerenciamento de chave p�blica (cadastro/altera��o) est� bloqueado para este sistema
			  		</div>
				</c:if>			
			</c:if>
			
			<c:if test="${param.unauthorized != null}">
				<div class="alert alert-danger" role="alert">
				 	Erro 401 - N�o autorizado
			  	</div>
			</c:if>
		</div>		
	</div>
	
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</body>

</html>