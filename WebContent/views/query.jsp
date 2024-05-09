<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>

<head>
	<!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
    
	<title>Query Endpoint - SisTB</title>
	
	<script type="text/javascript">
		function showQuery() {			
			var text = "SELECT ?propriedade ?valor \n" +
				"WHERE { \n" +
				"	?paciente ?propriedade ?valor .\n" +
				"	FILTER(?valor != \"Null\") .\n" +
				"	FILTER(isLiteral(?valor)) .\n" +
				"} \n" +
				"LIMIT 10";
			document.getElementById("query").value = text.replace(/@/g,"<").replace(/%/g,">");
		}
		
		function selectEndpoint() {
			$("#exemploQueryText").addClass("d-none");
			$("#query").val("");
			
			if(document.getElementById("endpoint").value == "sparql") {
				$("#exemploQueryText").removeClass("d-none");
			} else if(document.getElementById("endpoint").value == "graphql") {
				
			}
		}
	</script>
</head>

<body>
	
	
	<div class="col-md-12 mt-4">	
		<h1 style="text-align:center;">Query Endpoint - SisTB</h1>
		<div class="col-md-8 offset-md-2">
			<c:if test="${param.unauthorized != null}">
			     <div class="alert alert-danger alert-dismissible fade show" role="alert">
			 	  	Erro 401 - Não autorizado
			 	  	<button type="button" class="close" data-dismiss="alert" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
			  	</div>
			</c:if>

			<form action="executeQuery" method="POST">	
				<div class="form-group">
	      	  		<label for="apiKey" class="font-weight-bold">API Key</label> <br/>
		        	<input type="text" id="apiKey" name="apiKey" class="form-control" required/>
		    	</div>
		    	
		    	<div class="form-group">
	      	  		<label for="signature" class="font-weight-bold">Assinatura da Chave de API</label> <br/>
		        	<input type="text" id="signature" name="signature" class="form-control" required/>
		    	</div>
		    	
		    	<div class="form-group">
	      	  		<label for="endpoint" class="font-weight-bold">Endpoint</label> <br/>
		        	<select id="endpoint" name="endpoint" class="form-control" onchange="selectEndpoint();" required>
		        		<option value="">Selecione</option>
		        		<option value="sparql">SPARQL</option>
		        		<!--<option value="graphql">GraphQL</option>-->
		        	</select>
		    	</div>
		    										
				<div class="form-group">
	      	  		<label for="query" class="font-weight-bold">Query</label>
	      	  		<small id="exemploQueryText" class="d-none"><a href="javascript:void(0)" onclick="showQuery();" id="exemploQuery">Mostrar exemplo de query</a></small>
		        	<textarea cols="96" rows="20" id="query" name="query" class="form-control" required></textarea>
		    	</div>
				
				<p style="text-align:center;">
					<button type="submit" class="btn btn-primary">Enviar</button>
				</p>
			</form>
		</div>
	</div>
	
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
</body>

</html>