<?php
    $ShopID = $_POST['shopID'];
    $review = $_POST['review'];
    $star = $_POST['star'];
    $reviewer = $_POST['reviewer'];
    $review_date = $_POST['review_date'];

    /*------ db connect ------*/
    $connect = mysqli_connect('127.0.0.1', 'root', 'do0905ehgml!@', 'food');

    mysqli_query($connect, "set session character_set_connection=uft8;");
    mysqli_query($connect, "set session character_set_results=utf8;");
    mysqli_query($connect, "set session character_set_client=utf8;");
    /*------ db connect ------*/

    $sql = "insert into review (shopID, review, star, reviewerID, date)
        values ($ShopID, '$review', $star, '$reviewer', '$review_date')";

    $result = mysqli_query ($connect, $sql); 

    mysqli_close($connect);
?>