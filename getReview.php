<?php
    $ShopID = $_POST['shopID'];

    //$ShopID = 15739816;

    /*------ db connect ------*/
    $connect = mysqli_connect('127.0.0.1', 'root', 'do0905ehgml!@', 'food');

    mysqli_query($connect, "set session character_set_connection=uft8;");
    mysqli_query($connect, "set session character_set_results=utf8;");
    mysqli_query($connect, "set session character_set_client=utf8;");
    /*------ db connect ------*/

    $sql = "select *from review where shopID = $ShopID;";

    $result = mysqli_query ($connect, $sql); 
    $rowCnt = mysqli_num_rows($result);

    $arr = array();
    for($i=0;$i<$rowCnt;$i++)
    {
        $row = mysqli_fetch_array($result, MYSQLI_ASSOC);
        $arr[$i] = $row;
    }

    $json = json_encode($arr, JSON_UNESCAPED_UNICODE);
    echo "$json";

    mysqli_close($connect);
?>