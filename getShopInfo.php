<?php
    $ShopID = $_POST['shopID'];

    //$ShopID = 15739816;

    /*------ db connect ------*/
    $connect = mysqli_connect('127.0.0.1', 'root', 'do0905ehgml!@', 'food');

    mysqli_query($connect, "set session character_set_connection=uft8;");
    mysqli_query($connect, "set session character_set_results=utf8;");
    mysqli_query($connect, "set session character_set_client=utf8;");
    /*------ db connect ------*/

    $sql = "SELECT A.shopID, shopName, subName, shopKind, address, 
                IFNULL(Avg(star),0) as star, img_src, Longitude, Latitude               
                from
                (select * from shop
                where shopID = $ShopID) A
                Left Outer Join 
                    (select review.shopID, review.star, shop_img.img_src 
			            from review, shop_img where review.shopID = shop_img.shopID) as B
                ON A.shopID = B.shopID
            group by A.shopID;";

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