<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>formulaire</title>
</head>
<body>
    <form action="save.do" method="post" enctype="multipart/form-data" >
        <p>
            <label for="">Nom:</label>
            <input type="text" name="nom" id="" value=" the King (Lion) ">
        </p>
        <p>
            <label for="">Prenom:</label>
            <input type="text" name="prenom" id="" value="5">
        </p>
        <p>
            <label for="">date:</label>
            <input type="date" name="data" id="" value="2022/01/01">
        </p>
	<p>
            <label for="">type :</label>
            <input type="checkbox" name="check" id="" value="1"> Homme </br>
            <input type="checkbox" name="check" id="" value="2"> Femme </br>
            <input type="checkbox" name="check" id="" value="3"> Autre </br>
        </p>
        <p>
            <input type="file" name="fichier">
        </p>
        <p>
            <input type="submit" value="valider">
        </p>
    </form>
</body>
</html>
