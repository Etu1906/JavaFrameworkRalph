<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>formulaire</title>
</head>
<body>
	<form action="trait.do" method="post"  enctype="multipart/form-data">
			Nom : <input type="text" name="nom" value="ralph" >
			</br>
			Nbr chien : <input type="text" name="nb-chien" value="10" >
			<p>
            <input type="file" name="fichier">
        	</p>
		    <p>
            	<input type="submit" value="valider">
        	</p>	
	</form>
	<p>
	<a href="verif-emp.do" > class Emp :  Singleton </a>	
	</p>
	<p>
	<a href="verif-dept.do" > class Dept : Pas Singleton </a>
	</p>
</body>
</html>
