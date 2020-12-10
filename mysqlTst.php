<?php
    $Latitude  = $_POST['latitude'];
    $Longitude = $_POST['longitude'];

    /*------ db connect ------*/
    $connect = mysqli_connect('127.0.0.1', 'root', 'do0905ehgml!@', 'food');

    mysqli_query($connect, "set session character_set_connection=uft8;");
    mysqli_query($connect, "set session character_set_results=utf8;");
    mysqli_query($connect, "set session character_set_client=utf8;");
    /*------ db connect ------*/

    $sql = "SELECT A.shopID, A.shopName, A.distance, A.shopCode, IFNULL(Avg(star),0) as star, 
                Count(B.star) as reviews, B.img_src
                from (select shopID, shopName, shopCode,
                    (6371*acos(cos(radians($Latitude))*cos(radians(Latitude))*cos(radians(Longitude)
                    -radians($Longitude))+sin(radians($Latitude))*sin(radians(Latitude))))
                    AS distance
                FROM shop
                Group by shopID
                having distance <= 0.5
                Order by distance
                LIMIT 0,1000) as A
                Left Outer Join 
                    (select review.shopID, review.star, shop_img.img_src 
                        from review, shop_img where review.shopID = shop_img.shopID) as B
                ON A.shopID = B.shopID
            group by A.shopID";


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